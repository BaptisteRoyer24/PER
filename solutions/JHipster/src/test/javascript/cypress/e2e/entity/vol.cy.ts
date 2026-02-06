import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Vol e2e test', () => {
  const volPageUrl = '/vol';
  const volPageUrlPattern = new RegExp('/vol(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const volSample = { origin: 'SWY', destination: 'ZWN', allerRetour: false, prix: 19148.12 };

  let vol;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vols+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vols').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vols/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vol) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vols/${vol.id}`,
      }).then(() => {
        vol = undefined;
      });
    }
  });

  it('Vols menu should load Vols page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vol');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Vol').should('exist');
    cy.url().should('match', volPageUrlPattern);
  });

  describe('Vol page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(volPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Vol page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vol/new$'));
        cy.getEntityCreateUpdateHeading('Vol');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', volPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vols',
          body: volSample,
        }).then(({ body }) => {
          vol = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vols+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vols?page=0&size=20>; rel="last",<http://localhost/api/vols?page=0&size=20>; rel="first"',
              },
              body: [vol],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(volPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Vol page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vol');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', volPageUrlPattern);
      });

      it('edit button click should load edit Vol page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vol');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', volPageUrlPattern);
      });

      it('edit button click should load edit Vol page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vol');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', volPageUrlPattern);
      });

      it('last delete button click should delete instance of Vol', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('vol').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', volPageUrlPattern);

        vol = undefined;
      });
    });
  });

  describe('new Vol page', () => {
    beforeEach(() => {
      cy.visit(`${volPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Vol');
    });

    it('should create an instance of Vol', () => {
      cy.get(`[data-cy="origin"]`).type('QRP');
      cy.get(`[data-cy="origin"]`).should('have.value', 'QRP');

      cy.get(`[data-cy="destination"]`).type('UWM');
      cy.get(`[data-cy="destination"]`).should('have.value', 'UWM');

      cy.get(`[data-cy="allerRetour"]`).should('not.be.checked');
      cy.get(`[data-cy="allerRetour"]`).click();
      cy.get(`[data-cy="allerRetour"]`).should('be.checked');

      cy.get(`[data-cy="prix"]`).type('20357.69');
      cy.get(`[data-cy="prix"]`).should('have.value', '20357.69');

      cy.get(`[data-cy="createdAt"]`).type('2026-02-05T07:47');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2026-02-05T07:47');

      cy.get(`[data-cy="updatedAt"]`).type('2026-02-04T21:38');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2026-02-04T21:38');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vol = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', volPageUrlPattern);
    });
  });
});
