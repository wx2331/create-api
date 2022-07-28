/*     */ package sun.tools.jps;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.MonitoredVmUtil;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
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
/*     */ public class Jps
/*     */ {
/*     */   private static Arguments arguments;
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/*  44 */       arguments = new Arguments(paramArrayOfString);
/*  45 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  46 */       System.err.println(illegalArgumentException.getMessage());
/*  47 */       Arguments.printUsage(System.err);
/*  48 */       System.exit(1);
/*     */     } 
/*     */     
/*  51 */     if (arguments.isHelp()) {
/*  52 */       Arguments.printUsage(System.err);
/*  53 */       System.exit(0);
/*     */     } 
/*     */     
/*     */     try {
/*  57 */       HostIdentifier hostIdentifier = arguments.hostId();
/*     */       
/*  59 */       MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(hostIdentifier);
/*     */ 
/*     */       
/*  62 */       Set set = monitoredHost.activeVms();
/*     */       
/*  64 */       for (Integer integer : set) {
/*  65 */         StringBuilder stringBuilder = new StringBuilder();
/*  66 */         Throwable throwable = null;
/*     */         
/*  68 */         int i = integer.intValue();
/*     */         
/*  70 */         stringBuilder.append(String.valueOf(i));
/*     */         
/*  72 */         if (arguments.isQuiet()) {
/*  73 */           System.out.println(stringBuilder);
/*     */           
/*     */           continue;
/*     */         } 
/*  77 */         MonitoredVm monitoredVm = null;
/*  78 */         String str1 = "//" + i + "?mode=r";
/*     */         
/*  80 */         String str2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  90 */           str2 = " -- process information unavailable";
/*  91 */           VmIdentifier vmIdentifier = new VmIdentifier(str1);
/*  92 */           monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, 0);
/*     */           
/*  94 */           str2 = " -- main class information unavailable";
/*  95 */           stringBuilder.append(" " + MonitoredVmUtil.mainClass(monitoredVm, arguments
/*  96 */                 .showLongPaths()));
/*     */           
/*  98 */           if (arguments.showMainArgs()) {
/*  99 */             str2 = " -- main args information unavailable";
/* 100 */             String str = MonitoredVmUtil.mainArgs(monitoredVm);
/* 101 */             if (str != null && str.length() > 0) {
/* 102 */               stringBuilder.append(" " + str);
/*     */             }
/*     */           } 
/* 105 */           if (arguments.showVmArgs()) {
/* 106 */             str2 = " -- jvm args information unavailable";
/* 107 */             String str = MonitoredVmUtil.jvmArgs(monitoredVm);
/* 108 */             if (str != null && str.length() > 0) {
/* 109 */               stringBuilder.append(" " + str);
/*     */             }
/*     */           } 
/* 112 */           if (arguments.showVmFlags()) {
/* 113 */             str2 = " -- jvm flags information unavailable";
/* 114 */             String str = MonitoredVmUtil.jvmFlags(monitoredVm);
/* 115 */             if (str != null && str.length() > 0) {
/* 116 */               stringBuilder.append(" " + str);
/*     */             }
/*     */           } 
/*     */           
/* 120 */           str2 = " -- detach failed";
/* 121 */           monitoredHost.detach(monitoredVm);
/*     */           
/* 123 */           System.out.println(stringBuilder);
/*     */           
/* 125 */           str2 = null;
/* 126 */         } catch (URISyntaxException uRISyntaxException) {
/*     */           
/* 128 */           throwable = uRISyntaxException;
/*     */           assert false;
/* 130 */         } catch (Exception exception) {
/* 131 */           throwable = exception;
/*     */         } finally {
/* 133 */           if (str2 != null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 141 */             stringBuilder.append(str2);
/* 142 */             if (arguments.isDebug() && 
/* 143 */               throwable != null && throwable
/* 144 */               .getMessage() != null) {
/* 145 */               stringBuilder.append("\n\t");
/* 146 */               stringBuilder.append(throwable.getMessage());
/*     */             } 
/*     */             
/* 149 */             System.out.println(stringBuilder);
/* 150 */             if (arguments.printStackTrace()) {
/* 151 */               throwable.printStackTrace();
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 157 */     } catch (MonitorException monitorException) {
/* 158 */       if (monitorException.getMessage() != null) {
/* 159 */         System.err.println(monitorException.getMessage());
/*     */       } else {
/* 161 */         Throwable throwable = monitorException.getCause();
/* 162 */         if (throwable != null && throwable.getMessage() != null) {
/* 163 */           System.err.println(throwable.getMessage());
/*     */         } else {
/* 165 */           monitorException.printStackTrace();
/*     */         } 
/*     */       } 
/* 168 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jps\Jps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */