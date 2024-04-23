package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.annotation.Search;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.interfaces.ITry;
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
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * <h1>服务根类</h1>
 *
 * @param <E> 实体
 * @param <R> 数据源
 * @author Hamm.cn
 */
@SuppressWarnings({"unchecked", "SpringJavaInjectionPointsAutowiringInspection"})
@Slf4j
public class RootService<E extends RootEntity<E>, R extends RootRepository<E>> implements ITry {
    @Autowired
    protected R repository;

    @Autowired
    protected RedisUtil<E> redisUtil;

    @Autowired
    protected SecurityUtil secureUtil;

    @Autowired
    private GlobalConfig globalConfig;

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
        source = beforeAdd(source);
        Result.ERROR.whenNull(source, "新增的数据不能为空");
        source.setId(null).setIsDisabled(false).setCreateTime(System.currentTimeMillis());
        if (Objects.isNull(source.getRemark())) {
            source.setRemark("");
        }
        long id = saveToDatabase(source);
        E finalSource = source;
        tryCatch(() -> afterAdd(id, finalSource));
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
     * @see #beforeUpdate(E)
     * @see #afterUpdate(long, E)
     * @see #afterSaved(long, E)
     * @see #updateWithNull(E)
     */
    public final void update(E source) {
        source = beforeUpdate(source);
        updateToDatabase(source);
        E finalSource = source;
        tryCatch(
                () -> afterUpdate(finalSource.getId(), finalSource),
                () -> afterSaved(finalSource.getId(), finalSource)
        );
    }

    /**
     * <h2>🔴修改一条已经存在的数据</h2>
     *
     * @param source 保存的实体
     * @apiNote 此方法的 <code>null</code> 属性依然会被更新到数据库
     * @see #beforeUpdate(E)
     * @see #afterUpdate(long, E)
     * @see #afterSaved(long, E)
     * @see #update(E)
     */
    public final void updateWithNull(E source) {
        Result.PARAM_MISSING.whenNull(source.getId(),
                "修改失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!"
        );
        source = beforeUpdate(source);
        updateToDatabase(source, true);
        E finalSource = source;
        tryCatch(
                () -> afterUpdate(finalSource.getId(), finalSource),
                () -> afterSaved(finalSource.getId(), finalSource)
        );
    }

    /**
     * <h2>🟢修改后置方法</h2>
     *
     * <p>
     * 🔴请不要在重写此方法后再次调用 {@link #update(E)  } 与 {@link #updateWithNull(E)} 以避免循环调用
     * </p>
     * <p>
     * 🟢如需再次保存，请调用 {@link #updateToDatabase(E)}
     * </p>
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
        tryCatch(() -> afterDisable(id));
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
        tryCatch(() -> afterEnable(id));
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
        tryCatch(() -> afterDelete(id));
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
        queryRequest = Objects.requireNonNullElse(queryRequest, new QueryPageRequest<>());
        queryRequest.setFilter(Objects.requireNonNullElse(queryRequest.getFilter(), getNewInstance()));
        queryRequest = beforeGetList(queryRequest);
        List<E> list = repository.findAll(
                createSpecification(queryRequest.getFilter(), false), createSort(queryRequest.getSort())
        );
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
        queryRequest.setFilter(Objects.requireNonNullElse(queryRequest.getFilter(), filter));
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
    public final @Nullable E getMaybeNull(long id) {
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
        queryPageRequest = Objects.requireNonNullElse(queryPageRequest, new QueryPageRequest<>());
        queryPageRequest.setFilter(Objects.requireNonNullElse(queryPageRequest.getFilter(), getNewInstance()));
        queryPageRequest = beforeGetPage(queryPageRequest);
        org.springframework.data.domain.Page<E> pageData = repository.findAll(
                createSpecification(queryPageRequest.getFilter(), false), createPageable(queryPageRequest)
        );
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
     * <h2>🔴忽略只读字段</h2>
     *
     * @param entity 实体
     * @return 忽略只读字段之后的实体
     */
    protected final E ignoreReadOnlyFields(E entity) {
        ReflectUtil.getFieldList(getEntityClass()).stream()
                .filter(field -> Objects.nonNull(field.getAnnotation(ReadOnly.class)))
                .forEach(field -> ReflectUtil.clearFieldValue(entity, field));
        return entity;
    }

    /**
     * <h2>🔴更新到数据库</h2>
     *
     * @param source 原始实体
     * @apiNote 🔴请注意，此方法不会触发前后置
     * @see #update(E)
     * @see #updateWithNull(E)
     */
    protected final void updateToDatabase(E source) {
        updateToDatabase(source, false);
    }

    /**
     * <h2>🔴更新到数据库</h2>
     *
     * @param source   原始实体
     * @param withNull 是否更新空值
     * @apiNote 🔴请注意，此方法不会触发前后置
     * @see #update(E)
     * @see #updateWithNull(E)
     */
    protected final void updateToDatabase(E source, boolean withNull) {
        Result.ERROR.whenNull(source, "更新的数据不能为空");
        Result.PARAM_MISSING.whenNull(source.getId(),
                "修改失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!"
        );
        saveToDatabase(source, withNull);
    }

    /**
     * <h2>根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     */
    private E getById(long id) {
        Result.PARAM_MISSING.whenNull(id,
                "查询失败, 请传入" + ReflectUtil.getDescription(getEntityClass()) + "ID!"
        );
        Optional<E> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ResultException(
                Result.DATA_NOT_FOUND.getCode(),
                "没有查到ID为" + id + "的" + ReflectUtil.getDescription(getEntityClass()) + "!"
        );
    }

    /**
     * <h2>根据ID查询对应的实体</h2>
     *
     * @param id 主键ID
     * @return 实体
     * @apiNote 查不到返回null，不抛异常
     */
    private @Nullable E getByIdMaybeNull(long id) {
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
        return saveToDatabase(entity, false);
    }

    /**
     * <h2>保存到数据库</h2>
     *
     * @param entity   待保存实体
     * @param withNull 是否保存空值
     * @return 保存后的实体
     */
    private long saveToDatabase(E entity, boolean withNull) {
        checkUnique(entity);
        entity.setUpdateTime(System.currentTimeMillis());
        if (Objects.nonNull(entity.getId())) {
            // 修改前清掉JPA缓存，避免查询到旧数据
            entityManager.clear();
            // 有ID 走修改 且不允许修改下列字段
            E existEntity = getById(entity.getId());
            if (Objects.isNull(existEntity.getRemark()) && Objects.isNull(entity.getRemark())) {
                // 如果数据库是null 且 传入的也是null 签名给空字符串
                entity.setRemark("");
            }
            entity = withNull ? entity : getEntityForSave(entity, existEntity);
        }
        E target = getNewInstance();
        BeanUtils.copyProperties(entity, target);
        target = beforeSaveToDatabase(target);
        target = repository.saveAndFlush(target);
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
            if (Objects.isNull(annotation)) {
                // 不是数据库列 不校验
                continue;
            }
            if (!annotation.unique()) {
                // 没有标唯一 不校验
                continue;
            }
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            if (Objects.isNull(fieldValue)) {
                // 没有值 不校验
                continue;
            }
            E search = getNewInstance();
            ReflectUtil.setFieldValue(search, field, fieldValue);
            Example<E> example = Example.of(search);
            Optional<E> exist = repository.findOne(example);
            if (exist.isEmpty()) {
                // 没查到 不校验
                continue;
            }
            if (Objects.nonNull(entity.getId()) && exist.get().getId().equals(entity.getId())) {
                // 修改自己 不校验
                continue;
            }
            Result.FORBIDDEN_EXIST.show(fieldName + "(" + fieldValue + ")已经存在！");
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
        } catch (Exception ignored) {
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
        return Arrays.stream(srcBean.getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> Objects.isNull(srcBean.getPropertyValue(name)))
                .toArray(String[]::new);
    }

    /**
     * <h2>获取响应的分页数据</h2>
     *
     * @param data 分页数据
     * @return 输出分页对象
     */
    private QueryPageResponse<E> getResponsePageList(org.springframework.data.domain.Page<E> data) {
        return new QueryPageResponse<E>()
                .setList(data.getContent())
                .setTotal(Math.toIntExact(data.getTotalElements()))
                .setPageCount(data.getTotalPages())
                .setPage(new Page()
                        .setPageSize(data.getPageable().getPageSize())
                        .setPageNum(data.getPageable().getPageNumber() + 1)
                );
    }

    /**
     * <h2>创建Sort</h2>
     *
     * @param sort 排序对象
     * @return Sort Spring的排序对象
     */
    private org.springframework.data.domain.Sort createSort(Sort sort) {
        sort = Objects.requireNonNullElse(sort, new Sort());

        if (!StringUtils.hasText(sort.getField())) {
            sort.setField(globalConfig.getDefaultSortField());
        }

        if (!StringUtils.hasText(sort.getDirection())) {
            sort.setDirection(globalConfig.getDefaultSortDirection());
        }
        if (!globalConfig.getDefaultSortDirection().equals(sort.getDirection())) {
            return org.springframework.data.domain.Sort.by(
                    org.springframework.data.domain.Sort.Order.asc(sort.getField())
            );
        }
        return org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Order.desc(sort.getField())
        );
    }

    /**
     * <h2>创建Pageable</h2>
     *
     * @param queryPageData 查询请求
     * @return Pageable
     */
    private Pageable createPageable(QueryPageRequest<E> queryPageData) {
        Page page = Objects.requireNonNullElse(queryPageData.getPage(), new Page());
        page.setPageNum(Objects.requireNonNullElse(page.getPageNum(), 1));
        page.setPageSize(Objects.requireNonNullElse(page.getPageSize(), globalConfig.getDefaultPageSize()));
        int pageNumber = Math.max(0, page.getPageNum() - 1);
        int pageSize = Math.max(1, queryPageData.getPage().getPageSize());
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
    private List<Predicate> getPredicateList(
            Object root, CriteriaBuilder builder, Object search, boolean isRoot, boolean isEqual
    ) {
        List<Predicate> predicateList = new ArrayList<>();
        List<Field> fields = ReflectUtil.getFieldList(search.getClass());
        for (Field field : fields) {
            Object fieldValue = ReflectUtil.getFieldValue(search, field);
            if (Objects.isNull(fieldValue)) {
                // 没有传入查询值 跳过
                continue;
            }
            if (!StringUtils.hasText(fieldValue.toString())) {
                // 空字符串 跳过
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
    private void addCreateAndUpdateTimePredicate(
            Root<E> root, CriteriaBuilder builder, E search, List<Predicate> predicateList
    ) {
        if (Objects.nonNull(search.getCreateTimeFrom())) {
            predicateList.add(
                    builder.greaterThanOrEqualTo(root.get(Constant.CREATE_TIME_FIELD), search.getCreateTimeFrom())
            );
        }
        if (Objects.nonNull(search.getCreateTimeTo())) {
            predicateList.add(
                    builder.lessThan(root.get(Constant.CREATE_TIME_FIELD), search.getCreateTimeTo())
            );
        }
        if (Objects.nonNull(search.getUpdateTimeFrom())) {
            predicateList.add(
                    builder.greaterThanOrEqualTo(root.get(Constant.UPDATE_TIME_FIELD), search.getUpdateTimeFrom())
            );
        }
        if (Objects.nonNull(search.getUpdateTimeTo())) {
            predicateList.add(
                    builder.lessThan(root.get(Constant.UPDATE_TIME_FIELD), search.getUpdateTimeTo())
            );
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
        return (root, criteriaQuery, criteriaBuilder) ->
                createPredicate(root, criteriaQuery, criteriaBuilder, filter, isEqual);
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
    private Predicate createPredicate(
            Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder, E filter, boolean isEqual
    ) {
        List<Predicate> predicateList = this.getPredicateList(root, builder, filter, true, isEqual);
        predicateList.addAll(addSearchPredicate(root, builder, filter));
        addCreateAndUpdateTimePredicate(root, builder, filter, predicateList);
        Predicate[] predicates = new Predicate[predicateList.size()];
        criteriaQuery.where(builder.and(predicateList.toArray(predicates)));
        return criteriaQuery.getRestriction();
    }
}
