import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReglePaiement } from '../models/ReglePaiement';

@Injectable({
  providedIn: 'root'
})
export class ReglePaiementService {
  private apiUrl = 'http://localhost:8080/api/regles-paiements';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ReglePaiement[]> {
    return this.http.get<ReglePaiement[]>(this.apiUrl);
  }

  getById(id: number): Observable<ReglePaiement> {
    return this.http.get<ReglePaiement>(`${this.apiUrl}/${id}`);
  }

  create(regle: ReglePaiement): Observable<ReglePaiement> {
    return this.http.post<ReglePaiement>(this.apiUrl, regle);
  }

  update(id: number, regle: ReglePaiement): Observable<ReglePaiement> {
    return this.http.put<ReglePaiement>(`${this.apiUrl}/${id}`, regle);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
