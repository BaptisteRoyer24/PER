import dayjs from 'dayjs/esm';

export interface IVol {
  id: number;
  origin?: string | null;
  destination?: string | null;
  allerRetour?: boolean | null;
  prix?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewVol = Omit<IVol, 'id'> & { id: null };
