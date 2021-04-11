import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from '../common/model/project';
import { User } from '../common/model/user';
import { ProjectService } from '../common/services/project.service';
import { Skill } from '../common/model/skill';
import { StorageService } from '../common/services/storage.service';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})

export class ProjectComponent implements OnInit {
  id: String;
  owner: User;
  sortFlag = false;
  sort = 'desc';
  counter = 1;
  maxResult = 5;

  projects: Project[] = [];

  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'owner', 'skills'];

  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, route: ActivatedRoute,
              private storageService: StorageService) {
    route.queryParams.subscribe(params => {
    this.counter = params.page || this.counter;
    this.sort = params.sort || this.sort;
    this.maxResult = params.maxResult || this.maxResult;
});
  }

  ngOnInit(): void {
    this.getProjects();
  }

 private getProjects() {

  const params = new HttpParams()
  .set('page', this.counter.toString())
  .set('sort', this.sort)
  .set('maxResult', this.maxResult.toString())
  .set('Authorization', `Bearer ${this.storageService.getValue('token')}`) ;

  this.projectService.getProjects(this.counter.toString(), this.sort, this.maxResult.toString()).subscribe(
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
    if (this.sortFlag){
      this.sort = 'asc'
    } else {
      this.sort = 'desc'
    }
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
