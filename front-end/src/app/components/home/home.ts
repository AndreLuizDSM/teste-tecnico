import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTable, MatTableModule } from '@angular/material/table';
import { Auth } from '../../services/auth';
import { Task, TaskResponse } from '../../services/task';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    DatePipe,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {

  displayedColumns = ['name', 'description', 'eventAt', 'acoes'];



  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  constructor(private authService: Auth, private taskService: Task) {
  }

  getTasks(): TaskResponse[] {
    return this.taskService.task() ?? []
  }

  deleteTask(id: string): void {
    this.taskService.deleteTask(id).subscribe();
  }

  openCreateTask(): void {
    // implementar depois
  }
}
