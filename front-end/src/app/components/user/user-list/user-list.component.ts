import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
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

  constructor(private userService: UserHttpService, private router: Router) {
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
    this.subscriptions.push(this.userService.getUsers().subscribe(
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

  private showUserProfile(row: any): void {
    console.log(row);

    this.router.navigateByUrl(`my-profile`);
  }

}
