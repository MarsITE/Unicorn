import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../common/model/user';
import { UserInfo } from '../common/model/user-info';
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
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router) {
    this.email = router.snapshot.params['email'];
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
      firstName: new FormControl(
        firstName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      lastName: new FormControl(
        lastName,
        [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      phone: new FormControl(
        phone,
        [Validators.pattern('^[0-9]*$')]),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork),
      dateOfBirth: new FormControl(dateOfBirth),
      isShowInfo: new FormControl(isShowInfo, [Validators.required]),
      workStatus: new FormControl(workStatus),
      imageUrl: new FormControl(imageUrl)
    });
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
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

  submit() {
    const userInfo = this.user.userInfo;
    userInfo.firstName = this.userProfileForm.controls['firstName'].value;
    userInfo.lastName = this.userProfileForm.controls['lastName'].value;
    userInfo.phone = this.userProfileForm.controls['phone'].value;
    userInfo.linkToSocialNetwork = this.userProfileForm.controls['linkToSocialNetwork'].value;
    userInfo.dateOfBirth = this.userProfileForm.controls['dateOfBirth'].value;
    userInfo.showInfo = this.userProfileForm.controls['isShowInfo'].value;
    userInfo.imageUrl = this.imageSrc;

    // userInfo.workStatus = this.user.userInfo.workStatus;
    // userInfo.imageUrl = this.user.userInfo.imageUrl;

    this.userService.updateUserInfo(userInfo).subscribe(r => {
      console.log(r);

    },
      er => {
        console.log(er);
      },
      () => {
        this.router2.navigateByUrl(`user-profile/${this.user.email}`);
      });

  }

  loadImage(event) {
    let selectedFile = <File>event.target.files[0];
    const fd = new FormData();
    fd.append('image', selectedFile, selectedFile.name);
    const reader = new FileReader();
    reader.onload = e => this.imageSrc = reader.result;
    reader.readAsDataURL(selectedFile);
    //post file
  }
}
