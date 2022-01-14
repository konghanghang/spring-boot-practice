package com.tools.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2022/1/14
 */
class HttpClientUtilsTest {

    @Test
    void postBytes() {
        File file = new File("d:\\2021-11-12_1257512268746624.xlsx");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            Map<String, String> params = new HashMap<>();
            params.put("fileName", "2021-11-12_1257512268746624.xlsx");
            params.put("batchNo", "dsfdsg");
            params.put("uploadMode", "date");
            params.put("taskId", "10813");
            String s = HttpClientUtils.postBytes("http://localhost:8088/v1/task/sc/upload", bytes, params);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBigDecimal() {
        BigDecimal bigDecimal = new BigDecimal("1000.01").multiply(new BigDecimal(100));
        System.out.println(bigDecimal.intValue());
    }
}