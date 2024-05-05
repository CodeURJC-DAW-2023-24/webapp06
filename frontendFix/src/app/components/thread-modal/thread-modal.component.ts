import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThreadService } from '../../services/thread.service';
import { BsModalRef } from 'ngx-bootstrap/modal';

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
      text: ['', Validators.required],
      forumId: [this.forumId, Validators.required],
    });
  }

  uploadThread() {
    if (this.uploadForm.invalid) {
      this.uploadForm.markAllAsTouched();
      return;
    }

    console.log(this.uploadForm);
    this.threadService.addThread(this.uploadForm.value).subscribe({
      next: (response) => {
        this.threadCreated.emit(response);
        this.resetFormAndCloseModal();
      },
      error: (error) => {
        console.error('Error uploading thread', error);
      },
    });
  }

  resetFormAndCloseModal() {
    this.bsModalRef.hide();
    this.uploadForm.reset();
  }
}
