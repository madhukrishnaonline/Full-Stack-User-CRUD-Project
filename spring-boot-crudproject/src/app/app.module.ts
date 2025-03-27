import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule ,HTTP_INTERCEPTORS} from '@angular/common/http';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { provideAnimations } from '@angular/platform-browser/animations';
import {MatProgressBarModule} from '@angular/material/progress-bar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CRUDOperationsComponent } from './components/crud-operations/crud-operations.component';
import { LoginUserComponent } from './components/login-user/login-user.component';
import { UserDashBoardComponent } from './components/user-dash-board/user-dash-board.component';
import { RegisterUserComponent } from './components/register-user/register-user.component';
import { GetAllUsersComponent } from './components/get-all-users/get-all-users.component';
import { UserHeaderComponent } from './components/user-header/user-header.component';
import { UserMainComponent } from './components/user-main/user-main.component';
import { UserIndexComponent } from './components/user-index/user-index.component';
import { UserFiltersComponent } from './components/user-filters/user-filters.component';
import { GetAllUsersSortComponent } from './components/get-all-users-sort/get-all-users-sort.component';
import { GetAllUsersPagesComponent } from './components/get-all-users-pages/get-all-users-pages.component';
import { GetAllUsersSortDescComponent } from './components/get-all-users-sort-desc/get-all-users-sort-desc.component';
import { FindUserComponent } from './components/find-user/find-user.component';
import { AuthInterceptor } from './components/services/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    CRUDOperationsComponent,
    LoginUserComponent,
    UserDashBoardComponent,
    RegisterUserComponent,
    GetAllUsersComponent,
    UserHeaderComponent,
    UserMainComponent,
    UserIndexComponent,
    UserFiltersComponent,
    GetAllUsersSortComponent,
    GetAllUsersPagesComponent,
    GetAllUsersSortDescComponent,
    FindUserComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressBarModule
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA 
 ],
  providers: [
    provideAnimations(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [UserIndexComponent]
})
export class AppModule { }
