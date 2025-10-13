import { Routes } from '@angular/router';
import { Documentation } from './documentation/documentation';
import { Empty } from './empty/empty';
import { ClientComponent } from './company/client/crud/clients.component';
import { FoldersComponent } from './company/folders/crud/folders.component';
import { InvoicesComponent } from './company/invoice/crud/invoices.component';
import { ReglesPaiementsComponent } from './company/regles-paiement/crud/regles-paiements.component';
import { SuppliersComponent } from './company/supplier/crud/suppliers.component';

export default [
    { path: 'documentation', component: Documentation },
    { path: 'client', component: ClientComponent },
    { path: 'folders', component: FoldersComponent },
    { path: 'invoice', component: InvoicesComponent},
    { path: 'regles-paiement', component: ReglesPaiementsComponent},
    { path: 'supplier', component: SuppliersComponent }
] as Routes;
