package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.annotation.Search;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.model.Sort;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    protected RedisUtil<E> redisUtil;

    @Autowired
    protected SecurityUtil secureUtil;

    @Autowired
    private EntityManager entityManager;

    /**
     * <h2>🟢添加前置方法</h2>
     *
     * @param source 原始实体
     * @return 处理后的实体
     */
    protected E beforeAdd(E source) {
        return source;
    }

    /**
     * <h2>🟡添加一条数据</h2>
     *
     * @param source 原始实体
     * @return 保存后的主键ID
     * @see #beforeAdd(E)
     * @see #beforeSaveToDatabase(E)
     * @see #afterAdd(long, E)
     * @see #afterSaved(long, E)
     */
    public final long add(E source) {
        long id = addToDatabase(beforeAdd(source));
        afterAdd(id, source);
        return id;
    }

    /**
     * <h2>🟢添加后置方法</h2>
     *
     * @param id     主键ID
     * @param source 原始实体
     */
    protected void afterAdd(long id, E source) {
    }

    /**
     * <h2>🟢修改前置方法</h2>
     *
     * @param source 原始实体
     * @return 处理后的实体
     * @apiNote 如此处将属性设置为 <code>null</code>，实体将被保存为 <code>null</code>，其他类型的属性将不被修改
     */
    protected E beforeUpdate(E source) {
        return source;
    }

    /**
     * <h2>🟡修改一条已经存在的数据</h2>
     *
     * @param source 保存的实体
     * @apiNote 如需将非基本类型属性强制设置为 <code>null</code>，可为属性传入空实体参数，如 <code>UserEntity.createNull()</code>
     * @see #beforeUpdate(E)
     * @see #updateToDatabase(E)
     * @see #afterUpdate(long, E)
     * @see #afterSaved(long, E)
     */
    public final void update(E source) {
        source = beforeUpdate(source);
        updateToDatabase(source);
        afterUpdate(source.getId(), source);
        afterSaved(source.getId(), source);
    }

    /**
     * <h2>🟢修改后置方法</h2>
     *
     * @param id     主键ID
     * @param source 原始实体
     */
    protected void afterUpdate(long id, E source) {
    }

    /**
     * <h2>🟢保存后置方法</h2>
     *
     * @param id     主键ID
     * @param source 保存前的原数据
     * @apiNote 添加或修改后最后触发
     */
    protected void afterSaved(long id, E source) {

    }

    /**
     * <h2>🟢禁用前置方法</h2>
     *
     * @param id 主键ID
     */
    protected void beforeDisable(long id) {
    }

    /**
     * <h2>🟡禁用指定的数据</h2>
     *
     * @param id 主键ID
     * @see #beforeDisable(long)
     * @see #afterDisable(long)
     */
    public final void disable(long id) {
        beforeDisable(id);
        disableById(id);
        afterDisable(id);
    }

    /**
     * <h2>🟢禁用后置方法</h2>
     *
     * @param id 主键ID
     */
    protected void afterDisable(long id) {
    }

    /**
     * <h2>🟢启用前置方法</h2>
     *
     * @param id 主键ID
     */
    protected void beforeEnable(long id) {
    }

    /**
     * <h2>🟡启用指定的数据</h2>
     *
     * @param id 主键ID
     * @see #beforeEnable(long)
     * @see #afterEnable(long)
     */
    public final void enable(long id) {
        beforeEnable(id);
        enableById(id);
        afterEnable(id);
    }

    /**
     * <h2>🟢启用后置方法</h2>
     *
     * @param id 主键ID
     */
    protected void afterEnable(long id) {
    }

    /**
     * <h2>🟢删除前置方法</h2>
     *
     * @param id 主键ID
     */
    protected void beforeDelete(long id) {
    }

    /**
     * <h2>🟡删除指定的数据</h2>
     *
     * @param id 主键ID
     * @see #beforeDelete(long)
     * @see #afterDelete(long)
     */
    public final void delete(long id) {
        beforeDelete(id);
        deleteById(id);
        afterDelete(id);
    }

    /**
     * <h2>🟢删除后置方法</h2>
     *
     * @param id 主键ID
     */
    protected void afterDelete(long id) {
    }

    /**
     * <h2>🟢不分页查询前置方法</h2>
     *
     * @param sourceRequestData 查询条件
     * @return 处理后的查询条件
     * @see #getList(QueryRequest)
     */
    protected <T extends QueryRequest<E>> T beforeGetList(T sourceRequestData) {
        return sourceRequestData;
    }

    /**
     * <h2>🟡不分页查询数据</h2>
     *
     * @param queryRequest 请求的request
     * @return List数据
     * @see #beforeGetList(QueryRequest)
     * @see #afterGetList(List)
     */
    public final List<E> getList(QueryRequest<E> queryRequest) {
        if (Objects.isNull(queryRequest)) {
            queryRequest = new QueryRequest<>();
        }
        if (Objects.isNull(queryRequest.getFilter())) {
            queryRequest.setFilter(getNewInstance());
        }
        queryRequest = beforeGetList(queryRequest);
        List<E> list = repository.findAll(createSpecification(queryRequest.getFilter(), false), createSort(queryRequest.getSort()));
        return afterGetList(list);
    }

    /**
     * <h2>🟡过滤数据</h2>
     *
     * @param filter 全匹配过滤器
     * @return List数据
     */
    public final List<E> filter(E filter) {
        QueryRequest<E> queryRequest = new QueryRequest<>();
        if (Objects.isNull(queryRequest.getFilter())) {
            queryRequest.setFilter(filter);
        }
        return repository.findAll(createSpecification(filter, true), createSort(queryRequest.getSort()));
    }

    /**
     * <h2>🟢不分页查询后置方法</h2>
     *
     * @param list 查询到的数据
     * @return 处理后的数据
     * @see #getPage(QueryPageRequest)
     */
    protected List<E> afterGetList(List<E> list) {
        return list;
    }

    /**
     * <h2>🟢分页查询前置方法</h2>
     *
     * @param sourceRequestData 原始请求的数据
     * @return 处理后的请求数据
     */
    protected <T extends QueryPageRequest<E>> T beforeGetPage(T sourceRequestData) {
        return sourceRequestData;
    }

    /**
     * <h2>🟢分页查询后置方法</h2>
     *
     * @param queryPageResponse 查询到的数据
     * @return 处理后的数据
     */
    protected QueryPageResponse<E> afterGetPage(QueryPageResponse<E> queryPageResponse) {
        return queryPageResponse;
    }


    /**
     * <h2>🟢数据库操作前的最后一次确认</h2>
     *
     * @return 当前实体
     */
    protected E beforeSaveToDatabase(E entity) {
        return entity;
    }

    /**
     * <h2>🟢添加搜索的查询条件</h2>
     *
     * @param root    ROOT
     * @param builder 参数构造器
     * @param search  原始查询对象
     * @return 查询条件列表
     */
    protected List<Predicate> addSearchPredicate(Root<E> root, CriteriaBuilder builder, E search) {
        return new ArrayList<>();
    }

    /**
     * <h2>🟡根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     * @see #getMaybeNull(long)
     */
    public final E get(long id) {
        return afterGet(getById(id));
    }

    /**
     * <h2>🟡根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     * @apiNote 查不到返回null，不抛异常
     * @see #get(long)
     */
    public final E getMaybeNull(long id) {
        return afterGet(getByIdMaybeNull(id));
    }

    /**
     * <h2>🟢详情查询后置方法</h2>
     *
     * @param result 查到的数据
     * @return 处理后的数据
     */
    protected E afterGet(E result) {
        return result;
    }


    /**
     * <h2>🟡分页查询数据</h2>
     *
     * @param queryPageRequest 请求的request对象
     * @return 分页查询列表
     * @see #beforeGetPage(QueryPageRequest)
     * @see #afterGetPage(QueryPageResponse)
     */
    public final QueryPageResponse<E> getPage(QueryPageRequest<E> queryPageRequest) {
        if (Objects.isNull(queryPageRequest)) {
            queryPageRequest = new QueryPageRequest<>();
        }
        if (Objects.isNull(queryPageRequest.getFilter())) {
            queryPageRequest.setFilter(getNewInstance());
        }
        queryPageRequest = beforeGetPage(queryPageRequest);

        org.springframework.data.domain.Page<E> pageData = repository.findAll(createSpecification(queryPageRequest.getFilter(), false), createPageable(queryPageRequest));
        QueryPageResponse<E> queryPageResponse = getResponsePageList(pageData);
        queryPageResponse.setSort(queryPageRequest.getSort());
        return afterGetPage(queryPageResponse);
    }

    /**
     * <h2>🔴禁用指定的数据</h2>
     *
     * @param id 主键ID
     * @apiNote 不建议直接调用, 请优先使用前后置方法
     * @see #beforeDisable(long)
     * @see #afterDisable(long)
     */
    protected final void disableById(long id) {
        E entity = get(id);
        saveToDatabase(entity.setIsDisabled(true));
    }

    /**
     * <h2>🔴启用指定的数据</h2>
     *
     * @param id 主键ID
     * @apiNote 不建议直接调用, 请优先使用前后置方法
     * @see #beforeEnable(long)
     * @see #afterEnable(long)
     */
    protected final void enableById(long id) {
        E entity = get(id);
        saveToDatabase(entity.setIsDisabled(false));
    }

    /**
     * <h2>🔴删除指定的数据</h2>
     *
     * @param id 主键ID
     * @apiNote 不建议直接调用, 请优先使用前后置方法
     * @see #beforeDelete(long)
     * @see #afterDelete(long)
     */
    protected final void deleteById(long id) {
        repository.deleteById(id);
    }

    /**
     * <h2>🔴添加到数据库(直接保存)</h2>
     *
     * @param entity 实体
     * @return 实体
     * @see #beforeAdd(E)
     * @see #add(E)
     * @see #afterAdd(long, E)
     */
    protected final long addToDatabase(E entity) {
        entity.setId(null).setIsDisabled(false).setCreateTime(DateUtil.current()).setUpdateTime(entity.getCreateTime());
        if (Objects.isNull(entity.getRemark())) {
            entity.setRemark("");
        }
        return saveToDatabase(entity);
    }

    /**
     * <h2>🔴更新到数据库(直接保存)</h2>
     *
     * @param entity 待更新的实体
     * @see #beforeUpdate(E)
     * @see #afterUpdate(long, E)
     */
    protected final void updateToDatabase(E entity) {
        Result.PARAM_MISSING.whenNull(entity.getId(), "修改失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!");
        saveToDatabase(entity);
    }

    /**
     * <h2>🔴忽略只读字段</h2>
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
     * <h2>根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     */
    private E getById(long id) {
        Result.PARAM_MISSING.whenNull(id, "查询失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!");
        Optional<E> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        Result.DATA_NOT_FOUND.show("没有查到ID为" + id + "的" + ReflectUtil.getDescription(getEntityClass()) + "!");
        return getNewInstance();
    }

    /**
     * <h2>根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     * @apiNote 查不到返回null，不抛异常
     */
    private E getByIdMaybeNull(long id) {
        try {
            return get(id);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * <h2>保存到数据库</h2>
     *
     * @param entity 待保存实体
     * @return 保存后的实体
     */
    private long saveToDatabase(E entity) {
        checkUnique(entity);
        entity.setUpdateTime(DateUtil.current());
        if (Objects.nonNull(entity.getId())) {
            // 修改前清掉JPA缓存，避免查询到旧数据
            entityManager.clear();
            // 有ID 走修改 且不允许修改下列字段
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
        if (Objects.nonNull(entity.getId())) {
            return entity.getId();
        }
        // 新增完毕，清掉查询缓存，避免查询到旧数据
        entityManager.clear();
        return target.getId();
    }

    /**
     * <h2>获取用于更新的实体</h2>
     *
     * @param sourceEntity 来源实体
     * @param existEntity  已存在实体
     * @return 目标实体
     */
    private E getEntityForSave(E sourceEntity, E existEntity) {
        String[] nullProperties = getNullProperties(sourceEntity);
        BeanUtils.copyProperties(sourceEntity, existEntity, nullProperties);
        return existEntity;
    }

    /**
     * <h2>判断是否唯一</h2>
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
     * <h2>获取一个空实体</h2>
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
     * <h2>获取实体类</h2>
     *
     * @return 类
     */
    private Class<E> getEntityClass() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * <h2>获取null属性</h2>
     *
     * @param sourceEntity 来源对象
     * @return 非空属性列表
     */
    private String[] getNullProperties(E sourceEntity) {
        // 获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(sourceEntity);
        // 获取Bean的属性描述
        PropertyDescriptor[] propertyDescriptors = srcBean.getPropertyDescriptors();
        // 获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);

            // 需要强制更新为 null
            try {
                if (Objects.nonNull(propertyValue) && ((RootModel<?>) propertyValue).isNullModel()) {
                    srcBean.setPropertyValue(propertyName, null);
                    continue;
                }
            } catch (Exception ignored) {

            }

            // null 不更新到数据库 添加到忽略名单
            if (Objects.isNull(propertyValue)) {
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

    /**
     * <h2>获取响应的分页数据</h2>
     *
     * @param data 分页数据
     * @return 输出分页对象
     */
    private QueryPageResponse<E> getResponsePageList(org.springframework.data.domain.Page<E> data) {
        return new QueryPageResponse<E>().setList(data.getContent()).setTotal(Math.toIntExact(data.getTotalElements())).setPageCount(data.getTotalPages()).setPage(new Page().setPageSize(data.getPageable().getPageSize()).setPageNum(data.getPageable().getPageNumber() + 1));
    }

    /**
     * <h2>创建Sort</h2>
     *
     * @param sort 排序对象
     * @return Sort Spring的排序对象
     */
    private org.springframework.data.domain.Sort createSort(Sort sort) {
        if (Objects.isNull(sort)) {
            sort = new Sort();
        }
        if (StrUtil.isBlank(sort.getField())) {
            sort.setField(globalConfig.getDefaultSortField());
        }
        if (StrUtil.isBlank(sort.getDirection())) {
            sort.setDirection(globalConfig.getDefaultSortDirection());
        }
        if (!globalConfig.getDefaultSortDirection().equals(sort.getDirection())) {
            return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Order.asc(sort.getField()));
        }
        return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Order.desc(sort.getField()));
    }

    /**
     * <h2>创建Pageable</h2>
     *
     * @param queryPageData 查询请求
     * @return Pageable
     */
    private Pageable createPageable(QueryPageRequest<E> queryPageData) {
        Page page = queryPageData.getPage();
        if (Objects.isNull(page)) {
            page = new Page();
        }
        if (Objects.isNull(page.getPageNum())) {
            page.setPageNum(1);
        }
        if (Objects.isNull(page.getPageSize())) {
            page.setPageSize(globalConfig.getDefaultPageSize());
        }
        int pageNumber = page.getPageNum() - 1;
        pageNumber = Math.max(0, pageNumber);

        int pageSize = queryPageData.getPage().getPageSize();
        pageSize = Math.max(1, pageSize);
        return PageRequest.of(pageNumber, pageSize, createSort(queryPageData.getSort()));
    }

    /**
     * <h2>获取查询条件列表</h2>
     *
     * @param root    root
     * @param builder builder
     * @param search  搜索实体
     * @param isRoot  是否根查询条件
     * @param isEqual 是否强匹配
     * @return 搜索条件
     */
    private List<Predicate> getPredicateList(Object root, CriteriaBuilder builder, Object search, boolean isRoot, boolean isEqual) {
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
                        predicateList.addAll(this.getPredicateList(payload, builder, fieldValue, false, isEqual));
                    } else {
                        Join<?, ?> payload = ((Join<?, ?>) root).join(field.getName(), JoinType.INNER);
                        predicateList.addAll(this.getPredicateList(payload, builder, fieldValue, false, isEqual));
                    }
                    continue;
                }
                Predicate predicate;
                String searchValue = fieldValue.toString();
                // Boolean强匹配
                if (Boolean.class.equals(fieldValue.getClass())) {
                    // Boolean搜索
                    if (isRoot) {
                        predicate = builder.equal(((Root<E>) root).get(field.getName()), fieldValue);
                    } else {
                        predicate = builder.equal(((Join<?, ?>) root).get(field.getName()), fieldValue);
                    }
                    predicateList.add(predicate);
                    continue;
                }
                if (Search.Mode.LIKE.equals(searchMode.value()) && !isEqual) {
                    // LIKE 模糊搜索 且没有声明强匹配
                    searchValue = searchValue + Constant.SQL_LIKE_PERCENT;
                    if (isRoot) {
                        predicate = builder.like(((Root<E>) root).get(field.getName()), searchValue);
                    } else {
                        predicate = builder.like(((Join<?, ?>) root).get(field.getName()), searchValue);
                    }
                    predicateList.add(predicate);
                    continue;
                }

                // 强匹配
                if (isRoot) {
                    predicate = builder.equal(((Root<E>) root).get(field.getName()), fieldValue);
                } else {
                    predicate = builder.equal(((Join<?, ?>) root).get(field.getName()), fieldValue);
                }
                predicateList.add(predicate);
            } catch (IllegalAccessException exception) {
                log.error(exception.getMessage());
            }
        }
        return predicateList;
    }

    /**
     * <h2>添加创建时间和更新时间的查询条件</h2>
     *
     * @param root          ROOT
     * @param builder       参数构造器
     * @param search        原始查询对象
     * @param predicateList 查询条件列表
     */
    private void addCreateAndUpdateTimePredicate(Root<E> root, CriteriaBuilder builder, E search, List<Predicate> predicateList) {
        if (Objects.nonNull(search.getCreateTimeFrom())) {
            predicateList.add(builder.greaterThanOrEqualTo(root.get(Constant.CREATE_TIME_FIELD), search.getCreateTimeFrom()));
        }
        if (Objects.nonNull(search.getCreateTimeTo())) {
            predicateList.add(builder.lessThan(root.get(Constant.CREATE_TIME_FIELD), search.getCreateTimeTo()));
        }
        if (Objects.nonNull(search.getUpdateTimeFrom())) {
            predicateList.add(builder.greaterThanOrEqualTo(root.get(Constant.UPDATE_TIME_FIELD), search.getUpdateTimeFrom()));
        }
        if (Objects.nonNull(search.getUpdateTimeTo())) {
            predicateList.add(builder.lessThan(root.get(Constant.UPDATE_TIME_FIELD), search.getUpdateTimeTo()));
        }
    }

    /**
     * <h2>创建查询对象</h2>
     *
     * @param filter  过滤器对象
     * @param isEqual 是否强匹配
     * @return 查询对象
     */
    private Specification<E> createSpecification(E filter, boolean isEqual) {
        return (root, criteriaQuery, criteriaBuilder) -> createPredicate(root, criteriaQuery, criteriaBuilder, filter, isEqual);
    }

    /**
     * <h2>创建Predicate</h2>
     *
     * @param root          root
     * @param criteriaQuery query
     * @param builder       builder
     * @param filter        过滤器实体
     * @return 查询条件
     */
    private Predicate createPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder, E filter, boolean isEqual) {
        List<Predicate> predicateList = this.getPredicateList(root, builder, filter, true, isEqual);
        predicateList.addAll(addSearchPredicate(root, builder, filter));
        addCreateAndUpdateTimePredicate(root, builder, filter, predicateList);
        Predicate[] predicates = new Predicate[predicateList.size()];
        criteriaQuery.where(builder.and(predicateList.toArray(predicates)));
        return criteriaQuery.getRestriction();
    }
}
