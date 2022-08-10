import { AccountStatus } from "../enumeration/account-status.enum";
import { Status } from "../enumeration/status.enum";
import { Role } from "./role";

export interface User{
    id?:number;
    firstName?:string;
    lastName?:string;
    email?:string;
    username?:string;
    profileImage?:number[];
    profileImageInBase64?:string;
    password?:string;
    status?:Status;
    authAttempts?:number;
    accountStatus?:AccountStatus;
    role?:Role;
}