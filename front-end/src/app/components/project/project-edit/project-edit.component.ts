import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Project } from '../../../common/model/project';
import { ProjectStatus } from '../../../common/model/project-status';
import { ProjectService } from '../../../common/services/project.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-project-edit',
  templateUrl: './project-edit.component.html',
  styleUrls: ['./project-edit.component.css']
})
export class ProjectEditComponent implements OnInit, OnDestroy {
  id: string;
  name: string;
  description: string;
  creationDate: string;
  project: Project;
  private subscriptions: Subscription[] = [];

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];

  constructor(private projectService: ProjectService, router: ActivatedRoute, private router2: Router, private toastr: ToastrService) {
    this.id = router.snapshot.params.id;
   }

  ngOnInit(): void {
    this.getProject(this.id);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => {
      s.unsubscribe();
    });
  }


  private getProject(id: string): void {
    this.subscriptions.push(this.projectService.getById(id).subscribe(
      (response: Project) => {
        this.project = response;
        this.project.name,
        this.project.description,
        this.project.projectStatus
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    ));
  }

  update(): void {
    this.projectService.save(this.project)
      .subscribe(data => {
        this.toastr.success('Project has been updated successfully', 'Success!');
      },
      er => {
        console.log(er);
      },
      () => {
        this.router2.navigateByUrl(`projects`);
      });
  };

  public getProjectStatus(value: string): void {
    this.projectStatuses.forEach(ps => {
      if (ps.value === value) {
        this.project.projectStatus = ps.value;
      }
    });
  }


}
