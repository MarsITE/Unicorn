import { USER_ROLE_EMPLOYER, USER_ROLE_WORKER } from './../../../common/helper/token.helper';
import { Component, OnDestroy, OnInit, SecurityContext } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeUrl, SafeHtml, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/common/model/user';
import { WorkStatus } from 'src/app/common/model/work-status';
import { UserHttpService } from 'src/app/common/services/user-http.service';
import { TokenHelper } from 'src/app/common/helper/token.helper';

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
  isWorker: boolean;
  isEmployer: boolean;
  private subscriptions: Subscription[] = [];
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];
  isOnlyWatch: boolean;

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router, private domSanitizer: DomSanitizer,
              private toastr: ToastrService, private tokenHelper: TokenHelper) {
    this.id = this.tokenHelper.getIdFromToken();
    this.idInUrl = router.snapshot.params.id;
    this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl('../../../assets/default-profile-photo.jpg');
    this.isWorker = this.tokenHelper.isUserRole(USER_ROLE_WORKER);
    this.isEmployer = this.tokenHelper.isUserRole(USER_ROLE_EMPLOYER);
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    if (this.idInUrl) {
      this.getUser(this.idInUrl);
      this.isOnlyWatch = true;
    } else {
      this.getUser(this.id);
      this.isOnlyWatch = false;
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
        this.setViewForWorkStatus();
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
}
