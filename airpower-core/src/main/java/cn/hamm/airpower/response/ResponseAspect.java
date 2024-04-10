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
    public <M extends RootModel<M>> Object responseFilter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> returnCls = method.getReturnType();
        Object result = proceedingJoinPoint.proceed();
        if (!JsonData.class.equals(returnCls)) {
            // 返回不是JsonData 原样返回
            return result;
        }
        Filter filter = method.getAnnotation(Filter.class);
        if (Objects.isNull(filter)) {
            return result;
        }
        JsonData jsonData = (JsonData) result;
        Class<?> dataCls = jsonData.getData().getClass();

        if (ArrayList.class.equals(dataCls)) {
            // 如果 data 是 Model列表
            //noinspection unchecked
            List<M> list = (List<M>) jsonData.getData();
            jsonData.setData(filterResponseListBy(filter, list));
            return jsonData;
        }
        if (QueryPageResponse.class.equals(dataCls)) {
            // 如果 data 分页对象
            //noinspection unchecked
            QueryPageResponse<M> queryPageResponse = (QueryPageResponse<M>) jsonData.getData();
            List<M> list = queryPageResponse.getList();
            filterResponseListBy(filter, list);
            jsonData.setData(queryPageResponse.setList(list));
            return jsonData;
        }
        if (ReflectUtil.isModel(dataCls)) {
            // 如果 data 是 Model
            //noinspection unchecked
            jsonData.setData(filterResponseBy(filter, (M) jsonData.getData()));
        }

        // 其他数据 原样返回
        return jsonData;
    }

    /**
     * <h2>使用指定的过滤器过滤数据</h2>
     *
     * @param filter 过滤器
     * @param data   数据
     * @return 过滤后的数据
     */
    private <M extends RootModel<M>> M filterResponseBy(Filter filter, M data) {
        // 如果 responseFilter 是空 使用Void类进行转换
        return data.filterResponseDataBy(Objects.isNull(filter) ? Void.class : filter.value());
    }

    /**
     * <h2>使用指定的过滤器过滤数据列表</h2>
     *
     * @param filter 过滤器
     * @param list   数据列表
     * @return 列表
     */
    private <M extends RootModel<M>> List<M> filterResponseListBy(Filter filter, List<M> list) {
        try {
            for (M item : list) {
                filterResponseBy(filter, item);
            }
        } catch (Exception ignored) {
        }
        return list;
    }
}
