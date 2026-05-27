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
  selector: 'app-register',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {

  registerForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private userService: User,
              private authService: Auth,
              private router: Router
  ) {

    this.registerForm = this.formBuilder.group({
      name: this.formBuilder.control('', {validators: [Validators.required], nonNullable: true}),
      email: this.formBuilder.control('', {validators: [Validators.required, Validators.email], nonNullable: true}),
      password: this.formBuilder.control('', {validators: [Validators.required], nonNullable: true})
    });
  }

  onSubmit() {

    // Valor do form
    const formData = this.registerForm.value;

    // Registra o usuário e leva ele para a tela de login
    this.userService.register(formData).subscribe({

      next: (response) => { this.router.navigate(['/login']),
        console.log("Usuário registrado " , response)
       },
      error: (error) => { console.log('Erro ao registrar usuário', error) },
    })
  }
}
