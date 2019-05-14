//package com.mingxun.warehouse.management.config;
//
//import SysUserDao;
//import com.mingxun.warehouse.management.model.sys.SysUser;
//import Md5Util;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 认证
//
// */
//@Component
//public class UserRealm extends AuthorizingRealm {
//	@Autowired
//	private SysUserDao sysUserDao;
//
//    /**
//     * 授权(验证权限时调用)
//     */
//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		return  null;
//	}
//
//	/**
//	 * 认证(登录时调用)
//	 */
//	@Override
//	protected AuthenticationInfo doGetAuthenticationInfo(
//			AuthenticationToken authcToken) throws AuthenticationException {
//		 String username = (String) authcToken.getPrincipal();
//		String password = new String((char[]) authcToken.getCredentials());
//		//查询用户信息
//        SysUser user = sysUserDao.selectByPhone(username);
//
//
//        //账号不存在
//        if(user == null) {
//            throw new UnknownAccountException("此账号不存在");
//        }
//
//		String oldPward = Md5Util.stringMD5(password);
//
//        //密码错误
//        if(!oldPward.equals(user.getPassword())) {
//            throw new IncorrectCredentialsException("账号或密码不正确");
//
//        }
//
//		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
//        return info;
//	}
//
//}
