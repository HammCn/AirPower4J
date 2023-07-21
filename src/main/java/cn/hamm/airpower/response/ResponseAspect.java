package cn.hamm.airpower.response;

import cn.hamm.airpower.query.QueryPageResponse;
import cn.hamm.airpower.result.json.JsonData;
import cn.hamm.airpower.root.RootModel;
import cn.hamm.airpower.util.ReflectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <h1>API请求的响应拦截器</h1>
 *
 * @author Hamm
 */
@Aspect
@Component
public class ResponseAspect {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object responseFilter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> returnCls = method.getReturnType();
        Object result = proceedingJoinPoint.proceed();
        if (!JsonData.class.equals(returnCls)) {
            // 返回不是JsonData 原样返回
            return result;
        }
        ResponseFilter responseFilter = method.getAnnotation(ResponseFilter.class);
        JsonData jsonData = (JsonData) result;
        Class<?> dataCls = jsonData.getData().getClass();

        if (ArrayList.class.equals(dataCls)) {
            // 如果JsonData是数组
            List<RootModel<?>> list = (List<RootModel<?>>) (jsonData.getData());
            jsonData.setData(filterResponseListBy(responseFilter, list));
            return jsonData;
        }
        if (QueryPageResponse.class.equals(dataCls)) {
            // 如果JsonData是分页对象
            @SuppressWarnings("rawtypes")
            QueryPageResponse queryPageResponse = (QueryPageResponse) jsonData.getData();
            List<?> list = queryPageResponse.getList();
            filterResponseListBy(responseFilter, list);
            jsonData.setData(queryPageResponse.setList(list));
            return jsonData;
        }
        if (ReflectUtil.isModel(dataCls)) {
            // 其他 默认是模型对象 转换
            jsonData.setData(filterResponseBy(responseFilter, (RootModel<?>) jsonData.getData()));
        }
        return jsonData;
    }

    /**
     * <h1>使用指定的过滤器过滤数据</h1>
     *
     * @param responseFilter 过滤器
     * @param data           数据
     * @return 过滤后的数据
     */
    private RootModel<?> filterResponseBy(ResponseFilter responseFilter, RootModel<?> data) {
        // 如果 responseFilter 是空 使用Void类进行转换
        return data.filterResponseDataBy(Objects.isNull(responseFilter) ? Void.class : responseFilter.value());
    }

    /**
     * <h1>使用指定的过滤器过滤数据列表</h1>
     *
     * @param responseFilter 过滤器
     * @param list           数据列表
     * @return 列表
     */
    private List<?> filterResponseListBy(ResponseFilter responseFilter, List<?> list) {
        try {
            for (Object item : list) {
                Class<?> clazz = item.getClass();
                if (ReflectUtil.isModel(clazz)) {
                    filterResponseBy(responseFilter, (RootModel<?>) item);
                }
            }
        } catch (Exception exception) {
            return list;
        }
        return list;
    }
}
