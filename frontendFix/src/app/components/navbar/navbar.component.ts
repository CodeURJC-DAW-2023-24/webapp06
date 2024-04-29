import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user.model';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  isLoggedIn: boolean = false;
  user: User | undefined;
  userId: string = '';
  isAdmin: boolean | undefined = false;

  constructor(private loginService: LoginService) {
    this.reqIslogged();
    this.loginService.isLoggedIn.subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
      if (loggedIn) {
        this.reqIslogged();
      } else {
        this.user = undefined;
        this.isAdmin = false;
        this.isLoggedIn = false;
      }
    });
  }

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        this.isLoggedIn = isLogged;
        if (isLogged) {
          this.user = this.loginService.currentUser();
          this.isAdmin = this.loginService.isAdmin();
          this.userId = this.user?.id?.toString() ?? '';
        } else {
          this.user = undefined;
          this.isAdmin = false;
        }
      },
      error: (error) => {},
    });
  }

  logOut() {
    this.loginService.logOut().subscribe({
      next: () => {
        this.user = undefined;
        this.isAdmin = false;
        this.isLoggedIn = false;
      },
      error: (error) => {},
    });
  }
}
