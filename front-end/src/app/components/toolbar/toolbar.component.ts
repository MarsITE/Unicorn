import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  imageBlobUrl: SafeResourceUrl;
  constructor(private userService: UserHttpService , private router: Router, private domSanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(sessionStorage.getItem('avatar')));
    if (this.imageBlobUrl === null || this.imageBlobUrl === '') {
      this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl('../../../assets/default-profile-photo.jpg');
    }
  }

  public logout(): void {
    this.userService.loggedIn();
    this.router.navigateByUrl('login');
  }
}
