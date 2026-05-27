import { Injectable } from '@angular/core';

export interface TaskResponse {
  id: number;
  name: string;
  description: string;
  createdDate: string;
  eventAt: string;
}

@Injectable({
  providedIn: 'root',
})
export class Task {}
