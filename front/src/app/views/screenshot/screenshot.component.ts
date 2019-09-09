import { Component, OnInit } from '@angular/core';
import { ScreenshotService } from './screenshot.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-screenshot',
  templateUrl: './screenshot.component.html',
})
export class ScreenshotComponent implements OnInit {
  urls: Array<string>;
  username: string;
  assignmentName: string;
  commitNumber: number;
  constructor(private screenshotService: ScreenshotService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.username = this.route.snapshot.queryParamMap.get('username');
    this.assignmentName = this.route.snapshot.queryParamMap.get('assignmentName');
    this.commitNumber = Number(this.route.snapshot.queryParamMap.get('commitNumber'));
    this.getScreenshotUrls();
  }

  getScreenshotUrls() {
    this.screenshotService.getScreenshotUrls(this.username, this.assignmentName, this.commitNumber).subscribe(
      (resopnse) => {
        this.urls = resopnse.urls;
        console.log(resopnse);
      }
    );
  }

}
