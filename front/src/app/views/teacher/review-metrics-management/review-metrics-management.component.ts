import { ReviewMetricsManagementService } from './review-metrics-management.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Category, Assessment } from './Category';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';



@Component({
  selector: 'app-review-metrics-management',
  templateUrl: './review-metrics-management.component.html',
  styleUrls: ['./review-metrics-management.component.scss']
})
export class ReviewMetricsManagementComponent implements OnInit {

  assessments: Assessment[];
  categories: Category[];
  selectedCategory: Category;
  selectedAssessment: Assessment;
  errorResponse: HttpErrorResponse;
  errorTitle: string;

  @ViewChild('DeleteErrorModal', { static: true }) public deleteErrorModal: ModalDirective;

  constructor(private reviewMetricsManagementService: ReviewMetricsManagementService) { }

  ngOnInit() {
    this.reviewMetricsManagementService.getAllCategory().subscribe(
      response => {
        this.categories = response['allCategory'];
        this.getAllMetrics();
      }
    );
  }

  getAllMetrics() {
    for (const category of this.categories) {
      this.reviewMetricsManagementService.getMetrics(category).subscribe(
        response => {
          category.allMetrics = response['allMetrics'];
        }
      );
    }
  }

  addCategory(name: string, metrics: string): void {
    const new_category = { id: 0, name: name, metrics: metrics, allMetrics: [] };
    this.reviewMetricsManagementService.createCategory(new_category).subscribe(
      resopnse => {
        this.reviewMetricsManagementService.getAllCategory().subscribe(
          response => {
            this.categories = response['allCategory'];
            this.getAllMetrics();
          }
        );
      }
    );
  }
  editCategory(metrics: string): void {
    this.selectedCategory.metrics = metrics;
    this.reviewMetricsManagementService.editCategory(this.selectedCategory).subscribe();
  }
  deleteCategory(): void {
    if (this.selectedCategory != null) {
      this.reviewMetricsManagementService.deleteCategory(this.selectedCategory).subscribe(
        response => {
          this.categories.splice(this.categories.indexOf(this.selectedCategory), 1);
          this.selectedCategory = null;
        },
        error => {
          this.errorTitle = 'Delete Category Error';
          this.errorResponse = error;
        }
      );
    }
  }
  addAssessment(name: string, description: string, link: string, mode: string): void {
    const new_assessment = {
      id: 0, metrics: name,
      description: description, link: link, mode: mode === 'Yes No mode' ? 1 : 2, category: this.selectedCategory.id
    };
    this.reviewMetricsManagementService.createMetrics(this.selectedCategory, new_assessment).subscribe(
      response => {
        this.reviewMetricsManagementService.getMetrics(this.selectedCategory).subscribe(
          metrics => {
            this.selectedCategory.allMetrics = metrics['allMetrics'];
            this.onSelectCategory(this.selectedCategory);
          }
        );
      }
    );
  }
  editAssessment(metrics: string, description: string, link: string, mode: string): void {
    this.selectedAssessment.metrics = metrics;
    this.selectedAssessment.description = description;
    this.selectedAssessment.link = link;
    this.selectedAssessment.mode = (mode === 'Yes No mode' ? 1 : 2);
    this.reviewMetricsManagementService.editMetrics(this.selectedAssessment).subscribe();
  }
  deleteAssessment(): void {
    if (this.selectedAssessment != null) {
      this.reviewMetricsManagementService.deleteMetrics(this.selectedAssessment).subscribe(
        response => {
          this.assessments.splice(this.assessments.indexOf(this.selectedAssessment), 1);
          this.selectedAssessment = null;
        },
        error => {
          this.errorTitle = 'Delete Metrics Error';
          this.errorResponse = error;
          console.log(error);
        }
      );
    }
  }

  onSelectCategory(category: Category): void {
    this.selectedCategory = category;
    this.selectedAssessment = null;
    this.assessments = this.selectedCategory.allMetrics;
  }

  onSelectAssessment(assessment: Assessment): void {
    this.selectedAssessment = assessment;
  }


}
