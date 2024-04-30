import { Component, ViewChild } from '@angular/core';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  @ViewChild('username') usernameInput: any;
  @ViewChild('email') emailInput: any;
  @ViewChild('password') passwordInput: any;

  onSubmit() { 
    const username: string = this.usernameInput.nativeElement.value;
    const email: string = this.emailInput.nativeElement.value;
    const password: string = this.passwordInput.nativeElement.value;
    console.log(username);
    console.log(email);
    console.log(password);
  }
}