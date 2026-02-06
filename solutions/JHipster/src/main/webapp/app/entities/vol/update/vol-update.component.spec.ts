import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VolService } from '../service/vol.service';
import { IVol } from '../vol.model';
import { VolFormService } from './vol-form.service';

import { VolUpdateComponent } from './vol-update.component';

describe('Vol Management Update Component', () => {
  let comp: VolUpdateComponent;
  let fixture: ComponentFixture<VolUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let volFormService: VolFormService;
  let volService: VolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VolUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VolUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VolUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    volFormService = TestBed.inject(VolFormService);
    volService = TestBed.inject(VolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const vol: IVol = { id: 22912 };

      activatedRoute.data = of({ vol });
      comp.ngOnInit();

      expect(comp.vol).toEqual(vol);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVol>>();
      const vol = { id: 7219 };
      jest.spyOn(volFormService, 'getVol').mockReturnValue(vol);
      jest.spyOn(volService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vol }));
      saveSubject.complete();

      // THEN
      expect(volFormService.getVol).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(volService.update).toHaveBeenCalledWith(expect.objectContaining(vol));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVol>>();
      const vol = { id: 7219 };
      jest.spyOn(volFormService, 'getVol').mockReturnValue({ id: null });
      jest.spyOn(volService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vol: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vol }));
      saveSubject.complete();

      // THEN
      expect(volFormService.getVol).toHaveBeenCalled();
      expect(volService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVol>>();
      const vol = { id: 7219 };
      jest.spyOn(volService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(volService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
