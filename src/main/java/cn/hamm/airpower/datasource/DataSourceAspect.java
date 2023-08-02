package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.GlobalConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>数据源切面</h1>
 *
 * @author Hamm
 */
@Aspect
@Component
public class DataSourceAspect {
    @Autowired
    DataSourceResolver dataSourceResolver;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointCut() {

    }

    /**
     * <h1>多数据源切面方法</h1>
     */
    @Around("pointCut()")
    public Object multipleDataSource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String database = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String serverName = request.getServerName();
            if ("".equalsIgnoreCase(GlobalConfig.apiRootDomain)) {
                Pattern pattern = Pattern.compile("(.*?)" + GlobalConfig.apiRootDomain);
                Matcher matcher = pattern.matcher(serverName);
                if (matcher.find()) {
                    database = GlobalConfig.databasePrefix + matcher.group().replaceAll("." + GlobalConfig.apiRootDomain, "");
                }
            }
        } catch (Exception exception) {
            return proceedingJoinPoint.proceed();
        }
        if (Objects.nonNull(database)) {
            DataSourceResolver.setDataSourceParam(database);
        }
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DataSourceResolver.clearDataSourceParam();
        }

    }
}

