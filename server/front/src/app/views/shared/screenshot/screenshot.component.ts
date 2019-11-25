import { Component, OnInit, Input, OnChanges, ViewChild, Output, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
@Component({
  selector: 'app-screenshot',
  templateUrl: './screenshot.component.html',
  styleUrls: ['./screenshot.component.scss']
})
export class ScreenshotComponent implements OnInit, OnChanges {
  @ViewChild('screenshotModal', { static: true }) public screenshotModal: ModalDirective;
  @Input() screenshotUrls;
  @Input() commitNumber;
  @Output() screenshotEvent = new EventEmitter();

  selectedScreenshotName: string;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    // Initial screenshot Name
    if (this.screenshotUrls && !this.selectedScreenshotName) {
      const url_split = this.screenshotUrls[0].split('/');
      this.selectedScreenshotName = url_split[url_split.length - 1];
    }
  }

  updateScreenshotName() {
    if (this.screenshotUrls) {
      const url_split = $('.screenshot:visible').attr('src').split('/');
      this.selectedScreenshotName = url_split[url_split.length - 1];
    }

  }

}
