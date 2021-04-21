import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-confirmation-registration',
  templateUrl: './confirmation-registration.component.html',
  styleUrls: ['./confirmation-registration.component.css']
})
export class ConfirmationRegistrationComponent implements OnInit {
  isValidToken: boolean;
  token: string;

  constructor(private route: ActivatedRoute, private userService: UserHttpService) { }

  ngOnInit(): void {
    const token = this.route.queryParams
      .subscribe(params => {
        this.token = params.token;
      });
    console.log(this.token);
    this.checkRegistrationToken(this.token);
  }

  private checkRegistrationToken(token: string): void {
    this.userService.checkRegistrationToken(token)
      .subscribe(
        result => this.isValidToken = result,
        error => console.log(error),
      );
  }

}
