import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ReportedPost } from '../../models/reportedPost.model';
import { ThreadService } from '../../services/thread.service';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-post-reported',
  templateUrl: './post-reported.component.html',
  styleUrl: './post-reported.component.css',
})
export class PostReportedComponent {
  @Input()
  post!: ReportedPost;
  validationSuccessful: boolean = false;
  @Output() postValidated: EventEmitter<number> = new EventEmitter<number>();

  constructor(
    private postService: PostService,
    private threadService: ThreadService
  ) {}

  validatePost() {
    this.postService.validatePost(this.post.id).subscribe({
      next: () => {
        this.validationSuccessful = true;
        this.postValidated.emit(this.post.id);
      },
      error: () => {},
    });
  }
}
