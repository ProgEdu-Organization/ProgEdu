import { Component, OnInit, Input, Output, EventEmitter, OnChanges } from '@angular/core';

@Component({
  selector: 'app-screenshot',
  templateUrl: './screenshot.component.html',
  styleUrls: ['./screenshot.component.scss']
})
export class ScreenshotComponent implements OnInit, OnChanges {
  @Input() isCollapsed;
  @Input() selectedScreenshotName;
  @Input() screenshotUrls;
  @Output() messageToEmit = new EventEmitter<string>();
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
      this.messageToEmit.emit(this.selectedScreenshotName);
    }

  }

}
