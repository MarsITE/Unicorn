import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { UserHttpService } from './common/services/user-http.service';

export const HIDDEN_SIDEBAR = true;
export const SHOWN_SIDEBAR = false;
const GAP_WHEN_IS_SHOWN  = 50;
const GAP_WHEN_ISNT_SHOWN  = 0;


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  imageBlobUrl: SafeResourceUrl;
  isOpen: boolean;
  gap: number;

  constructor(private router: Router, private userService: UserHttpService, private domSanitizer: DomSanitizer) { }

  public isNotLoginOrRegistration(): boolean {
    const isShown = !this.router.url.includes('login') && !(this.router.url.includes('registration'));
    this.gap = isShown ? GAP_WHEN_IS_SHOWN : GAP_WHEN_ISNT_SHOWN;
    return isShown;
  }

  public logout(): void {    
    this.toggleSidebar(HIDDEN_SIDEBAR);        
    this.userService.loggedIn();
    this.router.navigateByUrl('login');
  }

  public showMyProfile(): void {
    this.router.navigateByUrl('my-profile');
  }

  public toggleSidebar(hide = false): void {
    this.isOpen = hide ? false : !this.isOpen;    
  }
}
