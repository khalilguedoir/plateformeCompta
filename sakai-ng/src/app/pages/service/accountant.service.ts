import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Accountant } from '../shared/models/Accountant';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountantService {
  private apiUrl = 'http://localhost:8080/api/accountants';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Accountant[]> {
    return this.http.get<Accountant[]>(this.apiUrl);
  }

  getById(id: number): Observable<Accountant> {
    return this.http.get<Accountant>(`${this.apiUrl}/${id}`);
  }

  create(accountant: Accountant): Observable<Accountant> {
    return this.http.post<Accountant>(this.apiUrl, accountant);
  }

  update(id: number, accountant: Accountant): Observable<Accountant> {
    return this.http.put<Accountant>(`${this.apiUrl}/${id}`, accountant);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}