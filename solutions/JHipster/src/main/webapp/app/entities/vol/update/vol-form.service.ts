import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVol, NewVol } from '../vol.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVol for edit and NewVolFormGroupInput for create.
 */
type VolFormGroupInput = IVol | PartialWithRequiredKeyOf<NewVol>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVol | NewVol> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type VolFormRawValue = FormValueOf<IVol>;

type NewVolFormRawValue = FormValueOf<NewVol>;

type VolFormDefaults = Pick<NewVol, 'id' | 'allerRetour' | 'createdAt' | 'updatedAt'>;

type VolFormGroupContent = {
  id: FormControl<VolFormRawValue['id'] | NewVol['id']>;
  origin: FormControl<VolFormRawValue['origin']>;
  destination: FormControl<VolFormRawValue['destination']>;
  allerRetour: FormControl<VolFormRawValue['allerRetour']>;
  prix: FormControl<VolFormRawValue['prix']>;
  createdAt: FormControl<VolFormRawValue['createdAt']>;
  updatedAt: FormControl<VolFormRawValue['updatedAt']>;
};

export type VolFormGroup = FormGroup<VolFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VolFormService {
  createVolFormGroup(vol: VolFormGroupInput = { id: null }): VolFormGroup {
    const volRawValue = this.convertVolToVolRawValue({
      ...this.getFormDefaults(),
      ...vol,
    });
    return new FormGroup<VolFormGroupContent>({
      id: new FormControl(
        { value: volRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      origin: new FormControl(volRawValue.origin, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(3), Validators.pattern('^[A-Z]{3}$')],
      }),
      destination: new FormControl(volRawValue.destination, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(3), Validators.pattern('^[A-Z]{3}$')],
      }),
      allerRetour: new FormControl(volRawValue.allerRetour, {
        validators: [Validators.required],
      }),
      prix: new FormControl(volRawValue.prix, {
        validators: [Validators.required, Validators.min(0)],
      }),
      createdAt: new FormControl(volRawValue.createdAt),
      updatedAt: new FormControl(volRawValue.updatedAt),
    });
  }

  getVol(form: VolFormGroup): IVol | NewVol {
    return this.convertVolRawValueToVol(form.getRawValue() as VolFormRawValue | NewVolFormRawValue);
  }

  resetForm(form: VolFormGroup, vol: VolFormGroupInput): void {
    const volRawValue = this.convertVolToVolRawValue({ ...this.getFormDefaults(), ...vol });
    form.reset(
      {
        ...volRawValue,
        id: { value: volRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VolFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      allerRetour: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertVolRawValueToVol(rawVol: VolFormRawValue | NewVolFormRawValue): IVol | NewVol {
    return {
      ...rawVol,
      createdAt: dayjs(rawVol.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawVol.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertVolToVolRawValue(
    vol: IVol | (Partial<NewVol> & VolFormDefaults),
  ): VolFormRawValue | PartialWithRequiredKeyOf<NewVolFormRawValue> {
    return {
      ...vol,
      createdAt: vol.createdAt ? vol.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: vol.updatedAt ? vol.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
