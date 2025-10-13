import { Address } from "./Adresse";
import { Company } from "./company";

export interface Client {
  id?: number;
  name: string;
  email: string;
  phonenumber: string;
  adresse: Address;
  company: Company;
}