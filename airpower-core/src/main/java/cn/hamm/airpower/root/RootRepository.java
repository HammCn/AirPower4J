package cn.hamm.airpower.root;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>数据源接口</h1>
 *
 * @param <E> 实体
 * @author Hamm.cn
 */
@NoRepositoryBean
public interface RootRepository<E extends RootEntity<E>> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
    /**
     * <h3>加写锁查询</h3>
     *
     * @param id ID
     * @return 实体
     */
    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    E getWidthLockById(Long id);
}
