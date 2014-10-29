package nl.ordina.reactive.rest;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Profiled
@Interceptor
public class ProfileAspect {
  private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  @AroundInvoke
  public Object around(InvocationContext ctx) throws Exception {
    final String name = ctx.getTarget().getClass().getName() + ctx.getMethod().getName();
    long start = System.currentTimeMillis();
    log.info("Start " + name);
    final Object result = ctx.proceed();
    log.info("End   " + name);
    System.out.println("Duur: " + (System.currentTimeMillis() - start));
    return result;
  }
}
