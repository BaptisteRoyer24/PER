package com.airfrance.admin.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VolCriteriaTest {

    @Test
    void newVolCriteriaHasAllFiltersNullTest() {
        var volCriteria = new VolCriteria();
        assertThat(volCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void volCriteriaFluentMethodsCreatesFiltersTest() {
        var volCriteria = new VolCriteria();

        setAllFilters(volCriteria);

        assertThat(volCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void volCriteriaCopyCreatesNullFilterTest() {
        var volCriteria = new VolCriteria();
        var copy = volCriteria.copy();

        assertThat(volCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(volCriteria)
        );
    }

    @Test
    void volCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var volCriteria = new VolCriteria();
        setAllFilters(volCriteria);

        var copy = volCriteria.copy();

        assertThat(volCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(volCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var volCriteria = new VolCriteria();

        assertThat(volCriteria).hasToString("VolCriteria{}");
    }

    private static void setAllFilters(VolCriteria volCriteria) {
        volCriteria.id();
        volCriteria.origin();
        volCriteria.destination();
        volCriteria.allerRetour();
        volCriteria.prix();
        volCriteria.createdAt();
        volCriteria.updatedAt();
        volCriteria.distinct();
    }

    private static Condition<VolCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrigin()) &&
                condition.apply(criteria.getDestination()) &&
                condition.apply(criteria.getAllerRetour()) &&
                condition.apply(criteria.getPrix()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VolCriteria> copyFiltersAre(VolCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrigin(), copy.getOrigin()) &&
                condition.apply(criteria.getDestination(), copy.getDestination()) &&
                condition.apply(criteria.getAllerRetour(), copy.getAllerRetour()) &&
                condition.apply(criteria.getPrix(), copy.getPrix()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
