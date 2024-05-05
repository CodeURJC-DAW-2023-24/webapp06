import { HttpClient } from '@angular/common/http';
import { Component} from '@angular/core';
import { EmailValidator, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  myForm: FormGroup;
  error: boolean = false;
  done: boolean = false;

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private router: Router,) { 
    this.myForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }
  
  onSubmit() {
    if (this.myForm.valid) {
      const formData = this.myForm.value;
      this.http.post<any>('http://localhost:4200/api/users/', formData).subscribe({
        next: (response) => {
          this.done = true;
        },
        error: (error) => {
          this.error = true;
        }
      });
    } else {
      if (this.myForm.get('email')?.hasError('email')) {
        alert('Please enter a valid email address.');
      } else {
        alert('Please complete all fields.');
      }
      
    }
  }
  
}