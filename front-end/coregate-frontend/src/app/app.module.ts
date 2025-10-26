import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReportVerifierComponent } from './features/reports/report-verifier/report-verifier.component';
import { SignatureVerifierComponent } from './features/reports/signature-verifier/signature-verifier.component';
import { VerifierDashboardComponent } from './features/reports/verifier-dashboard/verifier-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    ReportVerifierComponent,
    SignatureVerifierComponent,
    VerifierDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
