import { Component } from '@angular/core';
import { Forum } from '../../models/forum.model';
import { ForumService } from '../../services/forum.service';
import { ThreadService } from '../../services/thread.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
})
export class CategoriesComponent {
  forums: Forum[] = [];

  constructor(
    private forumService: ForumService,
    private threadService: ThreadService
  ) {}

  ngOnInit(): void {
    this.forumService.getForums().subscribe((forums: Forum[]) => {
      this.forums = forums;
    });
  }
}
