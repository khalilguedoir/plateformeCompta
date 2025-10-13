import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    if (!this.authService.isAuthenticated()) {
      return this.router.parseUrl('/auth/login');
    }

    const role = this.authService.getRole();
    if (role === 'COMPANY') {
      return true; // accès aux routes /dashboard /pages/company
    } else if (role === 'ACCOUNTANT') {
      return true; // accès aux routes /accountantRoutes
    }

    // si rôle inconnu
    return this.router.parseUrl('/auth/access');
  }
}
