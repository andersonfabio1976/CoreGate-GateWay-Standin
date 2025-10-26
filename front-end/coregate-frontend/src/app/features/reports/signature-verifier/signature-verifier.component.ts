import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface SignatureVerifyResponse {
valid: boolean;
issuer: string;
algorithm: string;
verifiedAt: string;
message: string;
}

@Component({
selector: 'app-signature-verifier',
templateUrl: './signature-verifier.component.html',
styleUrls: ['./signature-verifier.component.scss']
})
export class SignatureVerifierComponent {
fileName = '';
fileContent: string | null = null;
result: SignatureVerifyResponse | null = null;
loading = false;
error: string | null = null;

constructor(private http: HttpClient) {}

  onFileDropped(file: File) {
    this.readFile(file);
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) this.readFile(file);
  }

  private readFile(file: File) {
    this.loading = true;
    this.fileName = file.name;
    this.result = null;
    this.error = null;

    const reader = new FileReader();
    reader.onload = () => {
      try {
        this.fileContent = reader.result as string;
        const json = JSON.parse(this.fileContent);
        this.verifySignature(json);
      } catch (err) {
        this.error = 'Arquivo inválido ou corrompido.';
        this.loading = false;
      }
    };
    reader.readAsText(file);
  }

  private verifySignature(json: any) {
    this.http.post<SignatureVerifyResponse>('/api/v1/reports/verify-signature', json).subscribe({
      next: (resp) => {
        this.result = resp;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.error || 'Erro ao verificar assinatura.';
        this.loading = false;
      }
    });
  }

  reset() {
    this.fileName = '';
    this.fileContent = null;
    this.result = null;
    this.error = null;
  }
}
