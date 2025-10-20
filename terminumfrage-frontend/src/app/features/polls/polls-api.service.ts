import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PollSummary {
  id: number;
  title: string;
  status: 'DRAFT' | 'OPEN' | 'FINALIZED';
  createdAt: string;
}

export interface TimeSlotDto {
  id?: number;
  start: string;
  end: string;
}

export interface InvitationDto {
  id?: number;
  email: string;
  state?: string;
}

export interface PollDetail {
  id: number;
  title: string;
  description?: string;
  status: PollSummary['status'];
  createdAt: string;
  timeSlots: TimeSlotDto[];
  invitations: InvitationDto[];
  slotSummaries: Record<number, { yesCount: number; noCount: number; maybeCount: number }>;
}

@Injectable({ providedIn: 'root' })
export class PollsApiService {
  private readonly baseUrl = '/api/v1';

  constructor(private readonly http: HttpClient) {}

  listPolls(): Observable<PollSummary[]> {
    return this.http.get<PollSummary[]>(`${this.baseUrl}/polls`);
  }

  listInvitations(): Observable<PollSummary[]> {
    return this.http.get<PollSummary[]>(`${this.baseUrl}/invitations`);
  }

  createPoll(payload: { title: string; description?: string; timeSlots: TimeSlotDto[]; invitations: InvitationDto[] }): Observable<PollDetail> {
    return this.http.post<PollDetail>(`${this.baseUrl}/polls`, payload);
  }

  getPoll(id: number): Observable<PollDetail> {
    return this.http.get<PollDetail>(`${this.baseUrl}/polls/${id}`);
  }

  openPoll(id: number): Observable<PollDetail> {
    return this.http.post<PollDetail>(`${this.baseUrl}/polls/${id}/open`, {});
  }

  finalizePoll(id: number, slotId: number): Observable<PollDetail> {
    return this.http.post<PollDetail>(`${this.baseUrl}/polls/${id}/finalize`, { slotId });
  }

  submitResponses(pollId: number, responses: { slotId: number; value: 'YES' | 'NO' | 'MAYBE' }[]): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/polls/${pollId}/responses`, responses);
  }
}
