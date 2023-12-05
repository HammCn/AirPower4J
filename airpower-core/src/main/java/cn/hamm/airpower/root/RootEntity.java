package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.annotation.Exclude;
import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.annotation.Search;
import cn.hamm.airpower.interfaces.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

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
@SuppressWarnings("unchecked")
public class RootEntity<E extends RootEntity<E>> extends RootModel<E> implements Serializable, IEntity<E> {
    /**
     * 主键ID
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
     * 备注信息
     */
    @Description("备注")
    @Search(Search.Mode.LIKE)
    @Column(columnDefinition = "text comment '备注'")
    @Length(max = 1000, message = "备注最多允许{max}个字符")
    @Exclude(filters = {WhenPayLoad.class})
    private String remark;

    /**
     * 是否禁用
     */
    @Description("是否禁用")
    @ReadOnly
    @Search(Search.Mode.EQUALS)
    @Column(columnDefinition = "tinyint UNSIGNED default 0 comment '是否禁用'")
    @Exclude(filters = {WhenPayLoad.class})
    private Boolean isDisabled;

    /**
     * 创建时间
     */
    @Description("创建时间")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createTime;

    /**
     * 创建人ID
     */
    @Description("创建人ID")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '创建人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long createUserId;

    /**
     * 修改人ID
     */
    @Description("修改人ID")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改人ID'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateUserId;

    /**
     * 修改时间
     */
    @Description("修改时间")
    @ReadOnly
    @Column(columnDefinition = "bigint UNSIGNED default 0 comment '修改时间'")
    @Exclude(filters = {WhenPayLoad.class})
    private Long updateTime;

    /**
     * 设置ID
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
     * 设置备注
     *
     * @param remark 备注
     * @return 备注
     */
    public E setRemark(String remark) {
        this.remark = remark;
        return (E) this;
    }

    /**
     * 设置是否禁用
     *
     * @param isDisabled 禁用
     * @return 实体
     */
    public E setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
        return (E) this;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     * @return 实体
     */
    public E setCreateTime(Long createTime) {
        this.createTime = createTime;
        return (E) this;
    }

    /**
     * 设置创建人ID
     *
     * @param createUserId 创建人ID
     * @return 实体
     */
    public E setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return (E) this;
    }

    /**
     * 设置修改人ID
     *
     * @param updateUserId 修改人ID
     * @return 实体
     */
    public E setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
        return (E) this;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     * @return 实体
     */
    public E setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return (E) this;
    }

    /**
     * 获取简单实体对象
     * <p>
     * 一般来说会舍弃一些基础数据
     * </p>
     *
     * @return 简单对象
     */
    public E excludeBaseData() {
        return this.setCreateTime(null).setUpdateTime(null).setCreateUserId(null).setUpdateUserId(null).setRemark(null).setIsDisabled(null);
    }

    /**
     * 当添加时
     */
    public interface WhenAdd {
    }

    /**
     * 当更新时
     */
    public interface WhenUpdate {
    }

    /**
     * ID必须传入的场景
     */
    public interface WhenIdRequired {
    }

    /**
     * 当查询详情时
     */
    public interface WhenGetDetail {
    }

    /**
     * 当需要过滤挂载数据时
     */
    public interface WhenPayLoad {
    }

    /**
     * 分页查询
     */
    public interface WhenGetPage {
    }

    /**
     * 不分页查询
     */
    public interface WhenGetList {
    }
}
