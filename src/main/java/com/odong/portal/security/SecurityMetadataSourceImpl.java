package com.odong.portal.security;

import com.odong.portal.service.RbacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-10
 * Time: 下午2:29
 */
@Component("securityMetadataSource")
public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        String url = request.getServletPath();
        String method = request.getMethod();

        logger.debug("请求URL[{}, {}]", url, method);

        Collection<ConfigAttribute> list = new ArrayList<>();

        ConfigAttribute ca = null;
        String[] ss = url.split("/");

        switch (ss[0]){
            case "admin":
                ca = new SecurityConfig();
                break;
            case "article":
                switch (method){
                    case "POST":
                    case "DELETE":
                        ca = new SecurityConfig();
                        break;
                }
                break;
        }

        if (ca == null) {
            return null;
        }
        list.add(ca);
        return list;

    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        /*
        Collection<ConfigAttribute> list= new ArrayList<>();
        for(String k : resourceMap.keySet()){
            list.addAll(resourceMap.get(k));
        }
        return list;
        */
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;  //
    }


    private final static Logger logger = LoggerFactory.getLogger(SecurityMetadataSourceImpl.class);

    @Resource
    private RbacService rbacService;

    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }
}
