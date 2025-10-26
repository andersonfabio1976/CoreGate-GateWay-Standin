import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
selector: 'app-mode-sla-chart',
templateUrl: './mode-sla-chart.component.html',
styleUrls: ['./mode-sla-chart.component.scss']
})
export class ModeSlaChartComponent implements OnInit {
data: any[] = [];

constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any[]>('/api/v1/metrics/sla-timeline?hours=24').subscribe({
      next: (res) => this.data = res,
      error: (err) => console.error('Erro ao carregar timeline SLA', err)
    });
  }
}
