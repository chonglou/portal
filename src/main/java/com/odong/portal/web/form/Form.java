package com.odong.portal.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:25
 */
public class Form implements Serializable {
    public void addField(Field field){
        this.fields.add(field);
    }
    public void addButton(Button button){
        this.buttons.add(button);
    }
    public Form(String id, String title, String action) {
        this.id = id;
        this.title = title;
        this.action = action;
        this.fields = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.data = new ArrayList<>();
    }

    private static final long serialVersionUID = -3941326971007776611L;
    private String id;
    private String action;
    private String title;
    private List<Field> fields;
    private List<Button> buttons;
    private boolean captcha;
    private boolean ok;
    private List<String> data;

    public boolean isCaptcha() {
        return captcha;
    }

    public void setCaptcha(boolean captcha) {
        this.captcha = captcha;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }
}
