package com.odong.portal.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:45
 */
public final class CheckBoxField<T> extends Field<T> {
    public CheckBoxField(String id, String label, String tooltip) {
        super(id, label, "checkbox", null, true, tooltip);
        this.options = new ArrayList<>();
    }

    public CheckBoxField(String id, String label) {
        this(id, label, null);
    }
    public void addOption( String label, T value, boolean select) {
        Option option = new Option();
        option.setLabel(label);
        option.setValue(value);
        option.setSelect(select);
        this.options.add(option);
    }

    private static final long serialVersionUID = -1842675431343809707L;
    private List<Option> options;

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public final class Option implements Serializable {
        private static final long serialVersionUID = 6959337352835147535L;
        private T value;
        private String label;
        private boolean select;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }
    }

}
