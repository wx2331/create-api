package sun.jvmstat.monitor;

import java.util.List;
import sun.jvmstat.monitor.event.VmListener;

public interface MonitoredVm {
  VmIdentifier getVmIdentifier();
  
  Monitor findByName(String paramString) throws MonitorException;
  
  List<Monitor> findByPattern(String paramString) throws MonitorException;
  
  void detach();
  
  void setInterval(int paramInt);
  
  int getInterval();
  
  void setLastException(Exception paramException);
  
  Exception getLastException();
  
  void clearLastException();
  
  boolean isErrored();
  
  void addVmListener(VmListener paramVmListener) throws MonitorException;
  
  void removeVmListener(VmListener paramVmListener) throws MonitorException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\MonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */