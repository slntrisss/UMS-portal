import { Injectable } from '@angular/core';
import { User } from '../user/user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  jwtToken = 'jwt-token';
  authUser = 'user';
  
  constructor() { }

  public saveToken(newToken:string): void{
    window.sessionStorage.removeItem(this.jwtToken);
    window.sessionStorage.setItem(this.jwtToken, newToken);
  }

  public saveUser(user: User){
    window.sessionStorage.removeItem(this.authUser);
    window.sessionStorage.setItem(this.authUser, JSON.stringify(user));
  }

  public getUser(){
    if(window.sessionStorage.getItem(this.authUser) !== null){
      return JSON.parse(window.sessionStorage.getItem(this.authUser)!);
    }
  }

  public getToken(){
    return window.sessionStorage.getItem(this.jwtToken);
  }

  public logout(){
    window.sessionStorage.clear;
  }
}
