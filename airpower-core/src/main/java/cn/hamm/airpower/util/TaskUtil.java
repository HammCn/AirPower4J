package cn.hamm.airpower.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>任务流程管理器</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Component
public class TaskUtil {
    /**
     * <h2>执行任务(不会抛出异常)</h2>
     *
     * @param runnable     任务
     * @param moreRunnable 更多任务
     * @apiNote 如需事务处理，可使用 {@link TransactionUtil#run(TransactionUtil.Function)}
     */
    public final void run(Runnable runnable, Runnable... moreRunnable) {
        getRunnableList(runnable, moreRunnable).forEach(run -> {
            try {
                run.run();
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
            }
        });
    }

    /**
     * <h2>异步执行任务(不会抛出异常)</h2>
     *
     * @param runnable     任务
     * @param moreRunnable 更多任务
     * @apiNote 如需异步事务处理，可在此参数传入的方法中自行调用 {@link TransactionUtil#run(TransactionUtil.Function)}
     */
    public final void runAsync(Runnable runnable, Runnable... moreRunnable) {
        getRunnableList(runnable, moreRunnable).forEach(run -> new Thread(run).start());
    }

    /**
     * <h2>获取任务列表</h2>
     *
     * @param runnable     任务
     * @param moreRunnable 更多任务
     * @return 任务列表
     */
    private @NotNull List<Runnable> getRunnableList(Runnable runnable, Runnable[] moreRunnable) {
        List<Runnable> runnableList = new ArrayList<>();
        runnableList.add(runnable);
        runnableList.addAll(Arrays.asList(moreRunnable));
        return runnableList;
    }
}