package cn.hamm.airpower.util;

import cn.hamm.airpower.util.redis.RedisUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>Utils包</h1>
 *
 * @author Hamm
 */
@Component
public class Utils {
    @Getter
    private static RedisUtil<?> redisUtil;

    @Getter
    private static EmailUtil emailUtil;

    @Getter
    private static TransactionUtil transactionUtil;

    @Getter
    private static TreeUtil treeUtil;

    @Autowired
    Utils(
            RedisUtil<?> redisUtil,
            EmailUtil emailUtil,
            TransactionUtil transactionUtil,
            TreeUtil treeUtil
    ) {
        Utils.redisUtil = redisUtil;
        Utils.emailUtil = emailUtil;
        Utils.transactionUtil = transactionUtil;
        Utils.treeUtil = treeUtil;
    }
}
