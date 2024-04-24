package cn.hamm.airpower.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>异常代码执行接口</h1>
 *
 * @author Hamm.cn
 */
public interface ITry {
    /**
     * <h2>独立执行多段代码</h2>
     *
     * @param runnable     至少要被执行的代码
     * @param moreRunnable 更多的执行代码
     * @apiNote 任一代码段抛出异常，不影响其他代码段的执行
     */
    default void execute(Runnable runnable, Runnable... moreRunnable) {
        List<Runnable> runnableList = new ArrayList<>();
        runnableList.add(runnable);
        runnableList.addAll(Arrays.stream(moreRunnable).toList());
        for (Runnable run : runnableList) {
            try {
                run.run();
            } catch (Exception exception) {
                System.out.println("方法执行异常");
                //noinspection CallToPrintStackTrace
                exception.printStackTrace();
            }
        }
    }
}
