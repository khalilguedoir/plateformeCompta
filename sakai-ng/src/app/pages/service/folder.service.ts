// src/app/core/services/folder.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Folder } from '../models/folder';

@Injectable({
  providedIn: 'root'
})
export class FolderService {
  private apiUrl = 'http://localhost:8080/api/folders';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Folder[]> {
    return this.http.get<Folder[]>(this.apiUrl);
  }

  getById(id: number): Observable<Folder> {
    return this.http.get<Folder>(`${this.apiUrl}/${id}`);
  }

  create(folder: Folder): Observable<Folder> {
    return this.http.post<Folder>(this.apiUrl, folder);
  }

  update(id: number, folder: Folder): Observable<Folder> {
    return this.http.put<Folder>(`${this.apiUrl}/${id}`, folder);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
