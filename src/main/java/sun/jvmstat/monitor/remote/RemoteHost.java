package sun.jvmstat.monitor.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import sun.jvmstat.monitor.MonitorException;

public interface RemoteHost extends Remote {
  RemoteVm attachVm(int paramInt, String paramString) throws RemoteException, MonitorException;
  
  void detachVm(RemoteVm paramRemoteVm) throws RemoteException, MonitorException;
  
  int[] activeVms() throws RemoteException, MonitorException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\remote\RemoteHost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */