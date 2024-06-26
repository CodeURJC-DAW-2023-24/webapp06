import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedPostsComponent } from './reported-posts.component';

describe('ReportedPostsComponent', () => {
  let component: ReportedPostsComponent;
  let fixture: ComponentFixture<ReportedPostsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReportedPostsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportedPostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
