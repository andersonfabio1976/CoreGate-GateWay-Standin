exportResult(data: ReportVerifyResponse) {
  const exportData = {
    ...data,
    exportedAt: new Date().toISOString()
  };

  this.http.post<{ signature: string }>('/api/v1/reports/sign', exportData).subscribe({
    next: (resp) => {
      const signedData = {
        ...exportData,
        signature: resp.signature,
        algorithm: 'RSA-SHA256',
        issuer: 'CoreGate Secure Authority'
      };
      const blob = new Blob([JSON.stringify(signedData, null, 2)], { type: 'application/json' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `${data.filename.replace('.pdf', '')}_verify_signed.json`;
      link.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err) => alert('Erro ao gerar assinatura: ' + err.message)
  });
}
