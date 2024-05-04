import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { LoginService } from './../../services/login.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  users: User[] = [];
  id: number = -1;

  constructor(private userService: UserService, private loginService: LoginService) { }

  searchUser(username: string): void {
    if (!this.loginService.isAdmin()) return;
    this.userService.getUsersByUsername(username).subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (error) => {
        console.error('Error fetching users', error);
      }
    });
  }

  deleteUser(id?: number) {
    if (id === undefined) return;
    if (!this.loginService.isAdmin()) return;

    this.userService.deleteUser(id).subscribe({
      next: () => {
        alert('User deleted successfully');
      },
      error: (error) => {
        alert('Failed to delete user');
      }
    });
  }
}