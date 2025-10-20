import { TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthShellComponent } from './auth-shell.component';
import { AuthService } from '../../core/auth.service';
import { of, throwError } from 'rxjs';

class AuthServiceStub {
  login() {
    return of({ token: 'test', userId: 1, email: 'test@example.com', fullName: 'Test User' });
  }
  register() {
    return throwError(() => new Error('not implemented'));
  }
}

describe('AuthShellComponent', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [AuthShellComponent],
      providers: [{ provide: AuthService, useClass: AuthServiceStub }]
    }).compileComponents();
  });

  it('should mark login form invalid when empty', () => {
    const fixture = TestBed.createComponent(AuthShellComponent);
    const component = fixture.componentInstance;
    expect(component.loginForm.valid).toBe(false);
    component.loginForm.setValue({ email: 'invalid', password: '' });
    expect(component.loginForm.valid).toBe(false);
  });
});
