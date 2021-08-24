package com.test.jpa.dao;

import com.test.jpa.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByNumber(String number);

    @Query(value = "from Student where name = ?1")
    Student queryByNameUseHQL(String name);

    @Query(value = "select * from student where name=?",nativeQuery = true)
    Student queryByNameUseSQL(String name);
}
