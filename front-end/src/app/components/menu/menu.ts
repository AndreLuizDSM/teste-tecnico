import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { Auth } from '../../services/auth';
import { User } from '../../services/user';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './menu.html',
  styleUrl: './menu.scss'
})
export class Menu {

  userEmail: string | undefined

  constructor(private authService: Auth,
              private userService: User
  ){
  this.userEmail = this.userService.getUser()?.email;
  }




  get isLoggedIn(): boolean{
    return this.authService.isLoggedIn();
  }

  logout() {
    this.userService.logout()
  }
}
