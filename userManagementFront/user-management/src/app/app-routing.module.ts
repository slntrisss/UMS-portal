import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
    // { path: 'users', component: UserComponent, canActivate:[AuthGuard] },
    // { path: '', component: LoginComponent}
    { path: '', component: UserComponent}
  ];
  
  @NgModule({
    declarations: [],
    imports: [
      CommonModule,
      RouterModule.forRoot(routes)
    ],
    exports: [RouterModule],
    providers: []
  })
  
  export class AppRoutingModule { }