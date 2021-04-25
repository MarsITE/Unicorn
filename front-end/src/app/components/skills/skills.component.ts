import { OnDestroy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Skill } from '../../common/model/skill';
import { SkillService } from '../../common/services/skill.service';
import { ConfirmComponent } from '../modals/confirm/confirm.component';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { COMMA, ENTER, SEMICOLON } from '@angular/cdk/keycodes';
import { MatChipInputEvent } from '@angular/material/chips';
import { TokenHelper, USER_ROLE_ADMIN } from '../../common/helper/token.helper';

@Component({
  selector: 'app-skills',
  templateUrl: './skills.component.html',
  styleUrls: ['./skills.component.css']
})
export class SkillsComponent implements OnInit, OnDestroy {

  skills: Skill[] = [];
  approvedSkills: Skill[] = [];
  unapprovedSkills: Skill[] = [];
  isItAdmin: boolean;
  panelOpenState = false;
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, SEMICOLON];

  private subscriptions: Subscription[] = [];

  constructor(private skillService: SkillService, private dialog: MatDialog,
    private toastr: ToastrService, private tokenHelper: TokenHelper) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }

  ngOnInit(): void {
    this.isItAdmin = this.tokenHelper.isUserRole(USER_ROLE_ADMIN);
    this.isItAdmin ? this.getSkillsDetails() : this.getSkills();

  }

  private sortByEnabledAcs(s: Skill): number {
    return s.enabled ? 1 : -1;
  }

  getSkills(): void {
    this.subscriptions.push(this.skillService.getSkills().subscribe(
      (response: Skill[]) => {
        this.skills = response.sort((a: Skill) => (a.enabled ? 1 : -1));
      },
      (error) => { this.toastr.error('Skills was not received!', 'Error!'); }
    ));
  }

  getSkillsDetails(): void {
    this.subscriptions.push(this.skillService.getSkillsDetails().subscribe(
      (response: Skill[]) => {
        this.skills = response;
        this.approvedSkills = this.skills.filter(s => s.enabled).sort(this.sortByEnabledAcs);
        this.unapprovedSkills = this.skills.filter(s => !s.enabled).sort(this.sortByEnabledAcs);
      },
      (error) => { this.toastr.error('Skills was not received!', 'Oops!'); }
    ));
  }

  approveAllUnapprovedSkills(): void {
    const confirmDialog = this.dialog.open(ConfirmComponent, {
      data: {
        title: 'Confirm you want to approve all unapproved skills',
        message: 'Are you sure, you want to approve all unapproved skills?'
      }
    });
    confirmDialog.afterClosed().subscribe(result => {
      if (!!result) {
        this.approvedSkills = this.approvedSkills.concat(...this.unapprovedSkills).sort(this.sortByEnabledAcs);
        this.unapprovedSkills = [];
      }
    });
  }

  approveUnapprovedSkill(s: Skill): void {
    const index = this.unapprovedSkills.indexOf(s);
    if (index >= 0) {
      s.enabled = true;
      this.skillService.update(s)
        .subscribe(data => {
          this.approvedSkills = this.approvedSkills.concat(this.unapprovedSkills.splice(index, 1)).sort(this.sortByEnabledAcs);
          this.toastr.success('Skill has been approved successfully', 'Success!');
        },
          er => {
            console.log(er);
          });
    }
  }

  deleteAllUnapprovedSkill(): void {
    const confirmDialog = this.dialog.open(ConfirmComponent, {
      data: {
        title: 'Confirm you want to delete all unapproved skills',
        message: 'Are you sure, you want to all unapproved skills ?'
      }
    });
    confirmDialog.afterClosed().subscribe(result => {
      if (!!result) {
        this.unapprovedSkills = [];
      }
    });
  }

  deleteUnapprovedSkill(s: Skill): void {
    const confirmDialog = this.dialog.open(ConfirmComponent, {
      data: {
        title: 'Confirm you want to delete the skill',
        message: 'Are you sure, you want to delete the skill ' + s.name + '?'
      }
    });
    confirmDialog.afterClosed().subscribe(result => {
      if (result === true) {
        const index = this.unapprovedSkills.indexOf(s);
        if (index >= 0) {
          this.unapprovedSkills.splice(index, 1);
        }
      }
    });
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add new skill
    if ((value || '').trim()) {
      const skillName: string = value.trim();
      this.skillService.save({ skillId: '', name: skillName, enabled: true })
        .subscribe(data => {
          const message = `Skill ${skillName} has been created successfully`;
          this.approvedSkills.push({ skillId: '', name: value.trim(), enabled: true });
          this.approvedSkills = this.approvedSkills.sort(this.sortByEnabledAcs);
          this.toastr.success(message, 'Success!');
        },
          errMessage => { this.toastr.warning(errMessage, 'Warning!'); }
        );
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }
}
