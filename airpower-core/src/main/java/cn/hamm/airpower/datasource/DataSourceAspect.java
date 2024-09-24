package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.ServiceConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <h1>数据源切面</h1>
 *
 * @author Hamm.cn
 */
@Aspect
@Component
public class DataSourceAspect {
    @Autowired
    private ServiceConfig serviceConfig;

    @SuppressWarnings("EmptyMethod")
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointCut() {

    }

    /**
     * <h2>多数据源切面方法</h2>
     */
    @Around("pointCut()")
    public Object multipleDataSource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest();
        String database = request.getHeader(serviceConfig.getTenantHeader());
        if (!StringUtils.hasText(database)) {
            return proceedingJoinPoint.proceed();
        }
        DataSourceResolver.setDataSourceParam(database);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DataSourceResolver.clearDataSourceParam();
        }
    }
}

