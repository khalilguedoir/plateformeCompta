import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Folder } from '../../../models/folder';
import { FolderService } from '../../../service/folder.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-folders',
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
    ToastModule,
  ],
  templateUrl: './folders.component.html',
  providers: [ConfirmationService, MessageService],
})
export class FoldersComponent implements OnInit {
  folders: Folder[] = [];
  selectedFolders: Folder[] = [];
  folderDialog = false;
  folder: Folder = {} as Folder;
  submitted = false;

  constructor(
    private folderService: FolderService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadFolders();
  }

  loadFolders() {
    this.folderService.getAll().subscribe({
      next: (data) => (this.folders = data),
      error: (err) => console.error('Erreur chargement dossiers:', err),
    });
  }

  openNew() {
    this.folder = {} as Folder;
    this.submitted = false;
    this.folderDialog = true;
  }

  hideDialog() {
    this.folderDialog = false;
    this.submitted = false;
  }

  saveFolder() {
    this.submitted = true;
    if (!this.folder.type || !this.folder.dateGenerate) return;

    if (this.folder.id) {
      this.folderService.update(this.folder.id, this.folder).subscribe(() => {
        this.loadFolders();
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Folder updated successfully' });
      });
    } else {
      this.folderService.create(this.folder).subscribe(() => {
        this.loadFolders();
        this.messageService.add({ severity: 'success', summary: 'Created', detail: 'Folder created successfully' });
      });
    }

    this.folderDialog = false;
  }

  editFolder(folder: Folder) {
    this.folder = { ...folder };
    this.folderDialog = true;
  }

  deleteFolder(folder: Folder) {
    this.confirmationService.confirm({
      message: `Supprimer le dossier "${folder.type}" ?`,
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (folder.id)
          this.folderService.delete(folder.id).subscribe(() => {
            this.loadFolders();
            this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Folder deleted successfully' });
          });
      },
    });
  }

  deleteSelectedFolders() {
    this.confirmationService.confirm({
      message: 'Supprimer les dossiers sélectionnés ?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const ids = this.selectedFolders.map((f) => f.id);
        ids.forEach((id) => {
          if (id) this.folderService.delete(id).subscribe(() => this.loadFolders());
        });
        this.selectedFolders = [];
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Selected folders deleted' });
      },
    });
  }

  exportCSV() {
    const csv = this.folders
      .map((f) => `${f.id},${f.dateGenerate},${f.type},${f.company?.name || ''},${f.reglePaiement}`)
      .join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'folders.csv';
    link.click();
  }

  onGlobalFilter(table: any, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }
}
