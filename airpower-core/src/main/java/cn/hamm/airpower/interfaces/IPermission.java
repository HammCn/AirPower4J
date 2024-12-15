package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.util.PermissionUtil;

/**
 * <h1>权限接口</h1>
 *
 * @author Hamm
 * @see PermissionUtil#scanPermission
 */
public interface IPermission<E extends IPermission<E>> extends ITree<E> {
    /**
     * <h3>获取权限标识</h3>
     *
     * @return 权限标识
     */
    String getIdentity();

    /**
     * <h3>设置权限标识</h3>
     *
     * @param identity 权限标识
     * @return 权限实体
     */
    E setIdentity(String identity);
}
