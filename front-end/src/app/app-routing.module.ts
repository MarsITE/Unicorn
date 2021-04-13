import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user/user-list/user-list.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { ProjectComponent } from './project/project.component';
import { ProjectAddComponent } from './project/project-add/project-add.component';
import { ProjectInfoComponent } from './project/project-info/project-info.component';
import { ProjectEditComponent } from './project/project-edit/project-edit.component';
import { StartPageComponent } from "./start-page/start-page.component";
import { AdminComponent } from './admin/admin.component';
import { UserRegistrationComponent } from './user/user-registration/user-registration.component';
import { UserLoginComponent } from './user/user-login/user-login.component';
import { LoginGuard } from './common/services/guard/login.guard';

const routes: Routes = [
  {
    path: 'users-list',
    component: UserListComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'user-profile/:email',
    component: UserProfileComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'user-profile-edit/:email',
    component: UserEditComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'projects',
    component: ProjectComponent,
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
    path: 'admin',
    component: AdminComponent,
    canActivate: [LoginGuard]
  }, {
    path: 'registration',
    component: UserRegistrationComponent
  }, {
    path: 'login',
    component: UserLoginComponent
  }, {
    path: '',
    component:StartPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
