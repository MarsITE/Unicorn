import { Component, OnInit } from '@angular/core';
import { USER_ROLE_ADMIN, USER_ROLE_WORKER, TokenHelper } from '../../common/helper/token.helper';

@Component({
  selector: 'app-skills',
  templateUrl: './skills.component.html',
  styleUrls: ['./skills.component.css']
})

export class SkillsComponent implements OnInit {
  isAdmin: boolean;
  isWorker: boolean;

  constructor(private tokenHelper: TokenHelper) { }

  ngOnInit(): void {
    this.isAdmin = this.tokenHelper.isUserRole(USER_ROLE_ADMIN);
    this.isWorker = this.tokenHelper.isUserRole(USER_ROLE_WORKER);
  }
}
