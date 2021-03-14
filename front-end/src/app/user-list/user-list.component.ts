import { Component, OnInit } from '@angular/core';
import { User } from '../common/model/user';
import { UserHttpService } from '../common/services/user-http.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email'];

  constructor(private userService: UserHttpService) {
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

}
