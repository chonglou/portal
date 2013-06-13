package com.odong.portal.security;

import com.odong.portal.entity.User;
import com.odong.portal.entity.rbac.Resource;
import com.odong.portal.entity.rbac.Role;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.RbacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-10
 * Time: 下午2:54
 */
@Component("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("用户[{}]", email);
        User user = accountService.getUser(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : rbacService.listRole(user.getId())){
            for(Resource r : rbacService.listResource(role.getId())){

                GrantedAuthority ga = new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return null;  //
                    }
                }
                grantedAuthorities.add(new SimpleGrantedAuthority(r.getName()));
            }
        }

        boolean enables = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
        return new org.springframework.security.core.userdetails.User(email, null, enables,accountNonExpired,credentialsNonExpired,accountNonLocked, grantedAuthorities);
    }
    private final static Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
    private AccountService accountService;
    private RbacService rbacService;

    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
