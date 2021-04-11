import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegistration } from 'src/app/common/model/user-registration';
import { UserAuth } from 'src/app/common/model/user-auth';
import { UserHttpService } from 'src/app/common/services/user-http.service';
import { first } from 'rxjs/operators';

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
  constructor(private userService: UserHttpService, private router: Router) { }
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
      this.user = {
        email: this.userForm.controls.email.value,
        password: this.userForm.controls.password.value,
        isEmployer: false};
      this.login(this.user);

  }

  private login(user: UserRegistration): void {
    this.subscriptions.push(this.userService.login(user)
    .pipe(first())
    .subscribe(
      result => this.router.navigateByUrl(`my-profile/${user.email}`),
      error => {
        this.initForm(user.email);
        alert(error);
      }));
  }
}
