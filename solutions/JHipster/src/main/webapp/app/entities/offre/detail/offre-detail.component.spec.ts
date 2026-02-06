import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { OffreDetailComponent } from './offre-detail.component';

describe('Offre Management Detail Component', () => {
  let comp: OffreDetailComponent;
  let fixture: ComponentFixture<OffreDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffreDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./offre-detail.component').then(m => m.OffreDetailComponent),
              resolve: { offre: () => of({ id: 9345 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OffreDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load offre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OffreDetailComponent);

      // THEN
      expect(instance.offre()).toEqual(expect.objectContaining({ id: 9345 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
