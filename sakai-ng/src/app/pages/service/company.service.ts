// services/company.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Company } from '../models/company';
import { Observable } from 'rxjs';
import { Timezone } from '../models/Timezone';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private apiUrl = 'http://localhost:8080/api/companies'; // adapter ton backend
  private timezoneUrl = 'http://localhost:8080/api/timezones';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Company[]> {
    return this.http.get<Company[]>(this.apiUrl);
  }
  getCompaniesForAccountant(): Observable<Company[]> {
    return this.http.get<Company[]>(`${this.apiUrl}/accountant`);
  }
  getById(id: number): Observable<Company> {
    return this.http.get<Company>(`${this.apiUrl}/${id}`);
  }

  create(company: Company): Observable<Company> {
    return this.http.post<Company>(this.apiUrl, company);
  }

  update(id: number, company: Company): Observable<Company> {
    return this.http.put<Company>(`${this.apiUrl}/${id}`, company);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTimezones(): Observable<Timezone[]> {
    return this.http.get<Timezone[]>(this.timezoneUrl);
  }
}
