import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from '../../common/model/project';
import { User } from '../../common/model/user';
import { ProjectService } from '../../common/services/project.service';
import { TokenHelper, USER_ROLE_EMPLOYER } from '../../common/helper/token.helper';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})

export class ProjectComponent implements OnInit {
  ownerId: string;
  owner: User;
  sortFlag = false;
  sort = 'desc';
  counter = 1;
  maxResult = 5;
  isEmployer: boolean;
  involvedInProjects: boolean;

  projects: Project[] = [];

  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'skills', 'isApprove'];

  constructor(private projectService: ProjectService, private router: Router,
              private http: HttpClient, route: ActivatedRoute, private tokenHelper: TokenHelper) {
    route.queryParams.subscribe(params => {
        this.counter = params.page || this.counter;
        this.sort = params.sort || this.sort;
        this.maxResult = params.maxResult || this.maxResult;
        this.ownerId = this.tokenHelper.getIdFromToken();
        this.involvedInProjects = this.router.url.indexOf('/worker-projects') > -1;
    });
  }

  ngOnInit(): void {
    this.getProjects();
    this.isEmployer = this.tokenHelper.isUserRole(USER_ROLE_EMPLOYER);

    console.log('This get project.', this.projects.length);
  }

 private getProjects() {
  let req;
  if (this.involvedInProjects) {
     req = this.projectService.getWorkerProjects(this.counter.toString(), this.sort);
     if (this.displayedColumns.indexOf('isApprove') < 0) {
       this.displayedColumns.push('isApprove');
     }
   } else {
     req = this.projectService.getProjects(this.counter.toString(), this.sort, this.maxResult.toString(), false);
     let indexOf = this.displayedColumns.indexOf('isApprove');
     if (indexOf > -1) {
       this.displayedColumns.splice(indexOf, 1);
     }
   }

  req.subscribe(
      (response: Project[]) => {
        this.projects = response;
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
        let path = this.involvedInProjects ? 'worker-projects' : 'projects';
        this.router.navigateByUrl(`${path}?page=` + this.counter + `&maxResult=` + this.maxResult + `&sort=` + this.sort);
      }
    );
  }

  showProjectDescription(row: any) {
    this.router.navigateByUrl(`projects/${row.id}`);
  }

  createProject() {
    this.router.navigateByUrl(`addProjects`);
  }

  projectsSort() {
    this.sortFlag = !this.sortFlag;
    this.sort = this.sortFlag ? 'asc' : 'desc';
    this.getProjects();
  }

  projectsNext() {
    this.counter++;
    this.getProjects();
  }

  projectsPrev() {
    if (this.counter > 1){
      this.counter--;
    }
    this.getProjects();
  }

  projectsMaxResult5() {
    this.maxResult = 5;
    this.getProjects();
  }

  projectsMaxResult10() {
    this.maxResult = 10;
    this.getProjects();
  }

  projectsMaxResult25() {
    this.maxResult = 25;
    this.getProjects();
  }

}
