import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSelect } from '@angular/material/select';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { TokenHelper } from 'src/app/common/helper/token.helper';
import { Skill } from 'src/app/common/model/skill';
import { User } from 'src/app/common/model/user';
import { UserInfo } from 'src/app/common/model/user-info';
import { WorkStatus } from 'src/app/common/model/work-status';
import { SkillService } from 'src/app/common/services/skill.service';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})

export class UserEditComponent implements OnInit, OnDestroy {
  id: string;
  user: User;
  userProfileForm: FormGroup;
  imageURL: string;
  imageSrc: any;
  selectedImage: any;
  today: Date;
  skillStrings: string[] = [];
  selectedSkills: string[] = [];

  private skills: Skill[] = [];
  private subscriptions: Subscription[] = [];
  private maxPhotoLength: any = 2048;

  workStatuses: WorkStatus[] = [ // todo
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];
  selectedWorkStatus: WorkStatus = this.workStatuses[0];

  constructor(
    private userService: UserHttpService,
    private router2: Router,
    private tokenHelper: TokenHelper,
    private toastr: ToastrService,
    private skillService: SkillService) {
    this.initForm();
    this.id = this.tokenHelper.getIdFromToken();
    this.today = new Date();
  }

  ngOnInit(): void {
    this.getUser(this.id);
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
    skillStrings: string[] = [],
    isShowInfo: boolean = true,
    workStatus: string = '',
    imageUrl: string = '',
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
      birthDate: new FormControl(birthDate),
      skills: new FormControl(skillStrings),
      isShowInfo: new FormControl(isShowInfo),
      workStatus: new FormControl(workStatus),
      imageUrl: new FormControl(imageUrl)
    });
  }

  private getUser(id: string): void {
    this.subscriptions.push(this.userService.get(id)
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
            this.skillStrings,
            this.user.userInfo.showInfo,
            this.user.userInfo.workStatus,
            this.user.userInfo.imageUrl);
          this.loadAllSkills();
        },
        error => {
          this.initForm();
          this.toastr.error(error, 'Something wrong');
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
      this.subscriptions.push(this.userService.saveImage(formData, this.user.userInfo.userInfoId, this.maxPhotoLength)
        .subscribe(
          response => this.toastr.success('Image successfully saved!', 'Success!'),
          error => this.toastr.error(error, 'Something wrong'),
        ));
    }
  }

  private updateUserInfo(userInfo: UserInfo): void {
    this.subscriptions.push(this.userService.updateUserInfo(userInfo)
      .subscribe(
        response => {
          this.toastr.success('User data successfully saved!', 'Success!');
          this.router2.navigateByUrl('profile');
        },
        error => this.toastr.error(error, 'Something wrong'),
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

    userInfo.skills = this.mapSkills();

    if (userInfo.imageUrl == null) {
      userInfo.imageUrl = '';
    }
    return userInfo;
  }

  public loadImage(event: any): void {
    this.selectedImage = event.target.files[0];
    if (!(this.selectedImage.type.match('jpg') || this.selectedImage.type.match('jpeg') || this.selectedImage.type.match('png'))) {
      this.toastr.error('Invalid format of photo. Choose jpg, png or jpeg', 'error');
      this.selectedImage = undefined;
      return;
    }
    if (this.selectedImage.size > 2097152) {
      this.toastr.error('File is too big! Alowed size less 2 MB', 'error');
      this.selectedImage = undefined;
      return;
    }
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
    const dateStr = this.userProfileForm.controls.birthDate.value;
    if (dateStr != null) {
      const date = new Date(dateStr);
      return date.toISOString().substring(0, 10);
    } else {
      const date = new Date(this.today);
      return date.toISOString().substring(0, 10);
    }
  }


  private loadAllSkills(): void {
    this.skillService.getSkills()
      .subscribe(
        response => {
          this.skills = response;
          this.skillStrings = this.skills.map(s => s.name);
          this.selectedSkills = this.user.userInfo.skills.map(s => s.name);
          this.userProfileForm.controls.skills.setValue(this.selectedSkills);
        },
        error => this.toastr.error(error, 'error')
      );

  }


  public setSelectedSkills(data: any): void {
    if (data.selected) {
      this.selectedSkills.push(data.viewValue);
    } else {
      if (this.selectedSkills.find(x => x === data.viewValue)) {
        this.selectedSkills.splice(
          this.selectedSkills.findIndex(x => x === data.viewValue), 1);
      }
    }
  }

  private mapSkills(): Skill[] {
    const newSkills: Skill[] = [];
    this.selectedSkills.forEach(selectedSkill => {
      const skill = this.skills.find(s => s.name === selectedSkill);
      if (skill) {
        newSkills.push(skill);
      }
    });
    return newSkills;
  }

}
