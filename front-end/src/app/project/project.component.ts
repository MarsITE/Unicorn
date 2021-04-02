import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  sortFlag: boolean = false;
  sort: string = "desc";
  counter: number = 1;  

  projects: Project[] = []; 
  
  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate', 'owner'];
 
  constructor(private projectService: ProjectService, private router: Router, private http: HttpClient, route: ActivatedRoute) {   
    route.queryParams.subscribe(params => {
    this.counter = +params['page'] || 1;
    this.sort = params['sort'] || "desc";
});
  }  

  ngOnInit(): void {    
    this.getProjects();
  }

 private getProjects() { 

  const params = new HttpParams()
  .set('page', this.counter.toString())
  .set('sort', this.sort)  

    this.projectService.getProjects(params).subscribe(
      (response: Project[]) => {          
        this.projects = response;        
      },
      (error) => {
        console.log("error", error);
      },
      () => {
        console.log("complete"); 
        this.router.navigateByUrl(`projects?page=` + this.counter + `&sort=` + this.sort); 
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
    this.sortFlag = !this.sortFlag    
    if(this.sortFlag){
      this.sort= "asc"
    } else {
      this.sort = "desc"
    }
    this.getProjects() 
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
