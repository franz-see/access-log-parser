package com.ef.dto;

public enum Duration {
    Hourly("hourly"), Daily("daily");

    private final String value;

    Duration(String value) {
        this.value = value;
    }
}
