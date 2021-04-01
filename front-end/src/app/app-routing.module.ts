import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user/user-list/user-list.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { UserEditComponent } from './user/user-edit/user-edit.component';
import { ProjectComponent } from './project/project.component';
import { ProjectAddComponent } from './project/project-add/project-add.component';
import { ProjectInfoComponent } from './project/project-info/project-info.component';
import { ProjectEditComponent } from './project/project-edit/project-edit.component';

const routes: Routes = [
  {
    path: 'users-list',
    component: UserListComponent
  }, {
    path: 'user-profile/:email',
    component: UserProfileComponent
  }, {
    path: 'user-profile-edit/:email',
    component: UserEditComponent
  }, {
    path: 'projects/:counter/:sort',
   component: ProjectComponent
  }, {
    path: 'addProjects',
   component: ProjectAddComponent
  }, {
    path: 'projects/:id',
    component:ProjectInfoComponent
  }, {
    path: 'editProject/:id',
    component:ProjectEditComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
