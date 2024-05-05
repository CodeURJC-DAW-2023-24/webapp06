import { Component } from '@angular/core';
import { Forum } from '../../models/forum.model';
import { ForumService } from '../../services/forum.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
})
export class CategoriesComponent {
  forums: Forum[] = [];

  constructor(
    private forumService: ForumService
  ) {}

  ngOnInit(): void {
    this.forumService.getForums().subscribe((forums: Forum[]) => {
      this.forums = forums;
    });
  }
}
