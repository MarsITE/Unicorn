import {Component, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Project} from '../../../common/model/project';
import {ProjectStatus} from '../../../common/model/project-status';
import {ProjectService} from '../../../common/services/project.service';
import {ToastrService} from 'ngx-toastr';
import {MatSelect} from '@angular/material/select';
import {Skill} from 'src/app/common/model/skill';
import {SkillService} from 'src/app/common/services/skill.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-project-edit',
  templateUrl: './project-edit.component.html',
  styleUrls: ['./project-edit.component.css']
})
export class ProjectEditComponent implements OnInit {
  id: string;
  project: Project;
  public allSkills: Skill[] = [];

  projectStatuses: ProjectStatus[] = [
    {value: 'LOOKING_FOR_WORKER', viewValue: 'Looking for worker'},
    {value: 'DEVELOPING', viewValue: 'Developing'},
    {value: 'TESTING', viewValue: 'Testing'},
    {value: 'CLOSED', viewValue: 'Closed'}
  ];
  selectedProjectStatus: ProjectStatus = this.projectStatuses[0];

  @ViewChild('skillsSelect') skillsSelect: MatSelect;
  projectEditForm: FormGroup;

  constructor(private projectService: ProjectService, router: ActivatedRoute, private router2: Router,
              private toastr: ToastrService, private skillService: SkillService) {
    this.id = router.snapshot.params.id;
    this.initForm();
  }

  ngOnInit(): void {
    this.getProject(this.id);
  }

  public skillComparison(skill1: Skill, skill2: Skill): boolean {
    return skill1 && skill2 ? skill1.skillId === skill2.skillId : skill1 === skill2;
  }

  private initForm(): void {
    this.projectEditForm = new FormGroup({
      name: new FormControl(
        '',
        [Validators.required, Validators.minLength(2), Validators.maxLength(50)]),
      description: new FormControl(
        '',
        [Validators.required, Validators.minLength(20), Validators.maxLength(2000)]),

      projectStatus: new FormControl(''),
      skills: new FormControl([]),
    });
  }

  private getProject(id: string): void {
    this.projectService.getById(id)
      .pipe(first())
      .subscribe(
        (response: Project) => {
          this.project = response;
          console.log('Project', this.project);
          this.projectEditForm.setValue({
            name: this.project.name,
            description: this.project.description,
            skills: this.project.skills,
            projectStatus: this.getProjectStatusValue(this.project.projectStatus)
          });
          this.loadAllSkills();
        },
        (error) => {
          console.log('error', error);
        },
        () => {
          console.log('complete');
        }
      );
  }

  update(): void {
    this.projectService.update(this.id, this.projectEditForm.value)
      .subscribe(data => {
          this.toastr.success('Project has been updated successfully', 'Success!');
        },
        (error) => {
          this.toastr.error('Project is not unique', 'Error');
        },
        () => {
          this.router2.navigateByUrl(`projects`);
        });
  }

  private loadAllSkills(): void {
    this.skillService.getSkills()
      .pipe(first())
      .subscribe(
        response => {
          this.allSkills = response;
        },
        error => this.toastr.error(error, 'error')
      );
  }

  private getProjectStatusValue(projectStatusViewValue: string) {
    let projectStatusValue = null;
    this.projectStatuses.forEach(ps => {
      if (ps.viewValue === projectStatusViewValue) {
        projectStatusValue = ps.value;
      }
    });
    return projectStatusValue;
  }
}
