package com.sun.jdi;

import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.EventRequestManager;
import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public interface VirtualMachine extends Mirror {
  public static final int TRACE_NONE = 0;
  
  public static final int TRACE_SENDS = 1;
  
  public static final int TRACE_RECEIVES = 2;
  
  public static final int TRACE_EVENTS = 4;
  
  public static final int TRACE_REFTYPES = 8;
  
  public static final int TRACE_OBJREFS = 16;
  
  public static final int TRACE_ALL = 16777215;
  
  List<ReferenceType> classesByName(String paramString);
  
  List<ReferenceType> allClasses();
  
  void redefineClasses(Map<? extends ReferenceType, byte[]> paramMap);
  
  List<ThreadReference> allThreads();
  
  void suspend();
  
  void resume();
  
  List<ThreadGroupReference> topLevelThreadGroups();
  
  EventQueue eventQueue();
  
  EventRequestManager eventRequestManager();
  
  BooleanValue mirrorOf(boolean paramBoolean);
  
  ByteValue mirrorOf(byte paramByte);
  
  CharValue mirrorOf(char paramChar);
  
  ShortValue mirrorOf(short paramShort);
  
  IntegerValue mirrorOf(int paramInt);
  
  LongValue mirrorOf(long paramLong);
  
  FloatValue mirrorOf(float paramFloat);
  
  DoubleValue mirrorOf(double paramDouble);
  
  StringReference mirrorOf(String paramString);
  
  VoidValue mirrorOfVoid();
  
  Process process();
  
  void dispose();
  
  void exit(int paramInt);
  
  boolean canWatchFieldModification();
  
  boolean canWatchFieldAccess();
  
  boolean canGetBytecodes();
  
  boolean canGetSyntheticAttribute();
  
  boolean canGetOwnedMonitorInfo();
  
  boolean canGetCurrentContendedMonitor();
  
  boolean canGetMonitorInfo();
  
  boolean canUseInstanceFilters();
  
  boolean canRedefineClasses();
  
  boolean canAddMethod();
  
  boolean canUnrestrictedlyRedefineClasses();
  
  boolean canPopFrames();
  
  boolean canGetSourceDebugExtension();
  
  boolean canRequestVMDeathEvent();
  
  boolean canGetMethodReturnValues();
  
  boolean canGetInstanceInfo();
  
  boolean canUseSourceNameFilters();
  
  boolean canForceEarlyReturn();
  
  boolean canBeModified();
  
  boolean canRequestMonitorEvents();
  
  boolean canGetMonitorFrameInfo();
  
  boolean canGetClassFileVersion();
  
  boolean canGetConstantPool();
  
  void setDefaultStratum(String paramString);
  
  String getDefaultStratum();
  
  long[] instanceCounts(List<? extends ReferenceType> paramList);
  
  String description();
  
  String version();
  
  String name();
  
  void setDebugTraceMode(int paramInt);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\VirtualMachine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */