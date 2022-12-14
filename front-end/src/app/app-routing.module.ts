import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { UserProfileComponent } from './components/user/user-profile/user-profile.component';
import { UserEditComponent } from './components/user/user-edit/user-edit.component';
import { ProjectComponent } from './components/project/project.component';
import { ProjectAddComponent } from './components/project/project-add/project-add.component';
import { ProjectInfoComponent } from './components/project/project-info/project-info.component';
import { ProjectEditComponent } from './components/project/project-edit/project-edit.component';
import { UserRegistrationComponent } from './components/user/user-registration/user-registration.component';
import { UserLoginComponent } from './components/user/user-login/user-login.component';
import { LoginGuard } from './common/services/guard/login.guard';
import { StartPageComponent } from './components/start-page/start-page.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { VerifyEmailComponent } from './components/verify-email/verify-email';
import { SkillsAdministrationComponent } from './components/skills/skills-administration/skills-administration.component';
import { WorkerSkillsComponent } from './components/skills/worker-skills/worker-skills.component';
import { WorkersListComponent } from './components/workers-list/workers-list.component';

const routes: Routes = [
  {
    path: 'users-list',
    component: UserListComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'profile/:id',
    component: UserProfileComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'profile-edit',
    component: UserEditComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'projects',
    component: ProjectComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'worker-projects',
    component: ProjectComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'workers/:workerId/projects',
    component: ProjectComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'workers-list/:projectId',
    component: WorkersListComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'addProjects',
    component: ProjectAddComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'projects/:id',
    component: ProjectInfoComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'editProject/:id',
    component: ProjectEditComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'skills-administration',
    component: SkillsAdministrationComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'worker-skills',
    component: WorkerSkillsComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'registration',
    component: UserRegistrationComponent
  }, {
    path: 'login',
    component: UserLoginComponent
  }, {
    path: '',
    component: StartPageComponent
  }, {
    path: 'not-found',
    component: NotFoundComponent
  }, {
    path: 'verify-email',
    component: VerifyEmailComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
