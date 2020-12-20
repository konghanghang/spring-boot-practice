package com.ioc.service;

import com.ioc.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    public void print(){
        System.out.println(bookDao);
    }

}
