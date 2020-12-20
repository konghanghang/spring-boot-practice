package com.ioc;

import com.ioc.config.MainConfigOfAutowired;
import com.ioc.dao.BookDao;
import com.ioc.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocTest_Autowired {

    @Test
    public void test01(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);
        final String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
        final BookService bean = context.getBean(BookService.class);
        bean.print();
        final BookDao bean1 = context.getBean(BookDao.class);
        System.out.println(bean1);
    }

}
