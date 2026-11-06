package com.test.jpa.dsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.jpa.dsl.model.QQuerydslSample;
import com.test.jpa.dsl.model.QuerydslSample;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class QuerydslCompatibilityTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    void generatedJakartaQueryTypeExecutesWithHibernate() {
        entityManager.persist(new QuerydslSample("张三"));
        entityManager.persist(new QuerydslSample("李四"));
        entityManager.flush();
        entityManager.clear();

        QQuerydslSample sample = QQuerydslSample.querydslSample;
        assertThat(new JPAQueryFactory(entityManager).selectFrom(sample)
            .where(sample.name.eq("张三")).fetch())
            .extracting(QuerydslSample::getName).containsExactly("张三");
    }
}
