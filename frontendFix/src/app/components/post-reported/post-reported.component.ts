import { Component, Input } from '@angular/core';
import { ReportedPost } from '../../models/reportedPost.model';
import { ThreadService } from '../../services/thread.service';

@Component({
  selector: 'app-post-reported',
  templateUrl: './post-reported.component.html',
  styleUrl: './post-reported.component.css',
})
export class PostReportedComponent {
  @Input()
  post!: ReportedPost;

  constructor(private threadService: ThreadService) {
    
  }

  
}
