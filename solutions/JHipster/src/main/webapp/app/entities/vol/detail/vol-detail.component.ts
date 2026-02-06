import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IVol } from '../vol.model';

@Component({
  selector: 'jhi-vol-detail',
  templateUrl: './vol-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class VolDetailComponent {
  vol = input<IVol | null>(null);

  previousState(): void {
    window.history.back();
  }
}
