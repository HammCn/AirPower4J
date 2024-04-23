package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.api.Api;
import cn.hamm.airpower.api.Extends;
import cn.hamm.airpower.interfaces.IEntityAction;
import cn.hamm.airpower.query.QueryPageRequest;
import cn.hamm.airpower.query.QueryPageResponse;
import cn.hamm.airpower.query.QueryRequest;
import cn.hamm.airpower.response.Filter;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import cn.hamm.airpower.result.json.Json;
import cn.hamm.airpower.result.json.JsonData;
import cn.hamm.airpower.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <h1>实体控制器根类</h1>
 *
 * @param <S> Service
 * @param <E> 实体或实体的子类
 * @author Hamm.cn
 * @apiNote 提供了 {@link Extends} 处理接口黑白名单，同时提供了一些 前置/后置 方法，可被子控制器类重写(不建议)
 */
@Permission
public class RootEntityController<
        E extends RootEntity<E>,
        S extends RootService<E, R>,
        R extends RootRepository<E>> extends RootController implements IEntityAction {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected S service;

    /**
     * <h2>响应一个JsonData(只返回ID)</h2>
     *
     * @param id      主键ID
     * @param message 消息
     * @return JsonData
     * @apiNote 如需抛出异常, 直接使用 {@link Result}
     */
    protected JsonData jsonId(Long id, String message) {
        return new JsonData(new RootEntity<>().setId(id), message);
    }

    /**
     * <h2>添加一条新数据接口</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeAdd(E)
     * @see #afterAdd(long, E)
     * @see #afterSaved(long, E)
     */
    @Description("添加")
    @RequestMapping("add")
    @Filter(WhenGetDetail.class)
    public JsonData add(@RequestBody @Validated(WhenAdd.class) E entity) {
        checkApiAvailableStatus(Api.Add);
        service.ignoreReadOnlyFields(entity);
        long insertId = service.add(beforeAdd(entity));
        tryCatch(
                () -> afterAdd(insertId, entity),
                () -> afterSaved(insertId, entity)
        );
        return jsonId(insertId, "添加成功");
    }

    /**
     * <h2>修改一条已存在的数据接口</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeUpdate(E)
     * @see #afterUpdate(long, E)
     * @see #afterSaved(long, E)
     */
    @Description("修改")
    @RequestMapping("update")
    @Filter(WhenGetDetail.class)
    public JsonData update(@RequestBody @Validated(WhenUpdate.class) E entity) {
        checkApiAvailableStatus(Api.Update);
        long updateId = entity.getId();
        service.update(beforeUpdate(service.ignoreReadOnlyFields(entity)));
        tryCatch(
                () -> afterUpdate(updateId, entity),
                () -> afterSaved(updateId, entity)
        );
        return jsonId(updateId, "修改成功");
    }

    /**
     * <h2>删除一条已存在的数据接口</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeDelete(long)
     * @see #afterDelete(long)
     */
    @Description("删除")
    @RequestMapping("delete")
    public Json delete(@RequestBody @Validated(WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.Delete);
        long deleteId = entity.getId();
        beforeDelete(deleteId);
        service.delete(deleteId);
        tryCatch(
                () -> afterDelete(deleteId)
        );
        return jsonId(deleteId, "删除成功");
    }

    /**
     * <h2>查询一条详情数据</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #afterGetDetail(E)
     */
    @Description("查询详情")
    @RequestMapping("getDetail")
    @Filter(WhenGetDetail.class)
    public JsonData getDetail(@RequestBody @Validated(WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.GetDetail);
        return jsonData(afterGetDetail(service.get(entity.getId())));
    }

    /**
     * <h2>禁用一条已存在的数据</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeDisable(long)
     * @see #afterDisable(long)
     */
    @Description("禁用")
    @RequestMapping("disable")
    public Json disable(@RequestBody @Validated(WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.Disable);
        beforeDisable(entity.getId());
        service.disable(entity.getId());
        tryCatch(
                () -> afterDisable(entity.getId())
        );
        return jsonId(entity.getId(), "禁用成功");
    }

    /**
     * <h2>启用一条已存在的数据</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeEnable(long)
     * @see #afterEnable(long)
     */
    @Description("启用")
    @RequestMapping("enable")
    public Json enable(@RequestBody @Validated(WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.Enable);
        beforeEnable(entity.getId());
        service.enable(entity.getId());
        tryCatch(
                () -> afterEnable(entity.getId())
        );
        return jsonId(entity.getId(), "启用成功");
    }

    /**
     * <h2>不分页查询</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeGetList(QueryRequest)
     * @see #afterGetList(List)
     */
    @Description("不分页查询")
    @RequestMapping("getList")
    @Filter(WhenGetList.class)
    public JsonData getList(@RequestBody QueryRequest<E> queryRequest) {
        queryRequest = getQueryRequest(queryRequest);
        checkApiAvailableStatus(Api.GetList);
        return jsonData(afterGetList(service.getList(beforeGetList(queryRequest))));
    }

    /**
     * <h2>分页查询</h2>
     *
     * @apiNote 🔴可被子控制器类注解 {@link Extends} 继承或忽略，不建议重写，可使用前后置方法来处理业务逻辑。
     * @see #beforeGetPage(QueryPageRequest)
     * @see #afterGetPage(QueryPageResponse)
     */
    @Description("分页查询")
    @RequestMapping("getPage")
    @Filter(WhenGetPage.class)
    public JsonData getPage(@RequestBody QueryPageRequest<E> queryPageRequest) {
        queryPageRequest = getQueryRequest(queryPageRequest);
        checkApiAvailableStatus(Api.GetPage);
        return jsonData(afterGetPage(service.getPage(beforeGetPage(queryPageRequest))));
    }

    /**
     * <h2>查询分页后置方法</h2>
     *
     * @see #getPage(QueryPageRequest)
     */
    protected <T extends QueryPageResponse<E>> T afterGetPage(T queryPageResponse) {
        return queryPageResponse;
    }

    /**
     * <h2>查询分页前置方法</h2>
     *
     * @apiNote 可重写后重新设置查询条件
     * @see #getPage(QueryPageRequest)
     */
    protected <T extends QueryPageRequest<E>> T beforeGetPage(T queryPageRequest) {
        return queryPageRequest;
    }

    /**
     * <h2>查询不分页前置方法</h2>
     *
     * @apiNote 可重写后重新设置查询条件
     */
    protected <T extends QueryRequest<E>> T beforeGetList(T queryRequest) {
        return queryRequest;
    }

    /**
     * <h2>查询不分页后置方法</h2>
     *
     * @apiNote 可重写后执行装载更多数据的业务
     */
    protected List<E> afterGetList(List<E> list) {
        return list;
    }

    /**
     * <h2>查询详情后置方法</h2>
     *
     * @apiNote 可重写后执行装载更多数据的业务
     */
    protected E afterGetDetail(E entity) {
        return entity;
    }

    /**
     * <h2>新增前置方法</h2>
     *
     * @apiNote 可重写后执行新增前的数据处理
     */
    protected E beforeAdd(E entity) {
        return entity;
    }

    /**
     * <h2>新增后置方法</h2>
     *
     * @param id     主键ID
     * @param source 原始实体
     * @apiNote 可重写后执行新增后的其他业务
     */
    protected void afterAdd(long id, E source) {
    }

    /**
     * <h2>修改前置方法</h2>
     *
     * @param entity Api请求提交的实体数据，可能会缺失很多数据
     * @return 实体
     * @apiNote 可重写后执行修改前的其他业务或拦截
     */
    protected E beforeUpdate(E entity) {
        return entity;
    }

    /**
     * <h2>修改后置方法</h2>
     *
     * @param id     主键ID
     * @param source 原始实体
     * @apiNote 可重写后执行修改之后的其他业务
     */
    protected void afterUpdate(long id, E source) {
    }

    /**
     * <h2>保存后置方法</h2>
     *
     * @param id     主键ID
     * @param source 原始实体
     * @apiNote 新增和修改最后触发
     */
    protected void afterSaved(long id, E source) {
    }

    /**
     * <h2>删除前置方法</h2>
     *
     * @param id 主键ID
     * @apiNote 可重写后执行删除之前的业务处理或拦截
     */
    protected void beforeDelete(long id) {
    }

    /**
     * 删除后置方法
     *
     * @param id 主键ID
     * @apiNote 可重写后执行删除之后的其他业务
     */
    protected void afterDelete(long id) {
    }

    /**
     * <h2>禁用前置方法</h2>
     *
     * @param id 主键ID
     * @apiNote 可重写后执行禁用之前的业务处理或拦截
     */
    protected void beforeDisable(long id) {
    }

    /**
     * <h2>禁用后置方法</h2>
     *
     * @param id 主键ID
     * @apiNote 可重写后执行禁用之后的其他业务
     */
    protected void afterDisable(long id) {
    }

    /**
     * <h2>启用前置方法</h2>
     *
     * @param id 主键ID
     */
    protected void beforeEnable(long id) {
    }

    /**
     * <h2>启用后置方法</h2>
     *
     * @param id 主键ID
     */
    protected void afterEnable(long id) {
    }

    /**
     * <h2>获取查询请求</h2>
     *
     * @param queryRequest 传入的查询请求
     * @param <T>          QueryRequest子类
     * @return 处理后的查询请求
     */
    private <T extends QueryRequest<E>> T getQueryRequest(T queryRequest) {
        //noinspection unchecked
        queryRequest = Objects.requireNonNullElse(queryRequest, (T) new QueryRequest<E>());
        queryRequest.setFilter(Objects.requireNonNullElse(queryRequest.getFilter(), getNewInstance()));
        return queryRequest;
    }

    /**
     * <h2>检查Api可用状态</h2>
     */
    private void checkApiAvailableStatus(Api api) {
        Extends extendsApi = this.getClass().getAnnotation(Extends.class);
        if (Objects.isNull(extendsApi)) {
            // 没配置
            return;
        }
        if (extendsApi.value().length == 0 && extendsApi.exclude().length == 0) {
            // 配了个寂寞
            return;
        }
        if (extendsApi.value().length > 0 && Arrays.asList(extendsApi.value()).contains(api)) {
            // 在白名单里
            return;
        }
        if (extendsApi.exclude().length > 0 && !Arrays.asList(extendsApi.exclude()).contains(api)) {
            // 不在黑名单里
            return;
        }

        Result.API_SERVICE_UNSUPPORTED.show("该接口暂未提供");
    }

    /**
     * <h2>获取一个空实体</h2>
     *
     * @return 实体
     */
    private E getNewInstance() {
        try {
            return getEntityClass().getConstructor().newInstance();
        } catch (Exception exception) {
            throw new ResultException("初始化实体失败");
        }
    }

    /**
     * <h2>获取实体类</h2>
     *
     * @return 类
     */
    @SuppressWarnings("unchecked")
    private Class<E> getEntityClass() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}