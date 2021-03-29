import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../common/model/project';
import { ProjectService } from '../common/services/project.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})

export class ProjectComponent implements OnInit {
  id: String;

  projects: Project[] = []; 
  
  displayedColumns: string[] = ['name', 'projectStatus', 'creationDate'];

  constructor(private projectService: ProjectService, private router: Router) {
  }

  ngOnInit(): void {    
    this.getProjects();
  }

  private getProjects() {
    this.projectService.getProjects().subscribe(
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

}
