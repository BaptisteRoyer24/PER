import dayjs from 'dayjs/esm';
import { IVol } from 'app/entities/vol/vol.model';
import { PrioriteOffre } from 'app/entities/enumerations/priorite-offre.model';

export interface IOffre {
  id: number;
  priorite?: keyof typeof PrioriteOffre | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  vol?: Pick<IVol, 'id' | 'origin'> | null;
}

export type NewOffre = Omit<IOffre, 'id'> & { id: null };
