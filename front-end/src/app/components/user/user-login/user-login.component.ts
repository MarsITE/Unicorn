import { AuthenticationService } from './../../../common/services/authentication.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegistration } from 'src/app/common/model/user-registration';
import { UserAuth } from 'src/app/common/model/user-auth';
import { ToastrService } from 'ngx-toastr';
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
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private toastr: ToastrService
  ) { }
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
        [Validators.required, Validators.email, Validators.maxLength(50)]),
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
      isEmployer: false
    };
    this.login(this.user);
  }

  private login(user: UserRegistration): void {
    this.subscriptions.push(this.authenticationService.login(user)
      .pipe(first())
      .subscribe(
        result => this.router.navigateByUrl(``),
        error => {
          this.initForm(user.email);
          this.toastr.error(error, 'Something wrong');
        }));
  }
}
