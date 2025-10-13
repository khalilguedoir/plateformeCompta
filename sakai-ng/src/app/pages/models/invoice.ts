export interface Invoice {
  id?: number;
  number: string;
  date: Date;
  amount: number;
  type: string;
  tva: number;
}
