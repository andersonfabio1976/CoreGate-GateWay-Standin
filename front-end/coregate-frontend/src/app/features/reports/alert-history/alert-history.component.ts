import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Alert {
level: string;
message: string;
source: string;
timestamp: string;
}

@Component({
selector: 'app-alert-history',
templateUrl: './alert-history.component.html',
styleUrls: ['./alert-history.component.scss']
})
export class AlertHistoryComponent implements OnInit {
alerts: Alert[] = [];

constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.http.get<Alert[]>('/api/v1/metrics/alerts-history?limit=30').subscribe({
      next: (res) => (this.alerts = res),
      error: (err) => console.error('Erro ao carregar alertas', err)
    });
  }
}
