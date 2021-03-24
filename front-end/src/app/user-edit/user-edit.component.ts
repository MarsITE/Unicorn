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
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time'},
    { value: 'FULL_TIME', viewValue: 'Full time'},
    { value: 'OVERTIME', viewValue: 'Overtime'},
    { value: 'BUSY', viewValue: 'Busy'}
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
      firstName: new FormControl(firstName, [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      lastName: new FormControl(lastName, [Validators.required, Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(20), Validators.minLength(2)]),
      phone: new FormControl(phone),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork),
      dateOfBirth: new FormControl(dateOfBirth),//todo change to date
      isShowInfo: new FormControl(isShowInfo, [Validators.required]),
      workStatus: new FormControl(workStatus),//todo change to vipadayca
      imageUrl: new FormControl(imageUrl)
    });
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
        console.log(this.user);
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

  private saveUser(user: User) {
    this.userService.save(user);
  }


  submit() {
    this.saveUser(this.user);
    this.router2.navigateByUrl(`user-profile/${this.user.email}`);
  }

  loadImage(event) {
    let selectedFile = <File> event.target.files[0];
    const fd = new FormData();
    fd.append('image', selectedFile, selectedFile.name);
    const reader = new FileReader();
    reader.onload = e => this.imageSrc = reader.result;
    reader.readAsDataURL(selectedFile);
    //post file
  }
}
