import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User } from '../common/model/user';
import { UserHttpService } from '../common/services/user-http.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnDestroy {
  email: string;
  user: User;
  userProfileForm: FormGroup;
  constructor(private userService: UserHttpService, router: ActivatedRoute) {
    this.email = router.snapshot.params['email'];
    if (this.user != null) {
    this.initForm(
      this.user.userInfo.firstName,
      this.user.userInfo.lastName,
      this.user.userInfo.phone,
      this.user.userInfo.urlToSocialNetwork,
      this.user.userInfo.dateOfBirth,
      true,
      this.user.userInfo.workStatus,
      this.user.userInfo.imageUrl);
    }
  }

  ngOnInit(): void {
    this.getUser(this.email);
  }

  ngOnDestroy(): void {
  }

  private initForm(
    firstName: string,
    lastName: string,
    phone: string,
    linkToSocialNetwork: string,
    dateOfBirth: string,
    isShowInfo: boolean,
    workStatus: string,
    imageUrl: string
  ): void {
    this.userProfileForm = new FormGroup({
      firstName: new FormControl(firstName, [Validators.required]),
      lastName: new FormControl(lastName, [Validators.required]),
      phone: new FormControl(phone, [Validators.required]),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork, [Validators.required]),
      dateOfBirth: new FormControl(dateOfBirth, [Validators.required]),
      isShowInfo: new FormControl(isShowInfo, [Validators.required]),
      workStatus: new FormControl(workStatus, [Validators.required]),
      imageUrl: new FormControl(imageUrl, [Validators.required])
    });
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    );
  }


  submit() {
    //this.user.userInfo.firstName = this.userProfileForm.valueChanges.
  }

}
