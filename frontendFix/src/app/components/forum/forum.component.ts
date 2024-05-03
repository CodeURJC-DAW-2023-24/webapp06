import { LoginService } from './../../services/login.service';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ThreadService } from './../../services/thread.service';
import { Thread } from '../../models/thread.model';
import { User } from '../../models/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrl: './forum.component.css'
})
export class ForumComponent{

  threads: any = [] = [];
  forumName: string = "";
  forumIcon: string ="";
  forumId: number = 0;
  forums: any = [] = [];
  user: User | undefined;
  loggedIn: boolean = false;
  isAdmin: boolean | undefined;
  uploadForm!: FormGroup;

  constructor(public LoginService: LoginService, private activatedRoute: ActivatedRoute, private threadService: ThreadService, private fb: FormBuilder) {
    this.reqForum();

  }

  ngOnInit(): void {

  }

  reqForum(){
    this.activatedRoute.params.subscribe(params => {
      this.forumName = params['forumName'];
      this.forumId = this.threadService.getForumId(this.forumName);
      this.uploadForm = this.fb.group({
        text: ['', Validators.required],
        forumId: [this.forumId, Validators.required]
      });
      this.threadService.getThreadsByForumName(this.forumName).subscribe((threads: Thread[]) => {
        this.threads = threads;
        this.forumIcon = this.threadService.getForumIcon(this.forumName);
        this.reqIslogged();

      });
    });

  }

  reqIslogged() {
    this.LoginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        this.loggedIn = isLogged;
        if (isLogged) {
          this.user = this.LoginService.currentUser();
          this.isAdmin = this.LoginService.isAdmin();
        } else {
          this.user = undefined;
          this.isAdmin = false;
        }
      },
      error: () => { }
    });
  }

  uploadThread() {
    console.log(this.uploadForm)
    if (this.uploadForm.valid) {
      this.threadService.addThread(this.uploadForm.value).subscribe({
        next: (response) => {
          this.threads.push(response);
        },
        error: (error) => {
          console.error('Error uploading thread', error);
        }
      });
    } else {
      console.log('Form is not valid');
    }
  }
}




