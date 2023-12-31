package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.annotation.Search;
import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.interfaces.ITree;
import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.query.QueryPageRequest;
import cn.hamm.airpower.query.QueryPageResponse;
import cn.hamm.airpower.query.QueryRequest;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import cn.hamm.airpower.security.SecurityUtil;
import cn.hamm.airpower.util.ReflectUtil;
import cn.hamm.airpower.util.redis.RedisUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.persistence.Column;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * <h1>服务根类</h1>
 *
 * @param <E> 实体
 * @param <R> 数据源
 * @author Hamm
 */
@SuppressWarnings({"unchecked", "SpringJavaInjectionPointsAutowiringInspection"})
@Slf4j
public class RootService<E extends RootEntity<E>, R extends RootRepository<E>> {
    @Autowired
    protected R repository;

    /**
     * 当前请求的实例
     */
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected RedisUtil<E> redisUtil;

    @Autowired
    protected SecurityUtil secureUtil;

    /**
     * 添加一条数据(自定义请重写)
     *
     * @param entity 保存的实体
     * @return 保存后的实体
     * @see #beforeSaveToDatabase(RootEntity)
     */
    public E add(E entity) {
        return addToDatabase(entity);
    }

    /**
     * 修改一条已经存在的数据(自定义请重写)
     *
     * @param entity 保存的实体
     * @return 更新后的实体
     * @see #beforeSaveToDatabase(RootEntity)
     */
    public E update(E entity) {
        return saveToDatabase(entity);
    }

    /**
     * 不分页查询数据(自定义请重写)
     *
     * @param queryRequest 请求的request
     * @return List数据
     * @see #afterGetList(List)
     * @see #beforeGetList(QueryRequest)
     */
    public List<E> getList(QueryRequest<E> queryRequest) {
        if (Objects.isNull(queryRequest)) {
            queryRequest = new QueryRequest<>();
        }
        if (Objects.isNull(queryRequest.getFilter())) {
            queryRequest.setFilter(getNewInstance());
        }
        queryRequest = beforeGetList(queryRequest);
        List<E> list = repository.findAll(createSpecification(queryRequest), createSort(queryRequest));
        return afterGetList(list);
    }

    /**
     * 分页查询数据(自定义请重写)
     *
     * @param queryPageRequest 请求的request对象
     * @return 分页查询列表
     * @see #afterGetPage(QueryPageResponse)
     * @see #beforeGetPage(QueryRequest)
     */
    public QueryPageResponse<E> getPage(QueryPageRequest<E> queryPageRequest) {
        if (Objects.isNull(queryPageRequest)) {
            queryPageRequest = new QueryPageRequest<>();
        }
        if (Objects.isNull(queryPageRequest.getFilter())) {
            queryPageRequest.setFilter(getNewInstance());
        }
        queryPageRequest = beforeGetPage(queryPageRequest);

        QueryPageResponse<E> queryPageResponse = getResponsePageList(repository.findAll(createSpecification(queryPageRequest), createPageable(queryPageRequest)))
                .setSort(queryPageRequest.getSort());
        return afterGetPage(queryPageResponse);
    }

    /**
     * 分页查询前置方法
     *
     * @param sourceRequestData 原始请求的数据
     * @return 处理后的请求数据
     * @see #getPage(QueryPageRequest)
     */
    protected <T extends QueryRequest<E>> T beforeGetPage(T sourceRequestData) {
        return sourceRequestData;
    }

    /**
     * 分页查询后置方法
     *
     * @param queryPageResponse 查询到的数据
     * @return 处理后的数据
     * @see #getPage(QueryPageRequest)
     */
    protected QueryPageResponse<E> afterGetPage(QueryPageResponse<E> queryPageResponse) {
        return queryPageResponse;
    }

    /**
     * 不分页查询前置方法
     *
     * @param queryRequest 查询条件
     * @return 处理后的查询条件
     * @see #getList(QueryRequest)
     */
    protected QueryRequest<E> beforeGetList(QueryRequest<E> queryRequest) {
        return queryRequest;
    }

    /**
     * 不分页查询后置方法
     *
     * @param list 查询到的数据
     * @return 处理后的数据
     * @see #getList(QueryRequest)
     */
    protected List<E> afterGetList(List<E> list) {
        return list;
    }

    /**
     * 查到一条数据后置方法
     *
     * @param entity 查到的数据
     * @return 实体
     * @see #getById(Long)
     * @see #getByIdMaybeNull(Long)
     */
    protected E afterGetById(E entity) {
        return entity;
    }

    /**
     * 数据库操作前的最后一次确认
     *
     * @return 当前实体
     */
    protected E beforeSaveToDatabase(E entity) {
        return entity;
    }

    /**
     * 系统级删除前置确认方法
     *
     * @param id ID
     * @see #deleteById(Long)
     */
    protected void beforeDelete(Long id) {
    }

    /**
     * 禁用指定的数据
     *
     * @param id ID
     * @return 实体
     */
    public final E disableById(Long id) {
        E entity = getById(id);
        return saveToDatabase(entity.setIsDisabled(true));
    }

    /**
     * 启用指定的数据
     *
     * @param id ID
     * @return 实体
     */
    public final E enableById(Long id) {
        E entity = getById(id);
        return saveToDatabase(entity.setIsDisabled(false));
    }

    /**
     * 删除指定的数据
     *
     * @param id ID
     * @see #beforeDelete(Long)
     */
    public final void deleteById(Long id) {
        beforeDelete(id);
        repository.deleteById(id);
        if (GlobalConfig.isCacheEnabled) {
            redisUtil.deleteEntity(getNewInstance().setId(id));
        }
    }

    /**
     * 根据ID查询对应的实体
     *
     * @param id ID
     * @return 实体
     * @see #afterGetById(RootEntity)
     * @see #getById(Long)
     */
    public final E getById(Long id) {
        Result.PARAM_MISSING.whenNull(id, "查询失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!");
        if (GlobalConfig.isCacheEnabled) {
            //如果打开了缓存，优先读取缓存
            E entity = redisUtil.getEntity(getNewInstance().setId(id));
            if (Objects.nonNull(entity)) {
                // 查到了缓存 更新缓存
                saveToCache(entity);
                entity = afterGetById(entity);
                return entity;
            }
        }
        Optional<E> optional = repository.findById(id);
        if (optional.isPresent()) {
            E entity = optional.get();
            // 更新缓存
            saveToCache(entity);
            entity = afterGetById(entity);
            return entity;
        }
        Result.NOT_FOUND.show("没有查到ID为" + id + "的" + ReflectUtil.getDescription(getEntityClass()) + "!");
        E entity = getNewInstance();
        entity = afterGetById(entity);
        return entity;
    }

    /**
     * 根据ID查询对应的实体
     *
     * @param id ID
     * @return 实体
     * @apiNote 查不到返回null，不抛异常
     * @see #afterGetById(RootEntity)
     * @see #getById(Long)
     */
    public final E getByIdMaybeNull(Long id) {
        try {
            return getById(id);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 获取当前登录用户的信息
     *
     * @return 用户ID
     */
    protected final Long getCurrentUserId() {
        try {
            String accessToken = request.getHeader(GlobalConfig.authorizeHeader);
            return secureUtil.getUserIdFromAccessToken(accessToken);
        } catch (Exception ignored) {
        }
        return 0L;
    }

    /**
     * 添加到数据库(直接保存)
     *
     * @param entity 实体
     * @return 实体
     */
    protected final E addToDatabase(E entity) {
        entity.setId(null)
                .setIsDisabled(false)
                .setCreateTime(DateUtil.current())
                .setUpdateTime(entity.getCreateTime());
        if (Objects.isNull(entity.getRemark())) {
            entity.setRemark("");
        }
        entity.setCreateUserId(getCurrentUserId());
        return saveToDatabase(entity);
    }

    /**
     * 更新到数据库(直接保存)
     *
     * @param entity 待更新的实体
     * @return 更新后的实体
     */
    @SuppressWarnings("unused")
    protected final E updateToDatabase(E entity) {
        Result.PARAM_MISSING.whenNull(entity.getId(),
                "修改失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!");
        return saveToDatabase(entity);
    }

    /**
     * 忽略只读字段
     *
     * @param entity 实体
     * @return 忽略只读字段之后的实体
     */
    protected final E ignoreReadOnlyFields(E entity) {
        List<Field> fields = ReflectUtil.getFieldList(getEntityClass());
        for (Field field : fields) {
            ReadOnly annotation = field.getAnnotation(ReadOnly.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            try {
                field.setAccessible(true);
                field.set(entity, null);
            } catch (Exception e) {
                Result.ERROR.show();
            }
        }
        return entity;
    }

    /**
     * 普通平层树数组按层级转为树结构
     *
     * @param list     普通平层数组
     * @param parentId 父级ID
     * @param <T>      类型
     * @return 层级结构的树
     */
    @SuppressWarnings("rawtypes")
    protected final <T extends ITree> List<T> list2TreeList(List<T> list, Long parentId) {
        List<T> treeList = new ArrayList<>();
        list.forEach(item -> {
            if (parentId.equals(item.getParentId())) {
                treeList.add(item);
            }
        });
        for (T t : treeList) {
            t.setChildren(list2TreeList(list, t.getId()));
        }
        return treeList;
    }

    /**
     * 普通平层树数组按层级转为树结构
     *
     * @param list 普通平层数组
     * @param <T>  类型
     * @return 层级结构的树
     */
    @SuppressWarnings("rawtypes")
    protected final <T extends ITree> List<T> list2TreeList(List<T> list) {
        return list2TreeList(list, 0L);
    }

    /**
     * 保存到数据库
     *
     * @param entity 待保存实体
     * @return 保存后的实体
     */
    private E saveToDatabase(E entity) {
        checkUnique(entity);
        entity.setUpdateTime(DateUtil.current());
        entity.setUpdateUserId(getCurrentUserId());
        if (Objects.nonNull(entity.getId())) {
            //有ID 走修改 且不允许修改下列字段
            E existEntity = getById(entity.getId());
            if (Objects.isNull(existEntity.getRemark()) && Objects.isNull(entity.getRemark())) {
                // 如果数据库是null 且 传入的也是null 签名给空字符串
                entity.setRemark("");
            }
            entity = getEntityForSave(entity, existEntity);
        }
        E target = getNewInstance();
        BeanUtils.copyProperties(entity, target);
        target = beforeSaveToDatabase(target);
        target = repository.saveAndFlush(target);
        redisUtil.deleteEntity(target);
        return target;
    }

    /**
     * 保存实体到缓存
     *
     * @param entity 待缓存的实体
     */
    private void saveToCache(E entity) {
        if (GlobalConfig.isCacheEnabled) {
            //如果打开了缓存，将查询结果在缓存中存储一份
            redisUtil.saveEntityCacheData(entity);
        }
    }

    /**
     * 获取用于更新的实体
     *
     * @param sourceEntity 来源实体
     * @param targetEntity 已存在实体
     * @return 目标实体
     */
    private E getEntityForSave(E sourceEntity, E targetEntity) {
        String[] nullProperties = getNullProperties(sourceEntity);
        BeanUtils.copyProperties(sourceEntity, targetEntity, nullProperties);
        return targetEntity;
    }

    /**
     * 判断是否唯一
     *
     * @param entity 实体
     */
    private void checkUnique(E entity) {
        List<Field> fields = ReflectUtil.getFieldList(getEntityClass());
        for (Field field : fields) {
            String fieldName = ReflectUtil.getDescription(field);
            Column annotation = field.getAnnotation(Column.class);
            if (Objects.isNull(annotation) || !annotation.unique()) {
                // 没标注解 或者标了 但不做唯一校验
                continue;
            }
            Object fieldValue = "";
            try {
                field.setAccessible(true);
                fieldValue = field.get(entity);
                if (Objects.isNull(fieldValue)) {
                    // 没有值 不校验
                    continue;
                }
                E search = getNewInstance();
                field.set(search, fieldValue);
                Example<E> example = Example.of(search);
                Optional<E> exist = repository.findOne(example);
                if (exist.isEmpty()) {
                    // 没查到 不校验
                    continue;
                }
                if (Objects.nonNull(entity.getId()) && exist.get().getId().equals(entity.getId())) {
                    // 是修改 且查到的是自己 就不校验重复了
                    continue;
                }
            } catch (Exception e) {
                Result.ERROR.show();
            }
            Result.FORBIDDEN_EXIST.show(fieldName + "(" + fieldValue.toString() + ")已经存在！");
        }
    }

    /**
     * 获取一个空实体
     *
     * @return 实体
     */
    private E getNewInstance() {
        try {
            return getEntityClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new ResultException("初始化实体失败");
        }
    }

    /**
     * 获取实体类
     *
     * @return 类
     */
    private Class<E> getEntityClass() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 获取null属性
     *
     * @param src 来源对象
     * @return 非空属性列表
     */
    private String[] getNullProperties(Object src) {
        // 获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        // 获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        // 获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (Objects.isNull(propertyValue)) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

    /**
     * 获取响应的分页数据
     *
     * @param data 分页数据
     * @return 输出分页对象
     */
    private QueryPageResponse<E> getResponsePageList(org.springframework.data.domain.Page<E> data) {
        return new QueryPageResponse<E>()
                .setList(data.getContent())
                .setTotal(Math.toIntExact(data.getTotalElements()))
                .setPageCount(data.getTotalPages())
                .setPage(
                        new Page()
                                .setPageSize(
                                        data.getPageable().getPageSize()
                                )
                                .setPageNum(
                                        data.getPageable().getPageNumber() + 1
                                )
                )
                ;
    }

    /**
     * 创建Sort
     *
     * @param queryRequest 请求的request
     * @return Sort
     */
    private Sort createSort(QueryRequest<E> queryRequest) {
        String sortField = GlobalConfig.defaultSortField;
        if (Objects.isNull(queryRequest.getSort())) {
            queryRequest.setSort(new cn.hamm.airpower.model.Sort());
        }
        if (!"".equalsIgnoreCase(queryRequest.getSort().getField())) {
            // 如果传入了Sort和字段
            sortField = queryRequest.getSort().getField();
        }
        if (!GlobalConfig.defaultSortDirection.equals(queryRequest.getSort().getDirection())) {
            return Sort.by(Sort.Order.asc(sortField));
        }
        return Sort.by(Sort.Order.desc(sortField));
    }

    /**
     * 创建Pageable
     *
     * @param queryPageData 查询请求
     * @return Pageable
     */
    private Pageable createPageable(QueryPageRequest<E> queryPageData) {
        Sort sort = createSort(queryPageData);
        Page page = new Page();
        if (!Objects.isNull(queryPageData.getPage())) {
            page = queryPageData.getPage();
        }
        int pageNumber = page.getPageNum() - 1;
        pageNumber = Math.max(0, pageNumber);
        int pageSize = queryPageData.getPage().getPageSize();
        pageSize = Math.max(1, pageSize);
        return PageRequest.of(pageNumber, pageSize, sort);
    }

    /**
     * PredicateList
     *
     * @param root    root
     * @param builder builder
     * @param search  搜索实体
     * @return 搜索条件
     */
    private List<Predicate> getPredicateList(Object root, CriteriaBuilder builder, Object search, boolean isRoot) {
        List<Predicate> predicateList = new ArrayList<>();
        List<Field> fields = ReflectUtil.getFieldList(search.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(search);
                if (Objects.isNull(fieldValue) || StrUtil.isEmpty(fieldValue.toString())) {
                    // 没有传入查询值 跳过
                    continue;
                }
                Search searchMode = field.getAnnotation(Search.class);
                if (Objects.isNull(searchMode)) {
                    // 没有配置查询注解 跳过
                    continue;
                }

                if (searchMode.value() == Search.Mode.JOIN) {
                    // Join
                    if (isRoot) {
                        Join<E, ?> payload = ((Root<E>) root).join(field.getName(), JoinType.INNER);
                        predicateList.addAll(this.getPredicateList(payload, builder, fieldValue, false));
                    }
                } else {
                    String searchValue = fieldValue.toString();
                    if (Boolean.class.equals(fieldValue.getClass())) {
                        // Boolean搜索
                        if (isRoot) {
                            predicateList.add(builder.equal(((Root<E>) root).get(field.getName()), fieldValue));
                        } else {
                            predicateList.add(builder.equal(((Join<?, ?>) root).get(field.getName()), fieldValue));
                        }
                        continue;
                    }
                    if (Search.Mode.LIKE.equals(searchMode.value())) {
                        // LIKE 模糊搜索
                        searchValue = "%" + searchValue + "%";
                        if (isRoot) {
                            predicateList.add(builder.like(((Root<E>) root).get(field.getName()), searchValue));
                        } else {
                            predicateList.add(builder.like(((Join<?, ?>) root).get(field.getName()), searchValue));
                        }
                        continue;
                    }

                    if (isRoot) {
                        predicateList.add(builder.equal(((Root<E>) root).get(field.getName()), fieldValue));
                    } else {
                        predicateList.add(builder.equal(((Join<?, ?>) root).get(field.getName()), fieldValue));
                    }
                }
            } catch (IllegalAccessException exception) {
                log.error(exception.getMessage());
            }
        }
        return predicateList;
    }

    /**
     * 创建Specification
     *
     * @param queryRequest 查询请求
     * @return Specification
     */
    private Specification<E> createSpecification(QueryRequest<E> queryRequest) {
        return (root, criteriaQuery, criteriaBuilder) ->
                createPredicate(
                        root, criteriaQuery, criteriaBuilder, queryRequest.getFilter()
                );
    }

    /**
     * 创建Predicate
     *
     * @param root          root
     * @param criteriaQuery query
     * @param builder       builder
     * @param search        搜索的实体
     * @return 查询条件
     */
    protected Predicate createPredicate(
            Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder, E search
    ) {
        List<Predicate> predicateList = this.getPredicateList(root, builder, search, true);
        Predicate[] predicates = new Predicate[predicateList.size()];
        criteriaQuery.where(builder.and(predicateList.toArray(predicates)));
        return criteriaQuery.getRestriction();
    }
}
