import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from '../../../common/model/project';
import { ProjectService } from '../../../common/services/project.service';
import { ProjectStatus } from '../../../common/model/project-status';
import { Subscription } from 'rxjs';
import { USER_ROLE_EMPLOYER, TokenHelper } from '../../../common/helper/token.helper';
import { Worker } from '../../../common/model/worker';

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
  isShowButton = false;

  projectStatuses: ProjectStatus[] = [
    { value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker' },
    { value: 'DEVELOPING', viewValue: 'Developing' },
    { value: 'TESTING', viewValue: 'Testing' },
    { value: 'CLOSED', viewValue: 'Closed' }
  ];

  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];


  constructor(private projectService: ProjectService, router: ActivatedRoute, private router2: Router,
              private tokenHelper: TokenHelper) {
    this.id = router.snapshot.params.id;
   }

   ngOnInit(): void {
    this.getProject(this.id);
    this.isEmployer = this.tokenHelper.isUserRole(USER_ROLE_EMPLOYER);
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
        this.isShowButton = this.showButton();
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

  projects(): void {
    this.router2.navigateByUrl(`projects`);
  }

  public edit(id: string): void {
    this.router2.navigateByUrl(`editProject/${id}`);
  }

  workersList(): void {
    this.router2.navigateByUrl(`workers-list/${this.id}`);
  }

  public joinProject(projectId: string): void {
    this.projectService.joinProject(projectId).subscribe(() => {
      this.router2.navigateByUrl(`worker-projects`);
    });
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

  public getApprovedWorkers(workers: Worker[]): Worker[] {
    return workers.filter(worker => worker.isApprove);
  }

  public showButton(): boolean {
    return this.tokenHelper.getIdFromToken() === this.project.ownerId;
  }

}


