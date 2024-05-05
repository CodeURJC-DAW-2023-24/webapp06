import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Thread } from '../models/thread.model';
import { Forum } from '../models/forum.model';

const BASE_URL = '/api/threads';

@Injectable({
  providedIn: 'root',
})
export class ThreadService {
  private thread = new BehaviorSubject<Thread>({} as Thread);

  constructor(private http: HttpClient) {}

  getThreadsByForumName(forumName: string, page: number): Observable<any> {
    return this.http.get(BASE_URL + '/?forumName=' + forumName + '&page=' + page);
  }

  getThreadsByUser(userName: string): Observable<Thread[]> {
    return this.http
      .get(BASE_URL + '/user?username=' + userName)
      .pipe(map((response: any) => response.content)) as Observable<Thread[]>;
  }

  getThreadById(threadId: number): Observable<Thread> {
    return this.http.get<Thread>(BASE_URL + '/' + threadId).pipe(
      map((response: Thread) => {
        this.thread.next(response);
        return response;
      })
    );
  }

  deleteThread(threadId: number): Observable<any> {
    const options = {
      responseType: 'text' as 'json',
    };
    return this.http.delete(BASE_URL + '/' + threadId, options);
  }

  addThread(threadData: { forumId: number; text: string }): Observable<any> {
    return this.http.post(BASE_URL + '/', threadData);
  }

  getUserImageById(userId: number): Observable<Blob> {
    return this.http
      .get('api/users/' + userId + '/image', {
        responseType: 'arraybuffer',
      })
      .pipe(map((response) => new Blob([response], { type: 'image/png' })));
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

  getForumId(forumName: string): number {
    if (forumName === 'Books') {
      return 1;
    } else if (forumName === 'Technology') {
      return 2;
    } else if (forumName === 'Science') {
      return 3;
    } else if (forumName === 'Sports') {
      return 4;
    } else if (forumName === 'Music') {
      return 5;
    } else if (forumName === 'Movies') {
      return 6;
    } else if (forumName === 'Gastronomy') {
      return 7;
    } else if (forumName === 'Travel') {
      return 8;
    } else if (forumName === 'Gaming') {
      return 9;
    }

    return 10;
  }

  getForums(): Forum[] {
    return [
      { id: 1, name: 'Books', icon: 'book' },
      { id: 2, name: 'Technology', icon: 'laptop-code' },
      { id: 3, name: 'Science', icon: 'flask' },
      { id: 4, name: 'Sports', icon: 'football-ball' },
      { id: 5, name: 'Music', icon: 'music' },
      { id: 6, name: 'Movies', icon: 'film' },
      { id: 7, name: 'Gastronomy', icon: 'utensils' },
      { id: 8, name: 'Travel', icon: 'plane' },
      { id: 9, name: 'Gaming', icon: 'gamepad' },
    ];
  }
}
