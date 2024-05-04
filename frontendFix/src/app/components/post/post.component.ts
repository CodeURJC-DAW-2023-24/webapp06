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
  loggedIn: boolean = false;
  activeUser: User | undefined;

  @Input()
  post: Post = {} as Post;
  isAuthor: boolean = false;
  isAdmin: boolean = false;
  elapsedTime: String = '';
  isLiked: boolean = false;
  isDisliked: boolean = false;
  isReported: boolean = false;

  constructor(
    private loginService: LoginService,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.loggedIn = this.loginService.isLogged();
    this.activeUser = this.loginService.currentUser();
    if (this.loggedIn && this.activeUser != undefined) {
      this.isAdmin = this.activeUser.roles.includes('ADMIN');
    }

    const postTime: Date = new Date(this.post?.createdAt || '');
    const currentTime: Date = new Date();
    const seconds: number = Math.max(
      Math.floor((currentTime.getTime() - postTime.getTime()) / 1000),
      0
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

      this.isLiked = this.post.userLikes.some(
        (user) => user.id === this.activeUser?.id
      );

      this.isDisliked = this.post.userDislikes.some(
        (user) => user.id === this.activeUser?.id
      );
    }
  }

  handleImageError() {
    this.post.hasImage = false;
  }

  toggleLike() {
    if (this.loggedIn) {
      if (this.isLiked) {
        this.postService.unlikePost(this.post.id).subscribe(() => {});
        this.isLiked = false;
        this.post.likes--;
      } else {
        this.postService.likePost(this.post.id).subscribe(() => {});
        this.isLiked = true;
        this.post.likes++;
        if (this.isDisliked) {
          this.postService.undislikePost(this.post.id).subscribe(() => {});
          this.isDisliked = false;
          this.post.dislikes--;
        }
      }
    }
  }

  toggleDislike() {
    if (this.loggedIn) {
      if (this.isDisliked) {
        this.postService.undislikePost(this.post.id).subscribe(() => {});
        this.isDisliked = false;
        this.post.dislikes--;
      } else {
        this.postService.dislikePost(this.post.id).subscribe(() => {});
        this.isDisliked = true;
        this.post.dislikes++;
        if (this.isLiked) {
          this.postService.unlikePost(this.post.id).subscribe(() => {});
          this.isLiked = false;
          this.post.likes--;
        }
      }
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
