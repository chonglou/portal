package com.odong.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.UnsupportedOptionsException;
import org.tukaani.xz.XZOutputStream;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by flamen on 14-1-3上午10:04.
 */
@Component("core.zipHelper")
public class ZipHelper {
    public void compress(String input, boolean delete) throws IOException {
        try (
                InputStream in = new BufferedInputStream(new FileInputStream(input));
                OutputStream out = new XZOutputStream(new BufferedOutputStream(new FileOutputStream(input + ".xz")), options);
        ) {
            byte[] buf = new byte[1024];
            int size;
            while ((size = in.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
        } catch (IOException e) {
            logger.error("压缩文件[{}]出错", input);
            throw e;
        }
        if (delete) {
            if (new File(input).delete()) {
                logger.debug("删除文件[{}]成功", input);
            } else {
                logger.error("删除文件[{}]失败", input);
            }
        }
    }

    @PostConstruct
    void init() throws UnsupportedOptionsException {
        options = new LZMA2Options();
        options.setPreset(7);
    }

    private LZMA2Options options;
    private final static Logger logger = LoggerFactory.getLogger(ZipHelper.class);

}