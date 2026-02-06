import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IOffre } from '../offre.model';

@Component({
  selector: 'jhi-offre-detail',
  templateUrl: './offre-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class OffreDetailComponent {
  offre = input<IOffre | null>(null);

  previousState(): void {
    window.history.back();
  }
}
