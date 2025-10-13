import { Company } from "./company";
import { Folder } from "./folder";


export interface ReglePaiement {
  id?: number;
  montant: number;
  date: Date;
  type: string;
  status: string;
  folder?: Folder;
  company: Company;
}
