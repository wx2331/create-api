/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
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
/*     */ public class JStatLogger
/*     */ {
/*     */   private MonitoredVm monitoredVm;
/*     */   private volatile boolean active = true;
/*     */   
/*     */   public JStatLogger(MonitoredVm paramMonitoredVm) {
/*  47 */     this.monitoredVm = paramMonitoredVm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printNames(String paramString, Comparator<Monitor> paramComparator, boolean paramBoolean, PrintStream paramPrintStream) throws MonitorException, PatternSyntaxException {
/*  58 */     List<Monitor> list = this.monitoredVm.findByPattern(paramString);
/*  59 */     Collections.sort(list, paramComparator);
/*     */     
/*  61 */     for (Monitor monitor : list) {
/*  62 */       if (!monitor.isSupported() && !paramBoolean) {
/*     */         continue;
/*     */       }
/*  65 */       paramPrintStream.println(monitor.getName());
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
/*     */   public void printSnapShot(String paramString, Comparator<Monitor> paramComparator, boolean paramBoolean1, boolean paramBoolean2, PrintStream paramPrintStream) throws MonitorException, PatternSyntaxException {
/*  78 */     List<Monitor> list = this.monitoredVm.findByPattern(paramString);
/*  79 */     Collections.sort(list, paramComparator);
/*     */     
/*  81 */     printList(list, paramBoolean1, paramBoolean2, paramPrintStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printList(List<Monitor> paramList, boolean paramBoolean1, boolean paramBoolean2, PrintStream paramPrintStream) throws MonitorException {
/*  92 */     for (Monitor monitor : paramList) {
/*     */       
/*  94 */       if (!monitor.isSupported() && !paramBoolean2) {
/*     */         continue;
/*     */       }
/*     */       
/*  98 */       StringBuilder stringBuilder = new StringBuilder();
/*  99 */       stringBuilder.append(monitor.getName()).append("=");
/*     */       
/* 101 */       if (monitor instanceof sun.jvmstat.monitor.StringMonitor) {
/* 102 */         stringBuilder.append("\"").append(monitor.getValue()).append("\"");
/*     */       } else {
/* 104 */         stringBuilder.append(monitor.getValue());
/*     */       } 
/*     */       
/* 107 */       if (paramBoolean1) {
/* 108 */         stringBuilder.append(" ").append(monitor.getUnits());
/* 109 */         stringBuilder.append(" ").append(monitor.getVariability());
/* 110 */         stringBuilder.append(" ").append(monitor.isSupported() ? "Supported" : "Unsupported");
/*     */       } 
/*     */       
/* 113 */       paramPrintStream.println(stringBuilder);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopLogging() {
/* 121 */     this.active = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logSamples(OutputFormatter paramOutputFormatter, int paramInt1, int paramInt2, int paramInt3, PrintStream paramPrintStream) throws MonitorException {
/* 131 */     long l = 0L;
/* 132 */     int i = 0;
/*     */ 
/*     */     
/* 135 */     int j = paramInt1;
/* 136 */     if (j == 0) {
/*     */       
/* 138 */       paramPrintStream.println(paramOutputFormatter.getHeader());
/* 139 */       j = -1;
/*     */     } 
/*     */     
/* 142 */     while (this.active) {
/*     */       
/* 144 */       if (j > 0 && --i <= 0) {
/* 145 */         i = j;
/* 146 */         paramPrintStream.println(paramOutputFormatter.getHeader());
/*     */       } 
/*     */       
/* 149 */       paramPrintStream.println(paramOutputFormatter.getRow());
/*     */ 
/*     */       
/* 152 */       if (paramInt3 > 0 && ++l >= paramInt3) {
/*     */         break;
/*     */       }
/*     */       
/* 156 */       try { Thread.sleep(paramInt2); } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\JStatLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */