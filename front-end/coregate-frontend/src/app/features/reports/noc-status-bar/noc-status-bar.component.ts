import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { interval, Subscription } from 'rxjs';
import { AlertService } from '@/core/services/alert.service';

interface NocStatus {
mode: string;
tps: number;
sla: number;
updatedAt: string;
}import { Component, OnInit, OnDestroy } from '@angular/core';
import { Client, Message, Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

constructor(private http: HttpClient, private alert: AlertService) {}



interface NocStatus {
mode: string;
tps: number;
sla: number;
updatedAt: string;
}

@Component({
selector: 'app-noc-status-bar',
templateUrl: './noc-status-bar.component.html',
styleUrls: ['./noc-status-bar.component.scss']
})
export class NocStatusBarComponent implements OnInit, OnDestroy {
status: NocStatus = { mode: 'Conectando...', tps: 0, sla: 0, updatedAt: '' };
private client!: Client;

ngOnInit() {
    this.connectWebSocket();
  }

  ngOnDestroy() {
    if (this.client && this.client.connected) this.client.deactivate();
  }

  connectWebSocket() {
    this.client = Stomp.over(() => new SockJS('/ws-coregate'));
    this.client.reconnectDelay = 5000;
    this.client.onConnect = () => {
      this.client.subscribe('/topic/noc-status', (msg: Message) => {
        this.status = JSON.parse(msg.body);
      });
    };
    this.client.activate();
  }

alertLevel: string = '';

private evaluateAlerts(): void {
  const sla = this.status.sla || 100;
  if (sla < 97) this.alertLevel = 'critical';
  else if (sla < 99) this.alertLevel = 'warning';
  else this.alertLevel = '';

  // dispara sons conforme antes ...
}


  getModeColor(): string {
    switch (this.status.mode) {
      case 'Gateway': return '#2ea043';
      case 'Stand-In Automático': return '#f0b429';
      case 'Stand-In Manual': return '#f85149';
      default: return '#8b949e';
    }
  }
fetchStatus(): void {
  this.http.get<NocStatus>('/api/v1/metrics/noc-status').subscribe({
    next: (data) => {
      this.status = data;
      this.evaluateAlerts();
    },
    error: () => (this.status.mode = 'Offline')
  });
}

private evaluateAlerts(): void {
  const sla = this.status.sla || 100;
  if (sla < 97) this.alert.trigger('critical', `SLA crítico: ${sla.toFixed(2)}%`);
  else if (sla < 99) this.alert.trigger('warning', `SLA degradado: ${sla.toFixed(2)}%`);

  if (this.status.mode.startsWith('Stand-In')) {
    this.alert.trigger('critical', `Modo ${this.status.mode} ativo`);
  }

  if (this.status.mode === 'Gateway' && sla >= 99)
    this.alert.trigger('info', 'Sistema recuperado — Gateway normal');
}
}


@Component({
selector: 'app-noc-status-bar',
templateUrl: './noc-status-bar.component.html',
styleUrls: ['./noc-status-bar.component.scss']
})
export class NocStatusBarComponent implements OnInit, OnDestroy {
status: NocStatus = {
mode: 'Desconhecido',
tps: 0,
sla: 0,
updatedAt: new Date().toISOString()
  };

  private subscription!: Subscription;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // Atualiza a cada 5 segundos
    this.fetchStatus();
    this.subscription = interval(5000).subscribe(() => this.fetchStatus());
  }

  ngOnDestroy(): void {
    if (this.subscription) this.subscription.unsubscribe();
  }

  fetchStatus(): void {
    this.http.get<NocStatus>('/api/v1/metrics/noc-status').subscribe({
      next: (data) => (this.status = data),
      error: () => (this.status.mode = 'Offline')
    });
  }

  getModeColor(): string {
    switch (this.status.mode) {
      case 'Gateway': return '#2ea043';
      case 'Stand-In Automático': return '#f0b429';
      case 'Stand-In Manual': return '#f85149';
      default: return '#8b949e';
    }
  }
}

@keyframes blink-warning {
  0%, 100% { background-color: #f0b42933; }
  50% { background-color: #f0b42966; }
}

@keyframes blink-critical {
  0%, 100% { background-color: #f8514933; }
  50% { background-color: #f8514966; }
}

.noc-status-bar.warning {
  animation: blink-warning 1s infinite;
}
.noc-status-bar.critical {
  animation: blink-critical 0.8s infinite;
}
