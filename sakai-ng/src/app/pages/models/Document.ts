import { Folder } from "./folder";
import { Invoice } from "./invoice";
import { ReglePaiement } from "./ReglePaiement";


export interface Document {
  id?: number;
  number: string;
  dateUpload: Date;
  type: string;
  urlFile: string;
  invoice: Invoice;
  reglePaiement: ReglePaiement;
  folder: Folder;
}
