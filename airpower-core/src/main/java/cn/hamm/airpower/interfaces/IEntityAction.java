package cn.hamm.airpower.interfaces;

/**
 * <h1>实体的操作标准接口</h1>
 *
 * @author Hamm
 */
public interface IEntityAction extends IAction {
    /**
     * <h2>ID必须传入的场景</h2>
     */
    interface WhenIdRequired {
    }

    /**
     * <h2>当添加时</h2>
     */
    interface WhenAdd {
    }

    /**
     * <h2>当更新时</h2>
     */
    interface WhenUpdate {
    }

    /**
     * <h2>当查询详情时</h2>
     */
    interface WhenGetDetail {
    }

    /**
     * <h2>分页查询</h2>
     */
    interface WhenGetPage {
    }

    /**
     * <h2>不分页查询</h2>
     */
    interface WhenGetList {
    }
}
