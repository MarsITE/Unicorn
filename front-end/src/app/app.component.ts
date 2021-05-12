import { FormGroup, FormBuilder } from '@angular/forms';
import { Component } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AuthenticationService } from './common/services/authentication.service';

export const HIDDEN_SIDEBAR = true;
export const SHOWN_SIDEBAR = false;
const GAP_WHEN_IS_SHOWN = 50;
const GAP_WHEN_ISNT_SHOWN = 0;


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  imageBlobUrl: SafeResourceUrl;
  isOpen: boolean;
  options: FormGroup;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    fb: FormBuilder
  ) {
    this.options = fb.group({
      gap: 0
    });
  }

  public isNotLoginOrRegistration(): boolean {
    const isShown = !this.router.url.includes('login') && !(this.router.url.includes('registration'));
    this.options.controls.gap.setValue(isShown ? GAP_WHEN_IS_SHOWN : GAP_WHEN_ISNT_SHOWN);
    return isShown;
  }

  public logout(): void {
    this.toggleSidebar(HIDDEN_SIDEBAR);
    this.authenticationService.logout();
  }

  public navigateByLink(link: string): void {
    this.router.navigateByUrl(link);
  }

  public toggleSidebar(hide = false): void {
    this.isOpen = hide ? false : !this.isOpen;
  }

  public isUserLogedIn(): boolean {
    return this.authenticationService.isRefreshTokenValid();
  }
}
