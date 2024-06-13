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
     * <h2>执行任务</h2>
     *
     * @param runnable     任务
     * @param moreRunnable 更多任务
     */
    public final void run(Runnable runnable, Runnable... moreRunnable) {
        for (Runnable run : getRunnableList(runnable, moreRunnable)) {
            try {
                run.run();
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
            }
        }
    }

    /**
     * <h2>异步执行任务</h2>
     *
     * @param runnable     任务
     * @param moreRunnable 更多任务
     */
    public final void runAsync(Runnable runnable, Runnable... moreRunnable) {
        for (Runnable run : getRunnableList(runnable, moreRunnable)) {
            new Thread(run).start();
        }
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