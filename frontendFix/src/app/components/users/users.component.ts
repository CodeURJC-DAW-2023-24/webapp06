import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { LoginService } from './../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
})
export class UsersComponent {
  users: User[] = [];
  id: number = -1;
  currentPage: number = 0;
  totalPages: number = 0;
  lastPage: boolean = false;

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.reqIslogged();
  }

  searchUser(username: string): void {
    if (!this.loginService.isAdmin()) return;
    this.userService.getUsersByUsername(username).subscribe({
      next: (users) => {
        this.users = users;
        this.lastPage = true;
      },
      error: (error) => {
        console.error('Error fetching users', error);
      },
    });
  }

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        let isAdmin;
        if (isLogged) {
          isAdmin = this.loginService.isAdmin();
        } else {
          isAdmin = false;
        }
        if (!isAdmin) {
          this.router.navigate(['/']);
        } else {
          this.searchUserPag();
        }
      },
      error: () => {
        this.router.navigate(['/']);
      },
    });
  }

  searchUserPag(): void {
    this.userService.getAllUsersPaginated(this.currentPage).subscribe({
      next: (users) => {
        this.users = this.users.concat(users.content);
        this.totalPages = users.totalPages;
        this.lastPage = users.last;
      },
      error: (error) => {
        console.error('Error fetching users', error);
      },
    });
  }

  getMoreUsers() {
    this.currentPage++;
    this.searchUserPag();
  }

  deleteUser(id?: number) {
    if (id === undefined) return;
    if (!this.loginService.isAdmin()) return;

    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.currentPage = 0;
        this.users = [];
        this.searchUserPag();
      },
      error: (error) => {
        this.currentPage = 0;
        this.users = [];
        this.searchUserPag();
      },
    });
  }
}
