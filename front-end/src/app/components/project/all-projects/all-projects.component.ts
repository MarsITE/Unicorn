import { Component, OnInit,  ViewChild } from '@angular/core';
import { Project } from '../../../common/model/project';
import { ProjectService } from '../../../common/services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Skill } from 'src/app/common/model/skill';
import { SkillService } from 'src/app/common/services/skill.service';
import { PageEvent } from '@angular/material/paginator';


@Component({
  selector: 'app-all-projects',
  templateUrl: './all-projects.component.html',
  styleUrls: ['./all-projects.component.scss']
})
export class AllProjectsComponent implements OnInit {
  pageEvent: PageEvent;
  id: string;
  counter: number = 1;
  sortFlag = false;
  sort = 'desc';
  maxResult: number = 5;
  allPageCount: number;
  projects: Project[] = [];
  userSkills: Skill[] = [];
  userSkillsName: String[] = [];

  constructor(private projectService: ProjectService,private skillService: SkillService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.counter = params['page'] || this.counter;
      this.sort = params.sort || this.sort;
      this.maxResult = params['maxResult'] || this.maxResult;
    });
  }

  ngOnInit(): void {
    this.getProjects(this.maxResult);
    this.getUserSkills();
    this.getAllProjectsCount();
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
  private getAllProjectsCount() {
    this.projectService.getAllProjectsCount().subscribe(
      (response: number) => {
        this.allPageCount = response;
        console.log("count", response)
      },
      (error) => {
        console.log("error", error)
      }
    );
  }
  private getUserSkills() {
    this.skillService.getWorkerSkills().subscribe(
      (response: Skill[]) => {
        this.userSkills = response;
        this.userSkillsName = this.userSkills.map(s => s.name);
      },
      (error) => {
        console.log("error", error)
      });
  }
  public isSkillPresentInUser(str: string): boolean {
    for (let index = 0; index < this.userSkillsName.length; index++) {
      if(str === this.userSkillsName[index]){
        return true;
      } else {
        continue;
      }
    }
    return false;
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
  getPaginatorData(event?:PageEvent){
    console.log(event);
    if(event.pageIndex + 1 === this.counter + 1){
      this.projectsNext();
      }
    else if(event.pageIndex + 1 === this.counter - 1){
      this.projectsPrev();
     }   
    else if(event.pageSize != this.counter){
      this.onChangeObj(event.pageSize);
    }
  }
}
