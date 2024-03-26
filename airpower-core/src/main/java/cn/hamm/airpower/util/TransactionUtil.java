package cn.hamm.airpower.util;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>事务助手类</h1>
 *
 * @author Hamm
 */
@Service
public class TransactionUtil {
    /**
     * <h2>开始执行一个包含若干方法的事务</h2>
     *
     * @param function 事务包含的方法集合体
     */
    @Transactional(rollbackFor = Exception.class)
    public void run(Function function) {
        function.run();
    }

    @FunctionalInterface
    public interface Function {
        /**
         * <h2>开始执行一个包含若干方法的事务</h2>
         */
        void run();
    }

}
