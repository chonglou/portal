package com.odong.portal.job;

import com.odong.portal.util.DBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mysqldump -u user -p database | gzip -9 > database.sql.gz
 * gunzip < database.sql.gz | mysql -u user -p database
 * <p/>
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午12:32
 */
@Component("backupJobTarget")
public class BackupJob {
    public void execute() {
        dbHelper.backup();
    }

    @Resource
    private DBHelper dbHelper;
    private final static Logger logger = LoggerFactory.getLogger(BackupJob.class);

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
