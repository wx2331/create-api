package sun.tools.jstat;

import sun.jvmstat.monitor.MonitorException;

interface Closure {
  void visit(Object paramObject, boolean paramBoolean) throws MonitorException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Closure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */