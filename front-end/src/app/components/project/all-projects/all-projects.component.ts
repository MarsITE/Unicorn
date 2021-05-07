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
  pageEventProjects: PageEvent;
  id: string;
  pageIndex: number = 1;
  pageSize: number = 5;
  sortFlag = false;
  sort = 'desc';
  allPageCount: number;
  projects: Project[] = [];
  userSkills: Skill[] = [];
  userSkillsName: String[] = [];

  constructor(private projectService: ProjectService,private skillService: SkillService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.pageIndex = params['page'] || this.pageIndex;
      this.sort = params.sort || this.sort;
      this.pageSize = params['maxResult'] || this.pageSize;
    });
  }

  ngOnInit(): void {
    this.getProjects(this.pageSize);
    this.getUserSkills();
    this.getAllProjectsCount();
  }

  private getProjects(maxResult: number) {
    const params = new HttpParams()
      .set('page', this.pageIndex.toString())
      .set('sort', this.sort)
      .set('maxResult', maxResult.toString());

    this.projectService.getProjects(this.pageIndex.toString(), this.sort, this.pageSize.toString(), true).subscribe(
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
    return this.userSkillsName.some(s => str == s);
  }

  projectsSort() {
    this.sortFlag = !this.sortFlag;
    this.sort = this.sortFlag ? 'asc' : 'desc';
    this.getProjects(this.pageSize);
  }
  projectsNext() {
    this.pageIndex++;
    this.getProjects(this.pageSize);
  }

  projectsPrev() {
    if (this.pageIndex > 1) {
      this.pageIndex--;
    }
    this.getProjects(this.pageSize);
  }

  deviceObjects = [5, 10, 25];

  selectedDeviceObj = this.deviceObjects[0];
  onChangeObj(newObj) {
    console.log(newObj);
    this.selectedDeviceObj = newObj;
    this.pageSize = this.selectedDeviceObj;
    this.getProjects(this.pageSize);
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
  getPaginatorDataAllProjects(event?: PageEvent) {
    console.log(event);
    if (event.pageIndex + 1 === this.pageIndex + 1) {
      this.pageIndex++;
      this.getProjects(this.pageSize);
    }
    else if (event.pageIndex + 1 === this.pageIndex - 1) {
      if (this.pageIndex > 1) {
        this.pageIndex--;
      }
      this.getProjects(this.pageSize);
    }
    else if (event.pageSize != this.pageIndex) {
      return this.onChangeObj(event.pageSize);
    }
  }
  allProjectListSize(): boolean{
    return this.allPageCount > 5;
  }
}
