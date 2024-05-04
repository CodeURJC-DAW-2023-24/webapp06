import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { ReportedPost } from '../../models/reportedPost.model';

@Component({
  selector: 'app-reported-posts',
  templateUrl: './reported-posts.component.html',
  styleUrl: './reported-posts.component.css',
})
export class ReportedPostsComponent {
  posts: ReportedPost[] = [];
  loading: boolean = true;
  currentPage: number = 0;
  totalPages: number = 0;
  lastPage: boolean = false;

  constructor(private postService: PostService, private router: Router) {
    this.reqPosts();
  }

  reqPosts() {
    this.postService.getReportedPosts(this.currentPage).subscribe({
      next: (posts) => {
        this.posts = this.posts.concat(posts.content);
        this.totalPages = posts.totalPages;
        this.lastPage = posts.last;
        this.loading = false;
      },
      error: () => {}
    });
  }
  
  getMorePosts() {
    this.currentPage++;
    this.reqPosts();
  }
}
