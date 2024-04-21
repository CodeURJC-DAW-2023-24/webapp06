import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Thread } from '../models/thread.model';

const BASE_URL = '/api/threads';

@Injectable({
  providedIn: 'root',
})
export class ThreadService {
  constructor(private HttpClient: HttpClient) {}

  getThreadsByName(forumName: string): Observable<Thread[]> {
    return this.HttpClient.get(BASE_URL + '/?forumName=' + forumName).pipe(
      map((response: any) => response.content)
    ) as Observable<Thread[]>;
  }

  getThreadsByUser(userName: string): Observable<Thread[]> {
    return this.HttpClient.get(BASE_URL + '/user?username=' + userName).pipe(
      map((response: any) => response.content)
    ) as Observable<Thread[]>;
  }

  private handleError(error: any) {
    console.log('ERROR:');
    console.error(error);
    return throwError('Server error (' + error.status + '): ' + error.text());
  }

  public setNumberOfPost(thread: Thread) {
    thread.numberPosts = thread.posts.length;
  }

  getForumIcon(forumName: string): string {
    if (forumName === 'Books') {
      return 'book';
    } else if (forumName === 'Technology') {
      return 'laptop-code';
    } else if (forumName === 'Science') {
      return 'flask';
    } else if (forumName === 'Sports') {
      return 'football-ball';
    } else if (forumName === 'Music') {
      return 'music';
    } else if (forumName === 'Movies') {
      return 'film';
    } else if (forumName === 'Gastronomy') {
      return 'utensils';
    } else if (forumName === 'Travel') {
      return 'plane';
    } else if (forumName === 'Gaming') {
      return 'gamepad';
    }

    return '';
  }

  getForums(): any {
    return [
      { name: 'Books', icon: 'book' },
      { name: 'Technology', icon: 'laptop-code' },
      { name: 'Science', icon: 'flask' },
      { name: 'Sports', icon: 'football-ball' },
      { name: 'Music', icon: 'music' },
      { name: 'Movies', icon: 'film' },
      { name: 'Gastronomy', icon: 'utensils' },
      { name: 'Travel', icon: 'plane' },
      { name: 'Gaming', icon: 'gamepad' },
    ];
  }
}
