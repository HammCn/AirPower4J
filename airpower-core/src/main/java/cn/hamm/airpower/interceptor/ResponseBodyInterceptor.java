package cn.hamm.airpower.interceptor;

import cn.hamm.airpower.annotation.Filter;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.model.query.QueryPageResponse;
import cn.hamm.airpower.root.RootModel;
import cn.hamm.airpower.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <h1>全局拦截响应</h1>
 *
 * @author Hamm.cn
 */
@ControllerAdvice
@Slf4j
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object> {
    @Contract(pure = true)
    @Override
    public final boolean supports(
            @NotNull MethodParameter returnType,
            @NotNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true;
    }

    @Override
    public final Object beforeBodyWrite(
            Object body,
            @NotNull MethodParameter returnType,
            @NotNull MediaType selectedContentType,
            @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response
    ) {
        Method method = (Method) getShareData(AbstractRequestInterceptor.REQUEST_METHOD_KEY);
        if (Objects.isNull(method)) {
            return beforeResponseFinished(body, request, response);
        }
        return beforeResponseFinished(getResult(body, method), request, response);
    }

    @Contract("null, _ -> null")
    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    private <M extends RootModel<M>> Object getResult(Object result, Method method) {
        if (!(result instanceof Json json)) {
            // 返回不是JsonData 原样返回
            return result;
        }

        if (Objects.isNull(json.getData())) {
            return result;
        }

        Filter filter = Utils.getReflectUtil().getAnnotation(Filter.class, method);
        if (Objects.isNull(filter)) {
            return result;
        }

        if (Void.class.equals(filter.value())) {
            return result;
        }

        if (json.getData() instanceof QueryPageResponse) {
            QueryPageResponse<M> queryPageResponse = (QueryPageResponse<M>) json.getData();
            // 如果 data 分页对象
            filterResponseListBy(filter, queryPageResponse.getList());
            return json.setData(queryPageResponse);
        }

        Class<?> dataCls = json.getData().getClass();
        if (json.getData() instanceof Collection) {
            Collection<M> collection = Utils.getCollectionUtil().getCollectWithoutNull(
                    (Collection<M>) json.getData(), dataCls
            );
            return json.setData(filterResponseListBy(filter, collection.stream().toList()));
        }
        if (Utils.getReflectUtil().isModel(dataCls)) {
            // 如果 data 是 Model
            //noinspection unchecked
            return json.setData(filterResponseBy(filter, (M) json.getData()));
        }

        // 其他数据 原样返回
        return json;
    }

    /**
     * <h2>响应结束前置方法</h2>
     *
     * @param body 响应体
     * @return 响应体
     * @apiNote 如无其他操作，请直接返回<code>body</code>参数即可
     */
    @SuppressWarnings("unused")
    protected Object beforeResponseFinished(Object body, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }

    /**
     * <h2>获取共享数据</h2>
     *
     * @param key KEY
     * @return VALUE
     */
    protected final @Nullable Object getShareData(String key) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        return requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
    }


    /**
     * <h2>使用指定的过滤器过滤数据</h2>
     *
     * @param filter 过滤器
     * @param data   数据
     * @return 过滤后的数据
     */
    private <M extends RootModel<M>> M filterResponseBy(@NotNull Filter filter, @NotNull M data) {
        // 如果 responseFilter 是空 使用Void类进行转换
        return data.filterResponseDataBy(filter.value());
    }

    /**
     * <h2>使用指定的过滤器过滤数据列表</h2>
     *
     * @param filter 过滤器
     * @param list   数据列表
     * @return 列表
     */
    @Contract("_, _ -> param2")
    private <M extends RootModel<M>> List<M> filterResponseListBy(@NotNull Filter filter, List<M> list) {
        try {
            list.forEach(item -> filterResponseBy(filter, item));
        } catch (java.lang.Exception exception) {
            log.error(exception.getMessage(), exception);
        }
        return list;
    }
}
