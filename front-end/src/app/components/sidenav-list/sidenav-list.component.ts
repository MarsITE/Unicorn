import { Component, Input, OnChanges, SimpleChanges  } from '@angular/core';
import { USER_ROLE_ADMIN, TokenHelper } from '../../common/helper/token.helper';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnChanges {
  isItAdmin: boolean;
  @Input() isUserLoggedIn: boolean;

  constructor(private tokenHelper: TokenHelper) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.isItAdmin = this.tokenHelper.isUserRole(USER_ROLE_ADMIN);
  }
}
