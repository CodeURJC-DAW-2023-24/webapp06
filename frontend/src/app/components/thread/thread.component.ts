import { Component } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { ThreadService } from '../../services/thread.service';
import { Router, ActivatedRoute } from '@angular/router';
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
  loggedIn: boolean = this.LoginService.isLogged();
  user: User | undefined = this.LoginService.user;
  isAdmin: boolean = false;
  isThreadOwner: boolean = false;

  constructor(
    public LoginService: LoginService,
    private threadService: ThreadService,
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
  }
}
