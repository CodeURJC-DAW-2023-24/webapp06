import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Post } from '../../models/post.model';
import { User } from '../../models/user.model';
import { LoginService } from '../../services/login.service';
import { PostService } from '../../services/post.service';
import { PostModalComponent } from '../post-modal/post-modal.component';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class PostComponent {
  loggedIn: boolean = false;
  activeUser: User | undefined;

  @Input()
  post!: Post;
  @Input()
  threadId!: number;
  isAuthor: boolean = false;
  isAdmin: boolean = false;
  elapsedTime: String = '';
  isLiked: boolean = false;
  isDisliked: boolean = false;
  isReported: boolean = false;

  @Output() edited: EventEmitter<void> = new EventEmitter<void>();
  @Output() deleted: EventEmitter<number> = new EventEmitter<number>();

  constructor(
    private loginService: LoginService,
    private postService: PostService,
    private modalService: BsModalService
  ) {}

  ngOnInit(): void {
    this.loggedIn = this.loginService.isLogged();
    this.activeUser = this.loginService.currentUser();
    if (this.loggedIn && this.activeUser != undefined) {
      this.isAdmin = this.activeUser.roles.includes('ADMIN');
    }

    const postTime: Date = new Date(this.post?.createdAt || '');
    const currentTime: Date = new Date();
    const seconds: number = Math.max(
      Math.floor((currentTime.getTime() - postTime.getTime()) / 1000),
      0
    );
    const days: number = Math.floor(seconds / (60 * 60 * 24));
    const hours: number = Math.floor(seconds / (60 * 60));
    const minutes: number = Math.floor(seconds / 60);

    if (days > 0) {
      this.elapsedTime = days + ' days ago';
    } else if (hours > 0) {
      this.elapsedTime = hours + ' hours ago';
    } else if (minutes > 0) {
      this.elapsedTime = minutes + ' minutes ago';
    } else {
      this.elapsedTime = seconds + ' seconds ago';
    }

    if (this.activeUser != undefined) {
      this.isAuthor = this.activeUser.id === this.post.owner.id;

      this.isLiked = this.post.userLikes.some(
        (user) => user.id === this.activeUser?.id
      );

      this.isDisliked = this.post.userDislikes.some(
        (user) => user.id === this.activeUser?.id
      );
    }
  }

  handleImageError() {
    this.post.hasImage = false;
  }

  toggleLike() {
    if (this.loggedIn) {
      if (this.isLiked) {
        this.postService.unlikePost(this.post.id).subscribe(() => {});
        this.isLiked = false;
        this.post.likes--;
      } else {
        this.postService.likePost(this.post.id).subscribe(() => {});
        this.isLiked = true;
        this.post.likes++;
        if (this.isDisliked) {
          this.postService.undislikePost(this.post.id).subscribe(() => {});
          this.isDisliked = false;
          this.post.dislikes--;
        }
      }
    }
  }

  toggleDislike() {
    if (this.loggedIn) {
      if (this.isDisliked) {
        this.postService.undislikePost(this.post.id).subscribe(() => {});
        this.isDisliked = false;
        this.post.dislikes--;
      } else {
        this.postService.dislikePost(this.post.id).subscribe(() => {});
        this.isDisliked = true;
        this.post.dislikes++;
        if (this.isLiked) {
          this.postService.unlikePost(this.post.id).subscribe(() => {});
          this.isLiked = false;
          this.post.likes--;
        }
      }
    }
  }

  reportPost() {
    if (this.loggedIn) {
      if (!this.isReported) {
        this.postService.reportPost(this.post.id).subscribe(() => {
          this.isReported = true;
        });
      }
    }
  }

  editPost() {
    if (this.isAdmin || this.isAuthor) {
      const initialState = {
        threadId: this.threadId,
        post: this.post,
        isEditMode: true,
      };
      const modalRef = this.modalService.show(PostModalComponent, {
        initialState,
      });
      modalRef.content?.postCreated.subscribe(() => {
        this.edited.emit();
      });
    }
  }

  deletePost() {
    if (this.isAdmin || this.isAuthor) {
      this.postService.deletePost(this.post.id).subscribe({
        next: () => {
          this.deleted.emit(this.post.id);
        },
      });
    }
  }
}
