import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThreadModalComponent } from './thread-modal.component';

describe('ThreadModalComponent', () => {
  let component: ThreadModalComponent;
  let fixture: ComponentFixture<ThreadModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ThreadModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ThreadModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
