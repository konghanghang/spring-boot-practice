package com.test.list.model;

import lombok.Data;

@Data
public class Item {

    private int no;

    private Item next;

    public Item(int no) {
        this.no = no;
    }
}
