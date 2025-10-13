import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TenantService {
  private tenantId = '';

  setTenant(id: string) { this.tenantId = id; }
  getTenant() { return this.tenantId; }
}
