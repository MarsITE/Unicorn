import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../common/model/user';
import { UserHttpService } from '../common/services/user-http.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  email: string
  user: User;
  userProfileForm: FormGroup;

  constructor(private userService: UserHttpService, router: ActivatedRoute) {
    this.email = router.snapshot.params['email'];
  }

  ngOnInit() {
    this.getUser(this.email);
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete");
      }
    );
  }

}
