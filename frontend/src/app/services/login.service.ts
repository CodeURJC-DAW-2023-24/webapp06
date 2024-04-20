import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

const BASE_URL = '/api/auth';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  user?: User;
  logged: boolean = false;

  constructor(private http: HttpClient) {
    this.reqIsLogged()
  }

  reqIsLogged(): Observable<boolean> {
    return this.http
      .post<any>(`${BASE_URL}/users/me`, { withCredentials: true })
      .pipe(
        map((response) => {
          this.user = response as User;
          this.logged = true;
          return true;
        }),
        catchError((error) => {
          return throwError(() => new Error('Wrong credentials'));
        })
      );
  }

  logIn(user: string, pass: string): Observable<boolean> {
    return this.http
      .post<any>(
        `${BASE_URL}/login`,
        { username: user, password: pass },
        { withCredentials: true }
      )
      .pipe(
        map((response) => true),
        catchError((error) => {
          return throwError(() => new Error('Wrong credentials'));
        })
      );
  }

  logOut() {
    return this.http
      .post(BASE_URL + '/logout', { withCredentials: true })
      .subscribe((resp: any) => {
        console.log('LOGOUT: Successfully');
        this.logged = false;
        this.user = undefined;
      });
  }

  isLogged(): boolean {
    return this.logged;
  }

  isAdmin() {
    return this.user && this.user.roles.indexOf('ADMIN') !== -1;
  }

  currentUser() {
    return this.user;
  }
}
