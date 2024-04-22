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

  getUserImageByName(userName: string): any {
    return this.HttpClient.get(BASE_URL + '/users/'+ userName+'image');
  }

  getUserImageById(userId: number): Observable<Blob> {
    return this.HttpClient.get('api/users/' + userId + '/image', { responseType: 'arraybuffer' }).pipe(
      map(response => new Blob([response], { type: 'image/png' }))
    );
  }

  getThreadsByUser(userName:string): Observable<Thread[]> {
    return this.HttpClient.get(BASE_URL + '/user?username=' + userName).pipe(
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

  getForumIcon(forumName: string) : string{
    if(forumName === "books"){
      return "book";
    } else if(forumName === "tecnology"){
      return "laptop-code";
    } else if (forumName === "science"){
      return "flask";
    } else if (forumName === "sports"){
      return "football-ball";
    } else if (forumName === "music"){
      return "music";
    } else if (forumName === "movies"){
      return "film";
    } else if (forumName === "gastronomy"){
      return "utensils";
    } else if (forumName === "travel"){
      return "plane";
    } else if (forumName === "gaming"){
      return "gamepad";
    }


    return "";
}

  getForums(): any {
    return [
      {name: "books", icon: "book"},
      {name: "tecnology", icon: "laptop-code"},
      {name: "science", icon: "flask"},
      {name: "sports", icon: "football-ball"},
      {name: "music", icon: "music"},
      {name: "movies", icon: "film"},
      {name: "gastronomy", icon: "utensils"},
      {name: "travel", icon: "plane"},
      {name: "gaming", icon: "gamepad"}
    ];
  }

}

