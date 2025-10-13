import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../pages/service/auth.service';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { RippleModule } from 'primeng/ripple';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, PasswordModule, CheckboxModule, RippleModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  checked: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login(this.email, this.password).subscribe({
      next: (res) => {
        const role = this.authService.getRole();
        if (role === 'COMPANY') {
          this.router.navigate(['/pages/company/dashboard']);
        } else if (role === 'ACCOUNTANT') {
          this.router.navigate(['/pages/accountant/dashboard']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        console.error('Login failed', err);
        alert('Email ou mot de passe incorrect.');
      }
    });
  }
}
