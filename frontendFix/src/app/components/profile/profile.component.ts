import { User } from './../../models/user.model';
import { ProfileService } from './../../services/profile.service';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from './../../services/login.service';
import { Component } from '@angular/core';
import { ThreadService } from '../../services/thread.service';
import { UserService } from '../../services/user.service';
import { Thread } from '../../models/thread.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  userName: string = "";
  threads: any = [] = [];
  userId: number | null = null;

  constructor(public loginService: LoginService, private activatedRoute: ActivatedRoute, private threadService: ThreadService, private ProfileService : ProfileService, private UserService: UserService) {

    const userName = this.activatedRoute.snapshot.params['userName'];
    this.userName = userName;
    this.UserService.getUserIdByUsername(userName).subscribe
      (
        userId => {
          if (userId !== null) {
            this.userId = userId;
            console.log('ID del usuario:', userId);

          } else {
            console.log('El usuario no se encontró.');

          }
        },
        error => {
          console.error('Error al obtener la ID del usuario:', error);
        }
      );

    threadService.getThreadsByUser(userName).subscribe(
      (threads: Thread[]) => this.threads = threads
    );
  }



  isAdmin = this.loginService.isAdmin();


}
