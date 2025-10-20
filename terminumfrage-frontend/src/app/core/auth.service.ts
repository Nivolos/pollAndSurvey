import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { SessionStorageService } from './session-storage.service';

export interface AuthResponse {
  token: string;
  userId: number;
  email: string;
  fullName: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = '/api/v1/auth';

  constructor(private readonly http: HttpClient, private readonly storage: SessionStorageService) {}

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { email, password }).pipe(tap(res => this.persist(res)));
  }

  register(fullName: string, email: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/register`, { fullName, email, password })
      .pipe(tap(res => this.persist(res)));
  }

  logout(): void {
    this.storage.clear();
  }

  get token(): string | null {
    return this.storage.get('token');
  }

  private persist(response: AuthResponse): void {
    this.storage.set('token', response.token);
    this.storage.set('fullName', response.fullName);
    this.storage.set('email', response.email);
  }
}
