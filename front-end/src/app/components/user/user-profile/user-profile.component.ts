import { Component, OnDestroy, OnInit, SecurityContext } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, SafeUrl, SafeHtml, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/common/model/user';
import { WorkStatus } from 'src/app/common/model/work-status';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {
  email: string;
  user: User;
  imageUrl: string;
  image: any;
  imageBlobUrl: SafeResourceUrl;
  private subscriptions: Subscription[] = [];
  workStatuses: WorkStatus[] = [
    { value: 'PART_TIME', viewValue: 'Part time' },
    { value: 'FULL_TIME', viewValue: 'Full time' },
    { value: 'OVERTIME', viewValue: 'Overtime' },
    { value: 'BUSY', viewValue: 'Busy' }
  ];

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router, private domSanitizer: DomSanitizer,
    private toastr: ToastrService) {
    this.email = router.snapshot.params.email;
    this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl('../../../assets/default-profile-photo.jpg');
  }
  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.getUser(this.email);
  }

  private getUser(email: string): void {
    this.subscriptions.push(this.userService.getByEmail(email)
    .subscribe(
      response => {
        this.user = response;
        if (this.user.userInfo.imageUrl != null && this.user.userInfo.imageUrl !== '') {
          this.getImage(this.user.userInfo.imageUrl);
        }
        this.setViewForWorkStatus();
      },
      error => console.log('error', error),
    ));
  }

  private getImage(imageUrl: string): void {
    this.subscriptions.push(this.userService.loadImage(imageUrl)
    .subscribe(
      response => {
        this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(response));
        sessionStorage.setItem('avatar', window.URL.createObjectURL(response));
      },
      error => console.log('error', error),
    ));
  }

  public edit(email: string): void {
    this.router2.navigateByUrl(`my-profile-edit/${email}`);
  }

  public delete(email: string): void {
    this.subscriptions.push(this.userService.deleteUser(email).subscribe(
      (response) => {
        this.toastr.success('Deleted', 'Success!');
        this.router2.navigateByUrl('login');
      },
      error => console.log('error', error),
    ));
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
}