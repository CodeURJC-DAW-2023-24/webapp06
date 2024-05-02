import { PostService } from './../../../../../frontendFix/src/app/services/post.service';
import { User } from './../../models/user.model';
import { ProfileService } from './../../services/profile.service';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from './../../services/login.service';
import { Component } from '@angular/core';
import { ThreadService } from '../../services/thread.service';
import { UserService } from '../../services/user.service';
import { Thread } from '../../models/thread.model';
import { response } from 'express';

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
            this.PostService.getPostsByUser(this.userName).subscribe(
              (response: any) => {
                const posts = response.content;
                for (let i = 0; i < posts.length; i++) {
                  if (posts[i].ownerUsername == userName) {
                    this.numberPosts += 1;
                  }
                }
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
