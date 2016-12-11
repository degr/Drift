package org.forweb.drift.utils;

import org.forweb.drift.dto.UserDetail;
import org.forweb.drift.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserUtils {

    public static User getUser(){
        UserDetail userDetail = getUserDetail();
        if(userDetail != null) {
            return userDetail.getUser();
        } else {
            return null;
        }
    }
    public static UserDetail getUserDetail(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            return principal instanceof UserDetail ? (UserDetail) principal : null;
        }
        return null;
    }


}
