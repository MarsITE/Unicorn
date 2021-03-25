import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../common/model/user';
import { WorkStatus } from '../common/model/work-status';
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
  imageURL: string;
  imageSrc: any;
  selectedImage: any;
  today: Date;
  selectedWorkStatus: WorkStatus;
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router) {
    this.email = router.snapshot.params.email;
    this.initForm('', '', '', '', '', false, '', '');
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
    this.today = new Date();
    this.userProfileForm = new FormGroup({
      firstName: new FormControl(
        firstName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      lastName: new FormControl(
        lastName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      phone: new FormControl(
        phone,
        [Validators.required, Validators.pattern('[- +()0-9]+')]),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork),
      dateOfBirth: new FormControl(dateOfBirth),
      isShowInfo: new FormControl(isShowInfo),
      workStatus: new FormControl(workStatus),
      imageUrl: new FormControl(imageUrl)
    });
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
        this.setViewForWorkStatus(this.user.userInfo.workStatus);
        this.initForm(
          this.user.userInfo.firstName,
          this.user.userInfo.lastName,
          this.user.userInfo.phone,
          this.user.userInfo.linkToSocialNetwork,
          this.user.userInfo.dateOfBirth,
          this.user.userInfo.showInfo,
          this.user.userInfo.workStatus,
          this.user.userInfo.imageUrl);
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    );
  }

  public submit(): void {
    const userInfo = this.user.userInfo;
    userInfo.firstName = this.userProfileForm.controls.firstName.value;
    userInfo.lastName = this.userProfileForm.controls.lastName.value;
    userInfo.phone = this.userProfileForm.controls.phone.value;
    userInfo.linkToSocialNetwork = this.userProfileForm.controls.linkToSocialNetwork.value;
    userInfo.dateOfBirth = this.userProfileForm.controls.dateOfBirth.value;
    userInfo.showInfo = this.userProfileForm.controls.isShowInfo.value;
    if (this.selectedImage != null) {
      const formData = new FormData();
      formData.append('image', this.selectedImage);
      this.userService.saveImage(formData, this.user.userInfo.userInfoId).subscribe(
        response => {
          console.log(response);
        },
        error => {
          console.log(error);
        },
        () => {
          console.log('image saved');
        }
      );
    }

    this.userService.updateUserInfo(userInfo).subscribe(response => {
      console.log(response);
    },
      error => {
        console.log(error);
      },
      () => {
        this.router2.navigateByUrl(`user-profile/${this.user.email}`);
      });

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
    console.log(value);
    this.workStatuses.forEach(ws => {
      if (ws.value === value) {
        this.user.userInfo.workStatus = ws.value;
        console.log(this.user.userInfo.workStatus);
      }
    });
  }

  public setViewForWorkStatus(value: string): void {
    this.workStatuses.forEach(ws => {
      if (ws.value === value) {
        console.log(this.user.userInfo.workStatus);
        this.user.userInfo.workStatus = ws.viewValue;
        this.selectedWorkStatus = ws;
        console.log(this.selectedWorkStatus);
      }
    });
  }
}
