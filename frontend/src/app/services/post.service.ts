import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Thread } from '../models/thread.model';
import { Post } from '../models/post.model';

const BASE_URL = '/api/posts';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private HttpClient: HttpClient) {}

  getPostsByThread(threadId: number): Observable<Post[]> {
    return this.HttpClient.get(BASE_URL + '/?threadId=' + threadId).pipe(
      map((response: any) => response.content)
    ) as Observable<Post[]>;
  }

  getPostImage(postId: number): Observable<Uint8Array> {
    return this.HttpClient.get(BASE_URL + '/' + postId + '/image', { responseType: 'arraybuffer' }).pipe(
      map((response: ArrayBuffer) => new Uint8Array(response))
    ) as Observable<Uint8Array>;
  }
}
