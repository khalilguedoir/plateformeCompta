// company-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CompanyService } from '../../service/company.service';
import { Card } from "primeng/card";

@Component({
  selector: 'app-company-dashboard',
  template: `
    <div class="grid gap-4">
      <div class="col-12 md:col-3">
        <p-card header="Dossiers" (click)="navigateTo('dossiers')">Voir les dossiers</p-card>
      </div>
      <div class="col-12 md:col-3">
        <p-card header="Invoices" (click)="navigateTo('invoices')">Voir les factures</p-card>
      </div>
      <div class="col-12 md:col-3">
        <p-card header="Paiements" (click)="navigateTo('payments')">Voir les paiements</p-card>
      </div>
      <div class="col-12 md:col-3">
        <p-card header="Fournisseurs" (click)="navigateTo('suppliers')">Voir les fournisseurs</p-card>
      </div>
    </div>

    <router-outlet></router-outlet>
  `,
  imports: [Card, RouterModule]
})
export class CompanyDashboardComponent implements OnInit {
  companyId!: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.companyId = Number(this.route.snapshot.paramMap.get('id'));
  }

  navigateTo(path: string) {
    window.location.href = `/pages/accountant/company/${this.companyId}/${path}`;
  }
}
