import { Component } from '@angular/core';
import { Forum } from '../../models/forum.model';
import { ThreadService } from '../../services/thread.service';
import { ActivatedRoute } from '@angular/router';
import { ForumService } from '../../services/forum.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  $forums: Observable<Forum[]>;
  $trending: Observable<Forum[]>;

  constructor(private activatedRoute: ActivatedRoute, private forumService: ForumService) {
    this.$forums = forumService.getForums();
    this.$trending = forumService.getTrending();
  }
}
