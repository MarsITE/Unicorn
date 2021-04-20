import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { TokenHelper } from 'src/app/common/helper/token.helper';
import { User } from 'src/app/common/model/user';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit, OnDestroy {

  users: User[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'workStatus', 'phone', 'dateOfBirth', 'userRole', 'generalRating'];
  private subscriptions: Subscription[] = [];

  constructor(private userService: UserHttpService, private router: Router, private tokenHelper: TokenHelper) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.getUsers();
  }

  private getUsers(): void {
    this.subscriptions.push(this.userService.getUsers()
      .subscribe(
        (response: User[]) => {
          this.users = response;
        },
        (error) => {
          console.log('error', error);
        },
        () => {
          console.log('complete');
        }
      ));
  }

  public showUserProfile(row: any): void {
    if (this.tokenHelper.getEmailFromToken() === row.email) {
      this.router.navigateByUrl('my-profile');
    } else {
      this.router.navigateByUrl(`my-profile/${row.email}`);
    }
  }

  public converToPlain(str: string): string {
    return str.replace('_', ' ').toLowerCase();
  }
}
