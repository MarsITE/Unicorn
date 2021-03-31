import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../common/model/project';
import { User } from '../common/model/user';
import { ProjectService } from '../common/services/project.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})

export class ProjectComponent implements OnInit {
  id: String;
  owner: User;
  page: string;
  sort: boolean;

  counter: number = 1;
  increment() { this.counter++; }
  decrement() { this.counter--; }

  

  projects: Project[] = []; 
  
  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'owner'];
 
  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient) {
  }

  ngOnInit(): void {    
    this.getProjects();
  }

 private getProjects() {

 

  const params = new HttpParams()

    this.projectService.getProjects(params).subscribe(
      (response: Project[]) => {          
        this.projects = response;
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete");
      }
    )
  }

  showProjectDescription(row: any) {
    this.router.navigateByUrl(`projects/${row.id}`);
  }

  createProject() {
    this.router.navigateByUrl(`addProjects`);
  }

  projectsSort() {
    this.sort = !this.sort     
 
    const params = new HttpParams()  
    .set('sort', (this.sort).toString())
  
      this.projectService.getProjects(params).subscribe(
        (response: Project[]) => {          
          this.projects = response;
        },
        (error) => {
          console.log("error", error);
        },
        () => {
          console.log("complete");
        }
      )
  }

  projectsNext() {   

    this.increment();
    
    const params = new HttpParams() 
    
    .set('page', this.counter.toString())    
  
      this.projectService.getProjects(params).subscribe(
        (response: Project[]) => {          
          this.projects = response;
        },
        (error) => {
          console.log("error", error);
        },
        () => {
          console.log("complete");
        }
      )
  }

  projectsPrev() {   

    this.decrement();
    
    const params = new HttpParams() 
    
    .set('page', this.counter.toString())    
  
      this.projectService.getProjects(params).subscribe(
        (response: Project[]) => {          
          this.projects = response;
        },
        (error) => {
          console.log("error", error);
        },
        () => {
          console.log("complete");
        }
      )
  }

}
