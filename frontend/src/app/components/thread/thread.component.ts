import { Component } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { ThreadService } from '../../services/thread.service';
import { PostService } from '../../services/post.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Post } from '../../models/post.model';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-thread',
  templateUrl: './thread.component.html',
  styleUrl: './thread.component.css',
})
export class ThreadComponent {
  forumName: string = '';
  forumIcon: string = '';
  threadName: string = '';
  loggedIn: boolean = this.loginService.isLogged();
  user: User | undefined = this.loginService.user;
  isAdmin: boolean = false;
  isThreadOwner: boolean = false;
  posts: Post[] = [];

  constructor(
    public loginService: LoginService,
    private threadService: ThreadService,
    private postService: PostService,
    private activatedRoute: ActivatedRoute
  ) {
    const forumName = activatedRoute.snapshot.params['forumName'];
    this.forumName = forumName;
    this.forumIcon = threadService.getForumIcon(forumName);
    if (this.loggedIn && this.user != undefined) {
      //const activeUser = userService.getUserByUsername(this.user.username);
      //this.isAdmin = userService.isAdmin(this.user.username);
      //thread.getOwner().getUsername();
      //this.isThreadOwner = threadCreator.equals(this.user.username);
    }
    
    postService
      .getPostsByThread(activatedRoute.snapshot.params['threadId'])
      .subscribe({
        next: (posts) => {
          this.posts = posts;
        },
        error: (error) => {
          alert(error.message);
        },
      });

    for (let post of this.posts) {
      postService.getPostImage(post.id).subscribe({
        next: (image) => {
          post.setImage(image);
        },
        error: () => {          
        },
      });
    }
  }
}
