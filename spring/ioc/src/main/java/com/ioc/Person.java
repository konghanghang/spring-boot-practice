package com.ioc;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Person {

    private String name;

    private Integer age;

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
