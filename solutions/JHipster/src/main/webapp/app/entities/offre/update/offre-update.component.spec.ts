import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVol } from 'app/entities/vol/vol.model';
import { VolService } from 'app/entities/vol/service/vol.service';
import { OffreService } from '../service/offre.service';
import { IOffre } from '../offre.model';
import { OffreFormService } from './offre-form.service';

import { OffreUpdateComponent } from './offre-update.component';

describe('Offre Management Update Component', () => {
  let comp: OffreUpdateComponent;
  let fixture: ComponentFixture<OffreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let offreFormService: OffreFormService;
  let offreService: OffreService;
  let volService: VolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OffreUpdateComponent],
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
      .overrideTemplate(OffreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OffreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    offreFormService = TestBed.inject(OffreFormService);
    offreService = TestBed.inject(OffreService);
    volService = TestBed.inject(VolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Vol query and add missing value', () => {
      const offre: IOffre = { id: 8458 };
      const vol: IVol = { id: 7219 };
      offre.vol = vol;

      const volCollection: IVol[] = [{ id: 7219 }];
      jest.spyOn(volService, 'query').mockReturnValue(of(new HttpResponse({ body: volCollection })));
      const additionalVols = [vol];
      const expectedCollection: IVol[] = [...additionalVols, ...volCollection];
      jest.spyOn(volService, 'addVolToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ offre });
      comp.ngOnInit();

      expect(volService.query).toHaveBeenCalled();
      expect(volService.addVolToCollectionIfMissing).toHaveBeenCalledWith(volCollection, ...additionalVols.map(expect.objectContaining));
      expect(comp.volsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const offre: IOffre = { id: 8458 };
      const vol: IVol = { id: 7219 };
      offre.vol = vol;

      activatedRoute.data = of({ offre });
      comp.ngOnInit();

      expect(comp.volsSharedCollection).toContainEqual(vol);
      expect(comp.offre).toEqual(offre);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOffre>>();
      const offre = { id: 9345 };
      jest.spyOn(offreFormService, 'getOffre').mockReturnValue(offre);
      jest.spyOn(offreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offre }));
      saveSubject.complete();

      // THEN
      expect(offreFormService.getOffre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(offreService.update).toHaveBeenCalledWith(expect.objectContaining(offre));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOffre>>();
      const offre = { id: 9345 };
      jest.spyOn(offreFormService, 'getOffre').mockReturnValue({ id: null });
      jest.spyOn(offreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offre }));
      saveSubject.complete();

      // THEN
      expect(offreFormService.getOffre).toHaveBeenCalled();
      expect(offreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOffre>>();
      const offre = { id: 9345 };
      jest.spyOn(offreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(offreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVol', () => {
      it('should forward to volService', () => {
        const entity = { id: 7219 };
        const entity2 = { id: 22912 };
        jest.spyOn(volService, 'compareVol');
        comp.compareVol(entity, entity2);
        expect(volService.compareVol).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
