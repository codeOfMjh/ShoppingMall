package com.mjh.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @PackageName: com.mjh.aspect
 * @ClassName: ServiceLogAspect
 * @Author: majiahuan
 * @Date: 2020/10/31 17:29
 * @Description:
 */
@Aspect //声明为Spring的切面类
@Component //表示该类是一个Spring的组件类
public class ServiceLogAspect {
    // 全局日志标签
    public static final Logger Log = LoggerFactory.getLogger(ServiceLogAspect.class);

    /*
     * AOP通知类型：
     * 1. 前置通知：在方法调用之前执行
     * 2. 后置通知：在方法**正常**调用之后执行
     * 3. 环绕通知：在方法调用之前和之后都能执行
     * 4. 异常通知：如果在方法调用过程中发生异常，则通知
     * 5. 最终通知：在方法调用之后执行
     * */

    /*
     * @Description // 切面表达式
     * 第一处 * 代表方法的返回类型， * 代表所有类型
     * 第二处 包名代表AOP监控的类所在的包
     * 第三处 .. 代表该包及其子包下的所有类及其方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) 代表类中的所有方法和任意参数；
     *        *代表类中的所有方法，..代表方法中的任何参数
     * @Param [joinPoint]
     * @return java.lang.Object
     **/
    @Around("execution(* com.mjh.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.info("==== 开始执行 {}.{} ====", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        // 方法开始执行的时间点
        long begin = System.currentTimeMillis();
        // 开始执行目标方法
        Object result = joinPoint.proceed();
        // 方法结束执行的时间点
        long end = System.currentTimeMillis();
        // 执行方法耗时
        long takeTime = end - begin;

        // 进行日志级别输出
        if (takeTime > 3000) {
            Log.error("===== 执行结束，耗时：{} 毫秒 ====", takeTime);
        } else if (takeTime > 2000) {
            Log.warn("===== 执行结束，耗时：{} 毫秒 ====", takeTime);
        } else {
            Log.info("===== 执行结束，耗时：{} 毫秒 ====", takeTime);
        }
        // 返回方法对象
        return result;
    }
}
