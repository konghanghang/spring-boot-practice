package com.tools.service;

import com.jcraft.jsch.ChannelSftp;
import com.tools.ftp.SFTPHelper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author yslao@outlook.com
 * @since 2021/12/27
 */
@Service
public class JobService {

    private Logger logger = LoggerFactory.getLogger(JobService.class);

    //@Scheduled(cron = "0/1 * * * * ?")
    public void doJob() {
        logger.info("do job, begin");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
