import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { TokenHelper } from './common/helper/token.helper';
import { UserHttpService } from './common/services/user-http.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  imageBlobUrl: SafeResourceUrl;
  isOpen: boolean;
  gap: number;

  constructor(
    private router: Router,
    private userService: UserHttpService,
    private domSanitizer: DomSanitizer,
    private tokenHelper: TokenHelper
  ) { }

  ngOnInit(): void {
    // this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(sessionStorage.getItem('avatar')));
    // if (this.imageBlobUrl === null || this.imageBlobUrl === '') {
    //   this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl('../../../assets/default-profile-photo.jpg');
    // }
  }

  public isNotLoginOrRegistration(): boolean {
    const isShow = !this.router.url.includes('login') && !(this.router.url.includes('registration'));
    this.gap = isShow ? 50 : 0;
    return isShow;
  }

  public logout(): void {
    this.toggleSidebar(true);
    this.userService.loggedIn();
    this.router.navigateByUrl('login');
  }

  public navigateByLink(link: string): void {
    this.router.navigateByUrl(link);
  }

  public toggleSidebar(hide?: boolean): void {
    if (hide) {
      this.isOpen = false;
      return;
    }
    this.isOpen = !this.isOpen;
  }

  public isUserLogedIn(): boolean {
    return this.tokenHelper.isValidToken();
  }
}
