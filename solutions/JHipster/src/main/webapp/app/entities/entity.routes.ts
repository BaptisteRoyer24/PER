import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'airfranceAdminApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'vol',
    data: { pageTitle: 'airfranceAdminApp.vol.home.title' },
    loadChildren: () => import('./vol/vol.routes'),
  },
  {
    path: 'offre',
    data: { pageTitle: 'airfranceAdminApp.offre.home.title' },
    loadChildren: () => import('./offre/offre.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
