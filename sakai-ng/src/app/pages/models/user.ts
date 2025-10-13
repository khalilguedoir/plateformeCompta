import { Company } from "./company";

export enum Role {
  ADMIN = 'ADMIN',
  ACCOUNTANT = 'ACCOUNTANT',
  COMPANY = 'COMPANY'
}

export interface User {
  id?: number;
  username: string;
  password?: string;
  active: boolean;
  role: Role;
  company?: Company;
}