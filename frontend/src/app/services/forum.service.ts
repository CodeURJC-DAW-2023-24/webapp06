import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Forum } from '../models/forum.model';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ForumService {

  constructor(private HttpClient: HttpClient) { }
  
  getForums(): Observable<Forum[]> {
    return this.HttpClient.get('/api/forums') as Observable<Forum[]>;
  }
}
