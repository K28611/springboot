package me.k28611.manage.utils;

import me.k28611.manage.annotation.JwtIgnore;
import me.k28611.manage.entity.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    @Autowired
    Audience audience;
    public static String lastToken = "";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","*");
        response.addHeader("Access-Control-Max-Age","100");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Allow-Credentials","false");
        logger.info(request.getRequestURI());
        final String authHeader = request.getHeader(JwtTokenUtils.AUTH_HEADER_KEY);

        logger.info(request.getSession().toString());
        logger.info("## authHeader= {}",authHeader);
        //忽略带JwtIgnore注解的请求,不做后续验证
        //放行小绿叶
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if (jwtIgnore != null)
                return true;
            else if (StringUtils.isEmpty(authHeader)||!authHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)){
                logger.info("##用户未登陆，请先登陆##");
                return false;
            }
        }
        if(request.getRequestURI().equals("/favicon.ico")){
            return true;
        }
        if(HttpMethod.OPTIONS.equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }


        return super.preHandle(request, response, handler);
    }
}
