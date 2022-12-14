import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserRegistration } from 'src/app/common/model/user-registration';
import { ToastrService } from 'ngx-toastr';
import { UserHttpService } from '../../../common/services/user-http.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit, OnDestroy {
  user: UserRegistration;
  userForm: FormGroup;

  private timeToImproveEmail = 1;
  private subscriptions: Subscription[] = [];

  constructor(private userService: UserHttpService, private router: Router, private toastr: ToastrService) {
    this.initForm();
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
  }


  private initForm(
    email: string = '',
    password: string = '',
    isEmployer: boolean = false
  ): void {
    this.userForm = new FormGroup({
      email: new FormControl(
        email,
        [Validators.required, Validators.email, Validators.maxLength(50)]),
      password: new FormControl(
        password,
        [Validators.required, Validators.maxLength(15), Validators.minLength(6)]),
      isEmployer: new FormControl(isEmployer)
    });
  }

  private saveUser(user: UserRegistration): void {
    this.subscriptions.push(this.userService.save(user)
    .subscribe(
      response => {
        this.user = response;
        this.router.navigateByUrl('login');
        this.toastr.success(`User ${user.email} sussesfully saved!`, 'Success');
        this.toastr.info(`You need to improve your account by ${1} day`);
      },
      error => {
        this.initForm(
          user.email,
          '',
          user.isEmployer
        );
        this.toastr.error(error, 'Something wrong');
      },
    ));
  }

  public submit(): void {
    this.user = {
      email: this.userForm.controls.email.value,
      password: this.userForm.controls.password.value,
      isEmployer: this.userForm.controls.isEmployer.value };

    this.saveUser(this.user);
  }
}
