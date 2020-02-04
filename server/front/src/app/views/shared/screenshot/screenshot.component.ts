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
  @Input() type;
  @Output() screenshotEvent = new EventEmitter();

  selectedScreenshotName: string;

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    // Initial screenshot Name
    if (this.screenshotUrls && this.screenshotUrls.length !== 0 && !this.selectedScreenshotName) {
      const url_split = this.screenshotUrls[0].split('/');
      this.selectedScreenshotName = this.rename(url_split);
    }
  }

  updateScreenshotName() {
    if (this.screenshotUrls && this.screenshotUrls.length !== 0) {
      const url_split = $('.screenshot:visible').attr('src').split('/');
      this.selectedScreenshotName = this.rename(url_split);
    }
  }

  rename(url_split: Array<string>) {
    const screenshotName = url_split[url_split.length - 1];
    let name = '';
    // Change png to html. Ex: index.png -> index.html
    console.log(this.type);
    if (this.type === 'WEB') {
      name = screenshotName.substring(0, screenshotName.indexOf('.png')) + '.html';
    } else if (this.type === 'ANDROID') {
      name = screenshotName.substring(screenshotName.indexOf('_') + 1
        , screenshotName.indexOf('.png')) + '.java';
    }
    return name;

  }

}
