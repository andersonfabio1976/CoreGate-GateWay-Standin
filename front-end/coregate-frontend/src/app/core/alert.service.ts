import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AlertService {
private audio = new Audio();

trigger(level: 'info' | 'warning' | 'critical', message: string) {
    console.log(`[ALERT] ${level.toUpperCase()}: ${message}`);

    switch (level) {
      case 'critical':
        this.play('/assets/sounds/alert-critical.mp3');
        break;
      case 'warning':
        this.play('/assets/sounds/alert-warning.mp3');
        break;
      default:
        this.play('/assets/sounds/alert-info.mp3');
    }

    // evento custom global (pode ser escutado por outros componentes)
    window.dispatchEvent(new CustomEvent('coregate-alert', {
      detail: { level, message }
    }));
  }

  private play(path: string) {
    this.audio.src = path;
    this.audio.load();
    this.audio.play().catch(() => console.warn('🔇 Som bloqueado pelo navegador'));
  }
}

#📁 crie /assets/sounds/alert-info.mp3, alert-warning.mp3, alert-critical.mp3 (curto < 3 s).
#Use tons curtos para não incomodar o NOC.
