import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Supplier } from '../../../models/supplier';
import { SupplierService } from '../../../service/supplier.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToolbarModule } from 'primeng/toolbar';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    ToolbarModule,
    DialogModule,
    InputTextModule,
    ToastModule,
    ConfirmDialogModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './suppliers.component.html'
})
export class SuppliersComponent implements OnInit {
  suppliers: Supplier[] = [];
  selectedSuppliers: Supplier[] = [];
  supplierDialog = false;
  supplier: Supplier = {} as Supplier;
  submitted = false;
  loading = false;

  constructor(
    private supplierService: SupplierService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.loading = true;
    this.supplierService.getAll().subscribe({
      next: data => {
        this.suppliers = data.map(s => ({
          ...s,
          adresse: s.adresse || { street: '', city: '', country: '' }
        }));
        this.loading = false;
      },
      error: err => {
        console.error('Erreur chargement fournisseurs:', err);
        this.loading = false;
      }
    });
  }

  onGlobalFilter(table: any, event: any) {
    table.filterGlobal(event.target.value, 'contains');
  }

  openNew(): void {
    this.supplier = { adresse: { street: '', city: '', country: '' } } as Supplier;
    this.submitted = false;
    this.supplierDialog = true;
  }

  hideDialog(): void {
    this.supplierDialog = false;
    this.submitted = false;
  }

  saveSupplier(): void {
    this.submitted = true;
    if (!this.supplier.name || !this.supplier.email) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Champs requis',
        detail: 'Veuillez remplir le nom et l’email.'
      });
      return;
    }

    if (!this.supplier.adresse) {
      this.supplier.adresse = { street: '', city: '', country: '' };
    }

    if (this.supplier.id) {
      this.supplierService.update(this.supplier.id, this.supplier).subscribe(() => {
        this.loadSuppliers();
        this.messageService.add({
          severity: 'success',
          summary: 'Mise à jour',
          detail: 'Fournisseur mis à jour'
        });
      });
    } else {
      this.supplierService.create(this.supplier).subscribe(() => {
        this.loadSuppliers();
        this.messageService.add({
          severity: 'success',
          summary: 'Créé',
          detail: 'Fournisseur ajouté'
        });
      });
    }

    this.supplierDialog = false;
  }

  editSupplier(s: Supplier): void {
    this.supplier = { ...s, adresse: s.adresse || { street: '', city: '', country: '' } };
    this.supplierDialog = true;
  }

  deleteSupplier(s: Supplier): void {
    this.confirmationService.confirm({
      message: `Voulez-vous vraiment supprimer "${s.name}" ?`,
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (s.id) {
          this.supplierService.delete(s.id).subscribe(() => {
            this.loadSuppliers();
            this.messageService.add({
              severity: 'success',
              summary: 'Supprimé',
              detail: 'Fournisseur supprimé'
            });
          });
        }
      }
    });
  }
exportCSV(): void {
  const data = this.suppliers.map(s => ({
    Name: s.name,
    Email: s.email,
    Phone: s.phonenumber,
    City: s.adresse?.city,
    Country: s.adresse?.country
  }));

  const csvContent = [
    Object.keys(data[0]).join(','),
    ...data.map(d => Object.values(d).join(','))
  ].join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', 'suppliers.csv');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

  deleteSelectedSuppliers(): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer les fournisseurs sélectionnés ?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const ids = this.selectedSuppliers.map(s => s.id);
        ids.forEach(id => {
          if (id) this.supplierService.delete(id).subscribe(() => this.loadSuppliers());
        });
        this.selectedSuppliers = [];
        this.messageService.add({
          severity: 'success',
          summary: 'Supprimés',
          detail: 'Fournisseurs supprimés'
        });
      }
    });
  }
}
