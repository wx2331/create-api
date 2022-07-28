package sun.jvmstat.monitor.event;

import java.util.EventListener;

public interface HostListener extends EventListener {
  void vmStatusChanged(VmStatusChangeEvent paramVmStatusChangeEvent);
  
  void disconnected(HostEvent paramHostEvent);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\event\HostListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */