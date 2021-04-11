import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegistration } from 'src/app/common/model/user-registration';
import { UserHttpService } from '../../common/services/user-http.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit, OnDestroy {
  user: UserRegistration;
  userForm: FormGroup;
  private subscriptions: Subscription[] = [];
  constructor(private userService: UserHttpService, private router: Router) {
    this.initForm();
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    console.log('registration');
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

  private saveUser(user: UserRegistration): void {
    this.subscriptions.push(this.userService.save(user)
    .subscribe(
      response => this.user = response,
      error => {
        this.initForm(
          user.email,
          '',
          user.isEmployer
        );
        alert(error);
      },
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
