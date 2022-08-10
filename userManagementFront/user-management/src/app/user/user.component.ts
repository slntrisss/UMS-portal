import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { AppRole } from '../enumeration/app-role.enum';
import { Status } from '../enumeration/status.enum';
import { UserAction } from '../enumeration/user-action.enum';
import { UserFilter } from '../enumeration/user-filter.enum';
import { UserPermission } from '../enumeration/user-permission.enum';
import { TokenStorageService } from '../jwt/token-storage.service';
import { Response } from '../response/response';
import { User } from './user';
import { UserService } from './user.service';
import { Role } from './role';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor(private userService: UserService, 
              private domSanitizer: DomSanitizer,
              private tokenStorageService:TokenStorageService) { }

  response:Response | undefined;
  users:User[] = [];
  userToBeUpdated:User | undefined;
  userToBeCreated:User | undefined;
  userToBeDeleted:User | undefined;
  imageInput:any;
  
  readonly userFilter = UserFilter;
  filterLabel = UserFilter.ALL;
  readonly appRole = AppRole;
  appRoleLabel = AppRole.USER;
  readonly userAction = UserAction;
  loggedUser:User | undefined;
  choosedUser:User | undefined;
  choosedUserIndex:number | undefined;
  images = new Map<number, SafeUrl>();
  canUpdate:boolean = false;
  canDelete:boolean = false;
  canCreate:boolean = false;
  canRead:boolean = false;

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void{
    this.userService.getAllUsers().subscribe({
      next: (resp) => {
        this.response = resp;
        if(resp.data.users !== undefined){
          this.loggedUser = resp.data.users[resp.data.users.length-1];
          this.users = resp.data.users;
          for(let i = 0; i < this.loggedUser.role!.authorities!.length; i++){
            let authority = this.loggedUser.role!.authorities![i];
            switch(authority){
              case UserPermission.USER_CREATE:{
                this.canCreate = true;
                break;
              }
              case UserPermission.USER_DELETE:{
                this.canDelete = true;
                break;
              }
              case UserPermission.USER_READ:{
                this.canRead = true;
                break;
              }
              case UserPermission.USER_UPDATE:{
                this.canUpdate = true;
                break;
              }
            }
          }
        }
        this.convertBytesToImgs(this.response.data.users!)
      },
      error: (error) => console.log(error)
    })
  }

  deleteUser(userId:number){
    let closeBtn = document.getElementById('delete-user');
    if(closeBtn !== null){
      closeBtn.click();
    }
    this.userService.deleteUser(userId).subscribe({
      next: res => {
        console.log(res);
        this.getUsers();
      },
      error: error => {
        console.log(`Error occured: ${error}`);
      }
    });
  }

  addUser(createdUser:User){
    let closeBtn = document.getElementById('save-user-btn');
    if(closeBtn !== null){
      closeBtn?.click();
    }
    createdUser.profileImageInBase64 = this.userToBeCreated?.profileImageInBase64;
    if(this.userToBeCreated?.role === undefined){
      createdUser.role = {name:this.appRole.USER};
    }else{
      createdUser.role = this.userToBeCreated?.role;
    }
    createdUser.password = createdUser.username;
    createdUser.status = this.userToBeCreated?.status
    console.log(createdUser);
    this.imageInput.value = '';
    this.userService.addUser(createdUser).subscribe({
      next: response =>{
        console.log(response)
        this.getUsers();
      },
      error: error =>{
        console.log(error);
      }
    })
  }

  initEmptyUser(){
    this.userToBeCreated = {};
  }

  updateUser(updatedUser:User){
    const closeBtn = document.getElementById('update-user');
    const imageUploadInput = document.getElementById('editProfileImage');
    if(closeBtn !== null){
      closeBtn.click();
    }
    if(updatedUser.role?.name === undefined){
      updatedUser.role = this.userToBeUpdated?.role!;
    }
    updatedUser.profileImageInBase64 = this.userToBeUpdated!.profileImageInBase64;
    updatedUser.status = this.userToBeUpdated!.status
    this.imageInput.value = '';
    this.userService.updateUser(updatedUser).subscribe({
      next: res =>{
        this.getUsers();
      },
      error: error =>{
        console.log(error);
      }
    })
  }
  
  processFile(user: User, imageInput: any){
    console.log(imageInput)
    const file:File = imageInput.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if(reader.result !== undefined){
        user.profileImageInBase64 = reader.result as string;
        this.imageInput = imageInput;
      }
    }
  }

  getUserByClickEvent(user: User, index:number){
    this.choosedUser = user;
    this.choosedUserIndex = index;
  }

  filterUsers(userFilter:UserFilter){
    console.log(userFilter)
    this.filterLabel = userFilter;
    const results:User[] = [];
    if(this.response !== undefined && this.response.data !== undefined){
      for(const user of this.response.data.users!){
        if(user.status?.toString() === userFilter.toString()){
          results.push(user);
        }
      }
      if(results.length > 0){
        this.users = results;
      }else{
        this.users = this.response.data.users!;
      }
    }
  }

  searchUser(searchInput:string){
    searchInput = searchInput.toLowerCase();
    console.log(searchInput);
    const results:User[] = [];
    if(this.users.length > 0){
      for(const user of this.response?.data.users!){
        if(user.firstName?.toLowerCase().indexOf(searchInput) !== -1 ||
           user.lastName?.toLowerCase().indexOf(searchInput) !== -1 ||
           user.email?.toLowerCase().indexOf(searchInput) !== -1 ||
           user.username?.toLowerCase().indexOf(searchInput) !== -1){
             results.push(user);
           }
      }
      if(results.length > 0){
        this.users = results;
      }else{
        this.users = this.response?.data.users!;
      }
    }
  }

  openModal(user:User, userAction:UserAction){
    if(userAction === UserAction.DELETE){
      this.userToBeDeleted = user;
    }else{
      this.userToBeUpdated = user;
    }
  }

  chooseRole(user:User, appRole: AppRole){
    switch(appRole){
      case AppRole.MANAGER: {
        user.role = {name:AppRole.MANAGER};
        break;
      }
      case AppRole.ADMIN:{
        user.role = {name:AppRole.ADMIN};
        break;
      }
      case AppRole.USER:{
        user.role = {name:AppRole.USER};
        break;
      }
    }
  }

  getSelectedStatus(user:User, status:Event){
    if((status.target as HTMLInputElement).value === Status.ACTIVE){
      user.status = Status.ACTIVE;
    }else{
      user.status = Status.INACTIVE;
    }
  }

  private convertBytesToImgs(users:User[]){
    for(let i = 0; i < users.length; i++){
      let imgUrl = this.domSanitizer.bypassSecurityTrustUrl(`data:image/jpg;base64, ` + users[i].profileImage);
      this.images.set(users[i].id!, imgUrl);
    }
  }
}

