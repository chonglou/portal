package com.odong.portal.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:41
 */
public class RadioField<T> extends Field<T> {

    public RadioField(String id, String label, T value) {
        super(id, label, "radio", value);
        this.options = new ArrayList<>();
    }

    public void addOption(String label, T value) {
        Option option = new Option();
        option.setValue(value);
        option.setLabel(label);
        this.options.add(option);
    }

    private static final long serialVersionUID = 8218621869957653686L;

    public class Option implements Serializable {

        private static final long serialVersionUID = -2068326659499622098L;
        private T value;
        private String label;

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
    }

    private List<Option> options;

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
