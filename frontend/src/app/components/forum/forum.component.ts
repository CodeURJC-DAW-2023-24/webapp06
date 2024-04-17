import { LoginService } from './../../services/login.service';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ThreadService } from './../../services/thread.service';
import { Thread } from '../../models/thread.model';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrl: './forum.component.css'
})
export class ForumComponent{

  threads: any = [] = [];
  forumName: string = "";
  forumIcon: string ="";
  forums: any = [] = [];

  constructor(public LoginService: LoginService, private activatedRoute: ActivatedRoute, private threadService: ThreadService) {

    const forumName = activatedRoute.snapshot.params['forumName'];
    this.forumName = forumName;
    this.forumIcon = threadService.getForumIcon(forumName);
    this.forums = threadService.getForums();
    threadService.getThreadsByName(forumName).subscribe(
      (threads: Thread[]) => this.threads = threads
    );
  }



  loggedIn = this.LoginService.isLogged();
  user = this.LoginService.user;
}
