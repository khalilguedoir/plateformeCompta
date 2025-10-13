import { Company } from "./company";
import { Document } from "./Document";
import { ReglePaiement } from "./ReglePaiement";


export interface Folder {
  id?: number;
  dateGenerate: Date;
  type: string;
  documents?: Document[];
  reglePaiement: ReglePaiement;
  company: Company;
}
