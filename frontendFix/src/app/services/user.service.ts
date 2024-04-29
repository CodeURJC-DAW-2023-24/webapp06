import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private HttpClient: HttpClient) { }


  getAllUsers(): Observable<User[]> {
    return this.HttpClient.get<User[]>('http://localhost:4200/api/users/');
  }

  getUserIdByUsername(username: string): Observable<number | null> {
    return this.getAllUsers().pipe(
      map(users => {
        const user = users.find(u => u.username === username);
        return user ? user.id : null;
      }),
      map(userId => userId === undefined ? null : userId)
    );
  }

}
