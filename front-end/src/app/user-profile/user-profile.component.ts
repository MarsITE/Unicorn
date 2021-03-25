import { Component, OnInit, SecurityContext } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../common/model/user';
import { UserHttpService } from '../common/services/user-http.service';
import { DomSanitizer, SafeUrl, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  email: string;
  user: User;
  imageUrl: string;
  image;
  imageBlobUrl;

  constructor(private userService: UserHttpService, router: ActivatedRoute, private router2: Router, private domSanitizer: DomSanitizer) {
    this.email = router.snapshot.params.email;
  }

  ngOnInit(): void {
    this.getUser(this.email);
  }

  private getUser(email: string): void {
    this.userService.getByEmail(email).subscribe(
      (response: User) => {
        this.user = response;
        this.getImage(this.user.userInfo.imageUrl);
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    );
  }

  private getImage(imageUrl: string): void {
    this.userService.loadImage(imageUrl).subscribe(
      (response) => {
        const objectURL = 'data:image/jpeg;base64,';
        this.imageBlobUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(response));
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    );
  }

  public edit(email: string): void {
    this.router2.navigateByUrl(`user-profile-edit/${email}`);
  }

  public delete(email: string): void {

  }
}
