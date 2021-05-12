import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { Project } from 'src/app/common/model/project';
import { Skill } from 'src/app/common/model/skill';
import { ProjectService } from 'src/app/common/services/project.service';
import { SkillService } from 'src/app/common/services/skill.service';
import { AuthenticationService } from './../../common/services/authentication.service';

@Component({
  selector: 'app-start-page',
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {
  pageEventAllProjects: PageEvent;
  pageEventUserProjects: PageEvent;
  pageEventSearchProjects: PageEvent;
  id: String;
  pageIndex: number = 1;
  pageSize: number = 5;
  allPageCount: number;
  allPageBySkillsCount: number;
  projects: Project[] = [];
  workerProjects: Project[] = [];

  skills: Skill[] = [];
  skillsName: String[] = [];

  userSkills: Skill[] = [];
  userSkillsName: String[] = [];

  myForm: FormGroup;
  disabled = false;
  ShowFilter = false;
  selectedItems: Array<String> = [];
  dropdownSettings: any = {};

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private skillService: SkillService,
    private router: Router,
    private authenticationService: AuthenticationService,
    route: ActivatedRoute
  ) {
    route.queryParams.subscribe(params => {
      this.pageIndex = params['page'] || this.pageIndex;
      this.pageSize = params['maxResult'] || this.pageSize;
    });
  }

  ngOnInit(): void {

    this.getUserSkills();
    this.getProjectsByWorkerSkills(this.pageSize);
    this.getProjects(this.pageSize);
    this.getSkills();
    this.getAllProjectsCount();
    this.getAllProjectsBySkillsCount();
    this.myForm = this.fb.group({
      skillName: [this.selectedItems]
    });
    this.dropdownSettings = {
      allowSearchFilter: true
    };
  }

  private getProjects(pageSize: number) {
    const params = new HttpParams()
      .set('page', this.pageIndex.toString())
      .set('maxResult', pageSize.toString())

    this.projectService.getProjects(this.pageIndex.toString(), null, this.pageSize.toString(), true).subscribe(
      (response: Project[]) => {
        this.projects = response;
      },
      (error) => {
        console.log("error", error);
      });
  }

  private getSearchProject(pageSize: number) {
    const params = new HttpParams()
      .set('page', this.pageIndex.toString())
      .set('maxResult', pageSize.toString())

    this.projectService.getSearchProjects(this.pageIndex.toString(), null, this.pageSize.toString(), this.myForm.value.skillName).subscribe(
      (response: Project[]) => {
        this.projects = response;
      },
      (error) => {
        console.log("error", error)
      }
    );
  }

  private getProjectsByWorkerSkills(pageSize: number) {
    const params = new HttpParams()
      .set('page', this.pageIndex.toString())
      .set('maxResult', pageSize.toString())

    this.projectService.getProjectsByUserSkills(this.pageIndex.toString(), null, this.pageSize.toString()).subscribe(
      (response: Project[]) => {
        this.workerProjects = response;
      },
      (error) => {
        console.log("error", error)
      }
    );
  }

  private getSkills() {
    this.skillService.getSkills().subscribe(
      (response: Skill[]) => {
        this.skills = response;
        this.skillsName = this.skills.map(s => s.name);
      },
      (error) => {
        console.log("error", error)
      });
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

  private getAllProjectsBySkillsCount() {
    this.projectService.getAllProjectsCountBySkills().subscribe(
      (response: number) => {
        this.allPageBySkillsCount = response;
      },
      (error) => {
        console.log("error", error)
      }
    );
  }

  search() {
    if (this.myForm.value.skillName.length == 0) {
      this.getProjects(this.pageSize);
    }
    else {
      this.getSearchProject(this.pageSize);
    }
  }

  reset() {
    this.myForm.reset();
    this.getProjects(this.pageSize);
  }

  projectsNext() {
    this.pageIndex++;
    if (this.myForm.value.skillName.length == 0) {
      this.getProjects(this.pageSize);
    }
    else {
      this.getSearchProject(this.pageSize);
    }
  }

  userProjectsNext() {
    this.pageIndex++;
    this.getProjectsByWorkerSkills(this.pageSize);
  }

  projectsPrev() {
    if (this.pageIndex > 1) {
      this.pageIndex--;
    }
    if (this.myForm.value.skillName.length == 0) {
      this.getProjects(this.pageSize);
    }
    else {
      this.getSearchProject(this.pageSize);
    }
  }

  userProjectsPrev() {
    if (this.pageIndex > 1) {
      this.pageIndex--;
    }
    this.getProjectsByWorkerSkills(this.pageSize);
  }

  deviceObjects = [5, 10, 25];

  selectedDeviceObj = this.deviceObjects[0];
  onChangeObjAll(newObj) {
    console.log(newObj);
    this.selectedDeviceObj = newObj;
    this.pageSize = this.selectedDeviceObj;
    this.getProjects(this.pageSize);
  }
  onChangeObjUser(newObj) {
    console.log(newObj);
    this.selectedDeviceObj = newObj;
    this.pageSize = this.selectedDeviceObj;
    this.getProjectsByWorkerSkills(this.pageSize);
  }

  showProjectDescription(id: any) {
    this.router.navigateByUrl(`projects/${id}`);
  }
  public converToPlainSkills(str: string): string {
    return `#${str.toLowerCase()}`;
  }
  public setLimitOfText(str: string): string {
    if (str.length <= 200) {
      return str;
    }
    else {
      return `${str.substring(0, 200)}...`;
    }
  }
  public isUserLogedIn(): boolean {
    return this.authenticationService.isRefreshTokenValid();
  }
  public navigateByLink(link: string): void {
    this.router.navigateByUrl(link);
  }

  public isSkillPresentInUser(str: string): boolean {
    return this.userSkillsName.some(s => str == s);
  }

  public isUserHasSkills(): boolean {
    return this.userSkills.length != 0;
  }

  getPaginatorDataUser(event?: PageEvent) {
    console.log(event);
    if (event.pageIndex + 1 === this.pageIndex + 1) {
      this.userProjectsNext();
    }
    else if (event.pageIndex + 1 === this.pageIndex - 1) {
      this.userProjectsPrev();
    }
    else if (event.pageSize != this.pageSize) {
      this.onChangeObjUser(event.pageSize);
    }
  }

  getPaginatorData(event?: PageEvent) {
    console.log(event);
    if (event.pageIndex + 1 === this.pageIndex + 1) {
      this.projectsNext();
    }
    else if (event.pageIndex + 1 === this.pageIndex - 1) {
      this.projectsPrev();
    }
    else if (event.pageSize != this.pageSize) {
      this.onChangeObjAll(event.pageSize);
    }
  }

  userProjectListSize(): boolean {
    return this.allPageBySkillsCount > 5;
  }

  allProjectListSize(): boolean {
    return this.allPageCount > 5;
  }

isProjectDBIsEmpty(): boolean {
  return this.allPageCount == 0;
}

isThereAreProjectsWithUserSkills(): boolean {
  return this.workerProjects.length == 0;
}
}
