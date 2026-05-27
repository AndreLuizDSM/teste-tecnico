import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserResponse } from './user';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class Auth {

  // Dependencia do @auth0 para decodificar token
  private jwtService = new JwtHelperService;

  // Conteúdo salvo no LocalStorage
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER = 'logged_user';


  // Trabalhar com o token no localStorage.
  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Salvar usuário junto com token no LocalStorage.
  saveUser(user: UserResponse): void {

    localStorage.setItem(this.USER, JSON.stringify(user));
  }

  deleteToken(): void{
    localStorage.clear();
  }

  // Pegar o tempo de expiração para validar o token.
  getExpirationTimeByToken(token: string): Date | null{
    try {
    const decoded = this.jwtService.decodeToken(token);
    if(!decoded.exp) return null

    return new Date(decoded.exp * 1000)
    } catch(error){
      console.log(error)
      return null
    }

  }

  // Método para que o Signal no userService seja atualizado , por isso as UserResponse
  getUser(): any | null {
    const user = localStorage.getItem(this.USER)
    if (!user) return null

    return JSON.parse(user) as UserResponse;
  }

  // Logout removendo todo o conteúdo do LocalStorage
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER);
  }

  isTokenExpired(token: string): boolean{
    const tokenExp = this.jwtService.decodeToken(token)
    if (!tokenExp) return true; // Se não tiver token, trata como se tivesse expirado

    const dataAtual = Math.floor(Date.now() / 1000)
    return dataAtual > tokenExp.exp // Se dataAtual for maior, retorna true
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if(!token) return false

      return  !this.isTokenExpired(token);  // !! -> Retorna valor boolean do método
  }
}
