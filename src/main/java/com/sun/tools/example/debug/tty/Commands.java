/*      */ package com.sun.tools.example.debug.tty;
/*      */
/*      */ import com.sun.jdi.AbsentInformationException;
/*      */ import com.sun.jdi.ArrayReference;
/*      */ import com.sun.jdi.ArrayType;
/*      */ import com.sun.jdi.ClassType;
/*      */ import com.sun.jdi.Field;
/*      */ import com.sun.jdi.IncompatibleThreadStateException;
/*      */ import com.sun.jdi.InterfaceType;
/*      */ import com.sun.jdi.InvalidTypeException;
/*      */ import com.sun.jdi.InvocationException;
/*      */ import com.sun.jdi.LocalVariable;
/*      */ import com.sun.jdi.Location;
/*      */ import com.sun.jdi.Method;
/*      */ import com.sun.jdi.ObjectReference;
/*      */ import com.sun.jdi.PathSearchingVirtualMachine;
/*      */ import com.sun.jdi.ReferenceType;
/*      */ import com.sun.jdi.StackFrame;
/*      */ import com.sun.jdi.ThreadGroupReference;
/*      */ import com.sun.jdi.ThreadReference;
/*      */ import com.sun.jdi.Value;
/*      */ import com.sun.jdi.VirtualMachineManager;
/*      */ import com.sun.jdi.connect.Connector;
/*      */ import com.sun.jdi.request.EventRequest;
/*      */ import com.sun.jdi.request.EventRequestManager;
/*      */ import com.sun.jdi.request.MethodEntryRequest;
/*      */ import com.sun.jdi.request.MethodExitRequest;
/*      */ import com.sun.jdi.request.StepRequest;
/*      */ import com.sun.tools.example.debug.expr.ExpressionParser;
/*      */ import com.sun.tools.example.debug.expr.ParseException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */
/*      */ class Commands
/*      */ {
/*      */   abstract class AsyncExecution {
/*      */     abstract void action();
/*      */
/*      */     AsyncExecution() {
/*   53 */       execute();
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     void execute() {
/*   60 */       final ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*   61 */       final boolean stackFrame = (threadInfo == null) ? false : threadInfo.getCurrentFrameIndex();
/*   62 */       Thread thread = new Thread("asynchronous jdb command")
/*      */         {
/*      */           public void run() {
/*      */             try {
/*   66 */               AsyncExecution.this.action();
/*   67 */             } catch (UnsupportedOperationException unsupportedOperationException) {
/*      */
/*   69 */               MessageOutput.println("Operation is not supported on the target VM");
/*   70 */             } catch (Exception exception) {
/*   71 */               MessageOutput.println("Internal exception during operation:", exception
/*   72 */                   .getMessage());
/*      */
/*      */
/*      */             }
/*      */             finally {
/*      */
/*      */
/*   79 */               if (threadInfo != null) {
/*   80 */                 ThreadInfo.setCurrentThreadInfo(threadInfo);
/*      */                 try {
/*   82 */                   threadInfo.setCurrentFrameIndex(stackFrame);
/*   83 */                 } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*   84 */                   MessageOutput.println("Current thread isnt suspended.");
/*   85 */                 } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*   86 */                   MessageOutput.println("Requested stack frame is no longer active:", new Object[] { new Integer(this.val$stackFrame) });
/*      */                 }
/*      */               }
/*      */
/*   90 */               MessageOutput.printPrompt();
/*      */             }
/*      */           }
/*      */         };
/*   94 */       thread.start();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   private Value evaluate(String paramString) {
/*  102 */     Value value = null;
/*  103 */     ExpressionParser.GetFrame getFrame = null;
/*      */     try {
/*  105 */       final ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  106 */       if (threadInfo != null && threadInfo.getCurrentFrame() != null) {
/*  107 */         getFrame = new ExpressionParser.GetFrame()
/*      */           {
/*      */             public StackFrame get() throws IncompatibleThreadStateException {
/*  110 */               return threadInfo.getCurrentFrame();
/*      */             }
/*      */           };
/*      */       }
/*  114 */       value = ExpressionParser.evaluate(paramString, Env.vm(), getFrame);
/*  115 */     } catch (InvocationException invocationException) {
/*  116 */       MessageOutput.println("Exception in expression:", invocationException
/*  117 */           .exception().referenceType().name());
/*  118 */     } catch (Exception exception) {
/*  119 */       String str = exception.getMessage();
/*  120 */       if (str == null) {
/*  121 */         MessageOutput.printException(str, exception);
/*      */       } else {
/*      */         String str1;
/*      */         try {
/*  125 */           str1 = MessageOutput.format(str);
/*  126 */         } catch (MissingResourceException missingResourceException) {
/*  127 */           str1 = exception.toString();
/*      */         }
/*  129 */         MessageOutput.printDirectln(str1);
/*      */       }
/*      */     }
/*  132 */     return value;
/*      */   }
/*      */
/*      */   private String getStringValue() {
/*  136 */     Value value = null;
/*  137 */     String str = null;
/*      */     try {
/*  139 */       value = ExpressionParser.getMassagedValue();
/*  140 */       str = value.toString();
/*  141 */     } catch (ParseException parseException) {
/*  142 */       String str1 = parseException.getMessage();
/*  143 */       if (str1 == null) {
/*  144 */         MessageOutput.printException(str1, (Exception)parseException);
/*      */       } else {
/*      */         String str2;
/*      */         try {
/*  148 */           str2 = MessageOutput.format(str1);
/*  149 */         } catch (MissingResourceException missingResourceException) {
/*  150 */           str2 = parseException.toString();
/*      */         }
/*  152 */         MessageOutput.printDirectln(str2);
/*      */       }
/*      */     }
/*  155 */     return str;
/*      */   }
/*      */
/*      */   private ThreadInfo doGetThread(String paramString) {
/*  159 */     ThreadInfo threadInfo = ThreadInfo.getThreadInfo(paramString);
/*  160 */     if (threadInfo == null) {
/*  161 */       MessageOutput.println("is not a valid thread id", paramString);
/*      */     }
/*  163 */     return threadInfo;
/*      */   }
/*      */
/*      */   String typedName(Method paramMethod) {
/*  167 */     StringBuffer stringBuffer = new StringBuffer();
/*  168 */     stringBuffer.append(paramMethod.name());
/*  169 */     stringBuffer.append("(");
/*      */
/*  171 */     List<String> list = paramMethod.argumentTypeNames();
/*  172 */     int i = list.size() - 1;
/*      */
/*  174 */     for (byte b = 0; b < i; b++) {
/*  175 */       stringBuffer.append(list.get(b));
/*  176 */       stringBuffer.append(", ");
/*      */     }
/*  178 */     if (i >= 0) {
/*      */
/*  180 */       String str = list.get(i);
/*  181 */       if (paramMethod.isVarArgs()) {
/*      */
/*  183 */         stringBuffer.append(str.substring(0, str.length() - 2));
/*  184 */         stringBuffer.append("...");
/*      */       } else {
/*  186 */         stringBuffer.append(str);
/*      */       }
/*      */     }
/*  189 */     stringBuffer.append(")");
/*  190 */     return stringBuffer.toString();
/*      */   }
/*      */
/*      */   void commandConnectors(VirtualMachineManager paramVirtualMachineManager) {
/*  194 */     List list = paramVirtualMachineManager.allConnectors();
/*  195 */     if (list.isEmpty()) {
/*  196 */       MessageOutput.println("Connectors available");
/*      */     }
/*  198 */     for (Connector connector : list) {
/*      */
/*  200 */       String str = (connector.transport() == null) ? "null" : connector.transport().name();
/*  201 */       MessageOutput.println();
/*  202 */       MessageOutput.println("Connector and Transport name", new Object[] { connector
/*  203 */             .name(), str });
/*  204 */       MessageOutput.println("Connector description", connector.description());
/*      */
/*  206 */       for (Connector.Argument argument : connector.defaultArguments().values()) {
/*  207 */         MessageOutput.println();
/*      */
/*  209 */         boolean bool = argument.mustSpecify();
/*  210 */         if (argument.value() == null || argument.value() == "") {
/*      */
/*  212 */           MessageOutput.println(bool ? "Connector required argument nodefault" : "Connector argument nodefault", argument
/*      */
/*  214 */               .name());
/*      */         } else {
/*  216 */           MessageOutput.println(bool ? "Connector required argument default" : "Connector argument default", new Object[] { argument
/*      */
/*      */
/*  219 */                 .name(), argument.value() });
/*      */         }
/*  221 */         MessageOutput.println("Connector description", argument.description());
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void commandClasses() {
/*  229 */     StringBuffer stringBuffer = new StringBuffer();
/*  230 */     for (ReferenceType referenceType : Env.vm().allClasses()) {
/*  231 */       stringBuffer.append(referenceType.name());
/*  232 */       stringBuffer.append("\n");
/*      */     }
/*  234 */     MessageOutput.print("** classes list **", stringBuffer.toString());
/*      */   }
/*      */
/*      */
/*      */   void commandClass(StringTokenizer paramStringTokenizer) {
/*  239 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  240 */       MessageOutput.println("No class specified.");
/*      */
/*      */       return;
/*      */     }
/*  244 */     String str = paramStringTokenizer.nextToken();
/*  245 */     boolean bool = false;
/*      */
/*  247 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  248 */       if (paramStringTokenizer.nextToken().toLowerCase().equals("all")) {
/*  249 */         bool = true;
/*      */       } else {
/*  251 */         MessageOutput.println("Invalid option on class command");
/*      */         return;
/*      */       }
/*      */     }
/*  255 */     ReferenceType referenceType = Env.getReferenceTypeFromToken(str);
/*  256 */     if (referenceType == null) {
/*  257 */       MessageOutput.println("is not a valid id or class name", str);
/*      */       return;
/*      */     }
/*  260 */     if (referenceType instanceof ClassType) {
/*  261 */       ClassType classType1 = (ClassType)referenceType;
/*  262 */       MessageOutput.println("Class:", classType1.name());
/*      */
/*  264 */       ClassType classType2 = classType1.superclass();
/*  265 */       while (classType2 != null) {
/*  266 */         MessageOutput.println("extends:", classType2.name());
/*  267 */         classType2 = bool ? classType2.superclass() : null;
/*      */       }
/*      */
/*      */
/*  271 */       List list = bool ? classType1.allInterfaces() : classType1.interfaces();
/*  272 */       for (InterfaceType interfaceType : list) {
/*  273 */         MessageOutput.println("implements:", interfaceType.name());
/*      */       }
/*      */
/*  276 */       for (ClassType classType : classType1.subclasses()) {
/*  277 */         MessageOutput.println("subclass:", classType.name());
/*      */       }
/*  279 */       for (ReferenceType referenceType1 : classType1.nestedTypes()) {
/*  280 */         MessageOutput.println("nested:", referenceType1.name());
/*      */       }
/*  282 */     } else if (referenceType instanceof InterfaceType) {
/*  283 */       InterfaceType interfaceType = (InterfaceType)referenceType;
/*  284 */       MessageOutput.println("Interface:", interfaceType.name());
/*  285 */       for (InterfaceType interfaceType1 : interfaceType.superinterfaces()) {
/*  286 */         MessageOutput.println("extends:", interfaceType1.name());
/*      */       }
/*  288 */       for (InterfaceType interfaceType1 : interfaceType.subinterfaces()) {
/*  289 */         MessageOutput.println("subinterface:", interfaceType1.name());
/*      */       }
/*  291 */       for (ClassType classType : interfaceType.implementors()) {
/*  292 */         MessageOutput.println("implementor:", classType.name());
/*      */       }
/*  294 */       for (ReferenceType referenceType1 : interfaceType.nestedTypes()) {
/*  295 */         MessageOutput.println("nested:", referenceType1.name());
/*      */       }
/*      */     } else {
/*  298 */       ArrayType arrayType = (ArrayType)referenceType;
/*  299 */       MessageOutput.println("Array:", arrayType.name());
/*      */     }
/*      */   }
/*      */
/*      */   void commandMethods(StringTokenizer paramStringTokenizer) {
/*  304 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  305 */       MessageOutput.println("No class specified.");
/*      */
/*      */       return;
/*      */     }
/*  309 */     String str = paramStringTokenizer.nextToken();
/*  310 */     ReferenceType referenceType = Env.getReferenceTypeFromToken(str);
/*  311 */     if (referenceType != null) {
/*  312 */       StringBuffer stringBuffer = new StringBuffer();
/*  313 */       for (Method method : referenceType.allMethods()) {
/*  314 */         stringBuffer.append(method.declaringType().name());
/*  315 */         stringBuffer.append(" ");
/*  316 */         stringBuffer.append(typedName(method));
/*  317 */         stringBuffer.append('\n');
/*      */       }
/*  319 */       MessageOutput.print("** methods list **", stringBuffer.toString());
/*      */     } else {
/*  321 */       MessageOutput.println("is not a valid id or class name", str);
/*      */     }
/*      */   }
/*      */
/*      */   void commandFields(StringTokenizer paramStringTokenizer) {
/*  326 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  327 */       MessageOutput.println("No class specified.");
/*      */
/*      */       return;
/*      */     }
/*  331 */     String str = paramStringTokenizer.nextToken();
/*  332 */     ReferenceType referenceType = Env.getReferenceTypeFromToken(str);
/*  333 */     if (referenceType != null) {
/*  334 */       List list1 = referenceType.allFields();
/*  335 */       List list2 = referenceType.visibleFields();
/*  336 */       StringBuffer stringBuffer = new StringBuffer();
/*  337 */       for (Field field : list1) {
/*      */         String str1;
/*  339 */         if (!list2.contains(field)) {
/*  340 */           str1 = MessageOutput.format("list field typename and name hidden", new Object[] { field
/*  341 */                 .typeName(), field
/*  342 */                 .name() });
/*  343 */         } else if (!field.declaringType().equals(referenceType)) {
/*  344 */           str1 = MessageOutput.format("list field typename and name inherited", new Object[] { field
/*  345 */                 .typeName(), field
/*  346 */                 .name(), field
/*  347 */                 .declaringType().name() });
/*      */         } else {
/*  349 */           str1 = MessageOutput.format("list field typename and name", new Object[] { field
/*  350 */                 .typeName(), field
/*  351 */                 .name() });
/*      */         }
/*  353 */         stringBuffer.append(str1);
/*      */       }
/*  355 */       MessageOutput.print("** fields list **", stringBuffer.toString());
/*      */     } else {
/*  357 */       MessageOutput.println("is not a valid id or class name", str);
/*      */     }
/*      */   }
/*      */
/*      */   private void printThreadGroup(ThreadGroupReference paramThreadGroupReference) {
/*  362 */     ThreadIterator threadIterator = new ThreadIterator(paramThreadGroupReference);
/*      */
/*  364 */     MessageOutput.println("Thread Group:", paramThreadGroupReference.name());
/*  365 */     int i = 0;
/*  366 */     int j = 0;
/*  367 */     while (threadIterator.hasNext()) {
/*  368 */       ThreadReference threadReference = threadIterator.next();
/*  369 */       i = Math.max(i,
/*  370 */           Env.description((ObjectReference)threadReference).length());
/*  371 */       j = Math.max(j, threadReference
/*  372 */           .name().length());
/*      */     }
/*      */
/*  375 */     threadIterator = new ThreadIterator(paramThreadGroupReference);
/*  376 */     while (threadIterator.hasNext()) {
/*  377 */       String str; ThreadReference threadReference = threadIterator.next();
/*  378 */       if (threadReference.threadGroup() == null) {
/*      */         continue;
/*      */       }
/*      */
/*  382 */       if (!threadReference.threadGroup().equals(paramThreadGroupReference)) {
/*  383 */         paramThreadGroupReference = threadReference.threadGroup();
/*  384 */         MessageOutput.println("Thread Group:", paramThreadGroupReference.name());
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  394 */       StringBuffer stringBuffer1 = new StringBuffer(Env.description((ObjectReference)threadReference));
/*  395 */       for (int k = stringBuffer1.length(); k < i; k++) {
/*  396 */         stringBuffer1.append(" ");
/*      */       }
/*  398 */       StringBuffer stringBuffer2 = new StringBuffer(threadReference.name());
/*  399 */       for (int m = stringBuffer2.length(); m < j; m++) {
/*  400 */         stringBuffer2.append(" ");
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*  408 */       switch (threadReference.status()) {
/*      */         case -1:
/*  410 */           if (threadReference.isAtBreakpoint()) {
/*  411 */             String str1 = "Thread description name unknownStatus BP"; break;
/*      */           }
/*  413 */           str = "Thread description name unknownStatus";
/*      */           break;
/*      */
/*      */         case 0:
/*  417 */           if (threadReference.isAtBreakpoint()) {
/*  418 */             str = "Thread description name zombieStatus BP"; break;
/*      */           }
/*  420 */           str = "Thread description name zombieStatus";
/*      */           break;
/*      */
/*      */         case 1:
/*  424 */           if (threadReference.isAtBreakpoint()) {
/*  425 */             str = "Thread description name runningStatus BP"; break;
/*      */           }
/*  427 */           str = "Thread description name runningStatus";
/*      */           break;
/*      */
/*      */         case 2:
/*  431 */           if (threadReference.isAtBreakpoint()) {
/*  432 */             str = "Thread description name sleepingStatus BP"; break;
/*      */           }
/*  434 */           str = "Thread description name sleepingStatus";
/*      */           break;
/*      */
/*      */         case 3:
/*  438 */           if (threadReference.isAtBreakpoint()) {
/*  439 */             str = "Thread description name waitingStatus BP"; break;
/*      */           }
/*  441 */           str = "Thread description name waitingStatus";
/*      */           break;
/*      */
/*      */         case 4:
/*  445 */           if (threadReference.isAtBreakpoint()) {
/*  446 */             str = "Thread description name condWaitstatus BP"; break;
/*      */           }
/*  448 */           str = "Thread description name condWaitstatus";
/*      */           break;
/*      */
/*      */         default:
/*  452 */           throw new InternalError(MessageOutput.format("Invalid thread status."));
/*      */       }
/*  454 */       MessageOutput.println(str, new Object[] { stringBuffer1
/*  455 */             .toString(), stringBuffer2
/*  456 */             .toString() });
/*      */     }
/*      */   }
/*      */
/*      */   void commandThreads(StringTokenizer paramStringTokenizer) {
/*  461 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  462 */       printThreadGroup(ThreadInfo.group());
/*      */       return;
/*      */     }
/*  465 */     String str = paramStringTokenizer.nextToken();
/*  466 */     ThreadGroupReference threadGroupReference = ThreadGroupIterator.find(str);
/*  467 */     if (threadGroupReference == null) {
/*  468 */       MessageOutput.println("is not a valid threadgroup name", str);
/*      */     } else {
/*  470 */       printThreadGroup(threadGroupReference);
/*      */     }
/*      */   }
/*      */
/*      */   void commandThreadGroups() {
/*  475 */     ThreadGroupIterator threadGroupIterator = new ThreadGroupIterator();
/*  476 */     byte b = 0;
/*  477 */     while (threadGroupIterator.hasNext()) {
/*  478 */       ThreadGroupReference threadGroupReference = threadGroupIterator.nextThreadGroup();
/*  479 */       b++;
/*  480 */       MessageOutput.println("thread group number description name", new Object[] { new Integer(b),
/*      */
/*  482 */             Env.description((ObjectReference)threadGroupReference), threadGroupReference
/*  483 */             .name() });
/*      */     }
/*      */   }
/*      */
/*      */   void commandThread(StringTokenizer paramStringTokenizer) {
/*  488 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  489 */       MessageOutput.println("Thread number not specified.");
/*      */       return;
/*      */     }
/*  492 */     ThreadInfo threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  493 */     if (threadInfo != null) {
/*  494 */       ThreadInfo.setCurrentThreadInfo(threadInfo);
/*      */     }
/*      */   }
/*      */
/*      */   void commandThreadGroup(StringTokenizer paramStringTokenizer) {
/*  499 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  500 */       MessageOutput.println("Threadgroup name not specified.");
/*      */       return;
/*      */     }
/*  503 */     String str = paramStringTokenizer.nextToken();
/*  504 */     ThreadGroupReference threadGroupReference = ThreadGroupIterator.find(str);
/*  505 */     if (threadGroupReference == null) {
/*  506 */       MessageOutput.println("is not a valid threadgroup name", str);
/*      */     } else {
/*  508 */       ThreadInfo.setThreadGroup(threadGroupReference);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void commandRun(StringTokenizer paramStringTokenizer) {
/*      */     String str;
/*  521 */     VMConnection vMConnection = Env.connection();
/*  522 */     if (!vMConnection.isLaunch()) {
/*  523 */       if (!paramStringTokenizer.hasMoreTokens()) {
/*  524 */         commandCont();
/*      */       } else {
/*  526 */         MessageOutput.println("run <args> command is valid only with launched VMs");
/*      */       }
/*      */       return;
/*      */     }
/*  530 */     if (vMConnection.isOpen()) {
/*  531 */       MessageOutput.println("VM already running. use cont to continue after events.");
/*      */
/*      */
/*      */
/*      */       return;
/*      */     }
/*      */
/*      */
/*      */
/*  540 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  541 */       str = paramStringTokenizer.nextToken("");
/*  542 */       boolean bool = vMConnection.setConnectorArg("main", str);
/*  543 */       if (!bool) {
/*  544 */         MessageOutput.println("Unable to set main class and arguments");
/*      */         return;
/*      */       }
/*      */     } else {
/*  548 */       str = vMConnection.connectorArg("main");
/*  549 */       if (str.length() == 0) {
/*  550 */         MessageOutput.println("Main class and arguments must be specified");
/*      */         return;
/*      */       }
/*      */     }
/*  554 */     MessageOutput.println("run", str);
/*      */
/*      */
/*      */
/*      */
/*  559 */     vMConnection.open();
/*      */   }
/*      */
/*      */
/*      */   void commandLoad(StringTokenizer paramStringTokenizer) {
/*  564 */     MessageOutput.println("The load command is no longer supported.");
/*      */   }
/*      */
/*      */   private List<ThreadReference> allThreads(ThreadGroupReference paramThreadGroupReference) {
/*  568 */     ArrayList<ThreadReference> arrayList = new ArrayList();
/*  569 */     arrayList.addAll(paramThreadGroupReference.threads());
/*  570 */     for (ThreadGroupReference threadGroupReference : paramThreadGroupReference.threadGroups()) {
/*  571 */       arrayList.addAll(allThreads(threadGroupReference));
/*      */     }
/*  573 */     return arrayList;
/*      */   }
/*      */
/*      */   void commandSuspend(StringTokenizer paramStringTokenizer) {
/*  577 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  578 */       Env.vm().suspend();
/*  579 */       MessageOutput.println("All threads suspended.");
/*      */     } else {
/*  581 */       while (paramStringTokenizer.hasMoreTokens()) {
/*  582 */         ThreadInfo threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  583 */         if (threadInfo != null) {
/*  584 */           threadInfo.getThread().suspend();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandResume(StringTokenizer paramStringTokenizer) {
/*  591 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  592 */       ThreadInfo.invalidateAll();
/*  593 */       Env.vm().resume();
/*  594 */       MessageOutput.println("All threads resumed.");
/*      */     } else {
/*  596 */       while (paramStringTokenizer.hasMoreTokens()) {
/*  597 */         ThreadInfo threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  598 */         if (threadInfo != null) {
/*  599 */           threadInfo.invalidate();
/*  600 */           threadInfo.getThread().resume();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandCont() {
/*  607 */     if (ThreadInfo.getCurrentThreadInfo() == null) {
/*  608 */       MessageOutput.println("Nothing suspended.");
/*      */       return;
/*      */     }
/*  611 */     ThreadInfo.invalidateAll();
/*  612 */     Env.vm().resume();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void clearPreviousStep(ThreadReference paramThreadReference) {
/*  620 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/*  621 */     for (StepRequest stepRequest : eventRequestManager.stepRequests()) {
/*  622 */       if (stepRequest.thread().equals(paramThreadReference)) {
/*  623 */         eventRequestManager.deleteEventRequest((EventRequest)stepRequest);
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void commandStep(StringTokenizer paramStringTokenizer) {
/*      */     boolean bool;
/*  632 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  633 */     if (threadInfo == null) {
/*  634 */       MessageOutput.println("Nothing suspended.");
/*      */
/*      */       return;
/*      */     }
/*  638 */     if (paramStringTokenizer.hasMoreTokens() && paramStringTokenizer
/*  639 */       .nextToken().toLowerCase().equals("up")) {
/*  640 */       bool = true;
/*      */     } else {
/*  642 */       bool = true;
/*      */     }
/*      */
/*  645 */     clearPreviousStep(threadInfo.getThread());
/*  646 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/*  647 */     StepRequest stepRequest = eventRequestManager.createStepRequest(threadInfo.getThread(), -2, bool);
/*      */
/*  649 */     if (bool == true) {
/*  650 */       Env.addExcludes(stepRequest);
/*      */     }
/*      */
/*  653 */     stepRequest.addCountFilter(1);
/*  654 */     stepRequest.enable();
/*  655 */     ThreadInfo.invalidateAll();
/*  656 */     Env.vm().resume();
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void commandStepi() {
/*  663 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  664 */     if (threadInfo == null) {
/*  665 */       MessageOutput.println("Nothing suspended.");
/*      */       return;
/*      */     }
/*  668 */     clearPreviousStep(threadInfo.getThread());
/*  669 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/*  670 */     StepRequest stepRequest = eventRequestManager.createStepRequest(threadInfo.getThread(), -1, 1);
/*      */
/*      */
/*  673 */     Env.addExcludes(stepRequest);
/*      */
/*  675 */     stepRequest.addCountFilter(1);
/*  676 */     stepRequest.enable();
/*  677 */     ThreadInfo.invalidateAll();
/*  678 */     Env.vm().resume();
/*      */   }
/*      */
/*      */   void commandNext() {
/*  682 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  683 */     if (threadInfo == null) {
/*  684 */       MessageOutput.println("Nothing suspended.");
/*      */       return;
/*      */     }
/*  687 */     clearPreviousStep(threadInfo.getThread());
/*  688 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/*  689 */     StepRequest stepRequest = eventRequestManager.createStepRequest(threadInfo.getThread(), -2, 2);
/*      */
/*      */
/*  692 */     Env.addExcludes(stepRequest);
/*      */
/*  694 */     stepRequest.addCountFilter(1);
/*  695 */     stepRequest.enable();
/*  696 */     ThreadInfo.invalidateAll();
/*  697 */     Env.vm().resume();
/*      */   }
/*      */
/*      */   void doKill(ThreadReference paramThreadReference, StringTokenizer paramStringTokenizer) {
/*  701 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  702 */       MessageOutput.println("No exception object specified.");
/*      */       return;
/*      */     }
/*  705 */     String str = paramStringTokenizer.nextToken("");
/*  706 */     Value value = evaluate(str);
/*  707 */     if (value != null && value instanceof ObjectReference) {
/*      */       try {
/*  709 */         paramThreadReference.stop((ObjectReference)value);
/*  710 */         MessageOutput.println("killed", paramThreadReference.toString());
/*  711 */       } catch (InvalidTypeException invalidTypeException) {
/*  712 */         MessageOutput.println("Invalid exception object");
/*      */       }
/*      */     } else {
/*  715 */       MessageOutput.println("Expression must evaluate to an object");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void doKillThread(final ThreadReference threadToKill, final StringTokenizer tokenizer) {
/*  721 */     new AsyncExecution()
/*      */       {
/*      */         void action() {
/*  724 */           Commands.this.doKill(threadToKill, tokenizer);
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   void commandKill(StringTokenizer paramStringTokenizer) {
/*  730 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  731 */       MessageOutput.println("Usage: kill <thread id> <throwable>");
/*      */       return;
/*      */     }
/*  734 */     ThreadInfo threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  735 */     if (threadInfo != null) {
/*  736 */       MessageOutput.println("killing thread:", threadInfo.getThread().name());
/*  737 */       doKillThread(threadInfo.getThread(), paramStringTokenizer);
/*      */       return;
/*      */     }
/*      */   }
/*      */
/*      */   void listCaughtExceptions() {
/*  743 */     boolean bool = true;
/*      */
/*      */
/*  746 */     for (EventRequestSpec eventRequestSpec : Env.specList.eventRequestSpecs()) {
/*  747 */       if (eventRequestSpec instanceof ExceptionSpec) {
/*  748 */         if (bool) {
/*  749 */           bool = false;
/*  750 */           MessageOutput.println("Exceptions caught:");
/*      */         }
/*  752 */         MessageOutput.println("tab", eventRequestSpec.toString());
/*      */       }
/*      */     }
/*  755 */     if (bool) {
/*  756 */       MessageOutput.println("No exceptions caught.");
/*      */     }
/*      */   }
/*      */
/*      */   private EventRequestSpec parseExceptionSpec(StringTokenizer paramStringTokenizer) {
/*  761 */     String str1 = paramStringTokenizer.nextToken();
/*  762 */     boolean bool1 = false;
/*  763 */     boolean bool2 = false;
/*  764 */     EventRequestSpec eventRequestSpec = null;
/*  765 */     String str2 = null;
/*      */
/*  767 */     if (str1.equals("uncaught")) {
/*  768 */       bool1 = false;
/*  769 */       bool2 = true;
/*  770 */     } else if (str1.equals("caught")) {
/*  771 */       bool1 = true;
/*  772 */       bool2 = false;
/*  773 */     } else if (str1.equals("all")) {
/*  774 */       bool1 = true;
/*  775 */       bool2 = true;
/*      */
/*      */
/*      */
/*      */     }
/*      */     else {
/*      */
/*      */
/*      */
/*      */
/*  785 */       bool1 = true;
/*  786 */       bool2 = true;
/*  787 */       str2 = str1;
/*      */     }
/*  789 */     if (str2 == null && paramStringTokenizer.hasMoreTokens()) {
/*  790 */       str2 = paramStringTokenizer.nextToken();
/*      */     }
/*  792 */     if (str2 != null && (bool1 || bool2)) {
/*      */       try {
/*  794 */         eventRequestSpec = Env.specList.createExceptionCatch(str2, bool1, bool2);
/*      */
/*      */       }
/*  797 */       catch (ClassNotFoundException classNotFoundException) {
/*  798 */         MessageOutput.println("is not a valid class name", str2);
/*      */       }
/*      */     }
/*  801 */     return eventRequestSpec;
/*      */   }
/*      */
/*      */   void commandCatchException(StringTokenizer paramStringTokenizer) {
/*  805 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  806 */       listCaughtExceptions();
/*      */     } else {
/*  808 */       EventRequestSpec eventRequestSpec = parseExceptionSpec(paramStringTokenizer);
/*  809 */       if (eventRequestSpec != null) {
/*  810 */         resolveNow(eventRequestSpec);
/*      */       } else {
/*  812 */         MessageOutput.println("Usage: catch exception");
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandIgnoreException(StringTokenizer paramStringTokenizer) {
/*  818 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  819 */       listCaughtExceptions();
/*      */     } else {
/*  821 */       EventRequestSpec eventRequestSpec = parseExceptionSpec(paramStringTokenizer);
/*  822 */       if (Env.specList.delete(eventRequestSpec)) {
/*  823 */         MessageOutput.println("Removed:", eventRequestSpec.toString());
/*      */       } else {
/*  825 */         if (eventRequestSpec != null) {
/*  826 */           MessageOutput.println("Not found:", eventRequestSpec.toString());
/*      */         }
/*  828 */         MessageOutput.println("Usage: ignore exception");
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandUp(StringTokenizer paramStringTokenizer) {
/*  834 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  835 */     if (threadInfo == null) {
/*  836 */       MessageOutput.println("Current thread not set.");
/*      */
/*      */       return;
/*      */     }
/*  840 */     boolean bool = true;
/*  841 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  842 */       boolean bool1; String str = paramStringTokenizer.nextToken();
/*      */
/*      */       try {
/*  845 */         NumberFormat numberFormat = NumberFormat.getNumberInstance();
/*  846 */         numberFormat.setParseIntegerOnly(true);
/*  847 */         Number number = numberFormat.parse(str);
/*  848 */         bool1 = number.intValue();
/*  849 */       } catch (ParseException parseException) {
/*  850 */         bool1 = false;
/*      */       }
/*  852 */       if (bool1) {
/*  853 */         MessageOutput.println("Usage: up [n frames]");
/*      */         return;
/*      */       }
/*  856 */       bool = bool1;
/*      */     }
/*      */
/*      */     try {
/*  860 */       threadInfo.up(bool);
/*  861 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  862 */       MessageOutput.println("Current thread isnt suspended.");
/*  863 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*  864 */       MessageOutput.println("End of stack.");
/*      */     }
/*      */   }
/*      */
/*      */   void commandDown(StringTokenizer paramStringTokenizer) {
/*  869 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  870 */     if (threadInfo == null) {
/*  871 */       MessageOutput.println("Current thread not set.");
/*      */
/*      */       return;
/*      */     }
/*  875 */     boolean bool = true;
/*  876 */     if (paramStringTokenizer.hasMoreTokens()) {
/*  877 */       boolean bool1; String str = paramStringTokenizer.nextToken();
/*      */
/*      */       try {
/*  880 */         NumberFormat numberFormat = NumberFormat.getNumberInstance();
/*  881 */         numberFormat.setParseIntegerOnly(true);
/*  882 */         Number number = numberFormat.parse(str);
/*  883 */         bool1 = number.intValue();
/*  884 */       } catch (ParseException parseException) {
/*  885 */         bool1 = false;
/*      */       }
/*  887 */       if (bool1) {
/*  888 */         MessageOutput.println("Usage: down [n frames]");
/*      */         return;
/*      */       }
/*  891 */       bool = bool1;
/*      */     }
/*      */
/*      */     try {
/*  895 */       threadInfo.down(bool);
/*  896 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  897 */       MessageOutput.println("Current thread isnt suspended.");
/*  898 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*  899 */       MessageOutput.println("End of stack.");
/*      */     }
/*      */   }
/*      */
/*      */   private void dumpStack(ThreadInfo paramThreadInfo, boolean paramBoolean) {
/*  904 */     List<StackFrame> list = null;
/*      */     try {
/*  906 */       list = paramThreadInfo.getStack();
/*  907 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/*  908 */       MessageOutput.println("Current thread isnt suspended.");
/*      */       return;
/*      */     }
/*  911 */     if (list == null) {
/*  912 */       MessageOutput.println("Thread is not running (no stack).");
/*      */     } else {
/*  914 */       int i = list.size();
/*  915 */       for (int j = paramThreadInfo.getCurrentFrameIndex(); j < i; j++) {
/*  916 */         StackFrame stackFrame = list.get(j);
/*  917 */         dumpFrame(j, paramBoolean, stackFrame);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private void dumpFrame(int paramInt, boolean paramBoolean, StackFrame paramStackFrame) {
/*  923 */     Location location = paramStackFrame.location();
/*  924 */     long l1 = -1L;
/*  925 */     if (paramBoolean) {
/*  926 */       l1 = location.codeIndex();
/*      */     }
/*  928 */     Method method = location.method();
/*      */
/*  930 */     long l2 = location.lineNumber();
/*  931 */     String str = null;
/*  932 */     if (method.isNative()) {
/*  933 */       str = MessageOutput.format("native method");
/*  934 */     } else if (l2 != -1L) {
/*      */
/*      */       try {
/*  937 */         str = location.sourceName() + MessageOutput.format("line number", new Object[] { new Long(l2) });
/*      */       }
/*  939 */       catch (AbsentInformationException absentInformationException) {
/*  940 */         str = MessageOutput.format("unknown");
/*      */       }
/*      */     }
/*  943 */     if (l1 != -1L) {
/*  944 */       MessageOutput.println("stack frame dump with pc", new Object[] { new Integer(paramInt + 1), method
/*      */
/*  946 */             .declaringType().name(), method
/*  947 */             .name(), str, new Long(l1) });
/*      */     }
/*      */     else {
/*      */
/*  951 */       MessageOutput.println("stack frame dump", new Object[] { new Integer(paramInt + 1), method
/*      */
/*  953 */             .declaringType().name(), method
/*  954 */             .name(), str });
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void commandWhere(StringTokenizer paramStringTokenizer, boolean paramBoolean) {
/*  960 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  961 */       ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  962 */       if (threadInfo == null) {
/*  963 */         MessageOutput.println("No thread specified.");
/*      */         return;
/*      */       }
/*  966 */       dumpStack(threadInfo, paramBoolean);
/*      */     } else {
/*  968 */       String str = paramStringTokenizer.nextToken();
/*  969 */       if (str.toLowerCase().equals("all")) {
/*  970 */         for (ThreadInfo threadInfo : ThreadInfo.threads()) {
/*  971 */           MessageOutput.println("Thread:", threadInfo
/*  972 */               .getThread().name());
/*  973 */           dumpStack(threadInfo, paramBoolean);
/*      */         }
/*      */       } else {
/*  976 */         ThreadInfo threadInfo = doGetThread(str);
/*  977 */         if (threadInfo != null) {
/*  978 */           ThreadInfo.setCurrentThreadInfo(threadInfo);
/*  979 */           dumpStack(threadInfo, paramBoolean);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandInterrupt(StringTokenizer paramStringTokenizer) {
/*  986 */     if (!paramStringTokenizer.hasMoreTokens()) {
/*  987 */       ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/*  988 */       if (threadInfo == null) {
/*  989 */         MessageOutput.println("No thread specified.");
/*      */         return;
/*      */       }
/*  992 */       threadInfo.getThread().interrupt();
/*      */     } else {
/*  994 */       ThreadInfo threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*  995 */       if (threadInfo != null) {
/*  996 */         threadInfo.getThread().interrupt();
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandMemory() {
/* 1002 */     MessageOutput.println("The memory command is no longer supported.");
/*      */   }
/*      */
/*      */   void commandGC() {
/* 1006 */     MessageOutput.println("The gc command is no longer necessary.");
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   static String locationString(Location paramLocation) {
/* 1014 */     return MessageOutput.format("locationString", new Object[] { paramLocation
/* 1015 */           .declaringType().name(), paramLocation
/* 1016 */           .method().name(), new Integer(paramLocation
/* 1017 */             .lineNumber()), new Long(paramLocation
/* 1018 */             .codeIndex()) });
/*      */   }
/*      */
/*      */   void listBreakpoints() {
/* 1022 */     boolean bool = true;
/*      */
/*      */
/* 1025 */     for (EventRequestSpec eventRequestSpec : Env.specList.eventRequestSpecs()) {
/* 1026 */       if (eventRequestSpec instanceof BreakpointSpec) {
/* 1027 */         if (bool) {
/* 1028 */           bool = false;
/* 1029 */           MessageOutput.println("Breakpoints set:");
/*      */         }
/* 1031 */         MessageOutput.println("tab", eventRequestSpec.toString());
/*      */       }
/*      */     }
/* 1034 */     if (bool) {
/* 1035 */       MessageOutput.println("No breakpoints set.");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void printBreakpointCommandUsage(String paramString1, String paramString2) {
/* 1041 */     MessageOutput.println("printbreakpointcommandusage", new Object[] { paramString1, paramString2 });
/*      */   }
/*      */
/*      */
/*      */
/*      */   protected BreakpointSpec parseBreakpointSpec(StringTokenizer paramStringTokenizer, String paramString1, String paramString2) {
/* 1047 */     BreakpointSpec breakpointSpec = null;
/*      */     try {
/* 1049 */       String str2, str1 = paramStringTokenizer.nextToken(":( \t\n\r");
/*      */
/*      */
/*      */
/*      */
/*      */       try {
/* 1055 */         str2 = paramStringTokenizer.nextToken("").trim();
/* 1056 */       } catch (NoSuchElementException noSuchElementException) {
/* 1057 */         str2 = null;
/*      */       }
/*      */
/* 1060 */       if (str2 != null && str2.startsWith(":")) {
/* 1061 */         paramStringTokenizer = new StringTokenizer(str2.substring(1));
/* 1062 */         String str3 = str1;
/* 1063 */         String str4 = paramStringTokenizer.nextToken();
/*      */
/* 1065 */         NumberFormat numberFormat = NumberFormat.getNumberInstance();
/* 1066 */         numberFormat.setParseIntegerOnly(true);
/* 1067 */         Number number = numberFormat.parse(str4);
/* 1068 */         int i = number.intValue();
/*      */
/* 1070 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1071 */           printBreakpointCommandUsage(paramString1, paramString2);
/* 1072 */           return null;
/*      */         }
/*      */         try {
/* 1075 */           breakpointSpec = Env.specList.createBreakpoint(str3, i);
/*      */         }
/* 1077 */         catch (ClassNotFoundException classNotFoundException) {
/* 1078 */           MessageOutput.println("is not a valid class name", str3);
/*      */         }
/*      */       } else {
/*      */
/* 1082 */         int i = str1.lastIndexOf(".");
/* 1083 */         if (i <= 0 || i >= str1
/* 1084 */           .length() - 1) {
/* 1085 */           printBreakpointCommandUsage(paramString1, paramString2);
/* 1086 */           return null;
/*      */         }
/* 1088 */         String str3 = str1.substring(i + 1);
/* 1089 */         String str4 = str1.substring(0, i);
/* 1090 */         ArrayList<String> arrayList = null;
/* 1091 */         if (str2 != null) {
/* 1092 */           if (!str2.startsWith("(") || !str2.endsWith(")")) {
/* 1093 */             MessageOutput.println("Invalid method specification:", str3 + str2);
/*      */
/* 1095 */             printBreakpointCommandUsage(paramString1, paramString2);
/* 1096 */             return null;
/*      */           }
/*      */
/* 1099 */           str2 = str2.substring(1, str2.length() - 1);
/*      */
/* 1101 */           arrayList = new ArrayList();
/* 1102 */           paramStringTokenizer = new StringTokenizer(str2, ",");
/* 1103 */           while (paramStringTokenizer.hasMoreTokens()) {
/* 1104 */             arrayList.add(paramStringTokenizer.nextToken());
/*      */           }
/*      */         }
/*      */         try {
/* 1108 */           breakpointSpec = Env.specList.createBreakpoint(str4, str3, arrayList);
/*      */
/*      */         }
/* 1111 */         catch (MalformedMemberNameException malformedMemberNameException) {
/* 1112 */           MessageOutput.println("is not a valid method name", str3);
/* 1113 */         } catch (ClassNotFoundException classNotFoundException) {
/* 1114 */           MessageOutput.println("is not a valid class name", str4);
/*      */         }
/*      */       }
/* 1117 */     } catch (Exception exception) {
/* 1118 */       printBreakpointCommandUsage(paramString1, paramString2);
/* 1119 */       return null;
/*      */     }
/* 1121 */     return breakpointSpec;
/*      */   }
/*      */
/*      */   private void resolveNow(EventRequestSpec paramEventRequestSpec) {
/* 1125 */     boolean bool = Env.specList.addEagerlyResolve(paramEventRequestSpec);
/* 1126 */     if (bool && !paramEventRequestSpec.isResolved()) {
/* 1127 */       MessageOutput.println("Deferring.", paramEventRequestSpec.toString());
/*      */     }
/*      */   }
/*      */
/*      */   void commandStop(StringTokenizer paramStringTokenizer) {
/*      */     String str;
/* 1133 */     byte b = 2;
/*      */
/* 1135 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1136 */       str = paramStringTokenizer.nextToken();
/* 1137 */       if (str.equals("go") && paramStringTokenizer.hasMoreTokens()) {
/* 1138 */         b = 0;
/* 1139 */         str = paramStringTokenizer.nextToken();
/* 1140 */       } else if (str.equals("thread") && paramStringTokenizer.hasMoreTokens()) {
/* 1141 */         b = 1;
/* 1142 */         str = paramStringTokenizer.nextToken();
/*      */       }
/*      */     } else {
/* 1145 */       listBreakpoints();
/*      */
/*      */       return;
/*      */     }
/* 1149 */     BreakpointSpec breakpointSpec = parseBreakpointSpec(paramStringTokenizer, "stop at", "stop in");
/* 1150 */     if (breakpointSpec != null) {
/*      */
/*      */
/*      */
/* 1154 */       if (str.equals("at") && breakpointSpec.isMethodBreakpoint()) {
/* 1155 */         MessageOutput.println("Use stop at to set a breakpoint at a line number");
/* 1156 */         printBreakpointCommandUsage("stop at", "stop in");
/*      */         return;
/*      */       }
/* 1159 */       breakpointSpec.suspendPolicy = b;
/* 1160 */       resolveNow(breakpointSpec);
/*      */     }
/*      */   }
/*      */
/*      */   void commandClear(StringTokenizer paramStringTokenizer) {
/* 1165 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1166 */       listBreakpoints();
/*      */
/*      */       return;
/*      */     }
/* 1170 */     BreakpointSpec breakpointSpec = parseBreakpointSpec(paramStringTokenizer, "clear", "clear");
/* 1171 */     if (breakpointSpec != null) {
/* 1172 */       if (Env.specList.delete(breakpointSpec)) {
/* 1173 */         MessageOutput.println("Removed:", breakpointSpec.toString());
/*      */       } else {
/* 1175 */         MessageOutput.println("Not found:", breakpointSpec.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   private List<WatchpointSpec> parseWatchpointSpec(StringTokenizer paramStringTokenizer) {
/* 1181 */     ArrayList<WatchpointSpec> arrayList = new ArrayList();
/* 1182 */     boolean bool1 = false;
/* 1183 */     boolean bool2 = false;
/* 1184 */     byte b = 2;
/*      */
/* 1186 */     String str1 = paramStringTokenizer.nextToken();
/* 1187 */     if (str1.equals("go")) {
/* 1188 */       b = 0;
/* 1189 */       str1 = paramStringTokenizer.nextToken();
/* 1190 */     } else if (str1.equals("thread")) {
/* 1191 */       b = 1;
/* 1192 */       str1 = paramStringTokenizer.nextToken();
/*      */     }
/* 1194 */     if (str1.equals("access")) {
/* 1195 */       bool1 = true;
/* 1196 */       str1 = paramStringTokenizer.nextToken();
/* 1197 */     } else if (str1.equals("all")) {
/* 1198 */       bool1 = true;
/* 1199 */       bool2 = true;
/* 1200 */       str1 = paramStringTokenizer.nextToken();
/*      */     } else {
/* 1202 */       bool2 = true;
/*      */     }
/* 1204 */     int i = str1.lastIndexOf('.');
/* 1205 */     if (i < 0) {
/* 1206 */       MessageOutput.println("Class containing field must be specified.");
/* 1207 */       return arrayList;
/*      */     }
/* 1209 */     String str2 = str1.substring(0, i);
/* 1210 */     str1 = str1.substring(i + 1);
/*      */
/*      */
/*      */     try {
/* 1214 */       if (bool1) {
/* 1215 */         WatchpointSpec watchpointSpec = Env.specList.createAccessWatchpoint(str2, str1);
/*      */
/* 1217 */         watchpointSpec.suspendPolicy = b;
/* 1218 */         arrayList.add(watchpointSpec);
/*      */       }
/* 1220 */       if (bool2) {
/* 1221 */         WatchpointSpec watchpointSpec = Env.specList.createModificationWatchpoint(str2, str1);
/*      */
/* 1223 */         watchpointSpec.suspendPolicy = b;
/* 1224 */         arrayList.add(watchpointSpec);
/*      */       }
/* 1226 */     } catch (MalformedMemberNameException malformedMemberNameException) {
/* 1227 */       MessageOutput.println("is not a valid field name", str1);
/* 1228 */     } catch (ClassNotFoundException classNotFoundException) {
/* 1229 */       MessageOutput.println("is not a valid class name", str2);
/*      */     }
/* 1231 */     return arrayList;
/*      */   }
/*      */
/*      */   void commandWatch(StringTokenizer paramStringTokenizer) {
/* 1235 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1236 */       MessageOutput.println("Field to watch not specified");
/*      */
/*      */       return;
/*      */     }
/* 1240 */     for (WatchpointSpec watchpointSpec : parseWatchpointSpec(paramStringTokenizer)) {
/* 1241 */       resolveNow(watchpointSpec);
/*      */     }
/*      */   }
/*      */
/*      */   void commandUnwatch(StringTokenizer paramStringTokenizer) {
/* 1246 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1247 */       MessageOutput.println("Field to unwatch not specified");
/*      */
/*      */       return;
/*      */     }
/* 1251 */     for (WatchpointSpec watchpointSpec : parseWatchpointSpec(paramStringTokenizer)) {
/* 1252 */       if (Env.specList.delete(watchpointSpec)) {
/* 1253 */         MessageOutput.println("Removed:", watchpointSpec.toString()); continue;
/*      */       }
/* 1255 */       MessageOutput.println("Not found:", watchpointSpec.toString());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void turnOnExitTrace(ThreadInfo paramThreadInfo, int paramInt) {
/* 1261 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/* 1262 */     MethodExitRequest methodExitRequest = eventRequestManager.createMethodExitRequest();
/* 1263 */     if (paramThreadInfo != null) {
/* 1264 */       methodExitRequest.addThreadFilter(paramThreadInfo.getThread());
/*      */     }
/* 1266 */     Env.addExcludes(methodExitRequest);
/* 1267 */     methodExitRequest.setSuspendPolicy(paramInt);
/* 1268 */     methodExitRequest.enable();
/*      */   }
/*      */
/*      */
/* 1272 */   static String methodTraceCommand = null;
/*      */
/*      */
/*      */   void commandTrace(StringTokenizer paramStringTokenizer) {
/* 1276 */     byte b = 2;
/* 1277 */     ThreadInfo threadInfo = null;
/* 1278 */     String str = " ";
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1284 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1285 */       String str1 = paramStringTokenizer.nextToken();
/* 1286 */       if (str1.equals("go")) {
/* 1287 */         b = 0;
/* 1288 */         str = " go ";
/* 1289 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1290 */           str1 = paramStringTokenizer.nextToken();
/*      */         }
/* 1292 */       } else if (str1.equals("thread")) {
/*      */
/* 1294 */         b = 1;
/* 1295 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1296 */           str1 = paramStringTokenizer.nextToken();
/*      */         }
/*      */       }
/*      */
/* 1300 */       if (str1.equals("method")) {
/* 1301 */         String str2 = null;
/*      */
/* 1303 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1304 */           String str3 = paramStringTokenizer.nextToken();
/* 1305 */           if (str3.equals("exits") || str3.equals("exit")) {
/* 1306 */             if (paramStringTokenizer.hasMoreTokens()) {
/* 1307 */               threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*      */             }
/* 1309 */             if (str3.equals("exit")) {
/*      */               StackFrame stackFrame;
/*      */               try {
/* 1312 */                 stackFrame = ThreadInfo.getCurrentThreadInfo().getCurrentFrame();
/* 1313 */               } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/* 1314 */                 MessageOutput.println("Current thread isnt suspended.");
/*      */                 return;
/*      */               }
/* 1317 */               Env.setAtExitMethod(stackFrame.location().method());
/* 1318 */               str2 = MessageOutput.format("trace" + str + "method exit in effect for",
/*      */
/*      */
/* 1321 */                   Env.atExitMethod().toString());
/*      */             } else {
/* 1323 */               str2 = MessageOutput.format("trace" + str + "method exits in effect");
/*      */             }
/*      */
/*      */
/* 1327 */             commandUntrace(new StringTokenizer("methods"));
/* 1328 */             turnOnExitTrace(threadInfo, b);
/* 1329 */             methodTraceCommand = str2;
/*      */             return;
/*      */           }
/*      */         } else {
/* 1333 */           MessageOutput.println("Can only trace");
/*      */           return;
/*      */         }
/*      */       }
/* 1337 */       if (str1.equals("methods")) {
/*      */         MethodEntryRequest methodEntryRequest;
/*      */
/* 1340 */         EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/* 1341 */         if (paramStringTokenizer.hasMoreTokens()) {
/* 1342 */           threadInfo = doGetThread(paramStringTokenizer.nextToken());
/*      */         }
/* 1344 */         if (threadInfo != null) {
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
/* 1362 */           methodEntryRequest = eventRequestManager.createMethodEntryRequest();
/* 1363 */           methodEntryRequest.addThreadFilter(threadInfo.getThread());
/*      */         } else {
/* 1365 */           commandUntrace(new StringTokenizer("methods"));
/* 1366 */           methodEntryRequest = eventRequestManager.createMethodEntryRequest();
/*      */         }
/* 1368 */         Env.addExcludes(methodEntryRequest);
/* 1369 */         methodEntryRequest.setSuspendPolicy(b);
/* 1370 */         methodEntryRequest.enable();
/* 1371 */         turnOnExitTrace(threadInfo, b);
/* 1372 */         methodTraceCommand = MessageOutput.format("trace" + str + "methods in effect");
/*      */
/*      */
/*      */         return;
/*      */       }
/*      */
/* 1378 */       MessageOutput.println("Can only trace");
/*      */
/*      */       return;
/*      */     }
/*      */
/* 1383 */     if (methodTraceCommand != null) {
/* 1384 */       MessageOutput.printDirectln(methodTraceCommand);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void commandUntrace(StringTokenizer paramStringTokenizer) {
/* 1394 */     String str = null;
/* 1395 */     EventRequestManager eventRequestManager = Env.vm().eventRequestManager();
/* 1396 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 1397 */       str = paramStringTokenizer.nextToken();
/*      */     }
/* 1399 */     if (str == null || str.equals("methods")) {
/* 1400 */       eventRequestManager.deleteEventRequests(eventRequestManager.methodEntryRequests());
/* 1401 */       eventRequestManager.deleteEventRequests(eventRequestManager.methodExitRequests());
/* 1402 */       Env.setAtExitMethod(null);
/* 1403 */       methodTraceCommand = null;
/*      */     }
/*      */   }
/*      */
/*      */   void commandList(StringTokenizer paramStringTokenizer) {
/* 1408 */     StackFrame stackFrame = null;
/* 1409 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 1410 */     if (threadInfo == null) {
/* 1411 */       MessageOutput.println("No thread specified.");
/*      */       return;
/*      */     }
/*      */     try {
/* 1415 */       stackFrame = threadInfo.getCurrentFrame();
/* 1416 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/* 1417 */       MessageOutput.println("Current thread isnt suspended.");
/*      */
/*      */       return;
/*      */     }
/* 1421 */     if (stackFrame == null) {
/* 1422 */       MessageOutput.println("No frames on the current call stack");
/*      */
/*      */       return;
/*      */     }
/* 1426 */     Location location = stackFrame.location();
/* 1427 */     if (location.method().isNative()) {
/* 1428 */       MessageOutput.println("Current method is native");
/*      */
/*      */       return;
/*      */     }
/* 1432 */     String str = null;
/*      */     try {
/* 1434 */       str = location.sourceName();
/*      */
/* 1436 */       ReferenceType referenceType = location.declaringType();
/* 1437 */       int i = location.lineNumber();
/*      */
/* 1439 */       if (paramStringTokenizer.hasMoreTokens()) {
/* 1440 */         String str1 = paramStringTokenizer.nextToken();
/*      */
/*      */
/*      */         try {
/* 1444 */           NumberFormat numberFormat = NumberFormat.getNumberInstance();
/* 1445 */           numberFormat.setParseIntegerOnly(true);
/* 1446 */           Number number = numberFormat.parse(str1);
/* 1447 */           i = number.intValue();
/* 1448 */         } catch (ParseException parseException) {
/*      */
/* 1450 */           List<Method> list = referenceType.methodsByName(str1);
/* 1451 */           if (list == null || list.size() == 0) {
/* 1452 */             MessageOutput.println("is not a valid line number or method name for", new Object[] { str1, referenceType
/* 1453 */                   .name() }); return;
/*      */           }
/* 1455 */           if (list.size() > 1) {
/* 1456 */             MessageOutput.println("is an ambiguous method name in", new Object[] { str1, referenceType
/* 1457 */                   .name() });
/*      */             return;
/*      */           }
/* 1460 */           location = ((Method)list.get(0)).location();
/* 1461 */           i = location.lineNumber();
/*      */         }
/*      */       }
/* 1464 */       int j = Math.max(i - 4, 1);
/* 1465 */       int k = j + 9;
/* 1466 */       if (i < 0) {
/* 1467 */         MessageOutput.println("Line number information not available for");
/* 1468 */       } else if (Env.sourceLine(location, i) == null) {
/* 1469 */         MessageOutput.println("is an invalid line number for", new Object[] { new Integer(i), referenceType
/*      */
/* 1471 */               .name() });
/*      */       } else {
/* 1473 */         for (int m = j; m <= k; m++) {
/* 1474 */           String str1 = Env.sourceLine(location, m);
/* 1475 */           if (str1 == null) {
/*      */             break;
/*      */           }
/* 1478 */           if (m == i) {
/* 1479 */             MessageOutput.println("source line number current line and line", new Object[] { new Integer(m), str1 });
/*      */           }
/*      */           else {
/*      */
/* 1483 */             MessageOutput.println("source line number and line", new Object[] { new Integer(m), str1 });
/*      */           }
/*      */
/*      */         }
/*      */
/*      */       }
/* 1489 */     } catch (AbsentInformationException absentInformationException) {
/* 1490 */       MessageOutput.println("No source information available for:", location.toString());
/* 1491 */     } catch (FileNotFoundException fileNotFoundException) {
/* 1492 */       MessageOutput.println("Source file not found:", str);
/* 1493 */     } catch (IOException iOException) {
/* 1494 */       MessageOutput.println("I/O exception occurred:", iOException.toString());
/*      */     }
/*      */   }
/*      */
/*      */   void commandLines(StringTokenizer paramStringTokenizer) {
/* 1499 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1500 */       MessageOutput.println("Specify class and method");
/*      */     } else {
/* 1502 */       String str1 = paramStringTokenizer.nextToken();
/* 1503 */       String str2 = paramStringTokenizer.hasMoreTokens() ? paramStringTokenizer.nextToken() : null;
/*      */       try {
/* 1505 */         ReferenceType referenceType = Env.getReferenceTypeFromToken(str1);
/* 1506 */         if (referenceType != null) {
/* 1507 */           List list = null;
/* 1508 */           if (str2 == null) {
/* 1509 */             list = referenceType.allLineLocations();
/*      */           } else {
/* 1511 */             for (Method method : referenceType.allMethods()) {
/* 1512 */               if (method.name().equals(str2)) {
/* 1513 */                 list = method.allLineLocations();
/*      */               }
/*      */             }
/* 1516 */             if (list == null) {
/* 1517 */               MessageOutput.println("is not a valid method name", str2);
/*      */             }
/*      */           }
/* 1520 */           for (Location location : list) {
/* 1521 */             MessageOutput.printDirectln(location.toString());
/*      */           }
/*      */         } else {
/* 1524 */           MessageOutput.println("is not a valid id or class name", str1);
/*      */         }
/* 1526 */       } catch (AbsentInformationException absentInformationException) {
/* 1527 */         MessageOutput.println("Line number information not available for", str1);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void commandClasspath(StringTokenizer paramStringTokenizer) {
/* 1533 */     if (Env.vm() instanceof PathSearchingVirtualMachine) {
/* 1534 */       PathSearchingVirtualMachine pathSearchingVirtualMachine = (PathSearchingVirtualMachine)Env.vm();
/* 1535 */       MessageOutput.println("base directory:", pathSearchingVirtualMachine.baseDirectory());
/* 1536 */       MessageOutput.println("classpath:", pathSearchingVirtualMachine.classPath().toString());
/* 1537 */       MessageOutput.println("bootclasspath:", pathSearchingVirtualMachine.bootClassPath().toString());
/*      */     } else {
/* 1539 */       MessageOutput.println("The VM does not use paths");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void commandUse(StringTokenizer paramStringTokenizer) {
/* 1545 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1546 */       MessageOutput.printDirectln(Env.getSourcePath());
/*      */
/*      */
/*      */     }
/*      */     else {
/*      */
/*      */
/* 1553 */       Env.setSourcePath(paramStringTokenizer.nextToken("").trim());
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void printVar(LocalVariable paramLocalVariable, Value paramValue) {
/* 1559 */     MessageOutput.println("expr is value", new Object[] { paramLocalVariable
/* 1560 */           .name(), (paramValue == null) ? "null" : paramValue
/* 1561 */           .toString() });
/*      */   }
/*      */
/*      */
/*      */
/*      */   void commandLocals() {
/* 1567 */     ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 1568 */     if (threadInfo == null) {
/* 1569 */       MessageOutput.println("No default thread specified:");
/*      */       return;
/*      */     }
/*      */     try {
/* 1573 */       StackFrame stackFrame = threadInfo.getCurrentFrame();
/* 1574 */       if (stackFrame == null) {
/* 1575 */         throw new AbsentInformationException();
/*      */       }
/* 1577 */       List list = stackFrame.visibleVariables();
/*      */
/* 1579 */       if (list.size() == 0) {
/* 1580 */         MessageOutput.println("No local variables");
/*      */         return;
/*      */       }
/* 1583 */       Map map = stackFrame.getValues(list);
/*      */
/* 1585 */       MessageOutput.println("Method arguments:");
/* 1586 */       for (LocalVariable localVariable : list) {
/* 1587 */         if (localVariable.isArgument()) {
/* 1588 */           Value value = (Value)map.get(localVariable);
/* 1589 */           printVar(localVariable, value);
/*      */         }
/*      */       }
/* 1592 */       MessageOutput.println("Local variables:");
/* 1593 */       for (LocalVariable localVariable : list) {
/* 1594 */         if (!localVariable.isArgument()) {
/* 1595 */           Value value = (Value)map.get(localVariable);
/* 1596 */           printVar(localVariable, value);
/*      */         }
/*      */       }
/* 1599 */     } catch (AbsentInformationException absentInformationException) {
/* 1600 */       MessageOutput.println("Local variable information not available.");
/* 1601 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/* 1602 */       MessageOutput.println("Current thread isnt suspended.");
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   private void dump(ObjectReference paramObjectReference, ReferenceType paramReferenceType1, ReferenceType paramReferenceType2) {
/* 1608 */     for (Field field : paramReferenceType1.fields()) {
/* 1609 */       StringBuffer stringBuffer = new StringBuffer();
/* 1610 */       stringBuffer.append("    ");
/* 1611 */       if (!paramReferenceType1.equals(paramReferenceType2)) {
/* 1612 */         stringBuffer.append(paramReferenceType1.name());
/* 1613 */         stringBuffer.append(".");
/*      */       }
/* 1615 */       stringBuffer.append(field.name());
/* 1616 */       stringBuffer.append(MessageOutput.format("colon space"));
/* 1617 */       stringBuffer.append(paramObjectReference.getValue(field));
/* 1618 */       MessageOutput.printDirectln(stringBuffer.toString());
/*      */     }
/* 1620 */     if (paramReferenceType1 instanceof ClassType) {
/* 1621 */       ClassType classType = ((ClassType)paramReferenceType1).superclass();
/* 1622 */       if (classType != null) {
/* 1623 */         dump(paramObjectReference, (ReferenceType)classType, paramReferenceType2);
/*      */       }
/* 1625 */     } else if (paramReferenceType1 instanceof InterfaceType) {
/* 1626 */       for (InterfaceType interfaceType : ((InterfaceType)paramReferenceType1).superinterfaces()) {
/* 1627 */         dump(paramObjectReference, (ReferenceType)interfaceType, paramReferenceType2);
/*      */
/*      */       }
/*      */     }
/* 1631 */     else if (paramObjectReference instanceof ArrayReference) {
/* 1632 */       Iterator<Value> iterator = ((ArrayReference)paramObjectReference).getValues().iterator();
/* 1633 */       while (iterator.hasNext()) {
/* 1634 */         MessageOutput.printDirect(((Value)iterator.next()).toString());
/* 1635 */         if (iterator.hasNext()) {
/* 1636 */           MessageOutput.printDirect(", ");
/*      */         }
/*      */       }
/* 1639 */       MessageOutput.println();
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   void doPrint(StringTokenizer paramStringTokenizer, boolean paramBoolean) {
/* 1647 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1648 */       MessageOutput.println("No objects specified.");
/*      */
/*      */       return;
/*      */     }
/* 1652 */     while (paramStringTokenizer.hasMoreTokens()) {
/* 1653 */       String str1 = paramStringTokenizer.nextToken("");
/* 1654 */       Value value = evaluate(str1);
/* 1655 */       if (value == null) {
/* 1656 */         MessageOutput.println("expr is null", str1.toString()); continue;
/* 1657 */       }  if (paramBoolean && value instanceof ObjectReference && !(value instanceof com.sun.jdi.StringReference)) {
/*      */
/* 1659 */         ObjectReference objectReference = (ObjectReference)value;
/* 1660 */         ReferenceType referenceType = objectReference.referenceType();
/* 1661 */         MessageOutput.println("expr is value", new Object[] { str1
/* 1662 */               .toString(),
/* 1663 */               MessageOutput.format("grouping begin character") });
/* 1664 */         dump(objectReference, referenceType, referenceType);
/* 1665 */         MessageOutput.println("grouping end character"); continue;
/*      */       }
/* 1667 */       String str2 = getStringValue();
/* 1668 */       if (str2 != null) {
/* 1669 */         MessageOutput.println("expr is value", new Object[] { str1.toString(), str2 });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   void commandPrint(final StringTokenizer t, final boolean dumpObject) {
/* 1677 */     new AsyncExecution()
/*      */       {
/*      */         void action() {
/* 1680 */           Commands.this.doPrint(t, dumpObject);
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   void commandSet(StringTokenizer paramStringTokenizer) {
/* 1686 */     String str = paramStringTokenizer.nextToken("");
/*      */
/*      */
/*      */
/*      */
/* 1691 */     if (str.indexOf('=') == -1) {
/* 1692 */       MessageOutput.println("Invalid assignment syntax");
/* 1693 */       MessageOutput.printPrompt();
/*      */
/*      */
/*      */
/*      */       return;
/*      */     }
/*      */
/*      */
/* 1701 */     commandPrint(new StringTokenizer(str), false);
/*      */   }
/*      */
/*      */   void doLock(StringTokenizer paramStringTokenizer) {
/* 1705 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1706 */       MessageOutput.println("No object specified.");
/*      */
/*      */       return;
/*      */     }
/* 1710 */     String str = paramStringTokenizer.nextToken("");
/* 1711 */     Value value = evaluate(str);
/*      */
/*      */     try {
/* 1714 */       if (value != null && value instanceof ObjectReference) {
/* 1715 */         ObjectReference objectReference = (ObjectReference)value;
/* 1716 */         String str1 = getStringValue();
/* 1717 */         if (str1 != null) {
/* 1718 */           MessageOutput.println("Monitor information for expr", new Object[] { str
/* 1719 */                 .trim(), str1 });
/*      */         }
/*      */
/* 1722 */         ThreadReference threadReference = objectReference.owningThread();
/* 1723 */         if (threadReference == null) {
/* 1724 */           MessageOutput.println("Not owned");
/*      */         } else {
/* 1726 */           MessageOutput.println("Owned by:", new Object[] { threadReference
/* 1727 */                 .name(), new Integer(objectReference
/* 1728 */                   .entryCount()) });
/*      */         }
/* 1730 */         List list = objectReference.waitingThreads();
/* 1731 */         if (list.size() == 0) {
/* 1732 */           MessageOutput.println("No waiters");
/*      */         } else {
/* 1734 */           for (ThreadReference threadReference1 : list) {
/* 1735 */             MessageOutput.println("Waiting thread:", threadReference1.name());
/*      */           }
/*      */         }
/*      */       } else {
/* 1739 */         MessageOutput.println("Expression must evaluate to an object");
/*      */       }
/* 1741 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/* 1742 */       MessageOutput.println("Threads must be suspended");
/*      */     }
/*      */   }
/*      */
/*      */   void commandLock(final StringTokenizer t) {
/* 1747 */     new AsyncExecution()
/*      */       {
/*      */         void action() {
/* 1750 */           Commands.this.doLock(t);
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   private void printThreadLockInfo(ThreadInfo paramThreadInfo) {
/* 1756 */     ThreadReference threadReference = paramThreadInfo.getThread();
/*      */     try {
/* 1758 */       MessageOutput.println("Monitor information for thread", threadReference.name());
/* 1759 */       List list = threadReference.ownedMonitors();
/* 1760 */       if (list.size() == 0) {
/* 1761 */         MessageOutput.println("No monitors owned");
/*      */       } else {
/* 1763 */         for (ObjectReference objectReference1 : list) {
/* 1764 */           MessageOutput.println("Owned monitor:", objectReference1.toString());
/*      */         }
/*      */       }
/* 1767 */       ObjectReference objectReference = threadReference.currentContendedMonitor();
/* 1768 */       if (objectReference == null) {
/* 1769 */         MessageOutput.println("Not waiting for a monitor");
/*      */       } else {
/* 1771 */         MessageOutput.println("Waiting for monitor:", objectReference.toString());
/*      */       }
/* 1773 */     } catch (IncompatibleThreadStateException incompatibleThreadStateException) {
/* 1774 */       MessageOutput.println("Threads must be suspended");
/*      */     }
/*      */   }
/*      */
/*      */   void commandThreadlocks(StringTokenizer paramStringTokenizer) {
/* 1779 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1780 */       ThreadInfo threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 1781 */       if (threadInfo == null) {
/* 1782 */         MessageOutput.println("Current thread not set.");
/*      */       } else {
/* 1784 */         printThreadLockInfo(threadInfo);
/*      */       }
/*      */       return;
/*      */     }
/* 1788 */     String str = paramStringTokenizer.nextToken();
/* 1789 */     if (str.toLowerCase().equals("all")) {
/* 1790 */       for (ThreadInfo threadInfo : ThreadInfo.threads()) {
/* 1791 */         printThreadLockInfo(threadInfo);
/*      */       }
/*      */     } else {
/* 1794 */       ThreadInfo threadInfo = doGetThread(str);
/* 1795 */       if (threadInfo != null) {
/* 1796 */         ThreadInfo.setCurrentThreadInfo(threadInfo);
/* 1797 */         printThreadLockInfo(threadInfo);
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   void doDisableGC(StringTokenizer paramStringTokenizer) {
/* 1803 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1804 */       MessageOutput.println("No object specified.");
/*      */
/*      */       return;
/*      */     }
/* 1808 */     String str = paramStringTokenizer.nextToken("");
/* 1809 */     Value value = evaluate(str);
/* 1810 */     if (value != null && value instanceof ObjectReference) {
/* 1811 */       ObjectReference objectReference = (ObjectReference)value;
/* 1812 */       objectReference.disableCollection();
/* 1813 */       String str1 = getStringValue();
/* 1814 */       if (str1 != null) {
/* 1815 */         MessageOutput.println("GC Disabled for", str1);
/*      */       }
/*      */     } else {
/* 1818 */       MessageOutput.println("Expression must evaluate to an object");
/*      */     }
/*      */   }
/*      */
/*      */   void commandDisableGC(final StringTokenizer t) {
/* 1823 */     new AsyncExecution()
/*      */       {
/*      */         void action() {
/* 1826 */           Commands.this.doDisableGC(t);
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   void doEnableGC(StringTokenizer paramStringTokenizer) {
/* 1832 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1833 */       MessageOutput.println("No object specified.");
/*      */
/*      */       return;
/*      */     }
/* 1837 */     String str = paramStringTokenizer.nextToken("");
/* 1838 */     Value value = evaluate(str);
/* 1839 */     if (value != null && value instanceof ObjectReference) {
/* 1840 */       ObjectReference objectReference = (ObjectReference)value;
/* 1841 */       objectReference.enableCollection();
/* 1842 */       String str1 = getStringValue();
/* 1843 */       if (str1 != null) {
/* 1844 */         MessageOutput.println("GC Enabled for", str1);
/*      */       }
/*      */     } else {
/* 1847 */       MessageOutput.println("Expression must evaluate to an object");
/*      */     }
/*      */   }
/*      */
/*      */   void commandEnableGC(final StringTokenizer t) {
/* 1852 */     new AsyncExecution()
/*      */       {
/*      */         void action() {
/* 1855 */           Commands.this.doEnableGC(t);
/*      */         }
/*      */       };
/*      */   }
/*      */
/*      */   void doSave(StringTokenizer paramStringTokenizer) {
/* 1861 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1862 */       MessageOutput.println("No save index specified.");
/*      */
/*      */       return;
/*      */     }
/* 1866 */     String str1 = paramStringTokenizer.nextToken();
/*      */
/* 1868 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1869 */       MessageOutput.println("No expression specified.");
/*      */       return;
/*      */     }
/* 1872 */     String str2 = paramStringTokenizer.nextToken("");
/* 1873 */     Value value = evaluate(str2);
/* 1874 */     if (value != null) {
/* 1875 */       Env.setSavedValue(str1, value);
/* 1876 */       String str = getStringValue();
/* 1877 */       if (str != null) {
/* 1878 */         MessageOutput.println("saved", str);
/*      */       }
/*      */     } else {
/* 1881 */       MessageOutput.println("Expression cannot be void");
/*      */     }
/*      */   }
/*      */
/*      */   void commandSave(final StringTokenizer t) {
/* 1886 */     if (!t.hasMoreTokens()) {
/* 1887 */       Set<String> set = Env.getSaveKeys();
/* 1888 */       if (set.isEmpty()) {
/* 1889 */         MessageOutput.println("No saved values");
/*      */         return;
/*      */       }
/* 1892 */       for (String str : set) {
/* 1893 */         Value value = Env.getSavedValue(str);
/* 1894 */         if (value instanceof ObjectReference && ((ObjectReference)value)
/* 1895 */           .isCollected()) {
/* 1896 */           MessageOutput.println("expr is value <collected>", new Object[] { str, value
/* 1897 */                 .toString() }); continue;
/*      */         }
/* 1899 */         if (value == null) {
/* 1900 */           MessageOutput.println("expr is null", str); continue;
/*      */         }
/* 1902 */         MessageOutput.println("expr is value", new Object[] { str, value
/* 1903 */               .toString() });
/*      */       }
/*      */
/*      */     } else {
/*      */
/* 1908 */       new AsyncExecution()
/*      */         {
/*      */           void action() {
/* 1911 */             Commands.this.doSave(t);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void commandBytecodes(StringTokenizer paramStringTokenizer) {
/* 1919 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1920 */       MessageOutput.println("No class specified.");
/*      */       return;
/*      */     }
/* 1923 */     String str1 = paramStringTokenizer.nextToken();
/*      */
/* 1925 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1926 */       MessageOutput.println("No method specified.");
/*      */
/*      */       return;
/*      */     }
/* 1930 */     String str2 = paramStringTokenizer.nextToken();
/*      */
/* 1932 */     List<ReferenceType> list = Env.vm().classesByName(str1);
/*      */
/* 1934 */     if (list.size() == 0) {
/* 1935 */       if (str1.indexOf('.') < 0) {
/* 1936 */         MessageOutput.println("not found (try the full name)", str1);
/*      */       } else {
/* 1938 */         MessageOutput.println("not found", str1);
/*      */       }
/*      */
/*      */       return;
/*      */     }
/* 1943 */     ReferenceType referenceType = list.get(0);
/* 1944 */     if (!(referenceType instanceof ClassType)) {
/* 1945 */       MessageOutput.println("not a class", str1);
/*      */
/*      */       return;
/*      */     }
/* 1949 */     byte[] arrayOfByte = null;
/* 1950 */     for (Method method : referenceType.methodsByName(str2)) {
/* 1951 */       if (!method.isAbstract()) {
/* 1952 */         arrayOfByte = method.bytecodes();
/*      */
/*      */         break;
/*      */       }
/*      */     }
/* 1957 */     StringBuffer stringBuffer = new StringBuffer(80);
/* 1958 */     stringBuffer.append("0000: ");
/* 1959 */     for (byte b = 0; b < arrayOfByte.length; b++) {
/* 1960 */       if (b > 0 && b % 16 == 0) {
/* 1961 */         MessageOutput.printDirectln(stringBuffer.toString());
/* 1962 */         stringBuffer.setLength(0);
/* 1963 */         stringBuffer.append(String.valueOf(b));
/* 1964 */         stringBuffer.append(": ");
/* 1965 */         int j = stringBuffer.length();
/* 1966 */         for (byte b1 = 0; b1 < 6 - j; b1++) {
/* 1967 */           stringBuffer.insert(0, '0');
/*      */         }
/*      */       }
/* 1970 */       int i = 0xFF & arrayOfByte[b];
/* 1971 */       String str = Integer.toHexString(i);
/* 1972 */       if (str.length() == 1) {
/* 1973 */         stringBuffer.append('0');
/*      */       }
/* 1975 */       stringBuffer.append(str);
/* 1976 */       stringBuffer.append(' ');
/*      */     }
/* 1978 */     if (stringBuffer.length() > 6) {
/* 1979 */       MessageOutput.printDirectln(stringBuffer.toString());
/*      */     }
/*      */   }
/*      */
/*      */   void commandExclude(StringTokenizer paramStringTokenizer) {
/* 1984 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1985 */       MessageOutput.printDirectln(Env.excludesString());
/*      */     } else {
/* 1987 */       String str = paramStringTokenizer.nextToken("");
/* 1988 */       if (str.equals("none")) {
/* 1989 */         str = "";
/*      */       }
/* 1991 */       Env.setExcludes(str);
/*      */     }
/*      */   }
/*      */
/*      */   void commandRedefine(StringTokenizer paramStringTokenizer) {
/* 1996 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 1997 */       MessageOutput.println("Specify classes to redefine");
/*      */     } else {
/* 1999 */       String str1 = paramStringTokenizer.nextToken();
/* 2000 */       List<ReferenceType> list = Env.vm().classesByName(str1);
/* 2001 */       if (list.size() == 0) {
/* 2002 */         MessageOutput.println("No class named", str1);
/*      */         return;
/*      */       }
/* 2005 */       if (list.size() > 1) {
/* 2006 */         MessageOutput.println("More than one class named", str1);
/*      */         return;
/*      */       }
/* 2009 */       Env.setSourcePath(Env.getSourcePath());
/* 2010 */       ReferenceType referenceType = list.get(0);
/* 2011 */       if (!paramStringTokenizer.hasMoreTokens()) {
/* 2012 */         MessageOutput.println("Specify file name for class", str1);
/*      */         return;
/*      */       }
/* 2015 */       String str2 = paramStringTokenizer.nextToken();
/* 2016 */       File file = new File(str2);
/* 2017 */       byte[] arrayOfByte = new byte[(int)file.length()];
/*      */       try {
/* 2019 */         FileInputStream fileInputStream = new FileInputStream(file);
/* 2020 */         fileInputStream.read(arrayOfByte);
/* 2021 */         fileInputStream.close();
/* 2022 */       } catch (Exception exception) {
/* 2023 */         MessageOutput.println("Error reading file", new Object[] { str2, exception
/* 2024 */               .toString() });
/*      */         return;
/*      */       }
/* 2027 */       HashMap<Object, Object> hashMap = new HashMap<>();
/*      */
/* 2029 */       hashMap.put(referenceType, arrayOfByte);
/*      */       try {
/* 2031 */         Env.vm().redefineClasses(hashMap);
/* 2032 */       } catch (Throwable throwable) {
/* 2033 */         MessageOutput.println("Error redefining class to file", new Object[] { str1, str2, throwable });
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   void commandPopFrames(StringTokenizer paramStringTokenizer, boolean paramBoolean) {
/*      */     ThreadInfo threadInfo;
/* 2044 */     if (paramStringTokenizer.hasMoreTokens()) {
/* 2045 */       String str = paramStringTokenizer.nextToken();
/* 2046 */       threadInfo = doGetThread(str);
/* 2047 */       if (threadInfo == null) {
/*      */         return;
/*      */       }
/*      */     } else {
/* 2051 */       threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 2052 */       if (threadInfo == null) {
/* 2053 */         MessageOutput.println("No thread specified.");
/*      */
/*      */         return;
/*      */       }
/*      */     }
/*      */     try {
/* 2059 */       StackFrame stackFrame = threadInfo.getCurrentFrame();
/* 2060 */       threadInfo.getThread().popFrames(stackFrame);
/* 2061 */       threadInfo = ThreadInfo.getCurrentThreadInfo();
/* 2062 */       ThreadInfo.setCurrentThreadInfo(threadInfo);
/* 2063 */       if (paramBoolean) {
/* 2064 */         commandStepi();
/*      */       }
/* 2066 */     } catch (Throwable throwable) {
/* 2067 */       MessageOutput.println("Error popping frame", throwable.toString());
/*      */     }
/*      */   }
/*      */
/*      */   void commandExtension(StringTokenizer paramStringTokenizer) {
/* 2072 */     if (!paramStringTokenizer.hasMoreTokens()) {
/* 2073 */       MessageOutput.println("No class specified.");
/*      */
/*      */       return;
/*      */     }
/* 2077 */     String str1 = paramStringTokenizer.nextToken();
/* 2078 */     ReferenceType referenceType = Env.getReferenceTypeFromToken(str1);
/* 2079 */     String str2 = null;
/* 2080 */     if (referenceType != null) {
/*      */       try {
/* 2082 */         str2 = referenceType.sourceDebugExtension();
/* 2083 */         MessageOutput.println("sourcedebugextension", str2);
/* 2084 */       } catch (AbsentInformationException absentInformationException) {
/* 2085 */         MessageOutput.println("No sourcedebugextension specified");
/*      */       }
/*      */     } else {
/* 2088 */       MessageOutput.println("is not a valid id or class name", str1);
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   void commandVersion(String paramString, VirtualMachineManager paramVirtualMachineManager) {
/* 2094 */     MessageOutput.println("minus version", new Object[] { paramString, new Integer(paramVirtualMachineManager
/*      */
/* 2096 */             .majorInterfaceVersion()), new Integer(paramVirtualMachineManager
/* 2097 */             .minorInterfaceVersion()),
/* 2098 */           System.getProperty("java.version") });
/* 2099 */     if (Env.connection() != null)
/*      */       try {
/* 2101 */         MessageOutput.printDirectln(Env.vm().description());
/* 2102 */       } catch (VMNotConnectedException vMNotConnectedException) {
/* 2103 */         MessageOutput.println("No VM connected");
/*      */       }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\Commands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
