import { Component, OnInit, ViewChild } from '@angular/core';
import { Project } from '../../common/model/project';
import { ProjectService } from '../../common/services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';


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

  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      this.counter = params['page'] || this.counter;
      this.maxResult = params['maxResult'] || this.maxResult;
    });
  }


  ngOnInit(): void {
    this.getProjects(this.maxResult);
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
      }

    )
  }
  projectsNext() {
    this.counter++;
    this.getProjects(this.maxResult)
  }

  projectsPrev() {
    if (this.counter > 1) {
      this.counter--;
    }
    this.getProjects(this.maxResult)
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
