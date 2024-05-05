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
    return this.HttpClient.get<User[]>('/api/users/');
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

  getUsernamesByUsername(username: string): Observable<string[]> {
    return this.getAllUsers().pipe(
      map(users => users.filter(u => u.username.includes(username)).map(u => u.username))
    );
  }

  getUsersByUsername(username: string): Observable<User[]> {
    return this.getAllUsers().pipe(
      map(users => users.filter(u => u.username.includes(username)))
    );
  }

  deleteUser(id: number): Observable<any> {
    return this.HttpClient.delete('/api/users/' + id);
  }

  getAllUsersPaginated(page: number): Observable<any> {
    return this.HttpClient.get<User[]>('/api/users/paginated?page=' + page)
  }

}
