import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { ReportVerifierComponent } from './report-verifier/report-verifier.component';
import { SignatureVerifierComponent } from './signature-verifier/signature-verifier.component';
import { VerifierDashboardComponent } from './verifier-dashboard/verifier-dashboard.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
declarations: [
ReportVerifierComponent,
SignatureVerifierComponent,
VerifierDashboardComponent
],
imports: [
CommonModule,
FormsModule,
HttpClientModule,
MatTabsModule
],
exports: [VerifierDashboardComponent]
})
export class ReportsModule { }
