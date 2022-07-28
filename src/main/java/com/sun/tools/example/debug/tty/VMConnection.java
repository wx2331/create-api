/*     */ package com.sun.tools.example.debug.tty;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.PathSearchingVirtualMachine;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.ListeningConnector;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.ThreadDeathRequest;
/*     */ import com.sun.jdi.request.ThreadStartRequest;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VMConnection
/*     */ {
/*     */   private VirtualMachine vm;
/*     */   private Process process;
/*     */   private int outputCompleteCount;
/*     */   private final Connector connector;
/*     */   private final Map<String, Connector.Argument> connectorArgs;
/*     */   private final int traceFlags;
/*     */   
/*     */   synchronized void notifyOutputComplete() {
/*  58 */     this.outputCompleteCount++;
/*  59 */     notifyAll();
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void waitOutputComplete() {
/*  64 */     if (this.process != null) {
/*  65 */       while (this.outputCompleteCount < 2) { 
/*  66 */         try { wait(); } catch (InterruptedException interruptedException) {} }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Connector findConnector(String paramString) {
/*  73 */     for (Connector connector : Bootstrap.virtualMachineManager().allConnectors()) {
/*  74 */       if (connector.name().equals(paramString)) {
/*  75 */         return connector;
/*     */       }
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   private Map<String, Connector.Argument> parseConnectorArgs(Connector paramConnector, String paramString) {
/*  82 */     Map<String, Connector.Argument> map = paramConnector.defaultArguments();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     String str = "(quote=[^,]+,)|(\\w+=)(((\"[^\"]*\")|('[^']*')|([^,'\"]+))+,)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     Pattern pattern = Pattern.compile(str);
/*  98 */     Matcher matcher = pattern.matcher(paramString);
/*  99 */     while (matcher.find()) {
/* 100 */       int i = matcher.start();
/* 101 */       int j = matcher.end();
/* 102 */       if (i > 0)
/*     */       {
/*     */ 
/*     */         
/* 106 */         throw new IllegalArgumentException(
/* 107 */             MessageOutput.format("Illegal connector argument", paramString));
/*     */       }
/*     */ 
/*     */       
/* 111 */       String str1 = paramString.substring(i, j);
/* 112 */       int k = str1.indexOf('=');
/* 113 */       String str2 = str1.substring(0, k);
/* 114 */       String str3 = str1.substring(k + 1, str1
/* 115 */           .length() - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       if (str2.equals("options")) {
/* 123 */         StringBuilder stringBuilder = new StringBuilder();
/* 124 */         for (String str4 : splitStringAtNonEnclosedWhiteSpace(str3)) {
/* 125 */           while (isEnclosed(str4, "\"") || isEnclosed(str4, "'")) {
/* 126 */             str4 = str4.substring(1, str4.length() - 1);
/*     */           }
/* 128 */           stringBuilder.append(str4);
/* 129 */           stringBuilder.append(" ");
/*     */         } 
/* 131 */         str3 = stringBuilder.toString();
/*     */       } 
/*     */       
/* 134 */       Connector.Argument argument = (Connector.Argument)map.get(str2);
/* 135 */       if (argument == null)
/* 136 */         throw new IllegalArgumentException(
/* 137 */             MessageOutput.format("Argument is not defined for connector:", new Object[] {
/* 138 */                 str2, paramConnector.name()
/*     */               })); 
/* 140 */       argument.setValue(str3);
/*     */       
/* 142 */       paramString = paramString.substring(j);
/* 143 */       matcher = pattern.matcher(paramString);
/*     */     } 
/* 145 */     if (!paramString.equals(",") && paramString.length() > 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 150 */       throw new IllegalArgumentException(
/* 151 */           MessageOutput.format("Illegal connector argument", paramString));
/*     */     }
/* 153 */     return map;
/*     */   }
/*     */   
/*     */   private static boolean isEnclosed(String paramString1, String paramString2) {
/* 157 */     if (paramString1.indexOf(paramString2) == 0) {
/* 158 */       int i = paramString1.lastIndexOf(paramString2);
/* 159 */       if (i > 0 && i == paramString1.length() - 1) {
/* 160 */         return true;
/*     */       }
/*     */     } 
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   private static List<String> splitStringAtNonEnclosedWhiteSpace(String paramString) throws IllegalArgumentException {
/* 167 */     ArrayList<String> arrayList = new ArrayList();
/*     */     
/* 169 */     byte b1 = 0;
/* 170 */     byte b2 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     byte b3 = 32;
/*     */     
/* 186 */     if (paramString == null) {
/* 187 */       throw new IllegalArgumentException(
/* 188 */           MessageOutput.format("value string is null"));
/*     */     }
/*     */ 
/*     */     
/* 192 */     char[] arrayOfChar = paramString.toCharArray();
/*     */     
/* 194 */     for (byte b4 = 0; b4 < arrayOfChar.length; b4++) {
/* 195 */       switch (arrayOfChar[b4]) {
/*     */ 
/*     */         
/*     */         case ' ':
/* 199 */           if (isLastChar(arrayOfChar, b4)) {
/* 200 */             b2 = b4;
/*     */           } else {
/*     */             break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 263 */           if (b1 > b2) {
/* 264 */             throw new IllegalArgumentException(
/* 265 */                 MessageOutput.format("Illegal option values"));
/*     */           }
/*     */ 
/*     */           
/* 269 */           arrayList.add(paramString.substring(b1, ++b2));
/*     */ 
/*     */           
/* 272 */           b4 = b1 = b2; break;case '"': case '\'': default: if (b3 == 32) { if (isPreviousCharWhitespace(arrayOfChar, b4)) b1 = b4;  if (isNextCharWhitespace(arrayOfChar, b4)) { b2 = b4; } else { break; }  } else { break; }  if (b1 > b2) throw new IllegalArgumentException(MessageOutput.format("Illegal option values"));  arrayList.add(paramString.substring(b1, ++b2)); b4 = b1 = b2; break;
/*     */       } 
/*     */       continue;
/*     */     } 
/* 276 */     return arrayList;
/*     */   }
/*     */   
/*     */   private static boolean isPreviousCharWhitespace(char[] paramArrayOfchar, int paramInt) {
/* 280 */     return isCharWhitespace(paramArrayOfchar, paramInt - 1);
/*     */   }
/*     */   
/*     */   private static boolean isNextCharWhitespace(char[] paramArrayOfchar, int paramInt) {
/* 284 */     return isCharWhitespace(paramArrayOfchar, paramInt + 1);
/*     */   }
/*     */   
/*     */   private static boolean isCharWhitespace(char[] paramArrayOfchar, int paramInt) {
/* 288 */     if (paramInt < 0 || paramInt >= paramArrayOfchar.length)
/*     */     {
/* 290 */       return true;
/*     */     }
/* 292 */     if (paramArrayOfchar[paramInt] == ' ') {
/* 293 */       return true;
/*     */     }
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isLastChar(char[] paramArrayOfchar, int paramInt) {
/* 299 */     return (paramInt + 1 == paramArrayOfchar.length);
/*     */   }
/*     */   VMConnection(String paramString, int paramInt) {
/*     */     String str1, str2;
/*     */     this.process = null;
/*     */     this.outputCompleteCount = 0;
/* 305 */     int i = paramString.indexOf(':');
/* 306 */     if (i == -1) {
/* 307 */       str1 = paramString;
/* 308 */       str2 = "";
/*     */     } else {
/* 310 */       str1 = paramString.substring(0, i);
/* 311 */       str2 = paramString.substring(i + 1);
/*     */     } 
/*     */     
/* 314 */     this.connector = findConnector(str1);
/* 315 */     if (this.connector == null) {
/* 316 */       throw new IllegalArgumentException(
/* 317 */           MessageOutput.format("No connector named:", str1));
/*     */     }
/*     */     
/* 320 */     this.connectorArgs = parseConnectorArgs(this.connector, str2);
/* 321 */     this.traceFlags = paramInt;
/*     */   }
/*     */   
/*     */   synchronized VirtualMachine open() {
/* 325 */     if (this.connector instanceof LaunchingConnector) {
/* 326 */       this.vm = launchTarget();
/* 327 */     } else if (this.connector instanceof AttachingConnector) {
/* 328 */       this.vm = attachTarget();
/* 329 */     } else if (this.connector instanceof ListeningConnector) {
/* 330 */       this.vm = listenTarget();
/*     */     } else {
/* 332 */       throw new InternalError(
/* 333 */           MessageOutput.format("Invalid connect type"));
/*     */     } 
/* 335 */     this.vm.setDebugTraceMode(this.traceFlags);
/* 336 */     if (this.vm.canBeModified()) {
/* 337 */       setEventRequests(this.vm);
/* 338 */       resolveEventRequests();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     if (Env.getSourcePath().length() == 0) {
/* 347 */       if (this.vm instanceof PathSearchingVirtualMachine) {
/* 348 */         PathSearchingVirtualMachine pathSearchingVirtualMachine = (PathSearchingVirtualMachine)this.vm;
/*     */         
/* 350 */         Env.setSourcePath(pathSearchingVirtualMachine.classPath());
/*     */       } else {
/* 352 */         Env.setSourcePath(".");
/*     */       } 
/*     */     }
/*     */     
/* 356 */     return this.vm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean setConnectorArg(String paramString1, String paramString2) {
/* 363 */     if (this.vm != null) {
/* 364 */       return false;
/*     */     }
/*     */     
/* 367 */     Connector.Argument argument = this.connectorArgs.get(paramString1);
/* 368 */     if (argument == null) {
/* 369 */       return false;
/*     */     }
/* 371 */     argument.setValue(paramString2);
/* 372 */     return true;
/*     */   }
/*     */   
/*     */   String connectorArg(String paramString) {
/* 376 */     Connector.Argument argument = this.connectorArgs.get(paramString);
/* 377 */     if (argument == null) {
/* 378 */       return "";
/*     */     }
/* 380 */     return argument.value();
/*     */   }
/*     */   
/*     */   public synchronized VirtualMachine vm() {
/* 384 */     if (this.vm == null) {
/* 385 */       throw new VMNotConnectedException();
/*     */     }
/* 387 */     return this.vm;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isOpen() {
/* 392 */     return (this.vm != null);
/*     */   }
/*     */   
/*     */   boolean isLaunch() {
/* 396 */     return this.connector instanceof LaunchingConnector;
/*     */   }
/*     */   
/*     */   public void disposeVM() {
/*     */     try {
/* 401 */       if (this.vm != null) {
/* 402 */         this.vm.dispose();
/* 403 */         this.vm = null;
/*     */       } 
/*     */     } finally {
/* 406 */       if (this.process != null) {
/* 407 */         this.process.destroy();
/* 408 */         this.process = null;
/*     */       } 
/* 410 */       waitOutputComplete();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setEventRequests(VirtualMachine paramVirtualMachine) {
/* 415 */     EventRequestManager eventRequestManager = paramVirtualMachine.eventRequestManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     Commands commands = new Commands();
/* 426 */     commands
/* 427 */       .commandCatchException(new StringTokenizer("uncaught java.lang.Throwable"));
/*     */     
/* 429 */     ThreadStartRequest threadStartRequest = eventRequestManager.createThreadStartRequest();
/* 430 */     threadStartRequest.enable();
/* 431 */     ThreadDeathRequest threadDeathRequest = eventRequestManager.createThreadDeathRequest();
/* 432 */     threadDeathRequest.enable();
/*     */   }
/*     */   
/*     */   private void resolveEventRequests() {
/* 436 */     Env.specList.resolveAll();
/*     */   }
/*     */   
/*     */   private void dumpStream(InputStream paramInputStream) throws IOException {
/* 440 */     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
/*     */     
/*     */     try {
/*     */       int i;
/* 444 */       while ((i = bufferedReader.read()) != -1) {
/* 445 */         MessageOutput.printDirect((char)i);
/*     */       }
/*     */     }
/* 448 */     catch (IOException iOException) {
/* 449 */       String str = iOException.getMessage();
/* 450 */       if (!str.startsWith("Bad file number")) {
/* 451 */         throw iOException;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void displayRemoteOutput(final InputStream stream) {
/* 465 */     Thread thread = new Thread("output reader")
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 469 */             VMConnection.this.dumpStream(stream);
/* 470 */           } catch (IOException iOException) {
/* 471 */             MessageOutput.fatalError("Failed reading output");
/*     */           } finally {
/* 473 */             VMConnection.this.notifyOutputComplete();
/*     */           } 
/*     */         }
/*     */       };
/* 477 */     thread.setPriority(9);
/* 478 */     thread.start();
/*     */   }
/*     */   
/*     */   private void dumpFailedLaunchInfo(Process paramProcess) {
/*     */     try {
/* 483 */       dumpStream(paramProcess.getErrorStream());
/* 484 */       dumpStream(paramProcess.getInputStream());
/* 485 */     } catch (IOException iOException) {
/* 486 */       MessageOutput.println("Unable to display process output:", iOException
/* 487 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private VirtualMachine launchTarget() {
/* 493 */     LaunchingConnector launchingConnector = (LaunchingConnector)this.connector;
/*     */     try {
/* 495 */       VirtualMachine virtualMachine = launchingConnector.launch(this.connectorArgs);
/* 496 */       this.process = virtualMachine.process();
/* 497 */       displayRemoteOutput(this.process.getErrorStream());
/* 498 */       displayRemoteOutput(this.process.getInputStream());
/* 499 */       return virtualMachine;
/* 500 */     } catch (IOException iOException) {
/* 501 */       iOException.printStackTrace();
/* 502 */       MessageOutput.fatalError("Unable to launch target VM.");
/* 503 */     } catch (IllegalConnectorArgumentsException illegalConnectorArgumentsException) {
/* 504 */       illegalConnectorArgumentsException.printStackTrace();
/* 505 */       MessageOutput.fatalError("Internal debugger error.");
/* 506 */     } catch (VMStartException vMStartException) {
/* 507 */       MessageOutput.println("vmstartexception", vMStartException.getMessage());
/* 508 */       MessageOutput.println();
/* 509 */       dumpFailedLaunchInfo(vMStartException.process());
/* 510 */       MessageOutput.fatalError("Target VM failed to initialize.");
/*     */     } 
/* 512 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private VirtualMachine attachTarget() {
/* 517 */     AttachingConnector attachingConnector = (AttachingConnector)this.connector;
/*     */     try {
/* 519 */       return attachingConnector.attach(this.connectorArgs);
/* 520 */     } catch (IOException iOException) {
/* 521 */       iOException.printStackTrace();
/* 522 */       MessageOutput.fatalError("Unable to attach to target VM.");
/* 523 */     } catch (IllegalConnectorArgumentsException illegalConnectorArgumentsException) {
/* 524 */       illegalConnectorArgumentsException.printStackTrace();
/* 525 */       MessageOutput.fatalError("Internal debugger error.");
/*     */     } 
/* 527 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private VirtualMachine listenTarget() {
/* 532 */     ListeningConnector listeningConnector = (ListeningConnector)this.connector;
/*     */     try {
/* 534 */       String str = listeningConnector.startListening(this.connectorArgs);
/* 535 */       MessageOutput.println("Listening at address:", str);
/* 536 */       this.vm = listeningConnector.accept(this.connectorArgs);
/* 537 */       listeningConnector.stopListening(this.connectorArgs);
/* 538 */       return this.vm;
/* 539 */     } catch (IOException iOException) {
/* 540 */       iOException.printStackTrace();
/* 541 */       MessageOutput.fatalError("Unable to attach to target VM.");
/* 542 */     } catch (IllegalConnectorArgumentsException illegalConnectorArgumentsException) {
/* 543 */       illegalConnectorArgumentsException.printStackTrace();
/* 544 */       MessageOutput.fatalError("Internal debugger error.");
/*     */     } 
/* 546 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\example\debug\tty\VMConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */