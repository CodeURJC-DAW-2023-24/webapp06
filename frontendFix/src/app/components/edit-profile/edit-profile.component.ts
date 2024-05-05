import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { LoginService } from './../../services/login.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css',
})
export class EditProfileComponent {
  userName: string = '';
  userId: number | null = null;
  imageSrc: string | ArrayBuffer | null = '';

  constructor(
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private UserService: UserService,
    private router: Router
  ) {
    this.reqIslogged();
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files![0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => (this.imageSrc = reader.result);
      reader.readAsDataURL(file);
    }
  }

  updateProfile(): void {}

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        const user: User | undefined = this.loginService.currentUser();
        if (user != undefined) {
          if (
            user.username == this.activatedRoute.snapshot.params['userName']
          ) {
            this.getUser();
          } else {
            let isAdmin;
            isAdmin = this.loginService.isAdmin();
            if (!isAdmin) {
              this.router.navigate(['/accessDenied']);
            } else {
              this.getUser();
            }
          }
        }
      },
      error: () => {
        this.router.navigate(['/accessDenied']);
      },
    });
  }

  getUser() {
    const userName = this.activatedRoute.snapshot.params['userName'];
    this.userName = userName;
    this.UserService.getUserIdByUsername(userName).subscribe((userId) => {
      if (userId !== null) {
        this.userId = userId;
      } else {
        this.router.navigate(['/404']);
      }
    });
  }
}
