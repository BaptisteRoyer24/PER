import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVol } from '../vol.model';
import { VolService } from '../service/vol.service';

const volResolve = (route: ActivatedRouteSnapshot): Observable<null | IVol> => {
  const id = route.params.id;
  if (id) {
    return inject(VolService)
      .find(id)
      .pipe(
        mergeMap((vol: HttpResponse<IVol>) => {
          if (vol.body) {
            return of(vol.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default volResolve;
