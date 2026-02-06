import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVol } from '../vol.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vol.test-samples';

import { RestVol, VolService } from './vol.service';

const requireRestSample: RestVol = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.toJSON(),
  updatedAt: sampleWithRequiredData.updatedAt?.toJSON(),
};

describe('Vol Service', () => {
  let service: VolService;
  let httpMock: HttpTestingController;
  let expectedResult: IVol | IVol[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VolService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Vol', () => {
      const vol = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vol).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vol', () => {
      const vol = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vol).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vol', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vol', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vol', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVolToCollectionIfMissing', () => {
      it('should add a Vol to an empty array', () => {
        const vol: IVol = sampleWithRequiredData;
        expectedResult = service.addVolToCollectionIfMissing([], vol);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vol);
      });

      it('should not add a Vol to an array that contains it', () => {
        const vol: IVol = sampleWithRequiredData;
        const volCollection: IVol[] = [
          {
            ...vol,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVolToCollectionIfMissing(volCollection, vol);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vol to an array that doesn't contain it", () => {
        const vol: IVol = sampleWithRequiredData;
        const volCollection: IVol[] = [sampleWithPartialData];
        expectedResult = service.addVolToCollectionIfMissing(volCollection, vol);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vol);
      });

      it('should add only unique Vol to an array', () => {
        const volArray: IVol[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const volCollection: IVol[] = [sampleWithRequiredData];
        expectedResult = service.addVolToCollectionIfMissing(volCollection, ...volArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vol: IVol = sampleWithRequiredData;
        const vol2: IVol = sampleWithPartialData;
        expectedResult = service.addVolToCollectionIfMissing([], vol, vol2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vol);
        expect(expectedResult).toContain(vol2);
      });

      it('should accept null and undefined values', () => {
        const vol: IVol = sampleWithRequiredData;
        expectedResult = service.addVolToCollectionIfMissing([], null, vol, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vol);
      });

      it('should return initial array if no Vol is added', () => {
        const volCollection: IVol[] = [sampleWithRequiredData];
        expectedResult = service.addVolToCollectionIfMissing(volCollection, undefined, null);
        expect(expectedResult).toEqual(volCollection);
      });
    });

    describe('compareVol', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVol(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7219 };
        const entity2 = null;

        const compareResult1 = service.compareVol(entity1, entity2);
        const compareResult2 = service.compareVol(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7219 };
        const entity2 = { id: 22912 };

        const compareResult1 = service.compareVol(entity1, entity2);
        const compareResult2 = service.compareVol(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7219 };
        const entity2 = { id: 7219 };

        const compareResult1 = service.compareVol(entity1, entity2);
        const compareResult2 = service.compareVol(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
