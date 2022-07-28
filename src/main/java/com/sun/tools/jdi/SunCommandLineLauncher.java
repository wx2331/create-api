/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.connect.LaunchingConnector;
/*     */ import com.sun.jdi.connect.Transport;
/*     */ import com.sun.jdi.connect.VMStartException;
/*     */ import com.sun.jdi.connect.spi.TransportService;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public class SunCommandLineLauncher
/*     */   extends AbstractLauncher
/*     */   implements LaunchingConnector
/*     */ {
/*     */   private static final String ARG_HOME = "home";
/*     */   private static final String ARG_OPTIONS = "options";
/*     */   private static final String ARG_MAIN = "main";
/*     */   private static final String ARG_INIT_SUSPEND = "suspend";
/*     */   private static final String ARG_QUOTE = "quote";
/*     */   private static final String ARG_VM_EXEC = "vmexec";
/*     */   TransportService transportService;
/*     */   Transport transport;
/*     */   boolean usingSharedMemory = false;
/*     */
/*     */   TransportService transportService() {
/*  52 */     return this.transportService;
/*     */   }
/*     */
/*     */   public Transport transport() {
/*  56 */     return this.transport;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SunCommandLineLauncher() {
/*     */
/*  67 */     try { Class<?> clazz = Class.forName("com.sun.tools.jdi.SharedMemoryTransportService");
/*  68 */       this.transportService = (TransportService)clazz.newInstance();
/*  69 */       this.transport = new Transport() {
/*     */           public String name() {
/*  71 */             return "dt_shmem";
/*     */           }
/*     */         };
/*  74 */       this.usingSharedMemory = true; }
/*  75 */     catch (ClassNotFoundException classNotFoundException) {  }
/*  76 */     catch (UnsatisfiedLinkError unsatisfiedLinkError) {  }
/*  77 */     catch (InstantiationException instantiationException) {  }
/*  78 */     catch (IllegalAccessException illegalAccessException) {}
/*     */
/*  80 */     if (this.transportService == null) {
/*  81 */       this.transportService = new SocketTransportService();
/*  82 */       this.transport = new Transport() {
/*     */           public String name() {
/*  84 */             return "dt_socket";
/*     */           }
/*     */         };
/*     */     }
/*     */
/*  89 */     addStringArgument("home",
/*     */
/*  91 */         getString("sun.home.label"),
/*  92 */         getString("sun.home"),
/*  93 */         System.getProperty("java.home"), false);
/*     */
/*  95 */     addStringArgument("options",
/*     */
/*  97 */         getString("sun.options.label"),
/*  98 */         getString("sun.options"), "", false);
/*     */
/*     */
/* 101 */     addStringArgument("main",
/*     */
/* 103 */         getString("sun.main.label"),
/* 104 */         getString("sun.main"), "", true);
/*     */
/*     */
/*     */
/* 108 */     addBooleanArgument("suspend",
/*     */
/* 110 */         getString("sun.init_suspend.label"),
/* 111 */         getString("sun.init_suspend"), true, false);
/*     */
/*     */
/*     */
/* 115 */     addStringArgument("quote",
/*     */
/* 117 */         getString("sun.quote.label"),
/* 118 */         getString("sun.quote"), "\"", true);
/*     */
/*     */
/* 121 */     addStringArgument("vmexec",
/*     */
/* 123 */         getString("sun.vm_exec.label"),
/* 124 */         getString("sun.vm_exec"), "java", true);
/*     */   }
/*     */
/*     */
/*     */
/*     */   static boolean hasWhitespace(String paramString) {
/* 130 */     int i = paramString.length();
/* 131 */     for (byte b = 0; b < i; b++) {
/* 132 */       if (Character.isWhitespace(paramString.charAt(b))) {
/* 133 */         return true;
/*     */       }
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public VirtualMachine launch(Map<String, ? extends Argument> paramMap) throws IOException, IllegalConnectorArgumentsException, VMStartException {
/*     */     VirtualMachine virtualMachine;
/*     */     TransportService.ListenKey listenKey;
/* 146 */     String str1 = argument("home", paramMap).value();
/* 147 */     String str2 = argument("options", paramMap).value();
/* 148 */     String str3 = argument("main", paramMap).value();
/*     */
/* 150 */     boolean bool = ((BooleanArgumentImpl)argument("suspend", paramMap)).booleanValue();
/* 151 */     String str4 = argument("quote", paramMap).value();
/* 152 */     String str5 = argument("vmexec", paramMap).value();
/* 153 */     String str6 = null;
/*     */
/* 155 */     if (str4.length() > 1) {
/* 156 */       throw new IllegalConnectorArgumentsException("Invalid length", "quote");
/*     */     }
/*     */
/*     */
/* 160 */     if (str2.indexOf("-Djava.compiler=") != -1 && str2
/* 161 */       .toLowerCase().indexOf("-djava.compiler=none") == -1) {
/* 162 */       throw new IllegalConnectorArgumentsException("Cannot debug with a JIT compiler", "options");
/*     */     }
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
/* 177 */     if (this.usingSharedMemory) {
/* 178 */       Random random = new Random();
/* 179 */       byte b = 0;
/*     */
/*     */       while (true) {
/*     */         try {
/* 183 */           String str = "javadebug" + String.valueOf(random.nextInt(100000));
/* 184 */           listenKey = transportService().startListening(str);
/*     */           break;
/* 186 */         } catch (IOException iOException) {
/* 187 */           if (++b > 5) {
/* 188 */             throw iOException;
/*     */           }
/*     */         }
/*     */       }
/*     */     } else {
/* 193 */       listenKey = transportService().startListening();
/*     */     }
/* 195 */     String str7 = listenKey.address();
/*     */
/*     */     try {
/* 198 */       if (str1.length() > 0) {
/* 199 */         str6 = str1 + File.separator + "bin" + File.separator + str5;
/*     */       } else {
/* 201 */         str6 = str5;
/*     */       }
/*     */
/* 204 */       if (hasWhitespace(str6)) {
/* 205 */         str6 = str4 + str6 + str4;
/*     */       }
/*     */
/* 208 */       String str8 = "transport=" + transport().name() + ",address=" + str7 + ",suspend=" + (bool ? 121 : 110);
/*     */
/*     */
/*     */
/* 212 */       if (hasWhitespace(str8)) {
/* 213 */         str8 = str4 + str8 + str4;
/*     */       }
/*     */
/* 216 */       String str9 = str6 + ' ' + str2 + ' ' + "-Xdebug -Xrunjdwp:" + str8 + ' ' + str3;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 223 */       virtualMachine = launch(tokenizeCommand(str9, str4.charAt(0)), str7, listenKey,
/* 224 */           transportService());
/*     */     } finally {
/* 226 */       transportService().stopListening(listenKey);
/*     */     }
/*     */
/* 229 */     return virtualMachine;
/*     */   }
/*     */
/*     */   public String name() {
/* 233 */     return "com.sun.jdi.CommandLineLaunch";
/*     */   }
/*     */
/*     */   public String description() {
/* 237 */     return getString("sun.description");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SunCommandLineLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
