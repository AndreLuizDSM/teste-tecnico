import { Component, ViewEncapsulation } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Auth } from '../../services/auth';
import { Task, TaskResponse } from '../../services/task';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    DatePipe,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
  encapsulation: ViewEncapsulation.None,
})
export class Home {

  displayedColumns = ['name', 'description', 'eventAt', 'acoes'];
  showForm = false;
  taskForm: FormGroup;

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  constructor(
    private authService: Auth,
    private taskService: Task,
    private fb: FormBuilder
  ) {
    this.taskForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      eventAt: ['', Validators.required]
    });
  }

  getTasks(): TaskResponse[] {
    return this.taskService.task() ?? [];
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (!this.showForm) this.taskForm.reset();
  }

  createTask(): void {
    if (this.taskForm.invalid) return;

    this.taskService.createTask(this.taskForm.value).subscribe({
      next: () => {
        this.toggleForm();
      },
      error: (err) => console.error(err)
    });
  }

  deleteTask(id: string): void {
    this.taskService.deleteTask(id).subscribe();
  }
}
