// src/app/core/services/invoice.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice } from '../models/invoice';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private apiUrl = 'http://localhost:8080/api/invoices';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(this.apiUrl);
  }

  getById(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.apiUrl}/${id}`);
  }

  create(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(this.apiUrl, invoice);
  }

  update(id: number, invoice: Invoice): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.apiUrl}/${id}`, invoice);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
