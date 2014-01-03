package com.odong.core.service.impl;

import com.odong.core.entity.Task;
import com.odong.core.service.TaskService;
import com.odong.core.store.JdbcHelper;
import com.odong.core.util.TimeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-30下午7:36.
 */
@Service("core.taskService")
public class TaskServiceImpl extends JdbcHelper implements TaskService {
    @Override
    public void setTaskRequest(long task, String request) {
        execute("UPDATE Tasks SET request=?,version=version+1 WHERE id=?", request,task);
    }

    @Override
    public void addTask(String module, String type, String request, Date begin, int space, int total) {
        addTask(module, type, request, begin, timeHelper.max(), timeHelper.plus(begin, space), space, total);
    }

    @Override
    public void addTask(String module, String type, String request, int clock) {
        addTask(module, type, request, new Date(), timeHelper.max(), timeHelper.nextDay(clock), 24 * 60 * 60, 0);
    }

    @Override
    public void addTask(String module, String type, String request, Date begin, Date end, int space) {
        addTask(module, type, request, begin, end, timeHelper.plus(begin, space), space, 0);
    }

    @Override
    public String getTaskRequest(long task) {
        return select("SELECT request_ FROM Tasks WHERE id=?", new Object[]{task}, String.class);
    }

    @Override
    public List<Task> listRunnableTask() {
        return list("SELECT * FROM Tasks WHERE nextRun_<=?", new Object[]{new Date()}, mapperTask());
    }

    @Override
    public void setTaskNextRun(long task) {
        Task t = getTask(task);
        Date now = new Date();
        Date nextRun = timeHelper.plus(now, t.getSpace());
        if (nextRun.compareTo(t.getEnd()) >= 0 || (t.getTotal() != 0 && t.getIndex() + 1 >= t.getTotal())) {
            nextRun = timeHelper.max();
        }
        execute("UPDATE Tasks SET index_=index_+1,nextRun_=? WHERE id=?", nextRun, task);
    }

    @Override
    public Task getTask(long task) {
        return select("SELECT * FROM Tasks WHERE id=?", new Object[]{task}, mapperTask());
    }


    @PostConstruct
    void init() {
        install("Tasks",
                longIdColumn(),
                charsColumn("module_", 12, false),
                enumColumn("type_"),
                textColumn("request_", true),
                dateColumn("begin_", true),
                dateColumn("end_", true),
                dateColumn("nextRun_", true),
                longColumn("index_", true),
                longColumn("total_", true),
                intColumn("space_", true),
                versionColumn(),
                dateColumn("created_", true)
        );

    }

    private void addTask(String module, String type, String request, Date begin, Date end, Date nextRun, int space, long total) {
        execute("INSERT INTO Tasks(module_,type_,request_,begin_,end_,nextRun_,space_,total_,created_) VALUES(?,?,?,?,?,?,?,?,?)",
                module, type, request, begin, end, nextRun, space, total, new Date());
    }


    private RowMapper<Task> mapperTask() {
        return (ResultSet rs, int i) -> {
            Task t = new Task();
            t.setId(rs.getLong("id"));
            t.setModule(rs.getString("module_"));
            t.setRequest(rs.getString("request_"));
            t.setType(rs.getString("type_"));
            t.setBegin(rs.getTimestamp("begin_"));
            t.setEnd(rs.getTimestamp("end_"));
            t.setNextRun(rs.getTimestamp("nextRun_"));
            t.setIndex(rs.getLong("index_"));
            t.setTotal(rs.getLong("total_"));
            t.setSpace(rs.getInt("space_"));
            t.setCreated(rs.getTimestamp("created_"));
            return t;
        };
    }

    @Resource
    private TimeHelper timeHelper;

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

}
