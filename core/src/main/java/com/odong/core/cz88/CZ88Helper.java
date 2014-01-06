package com.odong.core.cz88;

import com.odong.core.store.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;

/**
 * Created by flamen on 14-1-3下午1:43.
 */
@Component("core.cz88Service")
public class CZ88Helper extends JdbcHelper {
    public int count() {
        return count("SELECT COUNT(*) FROM CZ88");
    }

    public void load(String file) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"))) {
            String line;

            CZ88 cz = new CZ88();
            while ((line = reader.readLine()) != null) {

                String[] ss = line.split("\\s+");
                if (ss.length < 3) {
                    logger.debug("丢弃IP数据:[{}]", line);
                    continue;
                }
                cz.setIpStart(ip2long(ss[0]));
                cz.setIpEnd(ip2long(ss[1]));
                cz.setExt1(ss[2].replace("'", ""));

                int i;
                String ext2 = "";
                for (i = 3; i < ss.length; i++) {
                    if (i > 3) {
                        ext2 += " ";
                    }
                    ext2 += ss[i];
                }
                cz.setExt2(ext2);

                //logger.debug("[{},{},{},{}]", cz.getIpStart(), cz.getIpEnd(), cz.getExt1(), cz.getExt2());


                execute("INSERT INTO CZ88(ipStart,ipEnd,ext1,ext2) VALUES(?,?,?,?)",
                        cz.getIpStart(), cz.getIpEnd(), cz.getExt1(), cz.getExt2());


            }
        } catch (IOException e) {
            logger.error("解析IP数据库出错", e);
        }
    }

    public CZ88 search(String ip) {
        long lip = ip2long(ip);
        return select("SELECT * FROM CZ88 WHERE ipStart<=? AND ipEnd>=?", new Object[]{lip, lip}, mapper());
    }

    private long ip2long(String ip) {
        long result = 0;
        String[] atoms = ip.split("\\.");
        for (int i = 3; i >= 0; i--) {
            result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
        }
        return result;
    }

    private String long2ip(long ip) {
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            ip >>= 8;
        }

        return sb.toString();

    }

    public void clear() {
        execute("DELETE FROM CZ88");
    }

    @PostConstruct
    void init() {
        install("CZ88",
                longIdColumn(),
                longColumn("ipStart", true),
                longColumn("ipEnd", true),
                stringColumn("ext1", 255, true, false),
                stringColumn("ext2", 255, false, false)
        );
    }

    private RowMapper<CZ88> mapper() {
        return (ResultSet rs, int i) -> {
            CZ88 cz = new CZ88();
            cz.setExt1(rs.getString("ext1"));
            cz.setExt2(rs.getString("ext2"));
            cz.setId(rs.getLong("id"));
            cz.setIpEnd(rs.getLong("ipStart"));
            cz.setIpEnd(rs.getLong("ipEnd"));
            return cz;
        };
    }

    private final static Logger logger = LoggerFactory.getLogger(CZ88Helper.class);
}
