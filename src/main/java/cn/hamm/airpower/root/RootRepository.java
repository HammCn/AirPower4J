package cn.hamm.airpower.root;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <h1>数据源接口</h1>
 *
 * @param <E> 实体
 * @author Hamm
 */
public interface RootRepository<E extends RootEntity<E>> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
}