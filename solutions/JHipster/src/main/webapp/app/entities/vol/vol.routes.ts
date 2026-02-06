import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VolResolve from './route/vol-routing-resolve.service';

const volRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vol.component').then(m => m.VolComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vol-detail.component').then(m => m.VolDetailComponent),
    resolve: {
      vol: VolResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vol-update.component').then(m => m.VolUpdateComponent),
    resolve: {
      vol: VolResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vol-update.component').then(m => m.VolUpdateComponent),
    resolve: {
      vol: VolResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default volRoute;
