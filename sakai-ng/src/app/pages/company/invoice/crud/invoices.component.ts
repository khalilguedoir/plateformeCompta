import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Invoice } from '../../../models/invoice';
import { InvoiceService } from '../../../service/invoice.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { InputNumberModule } from 'primeng/inputnumber';

@Component({
  selector: 'app-invoices',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    ToolbarModule,
    ConfirmDialogModule,
    InputTextModule,
    InputNumberModule,
    ToastModule,
  ],
  templateUrl: './invoices.component.html',
  providers: [ConfirmationService, MessageService],
})
export class InvoicesComponent implements OnInit {
  invoices: Invoice[] = [];
  selectedInvoices: Invoice[] = [];
  invoiceDialog = false;
  invoice: Invoice = {} as Invoice;
  submitted = false;
  loading = false;

  constructor(
    private invoiceService: InvoiceService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  /** Charger toutes les factures */
  loadInvoices(): void {
    this.loading = true;
    this.invoiceService.getAll().subscribe({
      next: (data) => {
        this.invoices = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur chargement factures:', err);
        this.loading = false;
      },
    });
  }

  /** Ouvrir le popup de création */
  openNew(): void {
    this.invoice = {} as Invoice;
    this.submitted = false;
    this.invoiceDialog = true;
  }

  /** Fermer le popup */
  hideDialog(): void {
    this.invoiceDialog = false;
    this.submitted = false;
  }

  /** Créer ou modifier une facture */
  saveInvoice(): void {
    this.submitted = true;

    if (!this.invoice.number || !this.invoice.date || this.invoice.amount == null) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Champs requis',
        detail: 'Veuillez remplir tous les champs obligatoires.',
      });
      return;
    }

    if (this.invoice.id) {
      // 🔄 Mise à jour
      this.invoiceService.update(this.invoice.id, this.invoice).subscribe(() => {
        this.loadInvoices();
        this.messageService.add({
          severity: 'success',
          summary: 'Mise à jour',
          detail: 'Facture mise à jour avec succès',
        });
      });
    } else {
      // ➕ Création
      this.invoiceService.create(this.invoice).subscribe(() => {
        this.loadInvoices();
        this.messageService.add({
          severity: 'success',
          summary: 'Créée',
          detail: 'Facture ajoutée avec succès',
        });
      });
    }

    this.invoiceDialog = false;
  }

  /** Éditer une facture existante */
  editInvoice(invoice: Invoice): void {
    this.invoice = { ...invoice };
    this.invoiceDialog = true;
  }

  /** Supprimer une seule facture */
  deleteInvoice(invoice: Invoice): void {
    this.confirmationService.confirm({
      message: `Voulez-vous vraiment supprimer la facture "${invoice.number}" ?`,
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (invoice.id)
          this.invoiceService.delete(invoice.id).subscribe(() => {
            this.loadInvoices();
            this.messageService.add({
              severity: 'success',
              summary: 'Supprimée',
              detail: 'Facture supprimée avec succès',
            });
          });
      },
    });
  }

  /** Supprimer plusieurs factures */
  deleteSelectedInvoices(): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer les factures sélectionnées ?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const ids = this.selectedInvoices.map((i) => i.id);
        ids.forEach((id) => {
          if (id) this.invoiceService.delete(id).subscribe(() => this.loadInvoices());
        });
        this.selectedInvoices = [];
        this.messageService.add({
          severity: 'success',
          summary: 'Supprimées',
          detail: 'Factures sélectionnées supprimées',
        });
      },
    });
  }

  /** Export CSV */
  exportCSV(): void {
    const csv =
      'Numéro,Date,Montant,TVA,Type\n' +
      this.invoices
        .map(
          (i) =>
            `${i.number},${i.date},${i.amount},${i.tva},${i.type}`
        )
        .join('\n');

    const blob = new Blob([csv], { type: 'text/csv' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'invoices.csv';
    link.click();
  }

  /** Filtre global de la table */
  onGlobalFilter(table: any, event: Event): void {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }
}
