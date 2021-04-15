import { Component, OnInit } from '@angular/core';
import { TokenHelper } from 'src/app/common/helper/token.helper';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnInit {
  isAdmin = false;

  constructor(private tokenHelper: TokenHelper) { }

  ngOnInit(): void {
    // if (this.tokenHelper.getEmailFromToken()) {
    //   const roles = this.tokenHelper.getRoles();
    //   for (let i = 0; i < roles.length; i++) {
    //     if (roles[i] === 'ADMIN'){
    //       this.isAdmin = true;
    //       console.log(this.isAdmin);
    //       break;
    //     }
    //   }
    // }
  }

}
