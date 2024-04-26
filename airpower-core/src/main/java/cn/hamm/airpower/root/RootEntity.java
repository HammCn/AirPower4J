package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.*;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.interfaces.IEntity;
import cn.hamm.airpower.interfaces.IEntityAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <h1>实体根类</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@MappedSuperclass
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamicInsert
@DynamicUpdate
@Description(Constant.EMPTY_STRING)
@SuppressWarnings("unchecked")
public class RootEntity<E extends RootEntity<E>> extends RootModel<E>
        implements Serializable, IEntity<E>, IEntityAction {
    @Description("主键ID")
    @Id
    @Search(Search.Mode.EQUALS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint UNSIGNED comment 'ID'")
    @Min(value = 0, message = "ID必须大于{value}")
    @NotNull(groups = {WhenUpdate.class, WhenIdRequired.class}, message = "ID不能为空")
    private Long id;

    @Description("备注")
    @Search(Search.Mode.LIKE)
    @Column(columnDefinition = "text comment '备注'")
    @Length(max = 1000, message = "备注最多允许{max}个字符")
    @Exclude(filters = {WhenPayLoad.class})
    private String remark;

    @Description("是否禁用")
    @ReadOnly
    @Search(Search.Mode.EQUALS)
    @Column(columnDefinition = "tinyint UNSIGNED default 0 comment '是否禁用'")
    @Exclude(filters = {WhenPayLoad.class})
    private Boolean isDisabled;

    @Description("创建时间")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createTime;

    @Description("创建人ID")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createUserId;

    @Description("修改人ID")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateUserId;

    @Description("修改时间")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateTime;

    @Transient
    @Description("创建时间开始")
    @Document(MessageConstant.GET_PAGE_OR_GET_LIST_USE_ONLY)
    private Long createTimeFrom;

    @Transient
    @Description("创建时间结束")
    @Document(MessageConstant.GET_PAGE_OR_GET_LIST_USE_ONLY)
    private Long createTimeTo;

    @Transient
    @Description("修改时间开始")
    @Document(MessageConstant.GET_PAGE_OR_GET_LIST_USE_ONLY)
    private Long updateTimeFrom;

    @Transient
    @Description("修改时间结束")
    @Document(MessageConstant.GET_PAGE_OR_GET_LIST_USE_ONLY)
    private Long updateTimeTo;

    @Transient
    @Getter
    @Setter
    private String text;

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
     * <h2>设置创建时间起点</h2>
     *
     * @param createTimeFrom 创建时间起点
     * @return 实体
     */
    public E setCreateTimeFrom(Long createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
        return (E) this;
    }

    /**
     * <h2>设置创建时间终点</h2>
     *
     * @param createTimeTo 创建时间终点
     * @return 实体
     */
    public E setCreateTimeTo(Long createTimeTo) {
        this.createTimeTo = createTimeTo;
        return (E) this;
    }

    /**
     * <h2>设置更新时间起点</h2>
     *
     * @param updateTimeFrom 更新时间起点
     * @return 实体
     */
    public E setUpdateTimeFrom(Long updateTimeFrom) {
        this.updateTimeFrom = updateTimeFrom;
        return (E) this;
    }

    /**
     * <h2>设置更新时间终点</h2>
     *
     * @param updateTimeTo 更新时间终点
     * @return 实体
     */
    public E setUpdateTimeTo(Long updateTimeTo) {
        this.updateTimeTo = updateTimeTo;
        return (E) this;
    }

    /**
     * <h2>获取简单实体对象</h2>
     * <p>
     * 一般来说会舍弃一些基础数据
     * </p>
     */
    public void excludeBaseData() {
        this.setCreateTime(null)
                .setUpdateTime(null)
                .setCreateUserId(null)
                .setUpdateUserId(null)
                .setRemark(null)
                .setIsDisabled(null);
    }
}
