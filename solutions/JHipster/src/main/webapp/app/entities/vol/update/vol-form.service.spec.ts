import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vol.test-samples';

import { VolFormService } from './vol-form.service';

describe('Vol Form Service', () => {
  let service: VolFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VolFormService);
  });

  describe('Service methods', () => {
    describe('createVolFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVolFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origin: expect.any(Object),
            destination: expect.any(Object),
            allerRetour: expect.any(Object),
            prix: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });

      it('passing IVol should create a new form with FormGroup', () => {
        const formGroup = service.createVolFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origin: expect.any(Object),
            destination: expect.any(Object),
            allerRetour: expect.any(Object),
            prix: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
          }),
        );
      });
    });

    describe('getVol', () => {
      it('should return NewVol for default Vol initial value', () => {
        const formGroup = service.createVolFormGroup(sampleWithNewData);

        const vol = service.getVol(formGroup) as any;

        expect(vol).toMatchObject(sampleWithNewData);
      });

      it('should return NewVol for empty Vol initial value', () => {
        const formGroup = service.createVolFormGroup();

        const vol = service.getVol(formGroup) as any;

        expect(vol).toMatchObject({});
      });

      it('should return IVol', () => {
        const formGroup = service.createVolFormGroup(sampleWithRequiredData);

        const vol = service.getVol(formGroup) as any;

        expect(vol).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVol should not enable id FormControl', () => {
        const formGroup = service.createVolFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVol should disable id FormControl', () => {
        const formGroup = service.createVolFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
