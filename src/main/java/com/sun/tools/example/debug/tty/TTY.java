/*      */ package com.sun.tools.example.debug.tty;
/*      */ 
/*      */ import com.sun.jdi.Bootstrap;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.VMCannotBeModifiedException;
/*      */ import com.sun.jdi.VMDisconnectedException;
/*      */ import com.sun.jdi.connect.Connector;
/*      */ import com.sun.jdi.event.BreakpointEvent;
/*      */ import com.sun.jdi.event.ClassPrepareEvent;
/*      */ import com.sun.jdi.event.ClassUnloadEvent;
/*      */ import com.sun.jdi.event.Event;
/*      */ import com.sun.jdi.event.ExceptionEvent;
/*      */ import com.sun.jdi.event.LocatableEvent;
/*      */ import com.sun.jdi.event.MethodEntryEvent;
/*      */ import com.sun.jdi.event.MethodExitEvent;
/*      */ import com.sun.jdi.event.ModificationWatchpointEvent;
/*      */ import com.sun.jdi.event.StepEvent;
/*      */ import com.sun.jdi.event.ThreadDeathEvent;
/*      */ import com.sun.jdi.event.ThreadStartEvent;
/*      */ import com.sun.jdi.event.VMDeathEvent;
/*      */ import com.sun.jdi.event.VMDisconnectEvent;
/*      */ import com.sun.jdi.event.VMStartEvent;
/*      */ import com.sun.jdi.event.WatchpointEvent;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TTY
/*      */   implements EventNotifier
/*      */ {
/*   46 */   EventHandler handler = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   51 */   private List<String> monitorCommands = new ArrayList<>();
/*   52 */   private int monitorCount = 0;
/*      */ 
/*      */   
/*      */   private static final String progname = "jdb";
/*      */ 
/*      */   
/*      */   private volatile boolean shuttingDown = false;
/*      */ 
/*      */   
/*      */   public void setShuttingDown(boolean paramBoolean) {
/*   62 */     this.shuttingDown = paramBoolean;
/*      */   }
/*      */   
/*      */   public boolean isShuttingDown() {
/*   66 */     return this.shuttingDown;
/*      */   }
/*      */ 
/*      */   
/*      */   public void vmStartEvent(VMStartEvent paramVMStartEvent) {
/*   71 */     Thread.yield();
/*   72 */     MessageOutput.lnprint("VM Started:");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void vmDeathEvent(VMDeathEvent paramVMDeathEvent) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void vmDisconnectEvent(VMDisconnectEvent paramVMDisconnectEvent) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void threadStartEvent(ThreadStartEvent paramThreadStartEvent) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void threadDeathEvent(ThreadDeathEvent paramThreadDeathEvent) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void classPrepareEvent(ClassPrepareEvent paramClassPrepareEvent) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void classUnloadEvent(ClassUnloadEvent paramClassUnloadEvent) {}
/*      */ 
/*      */   
/*      */   public void breakpointEvent(BreakpointEvent paramBreakpointEvent) {
/*  101 */     Thread.yield();
/*  102 */     MessageOutput.lnprint("Breakpoint hit:");
/*      */   }
/*      */ 
/*      */   
/*      */   public void fieldWatchEvent(WatchpointEvent paramWatchpointEvent) {
/*  107 */     Field field = paramWatchpointEvent.field();
/*  108 */     ObjectReference objectReference = paramWatchpointEvent.object();
/*  109 */     Thread.yield();
/*      */     
/*  111 */     if (paramWatchpointEvent instanceof ModificationWatchpointEvent) {
/*  112 */       MessageOutput.lnprint("Field access encountered before after", new Object[] { field, paramWatchpointEvent
/*      */             
/*  114 */             .valueCurrent(), ((ModificationWatchpointEvent)paramWatchpointEvent)
/*  115 */             .valueToBe() });
/*      */     } else {
/*  117 */       MessageOutput.lnprint("Field access encountered", field.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void stepEvent(StepEvent paramStepEvent) {
/*  123 */     Thread.yield();
/*  124 */     MessageOutput.lnprint("Step completed:");
/*      */   }
/*      */ 
/*      */   
/*      */   public void exceptionEvent(ExceptionEvent paramExceptionEvent) {
/*  129 */     Thread.yield();
/*  130 */     Location location = paramExceptionEvent.catchLocation();
/*  131 */     if (location == null) {
/*  132 */       MessageOutput.lnprint("Exception occurred uncaught", paramExceptionEvent
/*  133 */           .exception().referenceType().name());
/*      */     } else {
/*  135 */       MessageOutput.lnprint("Exception occurred caught", new Object[] { paramExceptionEvent
/*  136 */             .exception().referenceType().name(), 
/*  137 */             Commands.locationString(location) });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void methodEntryEvent(MethodEntryEvent paramMethodEntryEvent) {
/*  143 */     Thread.yield();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     if (paramMethodEntryEvent.request().suspendPolicy() != 0) {
/*      */       
/*  151 */       MessageOutput.lnprint("Method entered:");
/*      */     } else {
/*      */       
/*  154 */       MessageOutput.print("Method entered:");
/*  155 */       printLocationOfEvent((LocatableEvent)paramMethodEntryEvent);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean methodExitEvent(MethodExitEvent paramMethodExitEvent) {
/*  161 */     Thread.yield();
/*      */ 
/*      */ 
/*      */     
/*  165 */     Method method1 = Env.atExitMethod();
/*  166 */     Method method2 = paramMethodExitEvent.method();
/*      */     
/*  168 */     if (method1 == null || method1.equals(method2)) {
/*      */ 
/*      */ 
/*      */       
/*  172 */       if (paramMethodExitEvent.request().suspendPolicy() != 0)
/*      */       {
/*  174 */         MessageOutput.println();
/*      */       }
/*  176 */       if (Env.vm().canGetMethodReturnValues()) {
/*  177 */         MessageOutput.print("Method exitedValue:", paramMethodExitEvent.returnValue() + "");
/*      */       } else {
/*  179 */         MessageOutput.print("Method exited:");
/*      */       } 
/*      */       
/*  182 */       if (paramMethodExitEvent.request().suspendPolicy() == 0)
/*      */       {
/*  184 */         printLocationOfEvent((LocatableEvent)paramMethodExitEvent);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  201 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  205 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void vmInterrupted() {
/*  210 */     Thread.yield();
/*  211 */     printCurrentLocation();
/*  212 */     for (String str : this.monitorCommands) {
/*  213 */       StringTokenizer stringTokenizer = new StringTokenizer(str);
/*  214 */       stringTokenizer.nextToken();
/*  215 */       executeCommand(stringTokenizer);
/*      */     } 
/*  217 */     MessageOutput.printPrompt();
/*      */   }
/*      */ 
/*      */   
/*      */   public void receivedEvent(Event paramEvent) {}
/*      */ 
/*      */   
/*      */   private void printBaseLocation(String paramString, Location paramLocation) {
/*  225 */     MessageOutput.println("location", new Object[] { paramString, 
/*      */           
/*  227 */           Commands.locationString(paramLocation) });
/*      */   }
/*      */   private void printCurrentLocation() {
/*      */     StackFrame stackFrame;
/*  231 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*      */     
/*      */     try {
/*  234 */       stackFrame = threadInfo.getCurrentFrame();
/*  235 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  236 */       MessageOutput.println("<location unavailable>");
/*      */       return;
/*      */     } 
/*  239 */     if (stackFrame == null) {
/*  240 */       MessageOutput.println("No frames on the current call stack");
/*      */     } else {
/*  242 */       Location location = stackFrame.location();
/*  243 */       printBaseLocation(threadInfo.getThread().name(), location);
/*      */       
/*  245 */       if (location.lineNumber() != -1) {
/*      */         Object object;
/*      */         try {
/*  248 */           object = Env.sourceLine(location, location.lineNumber());
/*  249 */         } catch (IOException iOException) {
/*  250 */           object = null;
/*      */         } 
/*  252 */         if (object != null) {
/*  253 */           MessageOutput.println("source line number and line", new Object[] { new Integer(location
/*  254 */                   .lineNumber()), object });
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  259 */     MessageOutput.println();
/*      */   }
/*      */   
/*      */   private void printLocationOfEvent(LocatableEvent paramLocatableEvent) {
/*  263 */     printBaseLocation(paramLocatableEvent.thread().name(), paramLocatableEvent.location());
/*      */   }
/*      */   
/*      */   void help() {
/*  267 */     MessageOutput.println("zz help text");
/*      */   }
/*      */   
/*  270 */   private static final String[][] commandList = new String[][] { { "!!", "n", "y" }, { "?", "y", "y" }, { "bytecodes", "n", "y" }, { "catch", "y", "n" }, { "class", "n", "y" }, { "classes", "n", "y" }, { "classpath", "n", "y" }, { "clear", "y", "n" }, { "connectors", "y", "y" }, { "cont", "n", "n" }, { "disablegc", "n", "n" }, { "down", "n", "y" }, { "dump", "n", "y" }, { "enablegc", "n", "n" }, { "eval", "n", "y" }, { "exclude", "y", "n" }, { "exit", "y", "y" }, { "extension", "n", "y" }, { "fields", "n", "y" }, { "gc", "n", "n" }, { "help", "y", "y" }, { "ignore", "y", "n" }, { "interrupt", "n", "n" }, { "kill", "n", "n" }, { "lines", "n", "y" }, { "list", "n", "y" }, { "load", "n", "y" }, { "locals", "n", "y" }, { "lock", "n", "n" }, { "memory", "n", "y" }, { "methods", "n", "y" }, { "monitor", "n", "n" }, { "next", "n", "n" }, { "pop", "n", "n" }, { "print", "n", "y" }, { "quit", "y", "y" }, { "read", "y", "y" }, { "redefine", "n", "n" }, { "reenter", "n", "n" }, { "resume", "n", "n" }, { "run", "y", "n" }, { "save", "n", "n" }, { "set", "n", "n" }, { "sourcepath", "y", "y" }, { "step", "n", "n" }, { "stepi", "n", "n" }, { "stop", "y", "n" }, { "suspend", "n", "n" }, { "thread", "n", "y" }, { "threadgroup", "n", "y" }, { "threadgroups", "n", "y" }, { "threadlocks", "n", "y" }, { "threads", "n", "y" }, { "trace", "n", "n" }, { "unmonitor", "n", "n" }, { "untrace", "n", "n" }, { "unwatch", "y", "n" }, { "up", "n", "y" }, { "use", "y", "y" }, { "version", "y", "y" }, { "watch", "y", "n" }, { "where", "n", "y" }, { "wherei", "n", "y" } };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int isCommand(String paramString) {
/*  352 */     int i = 0;
/*  353 */     int j = commandList.length - 1;
/*  354 */     while (i <= j) {
/*  355 */       int k = i + j >>> 1;
/*  356 */       String str = commandList[k][0];
/*  357 */       int m = str.compareTo(paramString);
/*  358 */       if (m < 0) {
/*  359 */         i = k + 1; continue;
/*  360 */       }  if (m > 0) {
/*  361 */         j = k - 1;
/*      */         continue;
/*      */       } 
/*  364 */       return k;
/*      */     } 
/*      */     
/*  367 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isDisconnectCmd(int paramInt) {
/*  374 */     if (paramInt < 0 || paramInt >= commandList.length) {
/*  375 */       return false;
/*      */     }
/*  377 */     return commandList[paramInt][1].equals("y");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isReadOnlyCmd(int paramInt) {
/*  384 */     if (paramInt < 0 || paramInt >= commandList.length) {
/*  385 */       return false;
/*      */     }
/*  387 */     return commandList[paramInt][2].equals("y");
/*      */   }
/*      */ 
/*      */   
/*      */   void executeCommand(StringTokenizer paramStringTokenizer) {
/*  392 */     String str = paramStringTokenizer.nextToken().toLowerCase();
/*      */     
/*  394 */     boolean bool = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  400 */     if (!str.startsWith("#"))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  405 */       if (Character.isDigit(str.charAt(0)) && paramStringTokenizer.hasMoreTokens()) {
/*      */         try {
/*  407 */           int i = Integer.parseInt(str);
/*  408 */           String str1 = paramStringTokenizer.nextToken("");
/*  409 */           while (i-- > 0) {
/*  410 */             executeCommand(new StringTokenizer(str1));
/*  411 */             bool = false;
/*      */           } 
/*  413 */         } catch (NumberFormatException numberFormatException) {
/*  414 */           MessageOutput.println("Unrecognized command.  Try help...", str);
/*      */         } 
/*      */       } else {
/*  417 */         int i = isCommand(str);
/*      */ 
/*      */ 
/*      */         
/*  421 */         if (i < 0) {
/*  422 */           MessageOutput.println("Unrecognized command.  Try help...", str);
/*  423 */         } else if (!Env.connection().isOpen() && !isDisconnectCmd(i)) {
/*  424 */           MessageOutput.println("Command not valid until the VM is started with the run command", str);
/*      */         }
/*  426 */         else if (Env.connection().isOpen() && !Env.vm().canBeModified() && 
/*  427 */           !isReadOnlyCmd(i)) {
/*  428 */           MessageOutput.println("Command is not supported on a read-only VM connection", str);
/*      */         }
/*      */         else {
/*      */           
/*  432 */           Commands commands = new Commands();
/*      */           try {
/*  434 */             if (str.equals("print")) {
/*  435 */               commands.commandPrint(paramStringTokenizer, false);
/*  436 */               bool = false;
/*  437 */             } else if (str.equals("eval")) {
/*  438 */               commands.commandPrint(paramStringTokenizer, false);
/*  439 */               bool = false;
/*  440 */             } else if (str.equals("set")) {
/*  441 */               commands.commandSet(paramStringTokenizer);
/*  442 */               bool = false;
/*  443 */             } else if (str.equals("dump")) {
/*  444 */               commands.commandPrint(paramStringTokenizer, true);
/*  445 */               bool = false;
/*  446 */             } else if (str.equals("locals")) {
/*  447 */               commands.commandLocals();
/*  448 */             } else if (str.equals("classes")) {
/*  449 */               commands.commandClasses();
/*  450 */             } else if (str.equals("class")) {
/*  451 */               commands.commandClass(paramStringTokenizer);
/*  452 */             } else if (str.equals("connectors")) {
/*  453 */               commands.commandConnectors(Bootstrap.virtualMachineManager());
/*  454 */             } else if (str.equals("methods")) {
/*  455 */               commands.commandMethods(paramStringTokenizer);
/*  456 */             } else if (str.equals("fields")) {
/*  457 */               commands.commandFields(paramStringTokenizer);
/*  458 */             } else if (str.equals("threads")) {
/*  459 */               commands.commandThreads(paramStringTokenizer);
/*  460 */             } else if (str.equals("thread")) {
/*  461 */               commands.commandThread(paramStringTokenizer);
/*  462 */             } else if (str.equals("suspend")) {
/*  463 */               commands.commandSuspend(paramStringTokenizer);
/*  464 */             } else if (str.equals("resume")) {
/*  465 */               commands.commandResume(paramStringTokenizer);
/*  466 */             } else if (str.equals("cont")) {
/*  467 */               commands.commandCont();
/*  468 */             } else if (str.equals("threadgroups")) {
/*  469 */               commands.commandThreadGroups();
/*  470 */             } else if (str.equals("threadgroup")) {
/*  471 */               commands.commandThreadGroup(paramStringTokenizer);
/*  472 */             } else if (str.equals("catch")) {
/*  473 */               commands.commandCatchException(paramStringTokenizer);
/*  474 */             } else if (str.equals("ignore")) {
/*  475 */               commands.commandIgnoreException(paramStringTokenizer);
/*  476 */             } else if (str.equals("step")) {
/*  477 */               commands.commandStep(paramStringTokenizer);
/*  478 */             } else if (str.equals("stepi")) {
/*  479 */               commands.commandStepi();
/*  480 */             } else if (str.equals("next")) {
/*  481 */               commands.commandNext();
/*  482 */             } else if (str.equals("kill")) {
/*  483 */               commands.commandKill(paramStringTokenizer);
/*  484 */             } else if (str.equals("interrupt")) {
/*  485 */               commands.commandInterrupt(paramStringTokenizer);
/*  486 */             } else if (str.equals("trace")) {
/*  487 */               commands.commandTrace(paramStringTokenizer);
/*  488 */             } else if (str.equals("untrace")) {
/*  489 */               commands.commandUntrace(paramStringTokenizer);
/*  490 */             } else if (str.equals("where")) {
/*  491 */               commands.commandWhere(paramStringTokenizer, false);
/*  492 */             } else if (str.equals("wherei")) {
/*  493 */               commands.commandWhere(paramStringTokenizer, true);
/*  494 */             } else if (str.equals("up")) {
/*  495 */               commands.commandUp(paramStringTokenizer);
/*  496 */             } else if (str.equals("down")) {
/*  497 */               commands.commandDown(paramStringTokenizer);
/*  498 */             } else if (str.equals("load")) {
/*  499 */               commands.commandLoad(paramStringTokenizer);
/*  500 */             } else if (str.equals("run")) {
/*  501 */               commands.commandRun(paramStringTokenizer);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  508 */               if (this.handler == null && Env.connection().isOpen()) {
/*  509 */                 this.handler = new EventHandler(this, false);
/*      */               }
/*  511 */             } else if (str.equals("memory")) {
/*  512 */               commands.commandMemory();
/*  513 */             } else if (str.equals("gc")) {
/*  514 */               commands.commandGC();
/*  515 */             } else if (str.equals("stop")) {
/*  516 */               commands.commandStop(paramStringTokenizer);
/*  517 */             } else if (str.equals("clear")) {
/*  518 */               commands.commandClear(paramStringTokenizer);
/*  519 */             } else if (str.equals("watch")) {
/*  520 */               commands.commandWatch(paramStringTokenizer);
/*  521 */             } else if (str.equals("unwatch")) {
/*  522 */               commands.commandUnwatch(paramStringTokenizer);
/*  523 */             } else if (str.equals("list")) {
/*  524 */               commands.commandList(paramStringTokenizer);
/*  525 */             } else if (str.equals("lines")) {
/*  526 */               commands.commandLines(paramStringTokenizer);
/*  527 */             } else if (str.equals("classpath")) {
/*  528 */               commands.commandClasspath(paramStringTokenizer);
/*  529 */             } else if (str.equals("use") || str.equals("sourcepath")) {
/*  530 */               commands.commandUse(paramStringTokenizer);
/*  531 */             } else if (str.equals("monitor")) {
/*  532 */               monitorCommand(paramStringTokenizer);
/*  533 */             } else if (str.equals("unmonitor")) {
/*  534 */               unmonitorCommand(paramStringTokenizer);
/*  535 */             } else if (str.equals("lock")) {
/*  536 */               commands.commandLock(paramStringTokenizer);
/*  537 */               bool = false;
/*  538 */             } else if (str.equals("threadlocks")) {
/*  539 */               commands.commandThreadlocks(paramStringTokenizer);
/*  540 */             } else if (str.equals("disablegc")) {
/*  541 */               commands.commandDisableGC(paramStringTokenizer);
/*  542 */               bool = false;
/*  543 */             } else if (str.equals("enablegc")) {
/*  544 */               commands.commandEnableGC(paramStringTokenizer);
/*  545 */               bool = false;
/*  546 */             } else if (str.equals("save")) {
/*  547 */               commands.commandSave(paramStringTokenizer);
/*  548 */               bool = false;
/*  549 */             } else if (str.equals("bytecodes")) {
/*  550 */               commands.commandBytecodes(paramStringTokenizer);
/*  551 */             } else if (str.equals("redefine")) {
/*  552 */               commands.commandRedefine(paramStringTokenizer);
/*  553 */             } else if (str.equals("pop")) {
/*  554 */               commands.commandPopFrames(paramStringTokenizer, false);
/*  555 */             } else if (str.equals("reenter")) {
/*  556 */               commands.commandPopFrames(paramStringTokenizer, true);
/*  557 */             } else if (str.equals("extension")) {
/*  558 */               commands.commandExtension(paramStringTokenizer);
/*  559 */             } else if (str.equals("exclude")) {
/*  560 */               commands.commandExclude(paramStringTokenizer);
/*  561 */             } else if (str.equals("read")) {
/*  562 */               readCommand(paramStringTokenizer);
/*  563 */             } else if (str.equals("help") || str.equals("?")) {
/*  564 */               help();
/*  565 */             } else if (str.equals("version")) {
/*  566 */               commands.commandVersion("jdb", 
/*  567 */                   Bootstrap.virtualMachineManager());
/*  568 */             } else if (str.equals("quit") || str.equals("exit")) {
/*  569 */               if (this.handler != null) {
/*  570 */                 this.handler.shutdown();
/*      */               }
/*  572 */               Env.shutdown();
/*      */             } else {
/*  574 */               MessageOutput.println("Unrecognized command.  Try help...", str);
/*      */             } 
/*  576 */           } catch (VMCannotBeModifiedException vMCannotBeModifiedException) {
/*  577 */             MessageOutput.println("Command is not supported on a read-only VM connection", str);
/*  578 */           } catch (UnsupportedOperationException unsupportedOperationException) {
/*  579 */             MessageOutput.println("Command is not supported on the target VM", str);
/*  580 */           } catch (VMNotConnectedException vMNotConnectedException) {
/*  581 */             MessageOutput.println("Command not valid until the VM is started with the run command", str);
/*      */           }
/*  583 */           catch (Exception exception) {
/*  584 */             MessageOutput.printException("Internal exception:", exception);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  589 */     if (bool) {
/*  590 */       MessageOutput.printPrompt();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void monitorCommand(StringTokenizer paramStringTokenizer) {
/*  598 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  599 */       this.monitorCount++;
/*  600 */       this.monitorCommands.add(this.monitorCount + ": " + paramStringTokenizer.nextToken(""));
/*      */     } else {
/*  602 */       for (String str : this.monitorCommands) {
/*  603 */         MessageOutput.printDirectln(str);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   void unmonitorCommand(StringTokenizer paramStringTokenizer) {
/*  609 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  610 */       String str1 = paramStringTokenizer.nextToken();
/*      */       
/*      */       try {
/*  613 */         int i = Integer.parseInt(str1);
/*  614 */       } catch (NumberFormatException numberFormatException) {
/*  615 */         MessageOutput.println("Not a monitor number:", str1);
/*      */         return;
/*      */       } 
/*  618 */       String str2 = str1 + ":";
/*  619 */       for (String str : this.monitorCommands) {
/*  620 */         StringTokenizer stringTokenizer = new StringTokenizer(str);
/*  621 */         if (stringTokenizer.nextToken().equals(str2)) {
/*  622 */           this.monitorCommands.remove(str);
/*  623 */           MessageOutput.println("Unmonitoring", str);
/*      */           return;
/*      */         } 
/*      */       } 
/*  627 */       MessageOutput.println("No monitor numbered:", str1);
/*      */     } else {
/*  629 */       MessageOutput.println("Usage: unmonitor <monitor#>");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void readCommand(StringTokenizer paramStringTokenizer) {
/*  635 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  636 */       String str = paramStringTokenizer.nextToken();
/*  637 */       if (!readCommandFile(new File(str))) {
/*  638 */         MessageOutput.println("Could not open:", str);
/*      */       }
/*      */     } else {
/*  641 */       MessageOutput.println("Usage: read <command-filename>");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean readCommandFile(File paramFile) {
/*  650 */     BufferedReader bufferedReader = null;
/*      */     
/*  652 */     try { if (paramFile.canRead()) {
/*      */         
/*  654 */         MessageOutput.println("*** Reading commands from", paramFile.getPath());
/*  655 */         bufferedReader = new BufferedReader(new FileReader(paramFile));
/*      */         String str;
/*  657 */         while ((str = bufferedReader.readLine()) != null) {
/*  658 */           StringTokenizer stringTokenizer = new StringTokenizer(str);
/*  659 */           if (stringTokenizer.hasMoreTokens()) {
/*  660 */             executeCommand(stringTokenizer);
/*      */           }
/*      */         } 
/*      */       }  }
/*  664 */     catch (IOException iOException) {  }
/*      */     finally
/*  666 */     { if (bufferedReader != null) {
/*      */         try {
/*  668 */           bufferedReader.close();
/*  669 */         } catch (Exception exception) {}
/*      */       } }
/*      */ 
/*      */     
/*  673 */     return (bufferedReader != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String readStartupCommandFile(String paramString1, String paramString2, String paramString3) {
/*      */     String str;
/*  684 */     File file = new File(paramString1, paramString2);
/*  685 */     if (!file.exists()) {
/*  686 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  691 */       str = file.getCanonicalPath();
/*  692 */     } catch (IOException iOException) {
/*  693 */       MessageOutput.println("Could not open:", file.getPath());
/*  694 */       return null;
/*      */     } 
/*  696 */     if ((paramString3 == null || !paramString3.equals(str)) && 
/*  697 */       !readCommandFile(file)) {
/*  698 */       MessageOutput.println("Could not open:", file.getPath());
/*      */     }
/*      */     
/*  701 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public TTY() throws Exception {
/*  707 */     MessageOutput.println("Initializing progname", "jdb");
/*      */     
/*  709 */     if (Env.connection().isOpen() && Env.vm().canBeModified())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  715 */       this.handler = new EventHandler(this, true);
/*      */     }
/*      */     try {
/*  718 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*      */ 
/*      */       
/*  721 */       String str1 = null;
/*      */       
/*  723 */       Thread.currentThread().setPriority(5);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  743 */       String str2 = System.getProperty("user.home");
/*      */       
/*      */       String str3;
/*  746 */       if ((str3 = readStartupCommandFile(str2, "jdb.ini", null)) == null)
/*      */       {
/*  748 */         str3 = readStartupCommandFile(str2, ".jdbrc", null);
/*      */       }
/*      */       
/*  751 */       String str4 = System.getProperty("user.dir");
/*  752 */       if (readStartupCommandFile(str4, "jdb.ini", str3) == null)
/*      */       {
/*  754 */         readStartupCommandFile(str4, ".jdbrc", str3);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  759 */       MessageOutput.printPrompt();
/*      */       while (true) {
/*  761 */         str2 = bufferedReader.readLine();
/*  762 */         if (str2 == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  767 */           if (!isShuttingDown()) {
/*  768 */             MessageOutput.println("Input stream closed.");
/*      */           }
/*  770 */           str2 = "quit";
/*      */         } 
/*      */         
/*  773 */         if (str2.startsWith("!!") && str1 != null) {
/*  774 */           str2 = str1 + str2.substring(2);
/*  775 */           MessageOutput.printDirectln(str2);
/*      */         } 
/*      */         
/*  778 */         StringTokenizer stringTokenizer = new StringTokenizer(str2);
/*  779 */         if (stringTokenizer.hasMoreTokens()) {
/*  780 */           str1 = str2;
/*  781 */           executeCommand(stringTokenizer); continue;
/*      */         } 
/*  783 */         MessageOutput.printPrompt();
/*      */       }
/*      */     
/*  786 */     } catch (VMDisconnectedException vMDisconnectedException) {
/*  787 */       this.handler.handleDisconnectedException();
/*      */       return;
/*      */     } 
/*      */   }
/*      */   private static void usage() {
/*  792 */     MessageOutput.println("zz usage text", new Object[] { "jdb", File.pathSeparator });
/*      */     
/*  794 */     System.exit(1);
/*      */   }
/*      */   
/*      */   static void usageError(String paramString) {
/*  798 */     MessageOutput.println(paramString);
/*  799 */     MessageOutput.println();
/*  800 */     usage();
/*      */   }
/*      */   
/*      */   static void usageError(String paramString1, String paramString2) {
/*  804 */     MessageOutput.println(paramString1, paramString2);
/*  805 */     MessageOutput.println();
/*  806 */     usage();
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean supportsSharedMemory() {
/*  811 */     for (Connector connector : Bootstrap.virtualMachineManager().allConnectors()) {
/*  812 */       if (connector.transport() == null) {
/*      */         continue;
/*      */       }
/*  815 */       if ("dt_shmem".equals(connector.transport().name())) {
/*  816 */         return true;
/*      */       }
/*      */     } 
/*  819 */     return false;
/*      */   }
/*      */   
/*      */   private static String addressToSocketArgs(String paramString) {
/*  823 */     int i = paramString.indexOf(':');
/*  824 */     if (i != -1) {
/*  825 */       String str1 = paramString.substring(0, i);
/*  826 */       String str2 = paramString.substring(i + 1);
/*  827 */       return "hostname=" + str1 + ",port=" + str2;
/*      */     } 
/*  829 */     return "port=" + paramString;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean hasWhitespace(String paramString) {
/*  834 */     int i = paramString.length();
/*  835 */     for (byte b = 0; b < i; b++) {
/*  836 */       if (Character.isWhitespace(paramString.charAt(b))) {
/*  837 */         return true;
/*      */       }
/*      */     } 
/*  840 */     return false;
/*      */   }
/*      */   
/*      */   private static String addArgument(String paramString1, String paramString2) {
/*  844 */     if (hasWhitespace(paramString2) || paramString2.indexOf(',') != -1) {
/*      */       
/*  846 */       StringBuffer stringBuffer = new StringBuffer(paramString1);
/*  847 */       stringBuffer.append('"');
/*  848 */       for (byte b = 0; b < paramString2.length(); b++) {
/*  849 */         char c = paramString2.charAt(b);
/*  850 */         if (c == '"') {
/*  851 */           stringBuffer.append('\\');
/*      */         }
/*  853 */         stringBuffer.append(c);
/*      */       } 
/*  855 */       stringBuffer.append("\" ");
/*  856 */       return stringBuffer.toString();
/*      */     } 
/*  858 */     return paramString1 + paramString2 + ' ';
/*      */   }
/*      */ 
/*      */   
/*      */   public static void main(String[] paramArrayOfString) throws MissingResourceException {
/*  863 */     String str1 = "";
/*  864 */     String str2 = "";
/*  865 */     int i = 0;
/*  866 */     boolean bool = false;
/*  867 */     String str3 = null;
/*      */ 
/*      */     
/*  870 */     MessageOutput.textResources = ResourceBundle.getBundle("com.sun.tools.example.debug.tty.TTYResources", 
/*  871 */         Locale.getDefault());
/*      */     
/*  873 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  874 */       String str = paramArrayOfString[b];
/*  875 */       if (str.equals("-dbgtrace")) {
/*  876 */         if (b == paramArrayOfString.length - 1 || 
/*  877 */           !Character.isDigit(paramArrayOfString[b + 1].charAt(0))) {
/*  878 */           i = 16777215;
/*      */         } else {
/*  880 */           String str4 = "";
/*      */           try {
/*  882 */             str4 = paramArrayOfString[++b];
/*  883 */             i = Integer.decode(str4).intValue();
/*  884 */           } catch (NumberFormatException numberFormatException) {
/*  885 */             usageError("dbgtrace flag value must be an integer:", str4);
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } else {
/*  890 */         if (str.equals("-X")) {
/*  891 */           usageError("Use java minus X to see"); return;
/*      */         } 
/*  893 */         if (str
/*      */           
/*  895 */           .equals("-v") || str.startsWith("-v:") || str
/*  896 */           .startsWith("-verbose") || str
/*  897 */           .startsWith("-D") || str
/*      */ 
/*      */           
/*  900 */           .startsWith("-X") || str
/*      */ 
/*      */           
/*  903 */           .equals("-noasyncgc") || str.equals("-prof") || str
/*  904 */           .equals("-verify") || str.equals("-noverify") || str
/*  905 */           .equals("-verifyremote") || str
/*  906 */           .equals("-verbosegc") || str
/*  907 */           .startsWith("-ms") || str.startsWith("-mx") || str
/*  908 */           .startsWith("-ss") || str.startsWith("-oss"))
/*      */         
/*  910 */         { str2 = addArgument(str2, str); }
/*  911 */         else { if (str.equals("-tclassic")) {
/*  912 */             usageError("Classic VM no longer supported."); return;
/*      */           } 
/*  914 */           if (str.equals("-tclient"))
/*      */           
/*  916 */           { str2 = "-client " + str2; }
/*  917 */           else if (str.equals("-tserver"))
/*      */           
/*  919 */           { str2 = "-server " + str2; }
/*  920 */           else if (str.equals("-sourcepath"))
/*  921 */           { if (b == paramArrayOfString.length - 1) {
/*  922 */               usageError("No sourcepath specified.");
/*      */               return;
/*      */             } 
/*  925 */             Env.setSourcePath(paramArrayOfString[++b]); }
/*  926 */           else if (str.equals("-classpath"))
/*  927 */           { if (b == paramArrayOfString.length - 1) {
/*  928 */               usageError("No classpath specified.");
/*      */               return;
/*      */             } 
/*  931 */             str2 = addArgument(str2, str);
/*  932 */             str2 = addArgument(str2, paramArrayOfString[++b]); }
/*  933 */           else if (str.equals("-attach"))
/*  934 */           { if (str3 != null) {
/*  935 */               usageError("cannot redefine existing connection", str);
/*      */               return;
/*      */             } 
/*  938 */             if (b == paramArrayOfString.length - 1) {
/*  939 */               usageError("No attach address specified.");
/*      */               return;
/*      */             } 
/*  942 */             String str4 = paramArrayOfString[++b];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  950 */             if (supportsSharedMemory()) {
/*  951 */               str3 = "com.sun.jdi.SharedMemoryAttach:name=" + str4;
/*      */             } else {
/*      */               
/*  954 */               String str5 = addressToSocketArgs(str4);
/*  955 */               str3 = "com.sun.jdi.SocketAttach:" + str5;
/*      */             }  }
/*  957 */           else if (str.equals("-listen") || str.equals("-listenany"))
/*  958 */           { if (str3 != null) {
/*  959 */               usageError("cannot redefine existing connection", str);
/*      */               return;
/*      */             } 
/*  962 */             String str4 = null;
/*  963 */             if (str.equals("-listen")) {
/*  964 */               if (b == paramArrayOfString.length - 1) {
/*  965 */                 usageError("No attach address specified.");
/*      */                 return;
/*      */               } 
/*  968 */               str4 = paramArrayOfString[++b];
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  977 */             if (supportsSharedMemory()) {
/*  978 */               str3 = "com.sun.jdi.SharedMemoryListen:";
/*  979 */               if (str4 != null) {
/*  980 */                 str3 = str3 + "name=" + str4;
/*      */               }
/*      */             } else {
/*  983 */               str3 = "com.sun.jdi.SocketListen:";
/*  984 */               if (str4 != null) {
/*  985 */                 str3 = str3 + addressToSocketArgs(str4);
/*      */               }
/*      */             }  }
/*  988 */           else if (str.equals("-launch"))
/*  989 */           { bool = true; }
/*  990 */           else { if (str.equals("-listconnectors")) {
/*  991 */               Commands commands = new Commands();
/*  992 */               commands.commandConnectors(Bootstrap.virtualMachineManager()); return;
/*      */             } 
/*  994 */             if (str.equals("-connect"))
/*      */             
/*      */             { 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1001 */               if (str3 != null) {
/* 1002 */                 usageError("cannot redefine existing connection", str);
/*      */                 return;
/*      */               } 
/* 1005 */               if (b == paramArrayOfString.length - 1) {
/* 1006 */                 usageError("No connect specification.");
/*      */                 return;
/*      */               } 
/* 1009 */               str3 = paramArrayOfString[++b]; }
/* 1010 */             else if (str.equals("-help"))
/* 1011 */             { usage(); }
/* 1012 */             else if (str.equals("-version"))
/* 1013 */             { Commands commands = new Commands();
/* 1014 */               commands.commandVersion("jdb", 
/* 1015 */                   Bootstrap.virtualMachineManager());
/* 1016 */               System.exit(0); }
/* 1017 */             else { if (str.startsWith("-")) {
/* 1018 */                 usageError("invalid option", str);
/*      */                 
/*      */                 return;
/*      */               } 
/* 1022 */               str1 = addArgument("", str);
/* 1023 */               for (; ++b < paramArrayOfString.length; b++) {
/* 1024 */                 str1 = addArgument(str1, paramArrayOfString[b]);
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               break; }
/*      */              }
/*      */            }
/*      */       
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1051 */     if (str3 == null) {
/* 1052 */       str3 = "com.sun.jdi.CommandLineLaunch:";
/* 1053 */     } else if (!str3.endsWith(",") && !str3.endsWith(":")) {
/* 1054 */       str3 = str3 + ",";
/*      */     } 
/*      */     
/* 1057 */     str1 = str1.trim();
/* 1058 */     str2 = str2.trim();
/*      */     
/* 1060 */     if (str1.length() > 0) {
/* 1061 */       if (!str3.startsWith("com.sun.jdi.CommandLineLaunch:")) {
/* 1062 */         usageError("Cannot specify command line with connector:", str3);
/*      */         
/*      */         return;
/*      */       } 
/* 1066 */       str3 = str3 + "main=" + str1 + ",";
/*      */     } 
/*      */     
/* 1069 */     if (str2.length() > 0) {
/* 1070 */       if (!str3.startsWith("com.sun.jdi.CommandLineLaunch:")) {
/* 1071 */         usageError("Cannot specify target vm arguments with connector:", str3);
/*      */         
/*      */         return;
/*      */       } 
/* 1075 */       str3 = str3 + "options=" + str2 + ",";
/*      */     } 
/*      */     
/*      */     try {
/* 1079 */       if (!str3.endsWith(",")) {
/* 1080 */         str3 = str3 + ",";
/*      */       }
/* 1082 */       Env.init(str3, bool, i);
/* 1083 */       new TTY();
/* 1084 */     } catch (Exception exception) {
/* 1085 */       MessageOutput.printException("Internal exception:", exception);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\TTY.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */