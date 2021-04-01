import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserAuth } from 'src/app/common/model/user-auth';
import { UserLogin } from 'src/app/common/model/user-login';
import { StorageService } from 'src/app/common/services/storage.service';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit, OnDestroy {
  userLogin: UserLogin;
  userAuth: UserAuth;
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

      this.userAuth = {email, password, isEmployer: false};
      this.login(this.userAuth);

      this.router.navigateByUrl(`users-list`);
  }

  private login(user: UserAuth): void {
    this.subscriptions.push(this.userService.login(user).subscribe(
      (response: UserLogin) => {
        this.userLogin = response;
        this.storageService.saveValue('token', this.userLogin.token);
      },
      (error) => {
        this.initForm(
          user.email
        );
        console.log('error', error);
      },
      () => {

        console.log('complete');
      }
    ));
  }
}
