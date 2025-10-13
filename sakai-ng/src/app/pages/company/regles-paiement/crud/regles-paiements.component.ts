import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReglePaiement } from '../../../models/ReglePaiement';
import { ReglePaiementService } from '../../../service/regle-paiement.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-regles-paiements',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    ToolbarModule,
    InputTextModule,
    InputNumberModule,
    ToastModule
  ],
  templateUrl: './regles-paiements.component.html',
  providers: [MessageService, ConfirmationService]
})
export class ReglesPaiementsComponent implements OnInit {
  regles: ReglePaiement[] = [];
  selectedRegles: ReglePaiement[] = [];
  regleDialog = false;
  regle: ReglePaiement = {} as ReglePaiement;
  submitted = false;
  loading = false;

  constructor(
    private regleService: ReglePaiementService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadRegles();
  }

  loadRegles(): void {
    this.loading = true;
    this.regleService.getAll().subscribe({
      next: data => {
        this.regles = data;
        this.loading = false;
      },
      error: err => {
        console.error('Erreur chargement règlements:', err);
        this.loading = false;
      }
    });
  }

  openNew(): void {
    this.regle = {} as ReglePaiement;
    this.submitted = false;
    this.regleDialog = true;
  }

  hideDialog(): void {
    this.regleDialog = false;
    this.submitted = false;
  }

  saveRegle(): void {
    this.submitted = true;
    if (!this.regle.montant || !this.regle.date || !this.regle.type || !this.regle.status) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Champs requis',
        detail: 'Veuillez remplir tous les champs obligatoires.'
      });
      return;
    }

    if (this.regle.id) {
      // mise à jour
      this.regleService.update(this.regle.id, this.regle).subscribe(() => {
        this.loadRegles();
        this.messageService.add({
          severity: 'success',
          summary: 'Mise à jour',
          detail: 'Règle de paiement mise à jour'
        });
      });
    } else {
      // création
      this.regleService.create(this.regle).subscribe(() => {
        this.loadRegles();
        this.messageService.add({
          severity: 'success',
          summary: 'Créée',
          detail: 'Règle de paiement ajoutée'
        });
      });
    }

    this.regleDialog = false;
  }

  editRegle(regle: ReglePaiement): void {
    this.regle = { ...regle };
    this.regleDialog = true;
  }

  deleteRegle(regle: ReglePaiement): void {
    this.confirmationService.confirm({
      message: `Voulez-vous vraiment supprimer ce règlement ?`,
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (regle.id)
          this.regleService.delete(regle.id).subscribe(() => {
            this.loadRegles();
            this.messageService.add({
              severity: 'success',
              summary: 'Supprimée',
              detail: 'Règle supprimée'
            });
          });
      }
    });
  }

  deleteSelectedRegles(): void {
    this.confirmationService.confirm({
      message: 'Voulez-vous supprimer les règlements sélectionnés ?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        const ids = this.selectedRegles.map(r => r.id);
        ids.forEach(id => {
          if (id) this.regleService.delete(id).subscribe(() => this.loadRegles());
        });
        this.selectedRegles = [];
        this.messageService.add({
          severity: 'success',
          summary: 'Supprimées',
          detail: 'Règlements supprimés'
        });
      }
    });
  }
}
