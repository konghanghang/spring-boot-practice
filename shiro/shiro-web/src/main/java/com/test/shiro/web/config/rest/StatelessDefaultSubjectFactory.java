package com.test.shiro.web.config.rest;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 不生成session
 * @author konghang
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    private final DefaultSessionStorageEvaluator sessionStorageEvaluator;

    public StatelessDefaultSubjectFactory(DefaultSessionStorageEvaluator sessionStorageEvaluator) {
        this.sessionStorageEvaluator = sessionStorageEvaluator;
    }

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(Boolean.FALSE);
        this.sessionStorageEvaluator.setSessionStorageEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }
}
