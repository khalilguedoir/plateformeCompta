import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { AuthGuard } from './app/pages/service/auth.guard';

export const appRoutes: Routes = [
  { path: '', redirectTo: '/auth/login', pathMatch: 'full' },
  {
    path: '',
    component: AppLayout,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: Dashboard }, // pour COMPANY
      { path: 'accountantRoutes', loadChildren: () => import('./app/pages/accountantRoutes.routes') } // pour ACCOUNTANT
    ]
  },
  { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
  { path: '**', redirectTo: '/auth/login' }
];
