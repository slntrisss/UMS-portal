import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenStorageService } from '../jwt/token-storage.service';
import { Response } from '../response/response';
import { Role } from './role';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }
  
  private apiUrl = 'http://localhost:8080/api/users'
  private apiManagementUrl = 'http://localhost:8080/management/api/user'

  getAllUsers(){
    return this.http.get<Response>(`${this.apiUrl}/list`);
  }

  deleteUser(userId:number){
    return this.http.delete<Response>(`${this.apiManagementUrl}/delete/${userId}`);
  }

  updateUser(user:User){
    return this.http.put<Response>(`${this.apiManagementUrl}/update`, user);
  }

  addUser(user:User){
    return this.http.post<Response>(`${this.apiManagementUrl}/save`, user);
  }
}
