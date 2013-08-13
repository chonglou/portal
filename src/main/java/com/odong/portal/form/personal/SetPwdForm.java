package com.odong.portal.form.personal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:57
 */
public class SetPwdForm implements Serializable {
    private static final long serialVersionUID = 8215359471021027411L;

     @NotNull
    @Size(min = 6, max = 20, message = "{val.password}")
    private String oldPwd;
    @NotNull
    @Size(min = 6, max = 20, message = "{val.password}")
    private String newPwd;
    @NotNull
    private String rePwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getRePwd() {
        return rePwd;
    }

    public void setRePwd(String rePwd) {
        this.rePwd = rePwd;
    }
}
