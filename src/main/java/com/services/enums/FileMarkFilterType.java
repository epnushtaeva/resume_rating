package com.services.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileMarkFilterType {
    MORE_THAN(1),
    LESS_THAN(2),
    MORE_THAN_OR_EQUAL(3),
    LESS_THAN_OR_EQUAL(4),
    EQUAL(5),
    DEFAULT(6);

    private int type;

    FileMarkFilterType(int filterType) {
        this.type = filterType;
    }

    @JsonValue
    public int getType() {
        return type;
    }
}
