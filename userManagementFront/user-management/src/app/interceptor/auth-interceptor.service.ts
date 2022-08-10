import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthHeader } from '../enumeration/auth-header.enum';
import { TokenStorageService } from '../jwt/token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor(private tokenStorageService: TokenStorageService) { }
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    // const token = this.tokenStorageService.getToken;
    const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2JleSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJVU0VSX1JFQUQifSx7ImF1dGhvcml0eSI6IlVTRVJfVVBEQVRFIn0seyJhdXRob3JpdHkiOiJVU0VSX0NSRUFURSJ9LHsiYXV0aG9yaXR5IjoiVVNFUl9ERUxFVEUifSx7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTY2MDc1OTIwMH0.WEMfd8MaCSkfJv7W3xhWFp8oS78j9969B3BYyV3QEqR4zJW7C0YBLt_j8LSAqu-23ytUXHPTB2GTYyGLc77tpw'
    if(token !== null){
      authReq = req.clone(
        {headers: req.headers.set(AuthHeader.AUTHORIZATION, AuthHeader.AUTH_PREFIX + token)}
        );
    }
    return next.handle(authReq);
  }
}
