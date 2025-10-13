import { Timezone } from "./Timezone";

export interface Company {
  id?: number;
  name: string;
  databaseName: string;
  matriculeFiscale: string;
  address: string;
  timezone?: Timezone;
}
