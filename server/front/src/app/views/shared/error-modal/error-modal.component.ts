import { Component, OnInit, Input, ViewChild, OnChanges } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'progedu-error-modal',
  templateUrl: './error-modal.component.html',
  styleUrls: ['./error-modal.component.scss']
})
export class ErrorModalComponent implements OnInit, OnChanges {

  @Input() errorResponse: HttpErrorResponse;
  @Input() errorTitle: string;
  errorMsg: string;
  status: number;
  help: string = 'Please Find the Selab245';
  @ViewChild('errorModal', { static: false }) public errorModal: ModalDirective;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    if (this.errorResponse) {
      this.status = this.errorResponse.status;
      console.log(this.errorResponse.status);
      if (this.errorResponse.status === 500) {
        this.errorMsg = this.help;
        this.errorModal.show();
      } else {
        this.errorMsg = this.errorResponse.message;
        this.errorModal.show();
      }
    }
  }
}
