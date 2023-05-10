package com.tools.powermock;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @since 2022/8/11
 */
@ExtendWith(MockitoExtension.class)
public class MockitoTest {

    @InjectMocks
    TestService testService;

    @Test
    void callInnerGetNumberTest() {
        TestService spy = Mockito.spy(TestService.class);
        int result = 1;
        Mockito.when(spy.getNumber()).thenReturn(result);
        int number = spy.callInnerGetNumber();
        Assertions.assertEquals(result, number);
    }

    /**
     * 静态方法测试
     */
    @Test
    void getTodayStrTest() {
        try (MockedStatic mockedStatic = Mockito.mockStatic(DateUtil.class)) {
            String today = "2022-08-10";
            mockedStatic.when(DateUtil::today).thenReturn(today);
            // TestService mock = Mockito.mock(TestService.class);
            TestService mock = new TestService();
            String todayStr = mock.getTodayStr();
            Assertions.assertEquals(today, todayStr);
        }
    }

    @Test
    void getNameTest() {
    }

}
