package com.tools.ftp;

import com.jcraft.jsch.ChannelSftp;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yslao@outlook.com
 * @since 2021/12/27
 */
class SFTPHelperTest {

    private Logger logger = LoggerFactory.getLogger(SFTPHelperTest.class);

    @Disabled
    @Test
    void connection() throws IOException {
        try (SFTPHelper sftpHelper = new SFTPHelper("root", "123456", "127.0.0.1：22")) {
            if (sftpHelper.connection()) {
                Vector ls = sftpHelper.ls("/");
                Iterator iterator = ls.iterator();
                while (iterator.hasNext()) {
                    ChannelSftp.LsEntry file = (ChannelSftp.LsEntry) iterator.next();
                    logger.info("ls file: {}", file.getFilename());
                }
            }
            logger.info("do job, end");
        }

    }
}