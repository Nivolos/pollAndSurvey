import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PollDetail, PollsApiService } from './polls-api.service';
import { Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-poll-detail',
  templateUrl: './poll-detail.component.html',
  styleUrls: ['./poll-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PollDetailComponent implements OnInit {
  poll$!: Observable<PollDetail>;

  constructor(private readonly route: ActivatedRoute, private readonly pollsApi: PollsApiService) {}

  ngOnInit(): void {
    this.poll$ = this.route.paramMap.pipe(switchMap(params => this.pollsApi.getPoll(Number(params.get('id')))));
  }

  open(id: number): void {
    this.poll$ = this.pollsApi.openPoll(id);
  }

  finalize(id: number, slotId: number): void {
    this.poll$ = this.pollsApi.finalizePoll(id, slotId);
  }

  bestSlot(slotId: number, poll: PollDetail): boolean {
    if (!poll.slotSummaries || Object.keys(poll.slotSummaries).length === 0) {
      return false;
    }
    const summary = poll.slotSummaries[slotId];
    if (!summary) {
      return false;
    }
    const scores = Object.values(poll.slotSummaries).map(value => value.yesCount * 2 + value.maybeCount);
    const max = Math.max(...scores);
    return summary.yesCount * 2 + summary.maybeCount === max;
  }

  slotSummary(poll: PollDetail, slotId?: number) {
    if (!slotId) {
      return null;
    }
    return poll.slotSummaries?.[slotId] ?? null;
  }
}
