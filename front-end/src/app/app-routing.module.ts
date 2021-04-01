import { Component, NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user/user-list/user-list.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { ProjectComponent } from './project/project.component';
import { ProjectAddComponent } from './project/project-add/project-add.component';
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
    path: 'registration',
    component: UserRegistrationComponent
  }, {
    path: 'login',
    component: UserLoginComponent
  },

  {path: 'projects',
   component: ProjectComponent},

  {path: 'addProjects',
   component: ProjectAddComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
