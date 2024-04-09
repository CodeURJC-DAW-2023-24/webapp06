import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

const BASE_URL = '/auth';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  user?: User;
  logged: boolean = false;
  private apiUrl = environment.backendUrl;

  constructor(private http: HttpClient) {
    //this.reqIsLogged();
  }

  reqIsLogged() {
    this.http.get('/api/users/me', { withCredentials: true }).subscribe(
      (response) => {
        this.user = response as User;
        this.logged = true;
      },
      (error) => {
        if (error.status != 404) {
          console.error(
            'Error when asking if logged: ' + JSON.stringify(error)
          );
        }
      }
    );
  }

  logIn(user: string, pass: string) {
    console.log('respuesta aaa');
    this.http
      .post(
        this.apiUrl + BASE_URL + '/login',
        { username: user, password: pass },
        { withCredentials: true }
      )
      .subscribe(
        (response) => console.log('respuesta', response),
        (error) => alert('Wrong credentials')
      );
  }

  logOut() {
    return this.http
      .post(this.apiUrl + BASE_URL + '/logout', { withCredentials: true })
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
