import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ReportedPost } from '../../models/reportedPost.model';
import { PostService } from '../../services/post.service';
import { LoginService } from '../../services/login.service';

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

  constructor(
    private postService: PostService,
    private loginService: LoginService,
    private router: Router
  ) {
    this.reqIslogged();
  }

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        let isAdmin;
        if (isLogged) {
           isAdmin = this.loginService.isAdmin();
        } else {
          isAdmin = false;
        }
        if (!isAdmin) {
          this.router.navigate(['/']);
        } else {          
          this.reqPosts();
        }
      },
      error: () => {},
    });
  }

  reqPosts() {
    this.postService.getReportedPosts(this.currentPage).subscribe({
      next: (posts) => {
        this.posts = this.posts.concat(posts.content);
        this.totalPages = posts.totalPages;
        this.lastPage = posts.last;
        this.loading = false;
      },
      error: () => {},
    });
  }

  getMorePosts() {
    this.currentPage++;
    this.reqPosts();
  }

  removePostFromList(postId: number) {
    this.posts.find((post, index) => {
      if (post.id === postId) {
        this.posts.splice(index, 1);
      }
    });
  }
}
