import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, scheduled, asyncScheduler } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Post } from '../models/post.model';

const BASE_URL = '/api/posts';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private HttpClient: HttpClient) {}

  getPostById(postId: number): Observable<Post> {
    return this.HttpClient.get(BASE_URL + '/' + postId) as Observable<Post>;
  }

  getPostsByUser(username: string): Observable<Post[]> {
    return this.HttpClient.get(BASE_URL + '/') as Observable<Post[]>;
  }

  getPostsByThread(threadId: number): Observable<Post[]> {
    return this.HttpClient.get(
      BASE_URL + '/?threadId=' + threadId
    ) as Observable<Post[]>;
  }

  getPostImage(postId: number): Observable<any> {
    return this.HttpClient.get(BASE_URL + '/' + postId + '/image', {
      responseType: 'arraybuffer',
    }).pipe(
      map((response) => {
        return new Blob([response], { type: 'image/png' });
      }),
      catchError(() => {
        return scheduled([new Blob()], asyncScheduler);
      })
    );
  }

  createPost(text: string, threadId: number): Observable<Post> {
    return this.HttpClient.post(BASE_URL + '/', {
      text,
      threadId: +threadId,
    }) as Observable<Post>;
  }

  setPostImage(postId: number, formData: FormData): Observable<any> {
    const options = {
      responseType: 'text' as 'json',
    };
    return this.HttpClient.put(
      BASE_URL + '/' + postId + '/image',
      formData,
      options
    );
  }

  editPost(postId: number, text: string): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { text });
  }

  likePost(postId: number): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { liked: true });
  }

  unlikePost(postId: number): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { liked: false });
  }

  dislikePost(postId: number): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { disliked: true });
  }

  undislikePost(postId: number): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { disliked: false });
  }

  reportPost(postId: number): Observable<any> {
    return this.HttpClient.put(BASE_URL + '/' + postId, { reported: true });
  }
}
