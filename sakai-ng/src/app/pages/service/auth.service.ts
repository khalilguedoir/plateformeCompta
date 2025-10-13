// auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface User {
  id: number;
  username: string;
  email: string;
  roles?: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  // Login
  login(username: string, password: string): Observable<any> {
    return this.http.post<{ token: string; user: User }>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(res => {
        // Stocke le token et les infos utilisateur
        localStorage.setItem('token', res.token);
        localStorage.setItem('currentUser', JSON.stringify(res.user));
      })
    );
  }

  // Register
  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { username, email, password });
  }
getRole(): string | null {
  const user = this.getUser();
  return user?.roles?.[0] || null;
}

  // Logout
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
  }

  // Récupérer le token JWT
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUser(): User | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }
}
