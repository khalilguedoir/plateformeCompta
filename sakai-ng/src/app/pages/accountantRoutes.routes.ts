import { Supplier } from './company/supplier/crud/supplier';
import { Routes } from '@angular/router';
import { Dashboard } from '../pages/accountant/dashboard/dashboard';
import { Notifications } from '../pages/accountant/notifications/notifications';
import { CompanyDashboardComponent } from '../pages/accountant/company-dashboard/CompanyDashboardComponent';
import { Dossiers } from '../pages/accountant/dossiers/dossiers';
import { Invoices } from '../pages/accountant/invoices/invoices';
import { Payments } from '../pages/accountant/payments/payments';
import { Suppliers } from '../pages/accountant/suppliers/suppliers';

export default [
  {
    path: 'pages/accountant/dashboard',
    component: Dashboard
  },
  {
    path: 'pages/accountant/notifications',
    component: Notifications
  },
  {
    path: 'pages/accountant/company/:id',
    component: CompanyDashboardComponent,
    children: [
      { path: 'dossiers', component: Dossiers },
      { path: 'invoices', component: Invoices },
      { path: 'payments', component: Payments },
      { path: 'suppliers', component: Supplier },
      { path: '', redirectTo: 'dossiers', pathMatch: 'full' }
    ]
  }
] as Routes;