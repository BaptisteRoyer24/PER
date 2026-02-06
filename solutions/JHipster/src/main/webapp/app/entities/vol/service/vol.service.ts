import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVol, NewVol } from '../vol.model';

export type PartialUpdateVol = Partial<IVol> & Pick<IVol, 'id'>;

type RestOf<T extends IVol | NewVol> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestVol = RestOf<IVol>;

export type NewRestVol = RestOf<NewVol>;

export type PartialUpdateRestVol = RestOf<PartialUpdateVol>;

export type EntityResponseType = HttpResponse<IVol>;
export type EntityArrayResponseType = HttpResponse<IVol[]>;

@Injectable({ providedIn: 'root' })
export class VolService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vols');

  create(vol: NewVol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vol);
    return this.http.post<RestVol>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vol: IVol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vol);
    return this.http
      .put<RestVol>(`${this.resourceUrl}/${this.getVolIdentifier(vol)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vol: PartialUpdateVol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vol);
    return this.http
      .patch<RestVol>(`${this.resourceUrl}/${this.getVolIdentifier(vol)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVol>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVol[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVolIdentifier(vol: Pick<IVol, 'id'>): number {
    return vol.id;
  }

  compareVol(o1: Pick<IVol, 'id'> | null, o2: Pick<IVol, 'id'> | null): boolean {
    return o1 && o2 ? this.getVolIdentifier(o1) === this.getVolIdentifier(o2) : o1 === o2;
  }

  addVolToCollectionIfMissing<Type extends Pick<IVol, 'id'>>(volCollection: Type[], ...volsToCheck: (Type | null | undefined)[]): Type[] {
    const vols: Type[] = volsToCheck.filter(isPresent);
    if (vols.length > 0) {
      const volCollectionIdentifiers = volCollection.map(volItem => this.getVolIdentifier(volItem));
      const volsToAdd = vols.filter(volItem => {
        const volIdentifier = this.getVolIdentifier(volItem);
        if (volCollectionIdentifiers.includes(volIdentifier)) {
          return false;
        }
        volCollectionIdentifiers.push(volIdentifier);
        return true;
      });
      return [...volsToAdd, ...volCollection];
    }
    return volCollection;
  }

  protected convertDateFromClient<T extends IVol | NewVol | PartialUpdateVol>(vol: T): RestOf<T> {
    return {
      ...vol,
      createdAt: vol.createdAt?.toJSON() ?? null,
      updatedAt: vol.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVol: RestVol): IVol {
    return {
      ...restVol,
      createdAt: restVol.createdAt ? dayjs(restVol.createdAt) : undefined,
      updatedAt: restVol.updatedAt ? dayjs(restVol.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVol>): HttpResponse<IVol> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVol[]>): HttpResponse<IVol[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
