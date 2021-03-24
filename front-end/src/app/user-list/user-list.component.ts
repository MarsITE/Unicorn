import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../common/model/user';
import { UserHttpService } from '../common/services/user-http.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'workStatus', 'phone', 'dateOfBirth', 'userRole', 'generalRating'];

  constructor(private userService: UserHttpService, private router: Router) {
  }

  ngOnInit(): void {
    this.getUsers();
  }

  private getUsers() {
    this.userService.getUsers().subscribe(
      (response: User[]) => {
        this.users = response;
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete");
      }
    )
  }

  showUserProfile(row) {
    console.log(row);
    
    this.router.navigateByUrl(`user-profile/${row.email}`);
  }

}
