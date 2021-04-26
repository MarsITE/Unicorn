import { Component, OnInit, ViewChild, ChangeDetectionStrategy } from '@angular/core';
import { Project } from 'src/app/common/model/project';
import { Skill } from 'src/app/common/model/skill';
import { ProjectService } from 'src/app/common/services/project.service';
import { SkillService } from 'src/app/common/services/skill.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-start-page',
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {
  id: String;
  counter: number = 1;
  maxResult: number = 5;
  projects: Project[] = [];
  skills: Skill[] = [];
  skillsName: String[] = [];
  myForm: FormGroup;
  disabled = false;
  ShowFilter = false;
  selectedItems: Array<String> = [];
  dropdownSettings: any = {};

  constructor(private fb: FormBuilder, private projectService: ProjectService, private skillService: SkillService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.counter = params['page'] || this.counter;
      this.maxResult = params['maxResult'] || this.maxResult;
    });
  }

  ngOnInit(): void {
    this.getProjects(this.maxResult);
    this.getSkills();
    this.myForm = this.fb.group({
      skillName: [this.selectedItems]
    });
    this.dropdownSettings = {
      allowSearchFilter: true
    };
  }

  private getProjects(maxResult: number) {
    const params = new HttpParams()
      .set('page', this.counter.toString())
      .set('maxResult', maxResult.toString())

    this.projectService.getProjects(this.counter.toString(), null, this.maxResult.toString()).subscribe(
      (response: Project[]) => {
        console.log("response", response);
        this.projects = response;
      },
      (error) => {
        console.log("error", error);
      });
  }

  private getSearchProject(maxResult: number) {
    const params = new HttpParams()
      .set('page', this.counter.toString())
      .set('maxResult', maxResult.toString())

    this.projectService.getSearchProjects(this.counter.toString(), null, this.maxResult.toString(), this.myForm.value.skillName).subscribe(
      (response: Project[]) => {
        console.log("skillresponse", response);
        this.projects = response;
      },
      (error) => {
        console.log("error", error)
      }
    )
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

  search() {
    if(this.myForm.value.skillName.length == 0){
      this.getProjects(this.maxResult);
    }
    else{
      this.getSearchProject(this.maxResult);
    }
  }

  reset() {
    this.myForm.reset();
    this.getProjects(this.maxResult);
  }

  projectsNext() {
    this.counter++;
    if(this.myForm.value.skillName.length == 0){
      this.getProjects(this.maxResult);
    }
    else{
      this.getSearchProject(this.maxResult);
    }
  }

  projectsPrev() {
    if (this.counter > 1) {
      this.counter--;
    }
    if(this.myForm.value.skillName.length == 0){
      this.getProjects(this.maxResult);
    }
    else{
      this.getSearchProject(this.maxResult);
    }
  }

  deviceObjects = [5, 10, 25];

  selectedDeviceObj = this.deviceObjects[0];
  onChangeObj(newObj) {
    console.log(newObj);
    this.selectedDeviceObj = newObj;
    this.maxResult = this.selectedDeviceObj;
    this.getProjects(this.maxResult);
  }
}
