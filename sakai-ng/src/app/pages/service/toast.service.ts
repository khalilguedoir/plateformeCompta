import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
 constructor(private msg: MessageService) {}

  success(summary: string, detail?: string) { this.msg.add({severity:'success', summary, detail}); }
  error(summary: string, detail?: string) { this.msg.add({severity:'error', summary, detail}); }
  info(summary: string, detail?: string) { this.msg.add({severity:'info', summary, detail}); }
}