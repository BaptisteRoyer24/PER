import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOffre, NewOffre } from '../offre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOffre for edit and NewOffreFormGroupInput for create.
 */
type OffreFormGroupInput = IOffre | PartialWithRequiredKeyOf<NewOffre>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOffre | NewOffre> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type OffreFormRawValue = FormValueOf<IOffre>;

type NewOffreFormRawValue = FormValueOf<NewOffre>;

type OffreFormDefaults = Pick<NewOffre, 'id' | 'createdAt' | 'updatedAt'>;

type OffreFormGroupContent = {
  id: FormControl<OffreFormRawValue['id'] | NewOffre['id']>;
  priorite: FormControl<OffreFormRawValue['priorite']>;
  createdAt: FormControl<OffreFormRawValue['createdAt']>;
  updatedAt: FormControl<OffreFormRawValue['updatedAt']>;
  vol: FormControl<OffreFormRawValue['vol']>;
};

export type OffreFormGroup = FormGroup<OffreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OffreFormService {
  createOffreFormGroup(offre: OffreFormGroupInput = { id: null }): OffreFormGroup {
    const offreRawValue = this.convertOffreToOffreRawValue({
      ...this.getFormDefaults(),
      ...offre,
    });
    return new FormGroup<OffreFormGroupContent>({
      id: new FormControl(
        { value: offreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      priorite: new FormControl(offreRawValue.priorite, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(offreRawValue.createdAt),
      updatedAt: new FormControl(offreRawValue.updatedAt),
      vol: new FormControl(offreRawValue.vol),
    });
  }

  getOffre(form: OffreFormGroup): IOffre | NewOffre {
    return this.convertOffreRawValueToOffre(form.getRawValue() as OffreFormRawValue | NewOffreFormRawValue);
  }

  resetForm(form: OffreFormGroup, offre: OffreFormGroupInput): void {
    const offreRawValue = this.convertOffreToOffreRawValue({ ...this.getFormDefaults(), ...offre });
    form.reset(
      {
        ...offreRawValue,
        id: { value: offreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OffreFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertOffreRawValueToOffre(rawOffre: OffreFormRawValue | NewOffreFormRawValue): IOffre | NewOffre {
    return {
      ...rawOffre,
      createdAt: dayjs(rawOffre.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawOffre.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertOffreToOffreRawValue(
    offre: IOffre | (Partial<NewOffre> & OffreFormDefaults),
  ): OffreFormRawValue | PartialWithRequiredKeyOf<NewOffreFormRawValue> {
    return {
      ...offre,
      createdAt: offre.createdAt ? offre.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: offre.updatedAt ? offre.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
