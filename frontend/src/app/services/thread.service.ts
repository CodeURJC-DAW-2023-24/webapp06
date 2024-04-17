import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError} from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Thread } from '../models/thread.model';


const BASE_URL = '/api/threads';

@Injectable({
  providedIn: 'root'})

export class ThreadService {

  constructor(private HttpClient: HttpClient) { }

  getThreadsByName(forumName:string): Observable<Thread[]> {
  return this.HttpClient.get(BASE_URL + '/?forumName=' + forumName).pipe(
    map((response: any)=> response.content)
  ) as Observable<Thread[]>;
	}

  private handleError(error: any) {
    console.log("ERROR:");
    console.error(error);
    return throwError("Server error (" + error.status + "): " + error.text())
  }

  public setNumberOfPost(thread: Thread) {
    thread.numberPosts = thread.posts.length;
  }
}
