import { Component, OnInit } from '@angular/core';
import { Project } from '../common/model/project';
import { ProjectService } from '../common/services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { User } from '../common/model/user';



@Component({
  selector: 'app-start-page',
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {
  id: String;
  owner: User;  
  sortFlag: boolean = false;
  sort: string = "desc";
  counter: number = 1;  
  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'owner'];
  projects: Project[] = []; 

  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, router2: ActivatedRoute) {
    this.counter = router2.snapshot.params.counter;    
    this.sort = router2.snapshot.params.sort;   
  }

  ngOnInit(): void {
    this.getProjects();
  }

  private getProjects() { 
    const params = new HttpParams()
  .set('page', this.counter.toString())
  // .set('sort', this.sort) 
  
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
    projectsSort() {
      this.sortFlag = !this.sortFlag    
      if(this.sortFlag){
        this.sort= "asc"
      } else {
        this.sort = "desc"
      }
      this.getProjects() 
    }
    createProject() {
      this.router.navigateByUrl(`addProjects`);
    }
}
