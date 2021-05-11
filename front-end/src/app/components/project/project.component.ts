import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from '../../common/model/project';
import { User } from '../../common/model/user';
import { ProjectService } from '../../common/services/project.service';
import { AuthenticationService } from './../../common/services/authentication.service';

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

  projects: Project[] = [];

  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'skills'];

  constructor(private projectService: ProjectService, private router: Router,
              private authenticationService: AuthenticationService, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.counter = params.page || this.counter;
      this.sort = params.sort || this.sort;
      this.maxResult = params.maxResult || this.maxResult;
      this.ownerId = this.authenticationService.getIdFromToken();
    });
  }

  ngOnInit(): void {
    this.getProjects();
    this.isEmployer = this.authenticationService.isRoleEmployer();

    console.log('This get project.', this.projects.length);
  }

  private getProjects() {
    const params = new HttpParams()
      .set('page', this.counter.toString())
      .set('sort', this.sort)
      .set('maxResult', this.maxResult.toString())
      .set('Authorization', `Bearer ${sessionStorage.getItem('access_token')}`);

    this.projectService.getProjects(this.counter.toString(), this.sort, this.maxResult.toString(), false).subscribe(
      (response: Project[]) => {
        this.projects = response;
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
        this.router.navigateByUrl(`projects?page=` + this.counter + `&maxResult=` + this.maxResult + `&sort=` + this.sort);
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
    if (this.counter > 1) {
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
