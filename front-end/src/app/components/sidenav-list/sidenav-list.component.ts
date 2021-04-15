import { Component, OnInit } from '@angular/core';
import { TokenHelper } from 'src/app/common/helper/token.helper';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.css']
})
export class SidenavListComponent implements OnInit {

  constructor(private tokenHelper: TokenHelper) { }

  ngOnInit(): void {
  }
}
