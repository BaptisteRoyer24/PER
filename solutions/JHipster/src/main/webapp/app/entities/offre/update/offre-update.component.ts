import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVol } from 'app/entities/vol/vol.model';
import { VolService } from 'app/entities/vol/service/vol.service';
import { PrioriteOffre } from 'app/entities/enumerations/priorite-offre.model';
import { OffreService } from '../service/offre.service';
import { IOffre } from '../offre.model';
import { OffreFormGroup, OffreFormService } from './offre-form.service';

@Component({
  selector: 'jhi-offre-update',
  templateUrl: './offre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OffreUpdateComponent implements OnInit {
  isSaving = false;
  offre: IOffre | null = null;
  prioriteOffreValues = Object.keys(PrioriteOffre);

  volsSharedCollection: IVol[] = [];

  protected offreService = inject(OffreService);
  protected offreFormService = inject(OffreFormService);
  protected volService = inject(VolService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OffreFormGroup = this.offreFormService.createOffreFormGroup();

  compareVol = (o1: IVol | null, o2: IVol | null): boolean => this.volService.compareVol(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offre }) => {
      this.offre = offre;
      if (offre) {
        this.updateForm(offre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offre = this.offreFormService.getOffre(this.editForm);
    if (offre.id !== null) {
      this.subscribeToSaveResponse(this.offreService.update(offre));
    } else {
      this.subscribeToSaveResponse(this.offreService.create(offre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffre>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(offre: IOffre): void {
    this.offre = offre;
    this.offreFormService.resetForm(this.editForm, offre);

    this.volsSharedCollection = this.volService.addVolToCollectionIfMissing<IVol>(this.volsSharedCollection, offre.vol);
  }

  protected loadRelationshipsOptions(): void {
    this.volService
      .query()
      .pipe(map((res: HttpResponse<IVol[]>) => res.body ?? []))
      .pipe(map((vols: IVol[]) => this.volService.addVolToCollectionIfMissing<IVol>(vols, this.offre?.vol)))
      .subscribe((vols: IVol[]) => (this.volsSharedCollection = vols));
  }
}
