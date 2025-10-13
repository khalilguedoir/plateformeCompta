import { Country } from "./country";

export interface Address {
  id?: number;
  street: string;
  city: string;
  country: string;
  entityName?: string;
  entityId?: number;
}
