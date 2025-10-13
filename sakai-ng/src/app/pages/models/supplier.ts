import { Address } from "./Adresse";
import { Company } from "./company";

export interface Supplier {
  id?: number;
  name: string;
  email: string;
  phonenumber: string;
  adresse: Address;
  company: Company;
}