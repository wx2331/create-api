package sun.jvmstat.monitor.remote;

import sun.jvmstat.monitor.MonitoredVm;

public interface BufferedMonitoredVm extends MonitoredVm {
  byte[] getBytes();
  
  int getCapacity();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\remote\BufferedMonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */