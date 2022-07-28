package sun.jvmstat.monitor.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteVm extends Remote {
  byte[] getBytes() throws RemoteException;
  
  int getCapacity() throws RemoteException;
  
  int getLocalVmId() throws RemoteException;
  
  void detach() throws RemoteException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\remote\RemoteVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */