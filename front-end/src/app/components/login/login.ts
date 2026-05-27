import { Component, ViewEncapsulation } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { User } from '../../services/user';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private userService: User,
              private authService: Auth,
              private router: Router
  ) {
     this.loginForm = this.formBuilder.group({
      email: this.formBuilder.control('', {validators: [Validators.required, Validators.email], nonNullable: true}),
      password: this.formBuilder.control('', {validators: [Validators.required], nonNullable: true})
    });
  }

  onSubmit() {

    const formData = this.loginForm.value

    console.log(formData)

    this.userService.login(formData).subscribe({
      next: (response) => {

        // Salva o token
        this.authService.saveToken(response),
        console.log("Usuário logado , token salvo no storage como: auth_token")

        // Usa o método GET da service
        this.userService.getUserByEmail(response).subscribe({
        next: (user) => {
          // Salva no localStorage e no Signal
          this.authService.saveUser(user),
          this.userService.setUser(user),
          console.log("Usuário pego após login: " , user)

          // Leva para a homepage
          this.router.navigate(['/'])
        }
        })
      },

      error: (erro) => console.log('Erro ao fazer login: ', erro)
    })
  }
}
