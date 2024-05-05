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
  user: User | undefined;
  username: string = '';
  imageSrc: ArrayBuffer = new ArrayBuffer(0);
  isAdmin: boolean | undefined;

  constructor(
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private UserService: UserService,
    private router: Router
  ) {
    this.reqIslogged();
  }

  loadFile(event: any) {
    const file = event.target.files[0] as File;
    const output = document.getElementById('imagePreview') as HTMLInputElement;

    if (!file || !file.type.startsWith('image/')) {
      //this.fileErrorMessage = file ? 'Invalid file type' : null;
      //this.postForm.controls['imageFile'].setValue(null);
      output.src = '';
    } else {
      //this.fileErrorMessage = null;
      output.src = URL.createObjectURL(file);
    }

    output.onload = function () {
      URL.revokeObjectURL(output.src);
    };
  }

  updateProfile(): void {}

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: () => {
        this.user = this.loginService.currentUser();
        this.username = this.activatedRoute.snapshot.params['userName'];
        if (this.user != undefined) {
          if (this.user.username !== this.username) {
            this.isAdmin = this.loginService.isAdmin();
            if (!this.isAdmin) {
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
    this.UserService.getUserByUsername(this.username).subscribe((user) => {
      if (user !== undefined) {
        this.user = user;
      } else {
        this.router.navigate(['/404']);
      }
    });
  }
}
