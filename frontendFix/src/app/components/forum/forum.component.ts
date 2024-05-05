import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';
import { LoginService } from './../../services/login.service';
import { ThreadService } from './../../services/thread.service';
import { Forum } from '../../models/forum.model';
import { ForumService } from '../../services/forum.service';

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
  lastPage: boolean = false;

  uploadForm!: FormGroup;

  constructor(
    public LoginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private forumService: ForumService,
    private threadService: ThreadService,
    private fb: FormBuilder
  ) {
    this.uploadForm = this.fb.group({
      text: ['', Validators.required],
      forumId: [this.forumId, Validators.required],
    });
  }

  ngOnInit(): void {
    this.reqForum();
    this.reqIslogged();
  }

  reqForum() {
    this.activatedRoute.params.subscribe((params) => {
      this.forumId = params['forumId'];
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
      this.threadService.getThreadsByForumName(this.forum.name).subscribe({
        next: (threads: Thread[]) => {
          this.threads = threads;
        },
        error: () => {},
      });
    }
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

  uploadThread() {
    console.log(this.uploadForm);
    if (this.uploadForm.valid) {
      this.threadService.addThread(this.uploadForm.value).subscribe({
        next: (response) => {
          this.threads.push(response);
        },
        error: (error) => {
          console.error('Error uploading thread', error);
        },
      });
    } else {
      console.log('Form is not valid');
    }
  }
}
