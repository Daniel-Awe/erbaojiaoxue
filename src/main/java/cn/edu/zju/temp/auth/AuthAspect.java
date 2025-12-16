package cn.edu.zju.temp.auth;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.edu.zju.temp.config.MybatisContext;
import cn.edu.zju.temp.constant.TokenRelayConstant;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtOperator jwtOperator;

    @Autowired
    private MybatisContext mybatisContext;

    @Around("@annotation(cn.edu.zju.temp.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        try {
            // 1. 从header里面获取token
            HttpServletRequest request = getHttpServletRequest();

            String token = request.getHeader("X-Token");
            // 2. 检查是否超级管理员
            if (!token.equals(TokenRelayConstant.SUPERADMIN)) {

                // 3. 校验token是否合法&是否过期；如果不合法或已过期直接抛异常；如果合法放行
                Boolean isValid = jwtOperator.validateToken(token);
                if (!isValid) {
                    throw new SecurityException("Token不合法！");
                }

                // 4. 如果校验成功，那么就将用户的信息设置到request的attribute里面
                Claims claims = jwtOperator.getClaimsFromToken(token);
                Integer userId = (Integer) claims.get("userId");
                request.setAttribute("userId", userId);
                request.setAttribute("username", claims.get("username"));
                request.setAttribute("roleId", claims.get("roleId"));
                mybatisContext.setCurrentTenantId(userId);
            }
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法");
        }
        return point.proceed();
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }
}