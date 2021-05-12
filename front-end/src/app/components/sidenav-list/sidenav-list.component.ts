import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { AuthenticationService } from './../../common/services/authentication.service';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnChanges {
  isAdmin = false;
  isEmployer = false;
  isWorker = false;
  @Input() isUserLoggedIn: boolean;

  constructor(private authenticationService: AuthenticationService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.isUserLoggedIn && changes.isUserLoggedIn.currentValue) {
      this.isAdmin = this.authenticationService.isRoleAdmin();
      this.isEmployer = this.authenticationService.isRoleEmployer();
      this.isWorker = this.authenticationService.isRoleWorker();
    }
  }
}
