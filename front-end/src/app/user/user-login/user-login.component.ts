import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegistration } from 'src/app/common/model/user-registration';
import { UserAuth } from 'src/app/common/model/user-auth';
import { StorageService } from 'src/app/common/services/storage.service';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit, OnDestroy {
  userAuth: UserAuth;
  user: UserRegistration;
  userForm: FormGroup;
  private subscriptions: Subscription[] = [];
  constructor(private userService: UserHttpService, private router: Router, private storageService: StorageService) { }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(
    email: string = '',
    password: string = '',
    isLogged: boolean = false
  ): void {
    this.userForm = new FormGroup({
      email: new FormControl(
        email,
        [Validators.required, Validators.email, Validators.maxLength(30)]),
      password: new FormControl(
        password,
        [Validators.required, Validators.maxLength(15), Validators.minLength(6)]),
      isLogged: new FormControl(isLogged)
    });
  }

  public submit(): void {
      const email = this.userForm.controls.email.value;
      const password = this.userForm.controls.password.value;

      this.user = {email, password, isEmployer: false};
      this.login(this.user);

  }

  private login(user: UserRegistration): void {
    this.subscriptions.push(this.userService.login(user).subscribe(
      (response: UserAuth) => {
        this.userAuth = response;
        this.storageService.saveValue('token', this.userAuth.token);
        this.router.navigateByUrl(`user-profile/${user.email}`);
      },
      (error) => {
        this.initForm(
          user.email
        );
        alert(error);
        console.log(error);
      },
      () => {
        console.log('complete');
      }
    ));
  }
}
