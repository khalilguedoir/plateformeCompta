import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';
import { AuthService } from '../../pages/service/auth.service';
import { CompanyService } from '@/pages/service/company.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, AppMenuitem, RouterModule],
  template: `
    <ul class="layout-menu">
      <ng-container *ngFor="let item of model; let i = index">
        <li app-menuitem *ngIf="!item.separator" [item]="item" [index]="i" [root]="true"></li>
        <li *ngIf="item.separator" class="menu-separator"></li>
      </ng-container>
    </ul>
  `
})
export class AppMenu {
  model: MenuItem[] = [];

  constructor(private authService: AuthService, private companyService: CompanyService) {}

  ngOnInit() {
    const role = this.authService.getRole();

    if (role === 'COMPANY') {
      this.model = [
        {
          label: 'Home',
          items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/pages/company/client'] }]
        },
        {
          label: 'Gestion',
          icon: 'pi pi-fw pi-briefcase',
          items: [
            { label: 'Clients', icon: 'pi pi-fw pi-users', routerLink: ['/pages/company/client'] },
            { label: 'Folders', icon: 'pi pi-fw pi-folder', routerLink: ['/pages/company/folders'] },
            { label: 'Invoice', icon: 'pi pi-fw pi-file-check', routerLink: ['/pages/company/invoice'] },
            { label: 'Règles Paiements', icon: 'pi pi-fw pi-wallet', routerLink: ['/pages/company/regles-paiement'] },
            { label: 'Fournisseurs', icon: 'pi pi-fw pi-shopping-cart', routerLink: ['/pages/company/supplier'] }
          ]
        }
      ];
    } else if (role === 'ACCOUNTANT') {
      this.model = [
        {
          label: 'Home',
          items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-chart-bar', routerLink: ['/pages/accountant/dashboard'] }]
        },
        {
          label: 'Sociétés',
          icon: 'pi pi-fw pi-building',
          items: []
        },
        {
          label: 'Notifications',
          icon: 'pi pi-fw pi-bell',
          routerLink: ['/pages/accountant/notifications']
        }
      ];
      this.loadCompaniesForAccountant();
    }
  }

  loadCompaniesForAccountant() {
    this.companyService.getCompaniesForAccountant().subscribe({
      next: (companies) => {
        const companiesMenu = companies.map(c => ({
          label: c.name,
          icon: 'pi pi-fw pi-briefcase',
          routerLink: ['/pages/accountant/company', c.id]
        }));
        const index = this.model.findIndex(m => m.label === 'Sociétés');
        if (index !== -1) this.model[index].items = companiesMenu;
      },
      error: (err) => console.error('Erreur chargement sociétés:', err)
    });
  }
}
