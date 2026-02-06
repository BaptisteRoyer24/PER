import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VolDetailComponent } from './vol-detail.component';

describe('Vol Management Detail Component', () => {
  let comp: VolDetailComponent;
  let fixture: ComponentFixture<VolDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VolDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vol-detail.component').then(m => m.VolDetailComponent),
              resolve: { vol: () => of({ id: 7219 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VolDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VolDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vol on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VolDetailComponent);

      // THEN
      expect(instance.vol()).toEqual(expect.objectContaining({ id: 7219 }));
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
