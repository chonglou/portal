package com.odong.core.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by flamen on 13-12-30上午2:30.
 */
@Component("core.stringHelper")
public class StringHelper {
    public String underScore2CamelCase(String strs) {
        String[] ss = strs.split("_");
        for (int i = 0; i < ss.length; i++) {
            ss[i] = ss[i].toLowerCase();
            if (i != 0) {
                String elem = ss[i];
                char first = elem.toCharArray()[0];
                ss[i] = "" + (char) (first - 32) + elem.substring(1);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String e : ss) {
            sb.append(e);
        }
        return sb.toString();
    }

    public String camelCase2Underscore(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            sb.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == sb.charAt(0)) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public String random(int len) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }
        return sb.toString();
    }

    @PostConstruct
    public void init() {
        random = new Random();
    }

    private Random random;
    private static final char SEPARATOR = '_';
}
