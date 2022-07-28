package sun.jvmstat.monitor.event;

import java.util.EventListener;

public interface VmListener extends EventListener {
  void monitorStatusChanged(MonitorStatusChangeEvent paramMonitorStatusChangeEvent);
  
  void monitorsUpdated(VmEvent paramVmEvent);
  
  void disconnected(VmEvent paramVmEvent);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\event\VmListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */