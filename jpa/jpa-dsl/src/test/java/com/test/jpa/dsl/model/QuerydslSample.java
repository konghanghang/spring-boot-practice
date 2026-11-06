package com.test.jpa.dsl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Test-only entity proving that the Jakarta annotation processor generates a usable Q type. */
@Entity
public class QuerydslSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected QuerydslSample() {
    }

    public QuerydslSample(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
