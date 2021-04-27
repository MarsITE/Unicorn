import { Component, OnInit,  ViewChild } from '@angular/core';
import { Project } from '../../../common/model/project';
import { ProjectService } from '../../../common/services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';


@Component({
  selector: 'app-all-projects',
  templateUrl: './all-projects.component.html',
  styleUrls: ['./all-projects.component.scss']
})
export class AllProjectsComponent implements OnInit {
  id: string;
  counter: number = 1;
  sortFlag = false;
  sort = 'desc';
  maxResult: number = 5;
  projects: Project[] = [];

  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.counter = params['page'] || this.counter;
      this.sort = params.sort || this.sort;
      this.maxResult = params['maxResult'] || this.maxResult;
    });
  }

  ngOnInit(): void {
    this.getProjects(this.maxResult);
  }
  private getProjects(maxResult: number) {
    const params = new HttpParams()
      .set('page', this.counter.toString())
      .set('sort', this.sort)
      .set('maxResult', maxResult.toString());

    this.projectService.getProjects(this.counter.toString(), this.sort, this.maxResult.toString(), true).subscribe(
      (response: Project[]) => {
        console.log('response', response);
        this.projects = response;
      },
      (error) => {
        console.log('error', error);
      },
      () => {
        console.log('complete');
        // this.router.navigateByUrl(`projects?page=` + this.counter + `&maxResult=` + this.maxResult + `&sort` + this.sort);
      }

    );
  }

  projectsSort() {
    this.sortFlag = !this.sortFlag;
    this.sort = this.sortFlag ? 'asc' : 'desc';
    this.getProjects(this.maxResult);
  }
  projectsNext() {
    this.counter++;
    this.getProjects(this.maxResult);
  }

  projectsPrev() {
    if (this.counter > 1) {
      this.counter--;
    }
    this.getProjects(this.maxResult);
  }

  projectsMaxResult5() {
    this.maxResult = 5;
    this.getProjects(5);
  }

  projectsMaxResult10() {
    this.maxResult = 10;
    this.getProjects(10);
  }

  projectsMaxResult25() {
    this.maxResult = 25;
    this.getProjects(25);
  }

  deviceObjects = [5, 10, 25];

  selectedDeviceObj = this.deviceObjects[0];
  onChangeObj(newObj) {
    console.log(newObj);
    this.selectedDeviceObj = newObj;
    this.maxResult = this.selectedDeviceObj;
    this.getProjects(this.maxResult);
  }
  showProjectDescription(id:any) {
    this.router.navigateByUrl(`projects/${id}`);
  }
  public converToPlainSkills(str: string): string {
      return `#${str.toLowerCase()}`;
  }
  public setLimitOfText(str: string): string{
    if(str.length<=200){
      return str;
    }
    else{
      return `${str.substring(0,200)}...`;
    } 
  }
}
