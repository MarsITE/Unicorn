import { Component, OnInit, ViewChild } from '@angular/core';
import { Project } from '../common/model/project';
import { ProjectService } from '../common/services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MatPaginator } from '@angular/material/paginator';


@Component({
  selector: 'app-start-page',
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {
  id: String;
  counter: number = 1;  
  projects: Project[] = []; 
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
    this.counter = params['page'] || this.counter;
});
  }

  ngOnInit(): void {
    this.getProjects();
  }

  private getProjects() { 
    const params = new HttpParams()
  .set('page', this.counter.toString())
  
      this.projectService.getProjects(params).subscribe(
        (response: Project[]) => {   
          console.log("response",response);     
          this.projects = response;       
        },
        (error) => {
          console.log("error", error);
        }
        ,
        () => {
          console.log("complete");
          this.router.navigateByUrl(`/` + this.counter);
        }
      )
    }

    projectsNext() {     
      this.counter++;       
      this.getProjects()
    }
  
    projectsPrev() {   
      if(this.counter > 1){
        this.counter--; 
      }      
      this.getProjects()
    }
}
