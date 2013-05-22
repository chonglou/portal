package com.odong.portal.config;

import org.hibernate.cfg.DefaultNamingStrategy;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午4:05
 */
public class NamingStrategy extends DefaultNamingStrategy{
    @Override
    public String classToTableName(String className) {
        String[] ss = className.split(".");
        if(ss.length <=4 ){
            throw new IllegalArgumentException("类名不正确["+className+"]");
        }
        StringBuilder sb = new StringBuilder();
        for(int i=4; i<ss.length;){
            sb.append(ss[i]);
            i++;
            if(i == ss.length){
                break;
            }
            sb.append("_");
        }

        return encode(sb.toString());    //
    }

    @Override
    public String tableName(String tableName) {
        return "PORTAL_"+tableName;    //
    }

    @Override
    public String columnName(String columnName) {
        return columnName;    //
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        return encode(propertyName);    //
    }

    private String encode(String s){
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for(char c : s.toUpperCase().toCharArray()){
            if(c>='A' && c<='Z'){
                 c =base.charAt((base.indexOf(c)+5)%26);
            }
            else if(c != '_'){
                throw new IllegalArgumentException("只能由数字或下划线组成["+s+"]");
            }

            sb.append(c);
        }
        return sb.toString();
    }
}
