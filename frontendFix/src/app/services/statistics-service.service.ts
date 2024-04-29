import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class StatisticsService {
  constructor(private http: HttpClient) {}

  async getThreads(type: string, date: string): Promise<any> {
    const url = `/api/chart/threads/${type}?date=${date}`;
    return this.http
      .get<any>(url, { withCredentials: true })
      .toPromise()
      .then((res) => {
        console.log(res);
        return res;
      });
  }

  async getPosts(type: string, date: string): Promise<any> {
    const url = `/api/chart/posts/${type}?date=${date}`;
    return this.http
      .get<any>(url, { withCredentials: true })
      .toPromise()
      .then((res) => {
        console.log(res);
        return res;
      });
  }
}
