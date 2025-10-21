import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PollsApiService, PollSummary } from '../polls/polls-api.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-invitation-list',
  templateUrl: './invitation-list.component.html',
  styleUrls: ['./invitation-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InvitationListComponent implements OnInit {
  invitations$!: Observable<PollSummary[]>;

  constructor(private readonly pollsApi: PollsApiService) {}

  ngOnInit(): void {
    this.invitations$ = this.pollsApi.listInvitations();
  }
}
