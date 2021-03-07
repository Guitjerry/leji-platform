package com.macro.mall.common.constant;

import cn.hutool.core.codec.Base32;

/**
 * 权限相关常量定义
 * Created by macro on 2020/6/19.
 */
public interface AuthConstant {

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 后台管理client_id
     */
    String ADMIN_CLIENT_ID = "admin-app";

    /**
     * 前台商城client_id
     */
    String PORTAL_CLIENT_ID = "portal-app";

    /**
     * 后台管理接口路径匹配
     */
    String ADMIN_URL_PATTERN = "/mall-admin/**";

    /**
     * Redis缓存权限规则key
     */
    String RESOURCE_ROLES_MAP_KEY = "auth:resourceRolesMap";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 用户信息Http请求头
     */
    String USER_TOKEN_HEADER = "user";

    String appId = "wx80a2e9b5aa4a7050";//base32加密

    String secret = "c6722ec744ae5937373ad386d36a905e";//base32加密

    public static void main(String[] args) {
        String aes = Base32.encode("528346fcccaa1895befa81c86923305e");
        System.out.println("key.." + aes.toString());

        String password = new String(Base32.decode(aes));
        System.out.println("password.." + password.toString());
    }

}
