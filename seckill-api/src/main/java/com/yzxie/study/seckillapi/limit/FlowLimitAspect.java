package com.yzxie.study.seckillapi.limit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.yzxie.study.seckillcommon.exception.ApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description: 当前部署实例的全局限流
 **/
@Aspect
@Component
@Order(1)
// Order序号越小，优先级越高
public class FlowLimitAspect {

    private Map<String, RateLimiter> uriLimiterMap = new HashMap<>();

    private LoadingCache<String, RateLimiter> uuidLimiterMap;

    @Value("flow.uuid.limit")
    private String uuidLimit;

    @Value("flow.uris")
    private String uriList;

    @PostConstruct
    public void init() {
        // 初始化uri的limiter
        if (uriList != null) {
            String[] uris = uriList.split(",");
            for (String uri : uris) {
                // 每个uri每秒最多接收10000个请求，也可以优化为每个uri不一样
                uriLimiterMap.put(uri, RateLimiter.create(10000));
            }
        }
        // 初始化uuid的limiter
        uuidLimiterMap = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<String, RateLimiter>() {
                    @Override
                    public RateLimiter load(String s) throws Exception {
                        // 每个新的uuid，每秒只发出 uuidLimit 个令牌，即每秒只能发送 uuidLimit 个请求
                        return RateLimiter.create(Integer.valueOf(uuidLimit));
                    }
                });
    }

    @Pointcut("@annotation(com.yzxie.study.seckillapi.limit.FlowLimit)")
    public void flowLimitAspect() {}

    @Around("flowLimitAspect()")
    public Object limit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        FlowLimit flowLimit = method.getAnnotation(FlowLimit.class);
        if (flowLimit != null) {
            HttpServletRequest request = getCurrentRequest();
            String uri = request.getRequestURI();
            String uuid = request.getHeader("uuid");
            RateLimiter uriLimiter = uriLimiterMap.get(uri);
            // uri
            if (uriLimiter != null) {
                boolean allow = uriLimiter.tryAcquire();
                if (!allow) {
                    throw new ApiException("抢购人数太多，请稍后再试");
                }
            }
            // uuid
            if (uuid != null) {
                RateLimiter uuidLimiter = uuidLimiterMap.get(uuid);
                boolean allow = uuidLimiter.tryAcquire();
                if (!allow) {
                    throw new ApiException("抢购人数太多，请稍后再试");
                }
            }
        }

        // 继续执行
        Object result = proceedingJoinPoint.proceed();
        return result;
    }

    private HttpServletRequest getCurrentRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
}
