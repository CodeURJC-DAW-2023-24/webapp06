import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostService } from '../../services/post.service';
import { ActivatedRoute } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-post-modal',
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css',
})
export class PostModalComponent {
  @Output() postCreated: EventEmitter<void> = new EventEmitter<void>();
  postForm: FormGroup;
  fileErrorMessage: string | null = null;
  threadId!: number;

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
    private activatedRoute: ActivatedRoute,
    public bsModalRef: BsModalRef
  ) {
    this.postForm = this.formBuilder.group({
      text: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
        ],
      ],
      imageFile: [null],
    });
  }

  validateFileType(control: any) {
    const file = control.value as File;
    if (file) {
      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
      if (!allowedTypes.includes(file.type)) {
        return { invalidFileType: true };
      }
    }
    return null;
  }

  loadFile(event: any) {
    const file = event.target.files[0] as File;
    const output = document.getElementById('imagePreview') as HTMLInputElement;

    if (!file || !file.type.startsWith('image/')) {
      this.fileErrorMessage = file ? 'Invalid file type' : null;
      this.postForm.controls['imageFile'].setValue(null);
      output.src = '';
    } else {
      this.fileErrorMessage = null;
      output.src = URL.createObjectURL(file);
    }

    output.onload = function () {
      URL.revokeObjectURL(output.src);
    };
  }

  imagePreviewHeight() {
    return this.postForm.get('imageFile')?.value ? '150' : 'auto';
  }

  resetFormAndCloseModal() {
    this.postForm.reset();
    this.bsModalRef.hide();
  }

  uploadPost() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      return;
    }

    const text = this.postForm.get('text')?.value;
    const fileInput = document.getElementById(
      'postPicture'
    ) as HTMLInputElement;
    const formData = new FormData();
    if (fileInput.files && fileInput.files[0]) {
      formData.append('image', fileInput.files[0]);
    }

    this.postService.createPost(text, this.threadId).subscribe({
      next: (response) => {
        if (fileInput.files && fileInput.files[0]) {
          const postId = response.id;
          this.uploadImage(postId, formData);
        }
        this.postCreated.emit();
        this.resetFormAndCloseModal();
      },
      error: (error) => {
        console.error('Error uploading post', error);
      },
    });
  }

  uploadImage(postId: number, formData: FormData) {
    this.postService.setPostImage(postId, formData).subscribe({
      next: () => {},
      error: (error) => {
        console.error('Error uploading image', error);
      },
    });
  }
}
