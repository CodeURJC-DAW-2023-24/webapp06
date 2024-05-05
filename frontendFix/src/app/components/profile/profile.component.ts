import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Thread } from '../../models/thread.model';
import { PostService } from '../../services/post.service';
import { ThreadService } from '../../services/thread.service';
import { UserService } from '../../services/user.service';
import { LoginService } from './../../services/login.service';
import { ProfileService } from './../../services/profile.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent {
  userName: string = '';
  threads: any = ([] = []);
  userId: number | undefined = undefined;
  noUser: boolean = false;
  numberThreads: number = 0;
  numberPosts: number = 0;
  isAdmin = false;
  isUser = false;

  constructor(
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    private threadService: ThreadService,
    private ProfileService: ProfileService,
    private userService: UserService,
    private postService: PostService,
    private router: Router
  ) {
    const userName = this.activatedRoute.snapshot.params['userName'];
    this.loginService.reqIsLogged().subscribe({
      next: (isLogged) => {
        const user: User | undefined = this.loginService.currentUser();
        this.userName = userName;
        this.isUser = user?.username == userName;
        this.isAdmin =
          this.loginService.isAdmin() || user?.username == userName;
      },
      error: () => {},
    });

    this.userService.getUserIdByUsername(userName).subscribe((userId) => {
      if (userId !== null) {
        this.userId = userId;
        this.postService
          .getPostsByUser(this.userId)
          .subscribe((response: any) => {
            const posts = response.content;
            this.numberPosts = posts.length;
          });
      } else {
        this.router.navigate(['/404']);
      }
    });
    this.threadService
      .getThreadsByUser(userName)
      .subscribe((threads: Thread[]) => {
        this.threads = threads;
        this.numberThreads = threads.length;
      });
  }

  isLogged = this.loginService.isLogged();

  deleteUser(id: number | undefined) {
    if (id === undefined) return;
    console.log(id);
    this.userService.deleteUser(id).subscribe({
      next: () => {
        if (this.isUser) {
          this.loginService.especialLogout();
        }
        this.router.navigate(['/']);
      },
      error: (error) => {
        if (this.isUser) {
          this.loginService.especialLogout();
        }
        this.router.navigate(['/']);
      },
    });
  }
}
