package com.test.jpa.service.impl;

import com.test.jpa.dao.StudentRepository;
import com.test.jpa.model.Student;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class )
@SpringBootTest
class StudentServiceImplTest {

    @Resource
    private StudentRepository studentRepository;

    @Test
    void add() {
        Student student = new Student();
        student.setNumber("13100000000");
        student.setName("张三");
        student.setSex(1);
        student.setEmail("aa@aa.com");
        student.setRemark("================");
        studentRepository.save(student);
    }

    @Test
    void find() {
        Optional<Student> student = studentRepository.findById(1L);
        student.ifPresent(s -> System.out.println(s));
    }

    @Test
    void findAll() {
        Sort sort = Sort.by(new Sort.Order(Direction.DESC, "createTime"));
        List<Student> all = studentRepository.findAll(sort);
        System.out.println(all);
    }

    @Test
    void findByNumber() {
        Student student = studentRepository.findByNumber("13100000000");
        System.out.println(student);
    }

}