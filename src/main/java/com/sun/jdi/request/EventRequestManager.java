package com.sun.jdi.request;

import com.sun.jdi.Field;
import com.sun.jdi.Location;
import com.sun.jdi.Mirror;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import java.util.List;
import jdk.Exported;

@Exported
public interface EventRequestManager extends Mirror {
  ClassPrepareRequest createClassPrepareRequest();
  
  ClassUnloadRequest createClassUnloadRequest();
  
  ThreadStartRequest createThreadStartRequest();
  
  ThreadDeathRequest createThreadDeathRequest();
  
  ExceptionRequest createExceptionRequest(ReferenceType paramReferenceType, boolean paramBoolean1, boolean paramBoolean2);
  
  MethodEntryRequest createMethodEntryRequest();
  
  MethodExitRequest createMethodExitRequest();
  
  MonitorContendedEnterRequest createMonitorContendedEnterRequest();
  
  MonitorContendedEnteredRequest createMonitorContendedEnteredRequest();
  
  MonitorWaitRequest createMonitorWaitRequest();
  
  MonitorWaitedRequest createMonitorWaitedRequest();
  
  StepRequest createStepRequest(ThreadReference paramThreadReference, int paramInt1, int paramInt2);
  
  BreakpointRequest createBreakpointRequest(Location paramLocation);
  
  AccessWatchpointRequest createAccessWatchpointRequest(Field paramField);
  
  ModificationWatchpointRequest createModificationWatchpointRequest(Field paramField);
  
  VMDeathRequest createVMDeathRequest();
  
  void deleteEventRequest(EventRequest paramEventRequest);
  
  void deleteEventRequests(List<? extends EventRequest> paramList);
  
  void deleteAllBreakpoints();
  
  List<StepRequest> stepRequests();
  
  List<ClassPrepareRequest> classPrepareRequests();
  
  List<ClassUnloadRequest> classUnloadRequests();
  
  List<ThreadStartRequest> threadStartRequests();
  
  List<ThreadDeathRequest> threadDeathRequests();
  
  List<ExceptionRequest> exceptionRequests();
  
  List<BreakpointRequest> breakpointRequests();
  
  List<AccessWatchpointRequest> accessWatchpointRequests();
  
  List<ModificationWatchpointRequest> modificationWatchpointRequests();
  
  List<MethodEntryRequest> methodEntryRequests();
  
  List<MethodExitRequest> methodExitRequests();
  
  List<MonitorContendedEnterRequest> monitorContendedEnterRequests();
  
  List<MonitorContendedEnteredRequest> monitorContendedEnteredRequests();
  
  List<MonitorWaitRequest> monitorWaitRequests();
  
  List<MonitorWaitedRequest> monitorWaitedRequests();
  
  List<VMDeathRequest> vmDeathRequests();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\EventRequestManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */