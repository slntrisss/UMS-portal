import { Component, ErrorHandler, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthHeader } from '../enumeration/auth-header.enum';
import { TokenStorageService } from '../jwt/token-storage.service';
import { Response } from '../response/response';
import { LoginService } from './login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup ({
    username: new FormControl(''),
    password: new FormControl(''),
  })

  isSubmitted = false;
  invalidCredentials = false;
  authToken = '';
  response:Response | undefined
  private loginSuccessUrl = 'http://localhost:4200/users';
  private loginFailureUrl = 'http://localhost:4200';

  constructor(private loginService: LoginService,
    private router: Router,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
  }

  onSubmit(){
    this.isSubmitted = true;
    let username = this.loginForm.value.username?.trim();
    let password = this.loginForm.value.password;
    if(username?.length === 0 || password?.length === 0){
      this.invalidCredentials = true;
      return;
    }
    if(username !== null && username !== undefined &&
      password !== null && password !== undefined){
        this.loginService.login({username:username, password:password}).subscribe({
          next: (resp) =>{
          if(resp.headers.has(AuthHeader.AUTHORIZATION) &&
          resp.headers.get(AuthHeader.AUTHORIZATION) !== null){
            this.loginService.getLoggedUser(username!).subscribe({
              next: (resp) => {
                this.tokenStorageService.saveUser(resp.data.user!)
              },
              error: (error) => {
                console.log(error)
              }
            })
            this.tokenStorageService.saveToken(resp.headers.get(AuthHeader.AUTHORIZATION)!);
            this.loginService.isLoggedIn = true;
            this.router.navigate([this.loginService.redirectUrl]);
          }
          else{
            throw new Error('Invalid user credentials provided!')
          }
        },
        error: (error) => {
          if(error.status === 401){
            this.invalidCredentials = true;
          }
          else{
            console.log(`Error occured, status code: ${error.status}\n Error: ${error}`)
          }
        }});
    }
  }
}
