import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import { ProjectStatus } from '../../../common/model/project-status';
import { Project } from '../../../common/model/project';
import { ProjectService } from '../../../common/services/project.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TokenHelper } from 'src/app/common/helper/token.helper';

@Component({
  selector: 'app-project-add',
  templateUrl: './project-add.component.html',
  styleUrls: ['./project-add.component.css']
})
export class ProjectAddComponent implements OnInit {
  projectForm: FormGroup;

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];
  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];

  project: Project = new Project();

  constructor(private router: Router, private projectService: ProjectService,
              private toastr: ToastrService, private tokenHelper: TokenHelper) {
    this.project.projectStatus = this.selectedProjectStatus.value;
    this.initForm();
   }
   ngOnInit(): void {
     console.log('add');
   }

   private initForm(
     name: string = '',
     description: string = '',
     projectStatus: string = this.selectedProjectStatus.viewValue,

   ): void {
     this.projectForm = new FormGroup({
       name: new FormControl(
         name,
         [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
         description: new FormControl(
           description,
           [Validators.required, Validators.minLength(20), Validators.maxLength(2000)]),
           projectStatus: new FormControl(),
     });
   }

  create(): void {
    this.project.ownerId = this.tokenHelper.getIdFromToken();
    this.projectService.save(this.project)
      .subscribe(data => {
        this.toastr.success('Project has been created successfully', 'Success!');
      },
      (error) => {
        this.initForm(
          this.project.name,
          this.project.description,

        );
        this.toastr.error('Project is not unique', 'Error');
      },
      () => {
        this.router.navigateByUrl('projects');
      });
  }

  public getProjectStatus(value: string): void {
    this.projectStatuses.forEach(ps => {
      if (ps.value === value) {
        this.project.projectStatus = ps.value;
      }
    });
  }
}
