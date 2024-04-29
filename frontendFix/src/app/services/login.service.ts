import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';

const BASE_URL = '/api/';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  user?: User;
  logged: boolean = false;
  private loggedIn = new BehaviorSubject<boolean>(false);

  isLoggedIn = this.loggedIn.asObservable();

  constructor(private http: HttpClient) {}

  reqIsLogged(): Observable<boolean> {
    return this.http
      .get<any>(`${BASE_URL}users/me`, { withCredentials: true })
      .pipe(
        map((response) => {
          this.user = response as User;
          this.logged = true;
          return true;
        }),
        catchError((error) => {
          if (error.status === 401) {
            this.logOut();
          }
          return throwError(() => new Error('Wrong credentials'));
        })
      );
  }

  logIn(user: string, pass: string): Observable<boolean> {
    return this.http
      .post<any>(
        `${BASE_URL}auth/login`,
        { username: user, password: pass },
        { withCredentials: true }
      )
      .pipe(
        map((response) => {
          this.loggedIn.next(true);
          return true;
        }),
        catchError((error) => {
          return throwError(() => new Error('Wrong credentials'));
        })
      );
  }

  logOut(): Observable<boolean> {
    return this.http
      .post(BASE_URL + 'auth/logout', { withCredentials: true })
      .pipe(
        map((response) => {
          this.logged = false;
          this.user = undefined;
          this.loggedIn.next(false);
          return true;
        }),
        catchError((error) => {
          return throwError(() => new Error('Wrong credentials'));
        })
      );
  }

  isLogged(): boolean {
    return this.logged;
  }

  isAdmin(): boolean | undefined {
    return this.user && this.user.roles.indexOf('ADMIN') !== -1;
  }

  currentUser(): User | undefined {
    return this.user;
  }
}
