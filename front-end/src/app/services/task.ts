import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Auth } from './auth';
import { User } from './user';
import { Observable, tap } from 'rxjs';

export interface TaskResponse {
  id: number;
  name: string;
  description: string;
  createdDate: string;
  eventAt: string;
}

export interface TaskPayload {
  name: string;
  description: string;
  eventAt: string;
}

@Injectable({
  providedIn: 'root',
})
export class Task {

  private apiUrl = 'http://localhost:8080/'

  // Signal para eu poder atualizar as tasks em tempo real
  private _task = signal<TaskResponse[] | null>(null);
  // Camada de segurança
  readonly task = this._task.asReadonly();


  constructor(private http: HttpClient,
    private authService: Auth,
    private userService: User
  ) {

    // Sempre que der reload na página , aparecer as tasks
    this.getTask();
  }
  setTask(data: TaskResponse[] | null): void {
    this._task.set(data);
  }


  getTask(): void {

    // Pega as tasks e coloca dentro do signal atualizado.
    this.http.get<TaskResponse[]>(`${this.apiUrl}task`, { headers: this.getHeaders()  })
      .subscribe({
        next: tasks => {this._task.set(tasks),
                        console.log('método get com sucesso')
        },
        error: () => {this._task.set([]),
                    console.log('método get com sucesso')
        }
      })
  }


  createTask(body: TaskPayload): Observable<TaskResponse[]> {

    // Cria task e faz o getTask para atualizar o signal e a tabela
    return this.http.post<TaskResponse[]>(`${this.apiUrl}task`, body, { headers: this.getHeaders() })
    .pipe(tap(task => {this.getTask() ,
                      console.log('Task criada: ' , task)
    }))
  }



  deleteTask(id: string): Observable<TaskResponse> {
  console.log('Task de id ' , id , ' será deletada')
    // Deleta task e faz o getTask para atualizar signal e a tabela
    return this.http.delete<TaskResponse>(`${this.apiUrl}task?id=${id}`, { headers: this.getHeaders() })
      .pipe(tap(task => {this.getTask() ,
                      console.log('Task deletada com sucesso ') }))
  }

  // Method getHeaders() para Authorization header
  getHeaders(): HttpHeaders {
    //    Evitar fazer isso em todos os métodos
    // const email = this.getEmailByToken(token);
    // if (!email) throw new Error('Token inválido');

    // const headers = new HttpHeaders({ Authorization: `${token}` })

    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `${token}`})

  }
}
