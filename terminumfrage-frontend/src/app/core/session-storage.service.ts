import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SessionStorageService {
  private readonly prefix = 'terminumfrage.';

  set(key: string, value: string): void {
    sessionStorage.setItem(this.prefix + key, value);
  }

  get(key: string): string | null {
    return sessionStorage.getItem(this.prefix + key);
  }

  clear(): void {
    Object.keys(sessionStorage)
      .filter(key => key.startsWith(this.prefix))
      .forEach(key => sessionStorage.removeItem(key));
  }
}
