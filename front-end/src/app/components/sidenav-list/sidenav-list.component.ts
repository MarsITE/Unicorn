import { Component, Input, OnChanges, SimpleChanges  } from '@angular/core';
import { USER_ROLE_ADMIN, USER_ROLE_EMPLOYER, USER_ROLE_WORKER, TokenHelper } from '../../common/helper/token.helper';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnChanges {
  isAdmin: boolean  = false;
  isEmployer: boolean = false;
  isWorker: boolean = false;
  @Input() isUserLoggedIn: boolean;

  constructor(private tokenHelper: TokenHelper) { }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes.isUserLoggedIn && changes.isUserLoggedIn.currentValue){
      this.isAdmin = this.tokenHelper.isUserRole(USER_ROLE_ADMIN);
      this.isEmployer = this.tokenHelper.isUserRole(USER_ROLE_EMPLOYER);
      this.isWorker = this.tokenHelper.isUserRole(USER_ROLE_WORKER);      
    } 
  }
}
