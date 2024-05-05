import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ThreadService } from '../../services/thread.service';

@Component({
  selector: 'app-thread-modal',
  templateUrl: './thread-modal.component.html',
  styleUrl: './thread-modal.component.css',
})
export class ThreadModalComponent {
  @Output() threadCreated: EventEmitter<any> = new EventEmitter<any>();
  uploadForm: FormGroup;
  fileErrorMessage: string | null = null;
  @Input() forumId: number | undefined;

  constructor(
    private formBuilder: FormBuilder,
    private threadService: ThreadService,
    public bsModalRef: BsModalRef
  ) {
    this.uploadForm = this.formBuilder.group({
      title: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
        ],
      ],
    });
  }

  uploadThread() {
    if (this.uploadForm.invalid) {
      this.uploadForm.markAllAsTouched();
      return;
    }

    this.threadService
      .addThread({
        forumId: this.forumId ? this.forumId : 0,
        text: this.uploadForm.get('title')?.value,
      })
      .subscribe({
        next: (response) => {
          this.threadCreated.emit(response);
          this.resetFormAndCloseModal();
        },
        error: () => {},
      });
  }

  resetFormAndCloseModal() {
    this.bsModalRef.hide();
    this.uploadForm.reset();
  }
}
