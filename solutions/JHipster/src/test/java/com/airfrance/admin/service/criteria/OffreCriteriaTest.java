package com.airfrance.admin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OffreCriteriaTest {

    @Test
    void newOffreCriteriaHasAllFiltersNullTest() {
        var offreCriteria = new OffreCriteria();
        assertThat(offreCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void offreCriteriaFluentMethodsCreatesFiltersTest() {
        var offreCriteria = new OffreCriteria();

        setAllFilters(offreCriteria);

        assertThat(offreCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void offreCriteriaCopyCreatesNullFilterTest() {
        var offreCriteria = new OffreCriteria();
        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void offreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offreCriteria = new OffreCriteria();
        setAllFilters(offreCriteria);

        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offreCriteria = new OffreCriteria();

        assertThat(offreCriteria).hasToString("OffreCriteria{}");
    }

    private static void setAllFilters(OffreCriteria offreCriteria) {
        offreCriteria.id();
        offreCriteria.priorite();
        offreCriteria.createdAt();
        offreCriteria.updatedAt();
        offreCriteria.volId();
        offreCriteria.distinct();
    }

    private static Condition<OffreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPriorite()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getVolId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OffreCriteria> copyFiltersAre(OffreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPriorite(), copy.getPriorite()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getVolId(), copy.getVolId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
