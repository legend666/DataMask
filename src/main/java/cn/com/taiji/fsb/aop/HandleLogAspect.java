package cn.com.taiji.fsb.aop;

import cn.com.taiji.fsb.annotation.HandleLog;
//import cn.com.taiji.fsb.domain.Logs;
import cn.com.taiji.fsb.domain.User;
//import cn.com.taiji.fsb.repository.LogsRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class HandleLogAspect {

//	@Autowired
//	private LogsRepository logRepository;

	/**
	 * 配置切点为注解class
	 */
	@Pointcut("@annotation(cn.com.taiji.fsb.annotation.HandleLog)")
	public void controllerAspect(){

	}

//	@AfterReturning(value = "controllerAspect()",returning = "rvt")
//	public void doAround(JoinPoint joinPoint,Object rvt) {
//
//		Logs log = new Logs();
//		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		try {
//			Map<String,Object> map = getControllerMethodDescription(joinPoint);
//			log.setHandleTime(new Date());
//			log.setHandleType((String)map.get("type"));
//			log.setHandleDesc((String)map.get("describe"));
//			log.setHandleParam((String)map.get("param"));
//			log.setHandleResult(rvt.toString());
//			log.setHandlePerson(user.getId());
//			logRepository.save(log);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}



	/**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
     private static Map<String,Object> getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        Map<String,Object> map = new HashMap<String,Object>();
        //将方法入参放入map中
        String str = "";
        for(Object obj : arguments) {
     	   if(obj instanceof String) {
     		   str += (String)obj + ";";
     	   }
        }
        map.put("param", str);
         for (Method method : methods) {
             if (method.getName().equals(methodName)) {
                 Class[] clazzs = method.getParameterTypes();
                 if (clazzs.length == arguments.length) {
                   String description = method.getAnnotation(HandleLog. class).describe();
                   String type = method.getAnnotation(HandleLog. class).types();
                   map.put("describe", description);
                   map.put("type", type);

                   break;
                }
            }
        }
         return map;
    }



}
