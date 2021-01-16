package com.test.list.model;

import lombok.Data;
@Data
public class Hero{
    private int no;
    private String name;
    private String nickname;
    private Hero next;
    private Hero pre;

    public Hero(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
