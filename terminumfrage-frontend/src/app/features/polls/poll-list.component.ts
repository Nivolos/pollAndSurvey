import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PollsApiService, PollSummary } from './polls-api.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-poll-list',
  templateUrl: './poll-list.component.html',
  styleUrls: ['./poll-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PollListComponent implements OnInit {
  polls$!: Observable<PollSummary[]>;

  constructor(private readonly pollsApi: PollsApiService) {}

  ngOnInit(): void {
    this.polls$ = this.pollsApi.listPolls();
  }
}
