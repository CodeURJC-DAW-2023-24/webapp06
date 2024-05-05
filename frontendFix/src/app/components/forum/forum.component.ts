import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';
import { LoginService } from './../../services/login.service';
import { ThreadService } from './../../services/thread.service';
import { Forum } from '../../models/forum.model';
import { ForumService } from '../../services/forum.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ThreadModalComponent } from '../thread-modal/thread-modal.component';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrl: './forum.component.css',
})
export class ForumComponent {
  user: User | undefined;
  loggedIn: boolean = false;

  forumId: number = 0;
  forum: Forum | undefined;
  threads: Thread[] = [];
  loading: boolean = true;
  currentPage: number = 0;
  totalPages: number = 0;
  lastPage: boolean = true;

  constructor(
    public LoginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private forumService: ForumService,
    private threadService: ThreadService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.reqIslogged();
    this.reqForum();
  }

  reqIslogged() {
    this.LoginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        this.loggedIn = isLogged;
        if (isLogged) {
          this.user = this.LoginService.currentUser();
        } else {
          this.user = undefined;
        }
      },
      error: () => {},
    });
  }

  reqForum() {
    this.activatedRoute.params.subscribe((params) => {
      this.forumId = params['forumId'];
      this.threads = [];
      this.loading = true;
      this.currentPage = 0;
      this.totalPages = 0;
      this.lastPage = true;
      this.forumService.getForumById(this.forumId).subscribe({
        next: (forum) => {
          this.forum = forum;
          this.reqThreads();
        },
        error: () => {},
      });
    });
  }

  reqThreads() {
    if (this.forum) {
      this.threadService
        .getThreadsByForumName(this.forum.name, this.currentPage)
        .subscribe({
          next: (threads) => {
            this.threads = this.threads.concat(threads.content);
            this.totalPages = threads.totalPages;
            this.lastPage = threads.last;
            this.loading = false;
          },
          error: () => {},
        });
    }
  }

  getMoreThreads() {
    this.currentPage++;
    this.reqThreads();
  }

  openModal() {
    const initialState = {
      forumId: this.forumId,
    };
    const modalRef = this.modalService.show(ThreadModalComponent, {
      initialState,
    });
    modalRef.content?.threadCreated.subscribe((response) => {
      if (this.lastPage) {
        this.threads.push(response);
      }
    });
  }
}
