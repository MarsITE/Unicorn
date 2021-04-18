import { OnDestroy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Skill } from '../../common/model/skill';
import { SkillService } from '../../common/services/skill.service';
import { ConfirmComponent } from '../modals/confirm/confirm.component';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {

  skills: Skill[] = [];
  private subscriptions: Subscription[] = [];

  constructor(private skillService: SkillService, private router: Router, private dialog: MatDialog,
              private toastr: ToastrService) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.getSkills();
  }

  private getSkills(): void {
    this.subscriptions.push(this.skillService.getSkillsDetails().subscribe(
      (response: Skill[]) => {
        this.skills = response.sort((a: Skill) => (a.enabled ? 1 : -1));
      },
      (error) => {
        console.log("error", error);
        this.toastr.error('Skills was not received!', 'Oops!');
      },
      () => {
        console.log("complete");
        console.log(this.skills);
      }
    ));
  }

  createSkill(){
    const confirmDialog = this.dialog.open(ConfirmComponent, {
      data: {
        title: 'Confirm you want to add new skill',
        message: 'Are you sure, you want to add new skill'
      }
    });
    confirmDialog.afterClosed().subscribe(result => {
      if (result === true) {
      //  this.employeeList = this.employeeList.filter(item => item.employeeId !== employeeObj.employeeId);
      }
    });
  }

  showToasts(){
    this.toastr.success('You are awesome!', 'Success!');

    this.toastr.error('This is not good!', 'Oops!');

    this.toastr.warning('You are being warned.', 'Alert!');

    this.toastr.info('Just some information for you.');
  }
}
