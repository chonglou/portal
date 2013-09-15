package com.odong.portal.model.task;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午6:16
 */
public class ClockRequest extends Request {
    public ClockRequest(int clock) {
        this.clock = clock;
    }

    @Deprecated
    public ClockRequest() {
    }

    private int clock;
    private static final long serialVersionUID = -3096498073840900646L;

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }
}
