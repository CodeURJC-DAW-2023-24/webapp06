import { Component } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { ThreadService } from '../../services/thread.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';
import { BsModalService } from 'ngx-bootstrap/modal';
import { PostModalComponent } from '../post-modal/post-modal.component';

@Component({
  selector: 'app-thread',
  templateUrl: './thread.component.html',
  styleUrl: './thread.component.css',
})
export class ThreadComponent {
  user: User | undefined;
  loggedIn: boolean = false;
  threadId: number = 0;
  thread: Thread | undefined;
  isAdmin: boolean | undefined;
  isThreadOwner: boolean | undefined;

  constructor(
    private loginService: LoginService,
    private threadService: ThreadService,
    private activatedRoute: ActivatedRoute,
    private modalService: BsModalService
  ) {
    this.reqThread();
  }

  reqThread() {
    this.threadId = this.activatedRoute.snapshot.params['threadId'];
    this.threadService.getThreadById(this.threadId).subscribe({
      next: (thread) => {
        this.thread = thread;
        this.reqIslogged();
      },
      error: () => {}
    });
  }

  reqIslogged() {
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        this.loggedIn = isLogged;
        if (isLogged) {
          this.user = this.loginService.currentUser();
          this.isAdmin = this.loginService.isAdmin();
          this.isThreadOwner = this.thread?.owner.id === this.user?.id;
        } else {
          this.user = undefined;
          this.isAdmin = false;
          this.isThreadOwner = false;
        }
      },
      error: () => {}
    });
  }

  openModal() {
    const initialState = {
      threadId: this.threadId,
    };
    const modalRef = this.modalService.show(PostModalComponent, { initialState });
    modalRef.content?.postCreated.subscribe(() => {
      this.reqThread();
    });
  }

  deleteThread() {
    this.threadService.deleteThread(this.threadId).subscribe();
  }
}
