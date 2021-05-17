import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { User } from 'src/app/common/model/user';
import { WorkStatus } from 'src/app/common/model/work-status';
import { UserHttpService } from 'src/app/common/services/user-http.service';
import { USER_ROLE_EMPLOYER, USER_ROLE_WORKER } from 'src/app/constants';

import { Skill } from '../../../common/model/skill';
import { AuthenticationService } from './../../../common/services/authentication.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {
  id: string;
  idInUrl: string;
  user: User;
  imageUrl: string;
  image: any;
  imageBlobUrl: SafeResourceUrl;
  isWorker = false;
  isEmployer = false;
  isAuthUser = false;
  isShowInfo = false;
  skills: Skill[] = [];
  private subscriptions: Subscription[] = [];
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];
  isOnlyWatch: boolean;

  constructor(
    protected route: ActivatedRoute,
    private userService: UserHttpService,
    private router2: Router,
    private domSanitizer: DomSanitizer,
    private toastr: ToastrService,
    private authenticationService: AuthenticationService
  ) {
    this.idInUrl = route.snapshot.params.id;
    this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl('../../../assets/default-profile-photo.jpg');
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    if (this.idInUrl) {
      this.getUser(this.idInUrl);
    } else {
      this.getUser(this.authenticationService.getIdFromToken());
    }
  }

  private getUser(id: string): void {
    this.subscriptions.push(this.userService.get(id)
      .subscribe(
        response => {
          this.user = response;
          if (this.user.userInfo.imageUrl != null && this.user.userInfo.imageUrl !== '') {
            this.getImage(this.user.userInfo.imageUrl);
          }
          this.skills = this.user.userInfo.skills.sort((a: Skill) => (a.enabled ? -1 : 1));
          this.setViewForWorkStatus();
          if (this.authenticationService.getIdFromToken() === id) {
            this.isOnlyWatch = true;
          } else {
            this.isOnlyWatch = false;
          }
          this.isWorker = this.isRoleWorker();
          this.isEmployer = this.isRoleEmployer();
          this.isAuthUser = this.authenticationService.userValue.email === this.user.email;
          if (this.isAuthUser) {
            this.isShowInfo = true;
          } else if (this.isAuthUser && this.user.userInfo.showInfo) {
            this.isShowInfo = true;
          } else {
            this.isShowInfo = false;
          }
        },
        error => this.toastr.error('Can not load user info!', 'Something wrong'),
      ));
  }

  private getImage(imageUrl: string): void {
    this.subscriptions.push(this.userService.loadImage(imageUrl)
      .subscribe(
        response => {
          this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(response));
          sessionStorage.setItem('avatar', window.URL.createObjectURL(response));
        },
        error => this.toastr.error('Can not load user photo!', 'Something wrong'),
      ));
  }

  public edit(): void {
    this.router2.navigateByUrl(`profile-edit`);
  }

  public setViewForWorkStatus(): void {
    this.workStatuses.forEach(ws => {
      if (ws.value === this.user.userInfo.workStatus) {
        this.user.userInfo.workStatus = ws.viewValue;
      }
    });
  }
  public converToPlain(str: string): string {
    return str.toLowerCase();
  }

  public converToPlainSkills(str: string): string {
    return `#${str.toLowerCase()}`;
  }

  private isRoleWorker(): boolean {
    return this.user.roles.find(role => role === USER_ROLE_WORKER) != null;
  }

  private isRoleEmployer(): boolean {
    return this.user.roles.find(role => role === USER_ROLE_EMPLOYER) != null;
  }
}
