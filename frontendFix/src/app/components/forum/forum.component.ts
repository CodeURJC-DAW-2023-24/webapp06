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

  }

  ngOnInit(): void {

    this.activatedRoute.params.subscribe(params => {
      this.forumName = params['forumName'];
      this.threadService.getThreadsByForumName(this.forumName).subscribe((threads: Thread[]) => {
        this.threads = threads;
        this.forumIcon = this.threadService.getForumIcon(this.forumName);
      });
    });
  }



  loggedIn = this.LoginService.isLogged();
  user = this.LoginService.user;
}
