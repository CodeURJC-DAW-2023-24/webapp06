import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
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
  routeUsername: string = '';
  isAdmin: boolean | undefined;

  profileForm: FormGroup;

  imageSrc: string = '';
  fileErrorMessage: string | null = null;

  constructor(
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private UserService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    this.profileForm = this.formBuilder.group({
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
        ],
      ],
      imageFile: [null],
    });
  }

  ngOnInit() {
    this.reqIslogged();
  }

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: () => {
        this.user = this.loginService.currentUser();
        this.routeUsername = this.activatedRoute.snapshot.params['userName'];
        this.profileForm.controls['username'].setValue(this.routeUsername);
        if (this.user !== undefined) {
          if (this.user.username !== this.routeUsername) {
            this.isAdmin = this.loginService.isAdmin();
            if (!this.isAdmin) {
              this.router.navigate(['/accessDenied']);
            } else {
              this.getUser();
            }
          } else {
            this.imageSrc = '/api/users/' + this.user?.id + '/image';
          }
        }
      },
      error: () => {
        this.router.navigate(['/accessDenied']);
      },
    });
  }

  getUser() {
    this.UserService.getUserByUsername(this.routeUsername).subscribe((user) => {
      if (user !== undefined) {
        this.user = user;
        this.imageSrc = '/api/users/' + this.user?.id + '/image';
      } else {
        this.router.navigate(['/404']);
      }
    });
  }

  loadFile(event: any) {
    const file = event.target.files[0] as File;
    const output = document.getElementById('imagePreview') as HTMLInputElement;

    if (!file || !file.type.startsWith('image/')) {
      this.fileErrorMessage = file ? 'Invalid file type' : null;
      this.profileForm.controls['imageFile'].setValue(null);
    } else {
      this.fileErrorMessage = null;
      output.src = URL.createObjectURL(file);
    }
  }

  updateProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const usernameForm = this.profileForm.get('username')?.value;
    const fileInput = document.getElementById(
      'profilePicture'
    ) as HTMLInputElement;
    const formData = new FormData();
    if (fileInput.files && fileInput.files[0]) {
      formData.append('image', fileInput.files[0]);
    }
    const userId = this.user?.id;
    if (userId !== undefined) {
      this.UserService.updateUser(usernameForm, userId).subscribe({
        next: (response) => {
          if (fileInput.files && fileInput.files[0]) {
            this.uploadImage(userId, formData);
          } else {
            this.loginService.reqIsLogged();
            if (!this.isAdmin) {
              this.router.navigate(['/user/profile/' + usernameForm]);
            } else {
              this.router.navigate(['/users']);
            }
          }
        },
        error: (error) => {
          console.error('Error uploading profile', error);
        },
      });
    }
  }

  uploadImage(postId: number, formData: FormData) {
    this.UserService.updateImage(postId, formData).subscribe({
      next: () => {
        this.loginService.reqIsLogged();
        if (!this.isAdmin) {
          this.router.navigate(['/user/profile/' + this.profileForm.get('username')?.value]);
        } else {
          this.router.navigate(['/users']);
        }
      },
      error: (error) => {
        console.error('Error uploading image', error);
      },
    });
  }
}
