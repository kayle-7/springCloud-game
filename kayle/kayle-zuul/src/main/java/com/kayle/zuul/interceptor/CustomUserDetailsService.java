//package com.kayle.zuul.interceptor;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * @author : zx
// * create at:  2019-03-24  20:52
// * @description:
// */
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        UserDetail userDetail = new UserDetail();
//
//        if ("huang".equals(s)) {
//            Permission permission = new Permission();
//            permission.setPerCode("user:edit");
//            List<Permission> plist = new ArrayList<>();
//            plist.add(permission);
//
//            Role role = new Role();
//            role.setRoleCode("admin");
//            role.setPermissions(plist);
//
//            List<Role> roleList = new ArrayList<>();
//            roleList.add(role);
//            userDetail.setRoles(roleList);
//            userDetail.setUserName(s);
//            userDetail.setPassWord(BCryptUtil.encode("123456"));
//        } else {
//            throw new UsernameNotFoundException(s);
//        }
//
//        return userDetail;
//    }
//}