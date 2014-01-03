package com.odong.core.cz88;

import com.odong.core.store.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by flamen on 14-1-3下午1:43.
 */
@Component("core.cz88Service")
public class CZ88HelperImpl extends JdbcHelper implements CZ88Helper {
    @Override
    public int count() {
        return count("SELECT COUNT(*) FROM CZ88");
    }

    @Override
    public void load(String file) {

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"))){
                String line;
                CZ88 cz = new CZ88();
                while ((line=reader.readLine())!=null){

                    String[] ss = line.split("\\s+");
                    if(ss.length < 4){
                        logger.debug("丢弃IP数据:[{}]", line);
                        continue;
                    }
                    cz.setIpStart(ss[0]);
                    cz.setIpEnd(ss[1]);
                    cz.setExt1(ss[2].replace("'", ""));

                    int i;
                    String ext2="";
                    for(i=3;i<ss.length;i++){
                        if(i>3){
                            ext2 +=" ";
                        }
                        ext2 += ss[i];
                    }
                    cz.setExt2(ext2);

                    //logger.debug("[{},{},{},{}]", cz.getIpStart(), cz.getIpEnd(), cz.getExt1(), cz.getExt2());

                    execute("INSERT INTO CZ88(ipStart,ipEnd,ext1,ext2) VALUES(?,?,?,?)",
                            cz.getIpStart(), cz.getIpEnd(), cz.getExt1(), cz.getExt2());

                }
            }
            catch (IOException e){
                logger.error("解析IP数据库出错", e);
            }
    }

    @Override
    public CZ88 search(String ip) {
        //TODO
        return null;
    }

    @Override
    public void clear() {
        execute("DELETE FROM CZ88");
    }

    @PostConstruct
    void init() {
        install("CZ88",
                longIdColumn(),
                charsColumn("ipStart", 15, true),
                charsColumn("ipEnd", 15, true),
                stringColumn("ext1", 255, true, false),
                stringColumn("ext2", 255, false, false)
                );
    }

    private final static Logger logger = LoggerFactory.getLogger(CZ88HelperImpl.class);
}
