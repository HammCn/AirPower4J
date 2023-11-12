package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.api.Api;
import cn.hamm.airpower.api.Extends;
import cn.hamm.airpower.query.QueryPageRequest;
import cn.hamm.airpower.query.QueryRequest;
import cn.hamm.airpower.response.ResponseFilter;
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
 * @noinspection SpringJavaInjectionPointsAutowiringInspection
 */
@Permission
public class RootEntityController<E extends RootEntity<E>, S extends RootService<E, R>, R extends RootRepository<E>> extends RootController {
    @Autowired
    protected S service;

    @Description("添加")
    @PostMapping("add")
    @ResponseFilter(RootEntity.WhenGetDetail.class)
    public JsonData add(@RequestBody @Validated(RootEntity.WhenAdd.class) E entity) {
        checkApiAvailableStatus(Api.Add);
        entity = beforeAdd(entity.toEntity());
        entity = service.add(entity);
        entity = afterAdd(entity);
        return jsonData(entity, "创建成功");
    }

    @Description("删除")
    @PostMapping("delete")
    public Json delete(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Delete);
        service.deleteById(entity.getId());
        return json("删除成功");
    }

    @Description("强制删除")
    @PostMapping("forceDelete")
    public Json forceDelete(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.ForceDelete);
        deleteRelationData(entity.getId());
        service.deleteById(entity.getId());
        return json("强制删除数据成功");
    }

    @Description("修改")
    @PostMapping("update")
    @ResponseFilter(RootEntity.WhenGetDetail.class)
    public JsonData update(@RequestBody @Validated(RootEntity.WhenUpdate.class) E entity) {
        checkApiAvailableStatus(Api.Update);
        entity = beforeUpdate(entity.toEntity());
        entity = service.update(entity);
        entity = afterUpdate(entity);
        return jsonData(entity, "修改成功");
    }

    @Description("查询详情")
    @PostMapping("getDetail")
    @ResponseFilter(RootEntity.WhenGetDetail.class)
    public JsonData getDetail(@RequestBody @Validated(RootEntity.WhenIdRequired.class) E entity) {
        checkApiAvailableStatus(Api.GetDetail);
        E existEntity = service.getById(entity.getId());
        return jsonData(afterGetDetail(existEntity));
    }

    @Description("分页查询")
    @PostMapping("getPage")
    @ResponseFilter(RootEntity.WhenGetPage.class)
    public JsonData getPage(@RequestBody QueryPageRequest<E> queryPageData) {
        checkApiAvailableStatus(Api.GetPage);
        return jsonData(service.getPage(queryPageData));
    }

    @Description("禁用")
    @PostMapping("disable")
    public Json disable(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Disable);
        service.disableById(entity.getId());
        return json("禁用成功");
    }

    @Description("启用")
    @PostMapping("enable")
    public Json enable(@RequestBody @Validated({RootEntity.WhenIdRequired.class}) E entity) {
        checkApiAvailableStatus(Api.Enable);
        service.enableById(entity.getId());
        return json("启用成功");
    }

    @Description("不分页查询")
    @PostMapping("getList")
    @ResponseFilter(RootEntity.WhenGetList.class)
    public JsonData getList(@RequestBody QueryRequest<E> queryRequest) {
        checkApiAvailableStatus(Api.GetList);
        return jsonData(service.getList(queryRequest));
    }

    /**
     * <h2>查询详情后置方法</h2>
     *
     * @param entity 实体
     * @return 实体
     */
    protected E afterGetDetail(E entity) {
        return entity;
    }

    /**
     * <h2>强制删除前删除关联的数据</h2>
     *
     * @param id ID
     */
    @SuppressWarnings("unused")
    protected void deleteRelationData(Long id) {
        Result.SERVICE_NOT_FOUND.show();
    }

    /**
     * <h2>新增前置方法</h2>
     *
     * @param entity 实体
     * @return 实体
     */
    protected E beforeAdd(E entity) {
        return entity;
    }

    /**
     * <h2>新增后置方法</h2>
     *
     * @param entity 实体
     * @return 实体
     */
    protected E afterAdd(E entity) {
        return entity;
    }

    /**
     * <h2>修改前置方法</h2>
     *
     * @param entity 实体
     * @return 实体
     */
    protected E beforeUpdate(E entity) {
        return entity;
    }

    /**
     * <h2>修改后置方法</h2>
     *
     * @param entity 实体
     * @return 实体
     */
    protected E afterUpdate(E entity) {
        return entity;
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

        Result.SERVICE_NOT_FOUND.show("该接口暂未提供");
    }
}