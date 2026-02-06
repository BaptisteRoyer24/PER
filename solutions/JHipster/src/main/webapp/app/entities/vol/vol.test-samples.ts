import dayjs from 'dayjs/esm';

import { IVol, NewVol } from './vol.model';

export const sampleWithRequiredData: IVol = {
  id: 12672,
  origin: 'VRR',
  destination: 'VMC',
  allerRetour: true,
  prix: 243.48,
};

export const sampleWithPartialData: IVol = {
  id: 10532,
  origin: 'JAV',
  destination: 'NQF',
  allerRetour: false,
  prix: 9429.58,
  createdAt: dayjs('2026-02-05T03:46'),
  updatedAt: dayjs('2026-02-05T08:16'),
};

export const sampleWithFullData: IVol = {
  id: 26672,
  origin: 'DXR',
  destination: 'GYM',
  allerRetour: true,
  prix: 31958.8,
  createdAt: dayjs('2026-02-05T11:24'),
  updatedAt: dayjs('2026-02-05T09:47'),
};

export const sampleWithNewData: NewVol = {
  origin: 'BQY',
  destination: 'YRN',
  allerRetour: true,
  prix: 16716.2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
