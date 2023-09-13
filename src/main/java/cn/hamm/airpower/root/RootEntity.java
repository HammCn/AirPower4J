package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.annotation.Exclude;
import cn.hamm.airpower.annotation.Search;
import cn.hamm.airpower.interfaces.IEntity;
import cn.hamm.airpower.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <h1>实体根类</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@MappedSuperclass
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamicInsert
@DynamicUpdate
@Description("")
public class RootEntity<E extends RootEntity<E>> extends RootModel<E> implements Serializable, IEntity<E> {
    /**
     * <h2>主键ID</h2>
     */
    @Description("ID")
    @Id
    @Search(Search.Mode.EQUALS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint UNSIGNED comment 'ID'")
    @Min(value = 0, message = "ID必须大于{value}")
    @NotNull(groups = {WhenUpdate.class, WhenIdRequired.class}, message = "ID不能为空")
    private Long id;

    /**
     * <h2>备注信息</h2>
     */
    @Description("备注")
    @Search(Search.Mode.LIKE)
    @Column(columnDefinition = "text comment '备注'")
    @Length(max = 1000, message = "备注最多允许{max}个字符")
    @Exclude(filters = {WhenPayLoad.class})
    private String remark;

    /**
     * <h2>是否禁用</h2>
     */
    @Description("是否禁用")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Search(Search.Mode.EQUALS)
    @Column(columnDefinition = "tinyint UNSIGNED default 0 comment '是否禁用'")
    @Exclude(filters = {WhenPayLoad.class})
    private Boolean isDisabled;

    /**
     * <h2>创建时间</h2>
     */
    @Description("创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createTime;

    /**
     * <h2>创建人ID</h2>
     */
    @Description("创建人ID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createUserId;

    /**
     * <h2>修改人ID</h2>
     */
    @Description("修改人ID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateUserId;

    /**
     * <h2>修改时间</h2>
     */
    @Description("修改时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateTime;

    /**
     * <h2>设置ID</h2>
     *
     * @param id ID
     * @return 实体
     */
    @Override
    public E setId(Long id) {
        this.id = id;
        return (E) this;
    }

    /**
     * <h2>设置备注</h2>
     *
     * @param remark 备注
     * @return 备注
     */
    public E setRemark(String remark) {
        this.remark = remark;
        return (E) this;
    }

    /**
     * <h2>设置是否禁用</h2>
     *
     * @param isDisabled 禁用
     * @return 实体
     */
    public E setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
        return (E) this;
    }

    /**
     * <h2>设置创建时间</h2>
     *
     * @param createTime 创建时间
     * @return 实体
     */
    public E setCreateTime(Long createTime) {
        this.createTime = createTime;
        return (E) this;
    }

    /**
     * <h2>设置创建人ID</h2>
     *
     * @param createUserId 创建人ID
     * @return 实体
     */
    public E setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return (E) this;
    }

    /**
     * <h2>设置修改人ID</h2>
     *
     * @param updateUserId 修改人ID
     * @return 实体
     */
    public E setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
        return (E) this;
    }

    /**
     * <h2>设置更新时间</h2>
     *
     * @param updateTime 更新时间
     * @return 实体
     */
    public E setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return (E) this;
    }

    /**
     * <h2>获取简单实体对象</h2>
     * <p>
     * 一般来说会舍弃一些基础数据
     * </p>
     *
     * @return 简单对象
     * @noinspection unused
     */
    protected E getBaseData() {
        return this.exclude(ReflectUtil.getFieldNameList(RootEntity.class));
    }

    /**
     * <h2>当添加时</h2>
     */
    public interface WhenAdd {
    }

    /**
     * <h2>当更新时</h2>
     */
    public interface WhenUpdate {
    }

    /**
     * <h2>ID必须传入的场景</h2>
     */
    public interface WhenIdRequired {
    }

    /**
     * <h2>当查询详情时</h2>
     */
    public interface WhenGetDetail {
    }

    /**
     * <h2>作为负载时</h2>
     */
    public interface WhenPayLoad {
    }

    /**
     * <h2>分页查询</h2>
     */
    public interface WhenGetPage {
    }

    /**
     * <h2>不分页查询</h2>
     */
    public interface WhenGetList {
    }
}
