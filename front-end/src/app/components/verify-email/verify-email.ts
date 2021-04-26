import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserHttpService } from 'src/app/common/services/user-http.service';

@Component({
  selector: 'app-confirmation-registration',
  templateUrl: './verify-email.html',
  styleUrls: ['./verify-email.css']
})
export class VerifyEmailComponent implements OnInit {
  isValidToken: boolean;
  token: string;

  constructor(
    private route: ActivatedRoute,
    private userService: UserHttpService,
    private router: Router,
    private toast: ToastrService) { }

  ngOnInit(): void {
    const token = this.route.queryParams
      .subscribe(params => {
        this.token = params.token;
      });
    this.checkRegistrationToken(this.token);
  }

  private checkRegistrationToken(token: string): void {
    this.userService.checkRegistrationToken(token)
      .subscribe(
        result => this.isValidToken = result,
        error => this.toast.error(error, 'error'),
      );
  }

  public submit(): void {
    this.router.navigateByUrl('login');
  }

}
