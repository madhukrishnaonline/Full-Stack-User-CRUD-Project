import { UserIndexComponent } from './components/user-index/user-index.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterUserComponent } from './components/register-user/register-user.component';
import { LoginUserComponent } from './components/login-user/login-user.component';
import { GetAllUsersComponent } from './components/get-all-users/get-all-users.component';
import { GetAllUsersSortComponent } from './components/get-all-users-sort/get-all-users-sort.component';
import { GetAllUsersPagesComponent } from './components/get-all-users-pages/get-all-users-pages.component';
import { GetAllUsersSortDescComponent } from './components/get-all-users-sort-desc/get-all-users-sort-desc.component';
import { FindUserComponent } from './components/find-user/find-user.component';
import { UserDashBoardComponent } from './components/user-dash-board/user-dash-board.component';
import { UserApiAdminGuard } from './components/user-api-admin.guard';

const routes: Routes = [
  { path: 'register/user', component: RegisterUserComponent },
  { path: 'allUsers', component: GetAllUsersComponent ,canActivate:[UserApiAdminGuard]},
  { path: 'allUsers/page', component: GetAllUsersPagesComponent ,canActivate:[UserApiAdminGuard]},
  { path: 'allUsers/sort', component: GetAllUsersSortComponent ,canActivate:[UserApiAdminGuard]},
  { path: 'allusers/sort', component: GetAllUsersSortDescComponent ,canActivate:[UserApiAdminGuard]},
  { path: 'findUser', component: FindUserComponent, canActivate:[UserApiAdminGuard] },
  
  { path: 'user/dashBoard', component: UserDashBoardComponent, canActivate: [UserApiAdminGuard] },
  { path: 'login/user', component: LoginUserComponent },

  // { path: 'users/page/sort', component: GetAllUsersSortComponent },
  { path: "", redirectTo: "register/user", pathMatch: "full" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
