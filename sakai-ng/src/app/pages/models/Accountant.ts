import { Company } from "./company";
import { User } from "./user";

export interface AccountingCode {
  id?: number;
  code: string;
  description: string;
}

export interface Accountant {
  id?: number;
  numeroAgrement: string;
  specialite: string;
  user: User;
  company: Company;
  accountingCodes: AccountingCode[];
}
