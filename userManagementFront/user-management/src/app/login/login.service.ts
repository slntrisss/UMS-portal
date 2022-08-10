import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Response } from '../response/response';
import { Login } from './login';

@Injectable({
  providedIn: 'root'
})
export class LoginService{
  private apiUrl = 'http://localhost:8080/login';
  private loggedUserData = 'http://localhost:8080/api/users'

  private _isLoggedIn:boolean = false;
  private _redirectUrl = 'users';
  
  constructor(private http: HttpClient) { }

  public get isLoggedIn(){
    return this._isLoggedIn;
  }

  public set isLoggedIn(isLoggedIn:boolean){
    this._isLoggedIn = isLoggedIn;
  }

  public get redirectUrl(){
    return this._redirectUrl;
  }

  public set redirectUrl(redirectUrl: string){
    this._redirectUrl = redirectUrl;
  }

  login(loginData: Login){
    return this.http.post<string>(`${this.apiUrl}`, loginData, {observe: 'response'});
  }

  getLoggedUser(username:string): Observable<Response> {
    return this.http.get<Response>(`${this.loggedUserData}/${username}`);
  }
}
