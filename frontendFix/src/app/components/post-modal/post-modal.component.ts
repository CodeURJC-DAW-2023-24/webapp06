import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Post } from '../../models/post.model';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-post-modal',
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css',
})
export class PostModalComponent {
  @Output() postCreated: EventEmitter<void> = new EventEmitter<void>();
  postForm: FormGroup;
  fileErrorMessage: string | null = null;
  @Input() threadId: number | undefined;
  @Input() isEditMode: boolean = false;
  @Input() post: Post | undefined;

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
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

  ngOnInit() {
    this.setInitialValues();
  }

  setInitialValues() {
    if (this.isEditMode && this.post) {
      this.postForm.patchValue({
        text: this.post.text,
      });
      const output = document.getElementById('imagePreview') as HTMLInputElement;
      if (this.post.hasImage) {
        output.src = '/api/posts/' + this.post.id + '/image';
      }
    }
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
    const output = document.getElementById('imagePreview') as HTMLInputElement;
    if (output.src !== '') {
      return '150';
    }
    return this.postForm.get('imageFile')?.value ? '150' : 'auto';
  }

  resetFormAndCloseModal() {
    this.bsModalRef.hide();
    this.postForm.reset();
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
    if (!this.isEditMode && this.threadId) {
      this.postService.createPost(text, this.threadId).subscribe({
        next: (response) => {
          if (fileInput.files && fileInput.files[0]) {
            const postId = response.id;
            this.uploadImage(postId, formData);
          } else {
            this.postCreated.emit();
            this.resetFormAndCloseModal();
          }
        },
        error: (error) => {
          console.error('Error uploading post', error);
        },
      });
    } else if (this.isEditMode && this.post && this.threadId) {
      this.postService.editPost(this.post.id, text).subscribe({
        next: () => {
          if (fileInput.files && fileInput.files[0] && this.post) {
            this.uploadImage(this.post.id, formData);
          } else {
            this.postCreated.emit();
            this.resetFormAndCloseModal();
          }
        },
        error: (error) => {
          console.error('Error editing post', error);
        },
      });
    }
  }

  uploadImage(postId: number, formData: FormData) {
    this.postService.setPostImage(postId, formData).subscribe({
      next: () => {
        this.postCreated.emit();
        this.resetFormAndCloseModal();
      },
      error: (error) => {
        console.error('Error uploading image', error);
      },
    });
  }
}
