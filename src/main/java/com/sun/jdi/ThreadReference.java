package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface ThreadReference extends ObjectReference {
  public static final int THREAD_STATUS_UNKNOWN = -1;
  
  public static final int THREAD_STATUS_ZOMBIE = 0;
  
  public static final int THREAD_STATUS_RUNNING = 1;
  
  public static final int THREAD_STATUS_SLEEPING = 2;
  
  public static final int THREAD_STATUS_MONITOR = 3;
  
  public static final int THREAD_STATUS_WAIT = 4;
  
  public static final int THREAD_STATUS_NOT_STARTED = 5;
  
  String name();
  
  void suspend();
  
  void resume();
  
  int suspendCount();
  
  void stop(ObjectReference paramObjectReference) throws InvalidTypeException;
  
  void interrupt();
  
  int status();
  
  boolean isSuspended();
  
  boolean isAtBreakpoint();
  
  ThreadGroupReference threadGroup();
  
  int frameCount() throws IncompatibleThreadStateException;
  
  List<StackFrame> frames() throws IncompatibleThreadStateException;
  
  StackFrame frame(int paramInt) throws IncompatibleThreadStateException;
  
  List<StackFrame> frames(int paramInt1, int paramInt2) throws IncompatibleThreadStateException;
  
  List<ObjectReference> ownedMonitors() throws IncompatibleThreadStateException;
  
  List<MonitorInfo> ownedMonitorsAndFrames() throws IncompatibleThreadStateException;
  
  ObjectReference currentContendedMonitor() throws IncompatibleThreadStateException;
  
  void popFrames(StackFrame paramStackFrame) throws IncompatibleThreadStateException;
  
  void forceEarlyReturn(Value paramValue) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ThreadReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */