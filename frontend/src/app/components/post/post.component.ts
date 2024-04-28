import { Component, Input } from '@angular/core';
import { Post } from '../../models/post.model';
import { PostService } from '../../services/post.service';
import { LoginService } from '../../services/login.service';
import { ThreadService } from '../../services/thread.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css'
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
    private threadService: ThreadService) {
    this.loggedIn = this.loginService.isLogged();
  }
}
