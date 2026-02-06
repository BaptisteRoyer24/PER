import dayjs from 'dayjs/esm';

import { IOffre, NewOffre } from './offre.model';

export const sampleWithRequiredData: IOffre = {
  id: 11043,
  priorite: 'BASSE',
};

export const sampleWithPartialData: IOffre = {
  id: 30762,
  priorite: 'NORMALE',
};

export const sampleWithFullData: IOffre = {
  id: 14500,
  priorite: 'BASSE',
  createdAt: dayjs('2026-02-05T07:41'),
  updatedAt: dayjs('2026-02-04T17:25'),
};

export const sampleWithNewData: NewOffre = {
  priorite: 'ELEVEE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
