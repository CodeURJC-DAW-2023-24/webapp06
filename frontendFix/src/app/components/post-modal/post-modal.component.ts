import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post-modal',
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css',
})
export class PostModalComponent {
  postForm: FormGroup;
  selectedFile: File | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute
  ) {
    this.postForm = this.formBuilder.group({
      text: [
        '',
        [
          Validators.required,
          Validators.maxLength(255),
          Validators.minLength(1),
        ],
      ],
      imageFile: [null, [this.validateFileType]],
    });
  }

  validateFileType(control: any) {
    const file = control.value;
    if (file) {
      const fileType = file.type.split('/')[0];
      if (fileType !== 'image') {
        return { invalidFileType: true };
      }
    }
    return null;
  }

  loadFile(event: any) {
    var output = document.getElementById('imagePreview') as HTMLInputElement;
    output.src = URL.createObjectURL(event.target.files[0]);
    output.onload = function () {
      URL.revokeObjectURL(output.src);
    };
  }

  uploadPost() {
    throw new Error('Method not implemented.');
  }
}
