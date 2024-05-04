import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Thread } from '../../models/thread.model';
import { PostService } from '../../services/post.service';
import { ThreadService } from '../../services/thread.service';
import { UserService } from '../../services/user.service';
import { LoginService } from './../../services/login.service';
import { ProfileService } from './../../services/profile.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  userName: string = "";
  threads: any = [] = [];
  userId: number | null = null;
  noUser: boolean = false;
  numberThreads: number = 0;
  numberPosts: number = 0;

  constructor(public loginService: LoginService, private activatedRoute: ActivatedRoute, private threadService: ThreadService, private ProfileService : ProfileService, private UserService: UserService, private PostService: PostService) {

    const userName = this.activatedRoute.snapshot.params['userName'];
    this.userName = userName;
    this.UserService.getUserIdByUsername(userName).subscribe
      (
        userId => {
          if (userId !== null) {
            this.userId = userId;
            this.PostService.getPostsByUser(this.userId).subscribe(
              (response: any) => {
                const posts = response.content;
                this.numberPosts = posts.length;
              }
            );
          } else {
            this.userId = null;
          }
        },
      );
    this.threadService.getThreadsByUser(userName).subscribe(
      (threads: Thread[]) => {
        this.threads = threads;
        this.numberThreads = threads.length;
      }
    );




  }



  isAdmin = this.loginService.isAdmin();
  isLogged = this.loginService.isLogged();



}
