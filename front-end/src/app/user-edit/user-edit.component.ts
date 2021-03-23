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
      phone: new FormControl(phone, [Validators.required]),
      linkToSocialNetwork: new FormControl(linkToSocialNetwork, [Validators.required]),
      dateOfBirth: new FormControl(dateOfBirth, [Validators.required]),//todo change to vipadayca
      isShowInfo: new FormControl(isShowInfo, [Validators.required]),////todo change to checkbox
      workStatus: new FormControl(workStatus, [Validators.required]),//todo change to vipadayca
      imageUrl: new FormControl(imageUrl, [Validators.required])
    });
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
        console.log(this.user);
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        this.initForm(
          this.user.userInfo.firstName,
          this.user.userInfo.lastName,
          this.user.userInfo.phone,
          this.user.userInfo.linkToSocialNetwork,
          this.user.userInfo.dateOfBirth,
          this.user.userInfo.showInfo,
          this.user.userInfo.workStatus,
          this.user.userInfo.imageUrl);
        console.log('complete');
      }
    );
  }

  private saveUser(user: User) {
    this.userService.save(user);
  }


  submit() {
    this.saveUser(this.user).subscribe(
      res => {
        console.log(res);
      }
    );
  }

  loadImage(event) {
    let selectedFile = <File> event.target.files[0];
    const fd = new FormData();
    fd.append('image', selectedFile, selectedFile.name);
    //post file
  }
}
