import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  title = 'Terminumfrage';

  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  get isAuthenticated(): boolean {
    return !!this.authService.token;
  }

  get fullName(): string | null {
    return this.authService.fullName;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
