package com.tools.zip;

import com.google.common.collect.Lists;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author yslao@outlook.com
 * @since 2021/12/24
 */
public class JdkZipTest {

    @Disabled
    @Test
    void testZip() {
        ArrayList<String> paths = Lists.newArrayList("d:\\apiclient_cert.p12",
            "d:\\GoogleStyle.xml");
        String zipFile = "d:\\test.zip";
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String path : paths) {
                File file = new File(path);
                byte[] buf = new byte[1024];
                if (file.exists()) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    out.putNextEntry(entry);
                    int len;
                    FileInputStream in = new FileInputStream(file);
                    while ((len = in.read(buf)) != -1){
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(zipFile);
        if (file.exists()) {
            file.delete();
        }
    }

}
