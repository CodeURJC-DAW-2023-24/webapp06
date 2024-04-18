import { ActivatedRoute } from '@angular/router';
import { LoginService } from './../../services/login.service';
import { Component } from '@angular/core';
import { ThreadService } from '../../services/thread.service';
import { Thread } from '../../models/thread.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  userName: string = "";
  threads: any = [] = [];

  constructor(public loginService: LoginService, private activatedRoute: ActivatedRoute, private threadService: ThreadService) {

    const userName = this.activatedRoute.snapshot.params['userName'];
    this.userName = userName;
    threadService.getThreadsByUser(userName).subscribe(
      (threads: Thread[]) => this.threads = threads
    );




  }

  isAdmin = this.loginService.isAdmin();


}
