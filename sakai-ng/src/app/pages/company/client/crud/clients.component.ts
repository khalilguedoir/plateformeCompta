import { Component, OnInit, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputMaskModule } from 'primeng/inputmask';
import { ConfirmationService, MessageService } from 'primeng/api';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { ClientService } from '../../../service/client.service';
import { Client } from '../../../models/client';

interface Column {
  field: string;
  header: string;
}

@Component({
  selector: 'app-client',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    RippleModule,
    ToastModule,
    ToolbarModule,
    InputTextModule,
    DialogModule,
    ConfirmDialogModule,
    InputMaskModule,
    InputIconModule,
    IconFieldModule

  ],
  templateUrl: './client.component.html',
  providers: [MessageService, ClientService, ConfirmationService]
})
export class ClientComponent implements OnInit {
  clients = signal<Client[]>([]);
  client: Client = { 
    name: '', 
    email: '', 
    phonenumber: '', 
  company: { name: '', databaseName: '', matriculeFiscale: '', address: '' }, // obligatoire
    adresse: { street: '', city: '', country: '' } 
  };
  selectedClients: Client[] = [];
  clientDialog: boolean = false;
  submitted: boolean = false;
  cols!: Column[];

  @ViewChild('dt') dt!: Table;

  constructor(
    private clientService: ClientService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.loadClients();
    this.cols = [
      { field: 'name', header: 'Name' },
      { field: 'email', header: 'Email' },
      { field: 'phonenumber', header: 'Phone' },
      { field: 'company', header: 'Company' },
      { field: 'adresse.street', header: 'Street' },
      { field: 'adresse.city', header: 'City' },
      { field: 'adresse.country', header: 'Country' }
    ];
  }

  loadClients() {
    this.clientService.getAll().subscribe({
      next: (data) => this.clients.set(data),
      error: () =>
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Cannot load clients' })
    });
  }

  openNew() {
    this.client = { 
      name: '', 
      email: '', 
      phonenumber: '', 
  company: { name: '', databaseName: '', matriculeFiscale: '', address: '' },
      adresse: { street: '', city: '', country: '' } 
    };
    this.submitted = false;
    this.clientDialog = true;
  }

  editClient(client: Client) {
    this.client = { 
      ...client, 
      company: client.company || '', 
      adresse: client.adresse ? { ...client.adresse } : { street: '', city: '', country: '' } 
    };
    this.clientDialog = true;
  }

  deleteClient(client: Client) {
    this.confirmationService.confirm({
      message: `Are you sure you want to delete ${client.name}?`,
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.clientService.delete(client.id!).subscribe(() => {
          this.clients.set(this.clients().filter(c => c.id !== client.id));
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Client deleted' });
        });
      }
    });
  }
 exportCSV() {
        this.dt.exportCSV();
    }


  deleteSelectedClients() {
    if (!this.selectedClients || this.selectedClients.length === 0) return;
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete selected clients?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.selectedClients.forEach(client => this.clientService.delete(client.id!).subscribe());
        this.clients.set(this.clients().filter(c => !this.selectedClients.includes(c)));
        this.selectedClients = [];
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Clients deleted' });
      }
    });
  }

  saveClient() {
    this.submitted = true;
    if (!this.client.name?.trim()) return;

    if (this.client.id) {
      this.clientService.update(this.client.id, this.client).subscribe({
        next: (data) => {
          const index = this.clients().findIndex(c => c.id === data.id);
          const updated = [...this.clients()];
          updated[index] = data;
          this.clients.set(updated);
          this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Client updated' });
          this.clientDialog = false;
        }
      });
    } else {
      this.clientService.create(this.client).subscribe({
        next: (data) => {
          this.clients.set([...this.clients(), data]);
          this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Client created' });
          this.clientDialog = false;
        }
      });
    }
  }

  hideDialog() {
    this.clientDialog = false;
    this.submitted = false;
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }
}
