import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(public loginService: LoginService) {}

  logIn(event: any, user: string, pass: string) {
    event.preventDefault();
    console.log('prueba');
    this.loginService.logIn(user, pass);
  }

  prueba(): any {
    console.log('prueba');
    alert('Prueba');
  }
}
