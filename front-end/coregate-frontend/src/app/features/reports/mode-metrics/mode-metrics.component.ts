import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
selector: 'app-mode-metrics',
templateUrl: './mode-metrics.component.html',
styleUrls: ['./mode-metrics.component.scss']
})
export class ModeMetricsComponent implements OnInit {
data: any = {};

constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.http.get('/api/v1/metrics/mode-metrics').subscribe({
      next: (resp) => (this.data = resp),
      error: (err) => console.error('Erro ao carregar métricas', err)
    });
  }
}
