import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserAuth } from 'src/app/common/model/user-auth';
import { UserHttpService } from '../../common/services/user-http.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit, OnDestroy {
  user: UserAuth;
  userForm: FormGroup;
  private subscriptions: Subscription[] = [];
  constructor(private userService: UserHttpService) {
    this.initForm();
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit() {

  }


  private initForm(
    email: string = '',
    password: string = '',
    isEmployer: boolean = false
  ): void {
    this.userForm = new FormGroup({
      email: new FormControl(
        email,
        [Validators.required, Validators.email, Validators.maxLength(30)]),
      password: new FormControl(
        password,
        [Validators.required, Validators.maxLength(15), Validators.minLength(6)]),
      isEmployer: new FormControl(isEmployer)
    });
  }

  private saveUser(user: UserAuth): void {
    this.subscriptions.push(this.userService.save(user).subscribe(
      (response: UserAuth) => {
        this.user = response;
      },
      (error) => {
        this.initForm(
          user.email,
          user.password,
          user.isEmployer
        );
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    ));
  }

  public submit(): void {
    this.user = {
      email: this.userForm.controls.email.value,
      password: this.userForm.controls.password.value,
      isEmployer: Boolean(this.userForm.controls.isEmployer.value) };

    this.saveUser(this.user);
  }
}
