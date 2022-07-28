package sun.tools.jstat;

import sun.jvmstat.monitor.MonitorException;

public interface OutputFormatter {
  String getHeader() throws MonitorException;
  
  String getRow() throws MonitorException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\OutputFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */