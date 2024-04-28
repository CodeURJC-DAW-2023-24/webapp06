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
  user: User | undefined;
  loggedIn: boolean;
  thread: Thread | undefined;
  isAdmin: boolean = false;
  isThreadOwner: boolean = false;

  constructor(
    private loginService: LoginService,
    private threadService: ThreadService,
    private activatedRoute: ActivatedRoute
  ) {
    const threadId = activatedRoute.snapshot.params['threadId'];
    this.user = this.loginService.user;
    this.loggedIn = this.loginService.isLogged();

    this.threadService.getThreadById(threadId).subscribe((thread: Thread) => {
      this.thread = thread;
    });

    if (this.loggedIn && this.user != undefined) {
      this.isAdmin = this.user.roles.includes('ADMIN');
      this.isThreadOwner = this.thread?.owner.id === this.user.id;
    }
  }
}
