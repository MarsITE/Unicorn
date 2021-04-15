import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/common/model/user';
import { UserInfo } from 'src/app/common/model/user-info';
import { WorkStatus } from 'src/app/common/model/work-status';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})

export class UserEditComponent implements OnInit, OnDestroy {
  email: string;
  user: User;
  userProfileForm: FormGroup;
  imageURL: string;
  imageSrc: any;
  selectedImage: any;
  today: Date;
  private subscriptions: Subscription[] = [];
  workStatuses: WorkStatus[] = [//todo
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];
  selectedWorkStatus: WorkStatus = this.workStatuses[0];

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router) {
    this.email = router.snapshot.params.email;
    this.today = new Date();
  }

  ngOnInit(): void {
    this.getUser(this.email);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  private initForm(
    firstName: string = '',
    lastName: string = '',
    phone: string = '',
    linkToSocialNetwork: string = '',
    birthDate: Date = this.today,
    isShowInfo: boolean = true,
    workStatus: string = '',
    imageUrl: string = ''
  ): void {
    this.userProfileForm = new FormGroup({
      firstName: new FormControl(
        firstName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      lastName: new FormControl(
        lastName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      phone: new FormControl(
        phone,
        [Validators.pattern('[- +()0-9]+')]),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork, Validators.maxLength(255)),
      dateOfBirth: new FormControl(birthDate),
      isShowInfo: new FormControl(isShowInfo),
      workStatus: new FormControl(workStatus),
      imageUrl: new FormControl(imageUrl)
    });
  }

  private getUser(email: string): void {
    this.subscriptions.push(this.userService.getByEmail(email)
    .subscribe(
      response => {
        this.user = response;
        this.setViewForWorkStatus(this.user.userInfo.workStatus);
        this.initForm(
          this.user.userInfo.firstName,
          this.user.userInfo.lastName,
          this.user.userInfo.phone,
          this.user.userInfo.linkToSocialNetwork,
          new Date(this.user.userInfo.birthDate),
          this.user.userInfo.showInfo,
          this.user.userInfo.workStatus,
          this.user.userInfo.imageUrl);
      },
      error => {
        this.initForm();
        console.log('error', error);
      }
    ));
  }

  public submit(): void {
    this.sendImage();
    this.updateUserInfo(this.getUserInfo());
  }

  private sendImage(): void {
    if (this.selectedImage != null) {
      const formData = new FormData();
      formData.append('image', this.selectedImage);
      this.subscriptions.push(this.userService.saveImage(formData, this.user.userInfo.userInfoId)
      .subscribe(
        response => console.log(response),
        error => console.log(error)
      ));
    }
  }

  private updateUserInfo(userInfo: UserInfo): void {
    this.subscriptions.push(this.userService.updateUserInfo(userInfo)
    .subscribe(
      response => console.log(response),
      error => console.log(error)
    ));
  }

  private getUserInfo(): UserInfo {
    this.getWorkStatusFromViewValue(this.userProfileForm.controls.workStatus.value);
    const userInfo = this.user.userInfo;

    if (userInfo.workStatus == null) {
      userInfo.workStatus = this.selectedWorkStatus.value;
    }
    userInfo.firstName = this.userProfileForm.controls.firstName.value;
    userInfo.lastName = this.userProfileForm.controls.lastName.value;
    userInfo.phone = this.userProfileForm.controls.phone.value;
    if (userInfo.phone == null) {
      userInfo.phone = '';
    }
    userInfo.linkToSocialNetwork = this.userProfileForm.controls.linkToSocialNetwork.value;
    if (userInfo.linkToSocialNetwork == null) {
      userInfo.linkToSocialNetwork = '';
    }
    userInfo.birthDate = this.getDateFromDatePicker();
    if (userInfo.birthDate == null) {
      userInfo.birthDate = '';
    }
    userInfo.showInfo = this.userProfileForm.controls.isShowInfo.value;

    if (userInfo.generalRating == null) {
      userInfo.generalRating = '5';
    }

    if (userInfo.imageUrl == null) {
      userInfo.imageUrl = '';
    }
    console.log(userInfo);
    return userInfo;
  }

  public loadImage(event): void {
    this.selectedImage = event.target.files[0];
    const fd = new FormData();
    fd.append('image', this.selectedImage, this.selectedImage.name);
    const reader = new FileReader();
    reader.onload = e => this.imageSrc = reader.result;
    reader.readAsDataURL(this.selectedImage);
  }

  public getWorkStatus(value: string): void {
    this.workStatuses.forEach(ws => {
      if (ws.value === value) {
        this.user.userInfo.workStatus = ws.value;
      }
    });
  }

  public getWorkStatusFromViewValue(viewValue: string): void {
    this.workStatuses.forEach(ws => {
      if (ws.viewValue === viewValue) {
        this.user.userInfo.workStatus = ws.value;
      }
    });
  }

  public setViewForWorkStatus(value: string): void {
    this.workStatuses.forEach(ws => {
      if (ws.value === value) {
        this.user.userInfo.workStatus = ws.viewValue;
        this.selectedWorkStatus = ws;
      }
    });
  }

  private getDateFromDatePicker(): string {
    const dateStr = this.userProfileForm.controls.dateOfBirth.value;
    if (dateStr != null) {
      const date = new Date(dateStr);
      console.log(date.toISOString().substring(0, 10));
      return date.toISOString().substring(0, 10);
    } else {
      const date = new Date(this.today);
      return date.toISOString().substring(0, 10);
    }
  }
}
