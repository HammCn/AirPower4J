package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.api.Api;
import cn.hamm.airpower.api.Extends;
import cn.hamm.airpower.query.QueryPageRequest;
import cn.hamm.airpower.query.QueryRequest;
import cn.hamm.airpower.response.Filter;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.json.Json;
import cn.hamm.airpower.result.json.JsonData;
import cn.hamm.airpower.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.Objects;

/**
 * <h1>实体控制器根类</h1>
 *
 * @param <S> Service
 * @param <E> 实体或实体的子类
 * @author Hamm
 * @apiNote 如需自定义, 可使用前后置方法, 也可以自行重写后实现 如需继承或排除部分接口, 可使用 {@link Extends} 注解
 */
@Permission
public class RootEntityController<E extends RootEntity<E>, S extends RootService<E, R>, R extends RootRepository<E>> extends RootController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected S service;

    /**
     * 添加一条新数据接口
     *
     * @param entity 数据实体
     * @return 新增成功后的数据实体
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #beforeAdd(RootEntity) 前置方法
     * @see #afterAdd(RootEntity) 后置方法
     */
    @Description("添加")
    @PostMapping("add")
    @Filter(RootEntity.WhenGetDetail.class)
    public JsonData add(@RequestBody @Validated(RootEntity.WhenAdd.class) E entity) {
        checkApiAvailableStatus(Api.Add);
        return jsonData(afterAdd(service.add(service.ignoreReadOnlyFields(beforeAdd(entity)))), "创建成功");
    }

    /**
     * 删除一条已存在的数据接口
     *
     * @param entity 包含ID的实体
     * @return 删除结果
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #beforeDelete(RootEntity) 前置方法
     * @see #afterDelete(RootEntity) 后置方法
     */
    @Description("删除")
    @PostMapping("delete")
    public Json delete(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Delete);
        beforeDelete(entity);
        service.deleteById(entity.getId());
        afterDelete(entity);
        return json("删除成功");
    }

    /**
     * 修改一条已存在的数据接口
     *
     * @param entity 包含ID的实体
     * @return 修改后的实体数据
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #beforeUpdate(RootEntity) 前置方法
     * @see #afterUpdate(RootEntity) 后置方法
     */
    @Description("修改")
    @PostMapping("update")
    @Filter(RootEntity.WhenGetDetail.class)
    public JsonData update(@RequestBody @Validated(RootEntity.WhenUpdate.class) E entity) {
        checkApiAvailableStatus(Api.Update);
        return jsonData(afterUpdate(service.update(service.ignoreReadOnlyFields(beforeUpdate(entity)))), "修改成功");
    }

    /**
     * 查询一条详情数据
     *
     * @param entity 包含ID的实体
     * @return 详情数据
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #afterGetDetail(RootEntity) 后置方法
     */
    @Description("查询详情")
    @PostMapping("getDetail")
    @Filter(RootEntity.WhenGetDetail.class)
    public JsonData getDetail(@RequestBody @Validated(RootEntity.WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.GetDetail);
        return jsonData(afterGetDetail(service.getById(entity.getId())));
    }

    /**
     * 禁用一条已存在的数据
     *
     * @param entity 包含ID的实体
     * @return 禁用结果
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #beforeDisable(RootEntity) 前置方法
     * @see #afterDisable(RootEntity) 后置方法
     */
    @Description("禁用")
    @PostMapping("disable")
    public Json disable(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Disable);
        beforeDisable(entity);
        afterDisable(service.disableById(entity.getId()));
        return json("禁用成功");
    }

    /**
     * 启用一条已存在的数据
     *
     * @param entity 包含ID的实体
     * @return 启用结果
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     * @see #beforeEnable(RootEntity) 前置方法
     * @see #afterEnable(RootEntity) 后置方法
     */
    @Description("启用")
    @PostMapping("enable")
    public Json enable(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Enable);
        beforeEnable(entity);
        afterEnable(service.enableById(entity.getId()));
        return json("启用成功");
    }

    /**
     * 不分页查询
     *
     * @param queryRequest 查询请求
     * @return 查询结果
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     */
    @Description("不分页查询")
    @PostMapping("getList")
    @Filter(RootEntity.WhenGetList.class)
    public JsonData getList(@RequestBody QueryRequest<E> queryRequest) {
        checkApiAvailableStatus(Api.GetList);
        return jsonData(service.getList(queryRequest));
    }

    /**
     * 分页查询
     *
     * @param queryPageData 查询请求
     * @return 查询结果
     * @apiNote 可被子控制器类注解 {@link Extends} 继承或忽略
     */
    @Description("分页查询")
    @PostMapping("getPage")
    @Filter(RootEntity.WhenGetPage.class)
    public JsonData getPage(@RequestBody QueryPageRequest<E> queryPageData) {
        checkApiAvailableStatus(Api.GetPage);
        return jsonData(service.getPage(queryPageData));
    }


    /**
     * 查询详情后置方法
     *
     * @param entity 实体
     * @return 实体
     * @apiNote 可重写后执行装载更多数据的业务
     * @see #getDetail(RootEntity)
     */
    protected E afterGetDetail(E entity) {
        return entity;
    }

    /**
     * 新增前置方法
     *
     * @param entity Api请求提交的实体数据，可能会缺失很多数据
     * @return 实体
     * @apiNote 可重写后执行新增前的数据处理
     * @see #add(RootEntity)
     */
    protected E beforeAdd(E entity) {
        return entity;
    }

    /**
     * 新增后置方法
     *
     * @param entity 实体
     * @return 实体
     * @apiNote 可重写后执行新增后的其他业务
     * @see #add(RootEntity)
     */
    protected E afterAdd(E entity) {
        return entity;
    }

    /**
     * 修改前置方法
     *
     * @param entity Api请求提交的实体数据，可能会缺失很多数据
     * @return 实体
     * @apiNote 可重写后执行修改前的其他业务或拦截
     * @see #update(RootEntity)
     */
    protected E beforeUpdate(E entity) {
        return entity;
    }

    /**
     * 修改后置方法
     *
     * @param entity 实体
     * @return 实体
     * @apiNote 可重写后执行修改之后的其他业务
     * @see #update(RootEntity)
     */
    protected E afterUpdate(E entity) {
        return entity;
    }

    /**
     * 删除前置方法
     *
     * @param entity 实体
     * @apiNote 可重写后执行删除之前的业务处理或拦截
     * @see #delete(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void beforeDelete(E entity) {
    }

    /**
     * 删除后置方法
     *
     * @param entity 被删除的实体
     * @apiNote 可重写后执行删除之后的其他业务
     * @see #delete(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void afterDelete(E entity) {
    }

    /**
     * 禁用前置方法
     *
     * @param entity 带ID的实体,其他属性没有
     * @apiNote 可重写后执行禁用之前的业务处理或拦截
     * @see #disable(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void beforeDisable(E entity) {
    }

    /**
     * 禁用后置方法
     *
     * @param entity 带ID的实体,其他属性没有
     * @apiNote 可重写后执行禁用之后的其他业务
     * @see #disable(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void afterDisable(E entity) {
    }

    /**
     * 启用前置方法
     *
     * @param entity 带ID的实体,其他属性没有
     * @apiNote 可重写后执行启用之前的业务处理或拦截
     * @see #enable(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void beforeEnable(E entity) {
    }

    /**
     * 启用后置方法
     *
     * @param entity 带ID的实体,其他属性没有
     * @apiNote 可重写后执行启用之后的其他业务
     * @see #enable(RootEntity)
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    protected void afterEnable(E entity) {
    }

    /**
     * 检查Api可用状态
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

        Result.SERVICE_NOT_FOUND.show("该接口暂未提供");
    }
}