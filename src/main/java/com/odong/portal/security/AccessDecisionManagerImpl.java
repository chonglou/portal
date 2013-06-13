package com.odong.portal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-10
 * Time: 下午2:54
 */
@Component("accessDecisionManager")
public class AccessDecisionManagerImpl implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes == null){
            return;
        }
        for(ConfigAttribute ca : configAttributes){
            String needPermission = ca.getAttribute();
            logger.debug("需要权限:"+needPermission);
            for(GrantedAuthority ga : authentication.getAuthorities()){
                if(needPermission.equals(ga.getAuthority())){
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有权限访问！");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;  //
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;  //
    }
    private final static Logger logger = LoggerFactory.getLogger(AccessDecisionManagerImpl.class);
}
