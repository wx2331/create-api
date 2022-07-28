/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.VirtualMachineManager;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.connect.spi.Connection;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
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
/*     */ abstract class AbstractLauncher
/*     */   extends ConnectorImpl
/*     */   implements LaunchingConnector
/*     */ {
/*     */   public abstract VirtualMachine launch(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException, VMStartException;
/*     */
/*  55 */   ThreadGroup grp = Thread.currentThread().getThreadGroup(); AbstractLauncher() {
/*  56 */     ThreadGroup threadGroup = null;
/*  57 */     while ((threadGroup = this.grp.getParent()) != null)
/*  58 */       this.grp = threadGroup;
/*     */   }
/*     */   public abstract String name();
/*     */
/*     */   String[] tokenizeCommand(String paramString, char paramChar) {
/*  63 */     String str1 = String.valueOf(paramChar);
/*     */
/*     */
/*     */
/*     */
/*  68 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, paramChar + " \t\r\n\f", true);
/*     */
/*     */
/*  71 */     String str2 = null;
/*  72 */     String str3 = null;
/*  73 */     ArrayList<String> arrayList = new ArrayList();
/*  74 */     while (stringTokenizer.hasMoreTokens()) {
/*  75 */       String str = stringTokenizer.nextToken();
/*  76 */       if (str2 != null) {
/*  77 */         if (str.equals(str1)) {
/*  78 */           arrayList.add(str2);
/*  79 */           str2 = null; continue;
/*     */         }
/*  81 */         str2 = str2 + str; continue;
/*     */       }
/*  83 */       if (str3 != null) {
/*  84 */         if (str.equals(str1)) {
/*  85 */           str2 = str3;
/*  86 */         } else if (str.length() == 1 &&
/*  87 */           Character.isWhitespace(str.charAt(0))) {
/*  88 */           arrayList.add(str3);
/*     */         } else {
/*  90 */           throw new InternalException("Unexpected token: " + str);
/*     */         }
/*  92 */         str3 = null; continue;
/*     */       }
/*  94 */       if (str.equals(str1)) {
/*  95 */         str2 = ""; continue;
/*  96 */       }  if (str.length() == 1 &&
/*  97 */         Character.isWhitespace(str.charAt(0))) {
/*     */         continue;
/*     */       }
/* 100 */       str3 = str;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 108 */     if (str3 != null) {
/* 109 */       arrayList.add(str3);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 116 */     if (str2 != null) {
/* 117 */       arrayList.add(str2);
/*     */     }
/*     */
/* 120 */     String[] arrayOfString = new String[arrayList.size()];
/* 121 */     for (byte b = 0; b < arrayList.size(); b++) {
/* 122 */       arrayOfString[b] = arrayList.get(b);
/*     */     }
/* 124 */     return arrayOfString;
/*     */   }
/*     */
/*     */
/*     */   public abstract String description();
/*     */
/*     */   protected VirtualMachine launch(String[] paramArrayOfString, String paramString, TransportService.ListenKey paramListenKey, TransportService paramTransportService) throws IOException, VMStartException {
/* 131 */     Helper helper = new Helper(paramArrayOfString, paramString, paramListenKey, paramTransportService);
/* 132 */     helper.launchAndAccept();
/*     */
/*     */
/* 135 */     VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager();
/*     */
/* 137 */     return virtualMachineManager.createVirtualMachine(helper.connection(), helper
/* 138 */         .process());
/*     */   }
/*     */
/*     */
/*     */
/*     */   private class Helper
/*     */   {
/*     */     private final String address;
/*     */
/*     */     private TransportService.ListenKey listenKey;
/*     */
/*     */     private TransportService ts;
/*     */
/*     */     private final String[] commandArray;
/*     */
/* 153 */     private Process process = null;
/* 154 */     private Connection connection = null;
/* 155 */     private IOException acceptException = null;
/*     */
/*     */     private boolean exited = false;
/*     */
/*     */     Helper(String[] param1ArrayOfString, String param1String, TransportService.ListenKey param1ListenKey, TransportService param1TransportService) {
/* 160 */       this.commandArray = param1ArrayOfString;
/* 161 */       this.address = param1String;
/* 162 */       this.listenKey = param1ListenKey;
/* 163 */       this.ts = param1TransportService;
/*     */     }
/*     */
/*     */     String commandString() {
/* 167 */       String str = "";
/* 168 */       for (byte b = 0; b < this.commandArray.length; b++) {
/* 169 */         if (b > 0) {
/* 170 */           str = str + " ";
/*     */         }
/* 172 */         str = str + this.commandArray[b];
/*     */       }
/* 174 */       return str;
/*     */     }
/*     */
/*     */
/*     */
/*     */     synchronized void launchAndAccept() throws IOException, VMStartException {
/* 180 */       this.process = Runtime.getRuntime().exec(this.commandArray);
/*     */
/* 182 */       Thread thread1 = acceptConnection();
/* 183 */       Thread thread2 = monitorTarget();
/*     */       try {
/* 185 */         while (this.connection == null && this.acceptException == null && !this.exited)
/*     */         {
/*     */
/* 188 */           wait();
/*     */         }
/*     */
/* 191 */         if (this.exited) {
/* 192 */           throw new VMStartException("VM initialization failed for: " +
/* 193 */               commandString(), this.process);
/*     */         }
/* 195 */         if (this.acceptException != null)
/*     */         {
/* 197 */           throw this.acceptException;
/*     */         }
/* 199 */       } catch (InterruptedException interruptedException) {
/* 200 */         throw new InterruptedIOException("Interrupted during accept");
/*     */       } finally {
/* 202 */         thread1.interrupt();
/* 203 */         thread2.interrupt();
/*     */       }
/*     */     }
/*     */
/*     */     Process process() {
/* 208 */       return this.process;
/*     */     }
/*     */
/*     */     Connection connection() {
/* 212 */       return this.connection;
/*     */     }
/*     */
/*     */     synchronized void notifyOfExit() {
/* 216 */       this.exited = true;
/* 217 */       notify();
/*     */     }
/*     */
/*     */     synchronized void notifyOfConnection(Connection param1Connection) {
/* 221 */       this.connection = param1Connection;
/* 222 */       notify();
/*     */     }
/*     */
/*     */     synchronized void notifyOfAcceptException(IOException param1IOException) {
/* 226 */       this.acceptException = param1IOException;
/* 227 */       notify();
/*     */     }
/*     */
/*     */     Thread monitorTarget() {
/* 231 */       Thread thread = new Thread(AbstractLauncher.this.grp, "launched target monitor")
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 235 */               Helper.this.process.waitFor();
/*     */
/*     */
/*     */
/* 239 */               Helper.this.notifyOfExit();
/* 240 */             } catch (InterruptedException interruptedException) {}
/*     */           }
/*     */         };
/*     */
/*     */
/* 245 */       thread.setDaemon(true);
/* 246 */       thread.start();
/* 247 */       return thread;
/*     */     }
/*     */
/*     */     Thread acceptConnection() {
/* 251 */       Thread thread = new Thread(AbstractLauncher.this.grp, "connection acceptor")
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 255 */               Connection connection = Helper.this.ts.accept(Helper.this.listenKey, 0L, 0L);
/*     */
/*     */
/*     */
/* 259 */               Helper.this.notifyOfConnection(connection);
/* 260 */             } catch (InterruptedIOException interruptedIOException) {
/*     */
/* 262 */             } catch (IOException iOException) {
/*     */
/* 264 */               Helper.this.notifyOfAcceptException(iOException);
/*     */             }
/*     */           }
/*     */         };
/* 268 */       thread.setDaemon(true);
/* 269 */       thread.start();
/* 270 */       return thread;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\AbstractLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
