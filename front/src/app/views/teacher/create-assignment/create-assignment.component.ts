import { Component, OnInit, OnDestroy } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-assignment',
  templateUrl: './create-assignment.component.html'
})
export class CreateAssignmentComponent implements OnInit, OnDestroy {
  status: { isOpen: boolean } = { isOpen: false };
  disabled: boolean = false;
  isDropup: boolean = true;
  autoClose: boolean = false;

  items: string[] = [
    'The first choice!',
    'And another choice for you.',
    'but wait! A third!'
  ];
  constructor(private router: Router) { }

  ngOnInit() {
  }

  changeToAssignmentPage() {
    this.router.navigate(['./dashboard/assignmentManagement']);
  }


  ngOnDestroy() {
    this.status.isOpen = false;
  }

  onHidden(): void {
    console.log('Dropdown is hidden');
  }
  onShown(): void {
    console.log('Dropdown is shown');
  }
  isOpenChange(): void {
    console.log('Dropdown state is changed');
  }

  toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isOpen = !this.status.isOpen;
  }

  change(value: boolean): void {
    this.status.isOpen = value;
  }

}
