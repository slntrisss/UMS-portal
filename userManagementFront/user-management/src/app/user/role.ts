import { UserPermission } from "../enumeration/user-permission.enum";

export interface Role{
    id?:number;
    name:string;
    authorities?:UserPermission[];
}