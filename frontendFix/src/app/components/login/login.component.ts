import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(private loginService: LoginService, private router: Router) {}

  logIn(event: any, user: string, pass: string) {
    event.preventDefault();
    this.loginService.logIn(user, pass).subscribe({
      next: (loggedIn) => {
        if (loggedIn) {
          this.router.navigate(['/']);
        }
      },
      error: (error) => {
        alert(error.message);
      },
    });
  }
}
