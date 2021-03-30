import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserAuth } from 'src/app/common/model/user-auth';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit, OnDestroy {
  user: UserAuth;
  userForm: FormGroup;
  private subscriptions: Subscription[] = [];
  constructor(private userService: UserHttpService) { }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit() {
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

      this.login(email, password);
  }

  private login(email: string, password: string): void {
    this.subscriptions.push(this.userService.login(email, password).subscribe(
      (response: UserAuth) => {
        this.user = response;
      },
      (error) => {
        this.initForm(
          this.user.email,
          this.user.password
        );
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    ));
  }
}
