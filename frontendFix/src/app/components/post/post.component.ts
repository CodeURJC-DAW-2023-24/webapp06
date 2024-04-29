import { Component, Input } from '@angular/core';
import { Post } from '../../models/post.model';
import { PostService } from '../../services/post.service';
import { LoginService } from '../../services/login.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class PostComponent {
  loggedIn: boolean;
  activeUser: User | undefined;

  @Input()
  post: Post = {} as Post;
  isAuthor: boolean = false;
  isAdmin: boolean = false;
  elapsedTime: String = '';
  hasImage: boolean = false;
  isLiked: boolean = false;
  isDisliked: boolean = false;
  isReported: boolean = false;

  constructor(
    private loginService: LoginService,
    private postService: PostService
  ) {
    this.loggedIn = this.loginService.isLogged();
    this.activeUser = this.loginService.currentUser();
    if (this.loggedIn && this.activeUser != undefined) {
      this.isAdmin = this.activeUser.roles.includes('ADMIN');
    }
  }

  ngOnInit(): void {
    const postTime: Date = new Date(this.post?.createdAt || '');
    const currentTime: Date = new Date();
    const seconds: number = Math.floor(
      (currentTime.getTime() - postTime.getTime()) / 1000
    );
    const days: number = Math.floor(seconds / (60 * 60 * 24));
    const hours: number = Math.floor(seconds / (60 * 60));
    const minutes: number = Math.floor(seconds / 60);

    if (days > 0) {
      this.elapsedTime = days + ' days ago';
    } else if (hours > 0) {
      this.elapsedTime = hours + ' hours ago';
    } else if (minutes > 0) {
      this.elapsedTime = minutes + ' minutes ago';
    } else {
      this.elapsedTime = seconds + ' seconds ago';
    }

    if (this.activeUser != undefined) {
      this.isAuthor = this.activeUser.id === this.post.owner.id;

      if (this.post.userLikes.includes(this.activeUser)) {
        this.isLiked = true;
      }

      if (this.post.userDislikes.includes(this.activeUser)) {
        this.isDisliked = true;
      }
    }

    const postId: number = this.post.id;
    this.postService
      .getPostImage(postId)
      .subscribe((hasImage: boolean) => (this.hasImage = hasImage));
  }

  toggleLike() {
    if (this.loggedIn) {
      if (this.isLiked) {
        this.postService.unlikePost(this.post.id).subscribe(() => {
          this.isLiked = false;
        });
      } else {
        this.postService.likePost(this.post.id).subscribe(() => {
          this.isLiked = true;
        });
        if (this.isDisliked) {
          this.postService.undislikePost(this.post.id).subscribe(() => {
            this.isDisliked = false;
          });
        }
      }

      this.postService.getPostById(this.post.id).subscribe((post: Post) => {
        this.post = post;
      });
    }
  }

  toggleDislike() {
    if (this.loggedIn) {
      if (this.isDisliked) {
        this.postService.undislikePost(this.post.id).subscribe(() => {
          this.isDisliked = false;
        });
      } else {
        this.postService.dislikePost(this.post.id).subscribe(() => {
          this.isDisliked = true;
        });
        if (this.isLiked) {
          this.postService.unlikePost(this.post.id).subscribe(() => {
            this.isLiked = false;
          });
        }
      }

      this.postService.getPostById(this.post.id).subscribe((post: Post) => {
        this.post = post;
      });
    }
  }

  reportPost() {
    if (this.loggedIn) {
      if (!this.isReported) {
        this.postService.reportPost(this.post.id).subscribe(() => {
          this.isReported = true;
        });
      }
    }
  }

  editPost() {
    if (this.isAdmin || this.isAuthor) {
    }
  }

  deletePost() {
    if (this.isAdmin || this.isAuthor) {
    }
  }
}
