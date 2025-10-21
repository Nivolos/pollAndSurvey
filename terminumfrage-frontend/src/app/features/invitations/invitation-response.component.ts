import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Observable, switchMap, tap } from 'rxjs';
import { PollDetail, PollsApiService } from '../polls/polls-api.service';

interface ResponseFormValue {
  slotId: number;
  value: 'YES' | 'NO' | 'MAYBE';
}

type ResponseGroup = FormGroup<{
  slotId: FormControl<number>;
  value: FormControl<ResponseFormValue['value']>;
}>;

type ResponseForm = FormGroup<{
  responses: FormArray<ResponseGroup>;
}>;

@Component({
  selector: 'app-invitation-response',
  templateUrl: './invitation-response.component.html',
  styleUrls: ['./invitation-response.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InvitationResponseComponent implements OnInit {
  poll$!: Observable<PollDetail>;
  readonly form: ResponseForm = this.fb.nonNullable.group({
    responses: this.fb.array<ResponseGroup>([])
  });

  isSubmitting = false;
  success = false;
  error: string | null = null;

  constructor(private readonly route: ActivatedRoute, private readonly pollsApi: PollsApiService, private readonly fb: FormBuilder) {}

  ngOnInit(): void {
    this.poll$ = this.route.paramMap.pipe(
      switchMap(params => this.pollsApi.getPoll(Number(params.get('id')))),
      tap(poll => this.initializeResponses(poll))
    );
  }

  get responses(): FormArray<ResponseGroup> {
    return this.form.controls.responses;
  }

  choose(index: number, value: ResponseFormValue['value']): void {
    const control = this.responses.at(index);
    if (!control) {
      return;
    }
    control.patchValue({ value });
  }

  selectedValue(index: number): ResponseFormValue['value'] | null {
    const control = this.responses.at(index);
    if (!control) {
      return null;
    }
    const value = control.value as ResponseFormValue | null;
    return value?.value ?? null;
  }

  submit(poll: PollDetail): void {
    if (this.form.invalid) {
      return;
    }
    this.isSubmitting = true;
    this.success = false;
    this.error = null;
    const payload = this.responses.controls
      .map(control => control.getRawValue())
      .filter((response): response is ResponseFormValue => typeof response.slotId === 'number' && !!response.value)
      .map(response => ({
        slotId: response.slotId,
        value: response.value
      }));
    if (payload.length === 0) {
      this.isSubmitting = false;
      this.error = 'Es sind keine gültigen Terminslots vorhanden.';
      return;
    }
    this.pollsApi.submitResponses(poll.id, payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.success = true;
      },
      error: err => {
        this.isSubmitting = false;
        this.error = err.error?.error ?? 'Antworten konnten nicht gespeichert werden.';
      }
    });
  }

  slotSummary(poll: PollDetail, slotId?: number) {
    if (!slotId) {
      return null;
    }
    return poll.slotSummaries?.[slotId] ?? null;
  }

  private initializeResponses(poll: PollDetail): void {
    const responseGroups = poll.timeSlots.map(slot => this.createResponseGroup(slot.id ?? 0));
    const responsesArray = this.fb.array<ResponseGroup>(responseGroups);
    this.form.setControl('responses', responsesArray);
    this.success = false;
    this.error = null;
    this.isSubmitting = false;
  }

  private createResponseGroup(slotId: number): ResponseGroup {
    return this.fb.nonNullable.group({
      slotId: [slotId],
      value: ['MAYBE' as ResponseFormValue['value']]
    });
  }
}
