import { Component, Input } from '@angular/core';
import { Post } from '../../models/post.model';
import { PostService } from '../../services/post.service';
import { LoginService } from '../../services/login.service';
import { ThreadService } from '../../services/thread.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class PostComponent {
  @Input()
  post: Post | undefined;

  loggedIn: boolean;
  isAuthor: boolean = false;
  elapsedTime: String = '';
  hasImage: boolean = false;
  isLiked: boolean = false;
  isDisliked: boolean = false;

  constructor(
    private loginService: LoginService,
    private postService: PostService,
    private threadService: ThreadService
  ) {
    this.loggedIn = this.loginService.isLogged();
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

    const postId: number = this.post?.id || 0;
    this.postService.getPostImage(postId).pipe(
        map((image: any) => {
          return image != null;
        })
      )
      .subscribe((hasImage: boolean) => (this.hasImage = hasImage));
  }
}
