import { Routes } from '@angular/router';
import { AuthShellComponent } from './features/auth/auth-shell.component';
import { PollListComponent } from './features/polls/poll-list.component';
import { PollDetailComponent } from './features/polls/poll-detail.component';
import { PollEditorComponent } from './features/polls/poll-editor.component';
import { InvitationListComponent } from './features/invitations/invitation-list.component';
import { InvitationResponseComponent } from './features/invitations/invitation-response.component';
import { AuthGuard } from './core/auth.guard';

export const AppRoutes: Routes = [
  { path: 'login', component: AuthShellComponent },
  {
    path: '',
    canActivateChild: [AuthGuard],
    children: [
      { path: 'polls', component: PollListComponent },
      { path: 'polls/new', component: PollEditorComponent },
      { path: 'polls/:id', component: PollDetailComponent },
      { path: 'invitations', component: InvitationListComponent },
      { path: 'invitations/:id', component: InvitationResponseComponent },
      { path: '', pathMatch: 'full', redirectTo: 'polls' }
    ]
  },
  { path: '**', redirectTo: '' }
];
