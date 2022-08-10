import { User } from "../user/user";

export interface Response{
    status:string;
    statusCode:number;
    message:string;
    data: {users?: User[], user?:User};
    developerMessage:string;
}