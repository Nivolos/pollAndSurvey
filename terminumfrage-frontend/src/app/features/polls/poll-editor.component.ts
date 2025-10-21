import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
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
export class PollEditorComponent {
  error: string | null = null;

  readonly form = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    description: [''],
    timeSlots: this.fb.array<FormGroup>([
      this.createSlot()
    ]),
    invitations: this.fb.array<FormGroup>([
      this.createInvitation()
    ])
  });

  constructor(private readonly fb: FormBuilder, private readonly pollsApi: PollsApiService, private readonly router: Router) {}

  get timeSlots(): FormArray<FormGroup> {
    return this.form.controls.timeSlots as FormArray<FormGroup>;
  }

  get invitations(): FormArray<FormGroup> {
    return this.form.controls.invitations as FormArray<FormGroup>;
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
      invitations: payload.invitations.map(invitation => ({
        email: invitation.email
      }))
    };
    this.pollsApi.createPoll(body).subscribe({
      next: poll => this.router.navigate(['/polls', poll.id]),
      error: err => (this.error = err.error?.error ?? 'Speichern fehlgeschlagen')
    });
  }

  private createSlot() {
    const start = new Date();
    const end = new Date(start.getTime() + 60 * 60 * 1000);
    return this.fb.nonNullable.group({
      start: [toDateTimeLocal(start), Validators.required],
      end: [toDateTimeLocal(end), Validators.required]
    });
  }

  private createInvitation() {
    return this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }
}
