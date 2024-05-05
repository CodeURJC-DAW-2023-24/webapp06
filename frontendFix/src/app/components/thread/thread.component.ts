import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Post } from '../../models/post.model';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';
import { ThreadService } from '../../services/thread.service';
import { PostModalComponent } from '../post-modal/post-modal.component';
import { LoginService } from './../../services/login.service';

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

  loading: boolean = true;
  postsShown: Post[] = [];
  currentPage: number = 0;
  totalPages: number = 0;

  constructor(
    private loginService: LoginService,
    private threadService: ThreadService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private modalService: BsModalService
  ) {
    this.reqThread();
  }

  reqThread(loadMore: boolean = true) {
    this.threadId = this.activatedRoute.snapshot.params['threadId'];
    this.threadService.getThreadById(this.threadId).subscribe({
      next: (thread) => {
        this.thread = thread;
        this.reqIslogged();
        if (loadMore) {
          const page = this.thread.posts.slice(
            0 + this.currentPage * 10,
            Math.min((this.currentPage + 1) * 10, this.thread.posts.length)
          );
          this.postsShown = this.postsShown.concat(page);
          this.totalPages = Math.floor((this.thread.posts.length - 1) / 10) + 1;
        } else {
          this.postsShown = this.thread.posts.slice(0, this.postsShown.length);
          this.totalPages = Math.floor((this.thread.posts.length - 1) / 10) + 1;
        }
        this.loading = false;
      },
      error: () => {
        this.router.navigate(['/404']);
      },
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
      error: () => {},
    });
  }

  getMorePosts() {
    this.currentPage++;
    this.reqThread();
  }

  openModal() {
    const initialState = {
      threadId: this.threadId,
    };
    const modalRef = this.modalService.show(PostModalComponent, {
      initialState,
    });
    modalRef.content?.postCreated.subscribe(() => {
      this.postsShown = [];
      this.currentPage = 0;
      this.reqThread();
    });
  }

  deleteThread() {
    this.threadService.deleteThread(this.threadId).subscribe({
      next: () => {
        this.router.navigate(['/f', this.thread?.forum.id], {
          replaceUrl: true,
        });
      },
      error: () => {},
    });
  }

  editPost() {
    this.reqThread(false);
  }

  deletePost() {
    this.postsShown = [];
    this.currentPage = 0;
    this.reqThread();
  }
}
