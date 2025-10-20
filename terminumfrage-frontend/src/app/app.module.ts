import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { AppRoutes } from './app.routes';
import { AuthShellComponent } from './features/auth/auth-shell.component';
import { PollListComponent } from './features/polls/poll-list.component';
import { PollEditorComponent } from './features/polls/poll-editor.component';
import { PollDetailComponent } from './features/polls/poll-detail.component';
import { InvitationListComponent } from './features/invitations/invitation-list.component';
import { AuthGuard } from './core/auth.guard';
import { AuthInterceptor } from './core/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    AuthShellComponent,
    PollListComponent,
    PollEditorComponent,
    PollDetailComponent,
    InvitationListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(AppRoutes),
    BrowserAnimationsModule
  ],
  providers: [
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
