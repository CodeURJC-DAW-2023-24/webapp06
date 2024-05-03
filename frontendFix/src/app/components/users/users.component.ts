import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  usernames: string[] = [];

  constructor(private userService: UserService) { }

  searchUser(username: string): void {
    this.userService.getUsernamesByUsername(username).subscribe({
      next: (usernames) => {
        this.usernames = usernames;
      },
      error: (error) => {
        console.error('Error fetching usernames', error);
      }
    });
  }
}