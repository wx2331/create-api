package com.sun.tools.example.debug.tty;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.ClassUnloadEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.event.WatchpointEvent;

interface EventNotifier {
  void vmStartEvent(VMStartEvent paramVMStartEvent);
  
  void vmDeathEvent(VMDeathEvent paramVMDeathEvent);
  
  void vmDisconnectEvent(VMDisconnectEvent paramVMDisconnectEvent);
  
  void threadStartEvent(ThreadStartEvent paramThreadStartEvent);
  
  void threadDeathEvent(ThreadDeathEvent paramThreadDeathEvent);
  
  void classPrepareEvent(ClassPrepareEvent paramClassPrepareEvent);
  
  void classUnloadEvent(ClassUnloadEvent paramClassUnloadEvent);
  
  void breakpointEvent(BreakpointEvent paramBreakpointEvent);
  
  void fieldWatchEvent(WatchpointEvent paramWatchpointEvent);
  
  void stepEvent(StepEvent paramStepEvent);
  
  void exceptionEvent(ExceptionEvent paramExceptionEvent);
  
  void methodEntryEvent(MethodEntryEvent paramMethodEntryEvent);
  
  boolean methodExitEvent(MethodExitEvent paramMethodExitEvent);
  
  void vmInterrupted();
  
  void receivedEvent(Event paramEvent);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\EventNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */