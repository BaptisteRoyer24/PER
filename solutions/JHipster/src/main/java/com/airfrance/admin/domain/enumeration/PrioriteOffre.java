package com.airfrance.admin.domain.enumeration;

/**
 * The PrioriteOffre enumeration.
 */
public enum PrioriteOffre {
    ELEVEE("elevee"),
    NORMALE("normale"),
    BASSE("basse");

    private final String value;

    PrioriteOffre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
