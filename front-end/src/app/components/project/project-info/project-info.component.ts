import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { USER_ROLE_EMPLOYER } from 'src/app/constants';
import { Project } from '../../../common/model/project';
import { ProjectStatus } from '../../../common/model/project-status';
import { ProjectService } from '../../../common/services/project.service';
import { AuthenticationService } from './../../../common/services/authentication.service';

@Component({
  selector: 'app-project-info',
  templateUrl: './project-info.component.html',
  styleUrls: ['./project-info.component.css']
})
export class ProjectInfoComponent implements OnInit, OnDestroy {

  id: string;
  name: string;
  description: string;
  creationDate: string;
  project: Project;
  private subscriptions: Subscription[] = [];
  isEmployer: boolean;

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];

  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];


  constructor(private projectService: ProjectService,
              private authenticationService: AuthenticationService, router: ActivatedRoute, private router2: Router,
              ) {
    this.id = router.snapshot.params.id;
   }

   ngOnInit(): void {
    this.getProject(this.id);
    this.isEmployer = this.authenticationService.isRoleEmployer();
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
        this.setViewProjectStatus();
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
      }
    ));
  }

  public setViewProjectStatus(): void {
    this.projectStatuses.forEach(ps => {
      if (ps.value === this.project.projectStatus) {
        this.project.projectStatus = ps.viewValue;
      }
    });
  }

  projects() {
    this.router2.navigateByUrl(`projects`);
  }

  public edit(id: string): void {
    this.router2.navigateByUrl(`editProject/${id}`);
  }

  public delete(id: string): void {
    this.subscriptions.push(this.projectService.deleteProject(id).subscribe(
      (response) => {
        console.log('deleted');
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
        this.router2.navigateByUrl(`projects`);
      }
    ));
  }

}


