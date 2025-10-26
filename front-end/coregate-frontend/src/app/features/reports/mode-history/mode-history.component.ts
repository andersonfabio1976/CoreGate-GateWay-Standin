import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface ModeHistory {
mode: string;
origin: string;
reason: string;
timestamp: string;
}

@Component({
selector: 'app-mode-history',
templateUrl: './mode-history.component.html',
styleUrls: ['./mode-history.component.scss']
})
export class ModeHistoryComponent implements OnInit {
events: ModeHistory[] = [];

constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadHistory();
  }

  loadHistory(): void {
    this.http.get<ModeHistory[]>('/api/v1/metrics/mode-history?limit=20').subscribe({
      next: (data) => this.events = data,
      error: (err) => console.error('Erro ao carregar histórico', err)
    });
  }
}
