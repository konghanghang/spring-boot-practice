package com.test.jpa.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.jpa.dao.StudentRepository;
import com.test.jpa.model.Student;
import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class StudentServiceImplTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DataSource dataSource;

    @Test
    void persistsAllFieldsAndQueriesByDerivedHqlAndNativeQueries() {
        Student student = student("13100000000", "张三", LocalDateTime.of(2026, 1, 2, 3, 4, 5));
        studentRepository.saveAndFlush(student);
        Long id = student.getId();
        assertThat(id).isPositive();
        entityManager.clear();

        Student loaded = studentRepository.findById(id).orElseThrow();
        assertThat(loaded).usingRecursiveComparison().isEqualTo(student);
        assertThat(studentRepository.findByNumber(student.getNumber()).getId()).isEqualTo(id);
        assertThat(studentRepository.queryByNameUseHQL(student.getName()).getId()).isEqualTo(id);
        assertThat(studentRepository.queryByNameUseSQL(student.getName()).getId()).isEqualTo(id);
    }

    @Test
    void sortsByCreateTimeAndPreservesDeclarationOrderDdl() throws Exception {
        LocalDateTime now = LocalDateTime.of(2026, 1, 2, 3, 4, 5);
        Student older = studentRepository.save(student("13100000001", "李四", now.minusDays(1)));
        Student newer = studentRepository.save(student("13100000002", "王五", now));
        studentRepository.flush();
        entityManager.clear();

        assertThat(studentRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime")))
            .extracting(Student::getId).containsExactly(newer.getId(), older.getId());

        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getURL()).startsWith("jdbc:h2:mem:");
            List<String> columns = new ArrayList<>();
            try (ResultSet result = connection.getMetaData().getColumns(null, null, "STUDENT", null)) {
                while (result.next()) {
                    columns.add(result.getString("COLUMN_NAME"));
                }
            }
            assertThat(columns).containsExactly("ID", "NUMBER", "NAME", "SEX", "EMAIL", "REMARK", "CREATE_TIME");
        }
    }

    private Student student(String number, String name, LocalDateTime created) {
        Student student = new Student();
        student.setNumber(number);
        student.setName(name);
        student.setSex(1);
        student.setEmail("student@example.test");
        student.setRemark("中文与 emoji 😀");
        student.setCreateTime(created);
        return student;
    }
}
