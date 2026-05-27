import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TaskResponse } from './task';
import { Auth } from './auth';
import { JwtHelperService } from '@auth0/angular-jwt';

export interface UserResponse {
  id: string;
  email: string;
  name: string;
  tasks: TaskResponse[] | null;
}
export interface loginPayload {
  email: string;
  password: string
}

@Injectable({
  providedIn: 'root',
})
export class User {

   private apiUrl = "http://localhost:8080/";
  private jwtService = new JwtHelperService;

  // Signal para eu poder atualizar dados e as tasks em tempo real
  private _user = signal<any | null>(null);
  // Camada de segurança para o _user
  readonly user = this._user.asReadonly();

  constructor(private http: HttpClient, private authService: Auth, private router: Router) {

    // Como o signal começa com null , caso um usuário aperte F5 , o constructor atualiza o signal com o user salvo do LocalStorage.
    const usuarioSalvo = authService.getUser();
    if (usuarioSalvo) {
      this.setUser(usuarioSalvo);
    }
  }

  getUser(): UserResponse | null {
    return this.user();
  }

  setUser(data: UserResponse | null): void {
    this._user.set(data);
  }

  // Endpoint POST /user
  register(body: any): Observable<UserResponse> {
    return this.http.post<any>(`${this.apiUrl}user`, body)
  }

  // Endpoint POST /user/login , return token jwt
  login(body: loginPayload): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}user/login`, body, { responseType: 'text' as 'json' })
  }

  // Faz o logout , excluindo o token e user do LocalStorage , e setando o Signal como null
  logout(): void {
    this.authService.logout();
    this.setUser(null);
  }

  //Extrair subject do Token JWT
  getEmailByToken(token: string): string | null {
    try {
      const decoded = this.jwtService.decodeToken(token);
      return decoded?.sub || null;
    } catch (error) {
      console.log(error)
      return null
    }
  }

  // Endpoint GET /user
  getUserByEmail(token: string): Observable<UserResponse> {

    const email = this.getEmailByToken(token);
    if (!email) throw new Error('Token inválido');

    const headers = new HttpHeaders({ Authorization: `${token}` })

    return this.http.get<UserResponse>(`${this.apiUrl}user?email=${email}`, { headers })

  }


}
