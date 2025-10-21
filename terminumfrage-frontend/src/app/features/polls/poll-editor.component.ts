import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PollsApiService } from './polls-api.service';

function toDateTimeLocal(date: Date): string {
  const pad = (value: number) => value.toString().padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

@Component({
  selector: 'app-poll-editor',
  templateUrl: './poll-editor.component.html',
  styleUrls: ['./poll-editor.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
type SlotFormGroup = FormGroup<{
  start: FormControl<string>;
  end: FormControl<string>;
}>;

type InvitationFormGroup = FormGroup<{
  email: FormControl<string>;
}>;

export class PollEditorComponent {
  error: string | null = null;

  readonly form = this.fb.nonNullable.group({
    title: this.fb.nonNullable.control('', [Validators.required]),
    description: this.fb.nonNullable.control(''),
    timeSlots: this.fb.array<SlotFormGroup>([
      this.createSlot()
    ]),
    invitations: this.fb.array<InvitationFormGroup>([
      this.createInvitation()
    ])
  });

  constructor(private readonly fb: FormBuilder, private readonly pollsApi: PollsApiService, private readonly router: Router) {}

  get timeSlots(): FormArray<SlotFormGroup> {
    return this.form.controls.timeSlots;
  }

  get invitations(): FormArray<InvitationFormGroup> {
    return this.form.controls.invitations;
  }

  addSlot(): void {
    this.timeSlots.push(this.createSlot());
  }

  removeSlot(index: number): void {
    this.timeSlots.removeAt(index);
  }

  addInvitation(): void {
    this.invitations.push(this.createInvitation());
  }

  removeInvitation(index: number): void {
    this.invitations.removeAt(index);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.getRawValue();
    const body = {
      title: payload.title,
      description: payload.description,
      timeSlots: payload.timeSlots.map(slot => ({
        start: new Date(slot.start).toISOString(),
        end: new Date(slot.end).toISOString()
      })),
      invitations: payload.invitations
    };
    this.pollsApi.createPoll(body).subscribe({
      next: poll => this.router.navigate(['/polls', poll.id]),
      error: err => (this.error = err.error?.error ?? 'Speichern fehlgeschlagen')
    });
  }

  private createSlot(): SlotFormGroup {
    const start = new Date();
    const end = new Date(start.getTime() + 60 * 60 * 1000);
    return this.fb.nonNullable.group({
      start: this.fb.nonNullable.control(toDateTimeLocal(start), Validators.required),
      end: this.fb.nonNullable.control(toDateTimeLocal(end), Validators.required)
    });
  }

  private createInvitation(): InvitationFormGroup {
    return this.fb.nonNullable.group({
      email: this.fb.nonNullable.control('', [Validators.required, Validators.email])
    });
  }
}
