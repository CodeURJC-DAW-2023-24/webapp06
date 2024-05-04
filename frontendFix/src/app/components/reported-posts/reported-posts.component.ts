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
  posts!: ReportedPost[];

  constructor(private postService: PostService, private router: Router) {
    this.reqPosts();
  }

  reqPosts() {
    this.postService.getReportedPosts().subscribe({
      next: (posts) => {
        this.posts = posts;
      },
      error: () => {},
    });
  }
  
  getMorePosts() {
    throw new Error('Method not implemented.');
  }
}
