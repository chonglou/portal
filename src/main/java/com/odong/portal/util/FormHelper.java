package com.odong.portal.util;

import com.google.code.kaptcha.Constants;
import com.odong.portal.model.ResponseItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:02
 */
@Component
public class FormHelper {
    public ResponseItem check(BindingResult result){

        ResponseItem ri = new ResponseItem();
        for (ObjectError error : result.getAllErrors()) {
            ri.add(error.getDefaultMessage());
        }
        setOk(ri);
        return ri;
    }
    public ResponseItem check(BindingResult result, HttpServletRequest request, boolean captcha) {
        ResponseItem ri = check(result);
        if (captcha) {
            String captchaS = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            String captchaR = request.getParameter("captcha");
            if (!StringUtils.equals(captchaS, captchaR)) {
                ri.add("验证码输入不正确");
            }
        }

        setOk(ri);
        return ri;
    }
    private void setOk(ResponseItem ri){
        if (ri.getData().size() == 0) {
            ri.setOk(true);
        }

    }

}
