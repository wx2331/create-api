/*     */ package sun.tools.jstat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.jvmstat.monitor.Monitor;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.Units;
/*     */ import sun.jvmstat.monitor.Variability;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
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
/*     */ public class Jstat
/*     */ {
/*     */   private static Arguments arguments;
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/*  46 */       arguments = new Arguments(paramArrayOfString);
/*  47 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  48 */       System.err.println(illegalArgumentException.getMessage());
/*  49 */       Arguments.printUsage(System.err);
/*  50 */       System.exit(1);
/*     */     } 
/*     */     
/*  53 */     if (arguments.isHelp()) {
/*  54 */       Arguments.printUsage(System.out);
/*  55 */       System.exit(0);
/*     */     } 
/*     */     
/*  58 */     if (arguments.isOptions()) {
/*  59 */       OptionLister optionLister = new OptionLister(arguments.optionsSources());
/*  60 */       optionLister.print(System.out);
/*  61 */       System.exit(0);
/*     */     } 
/*     */     
/*     */     try {
/*  65 */       if (arguments.isList()) {
/*  66 */         logNames();
/*  67 */       } else if (arguments.isSnap()) {
/*  68 */         logSnapShot();
/*     */       } else {
/*  70 */         logSamples();
/*     */       } 
/*  72 */     } catch (MonitorException monitorException) {
/*  73 */       if (monitorException.getMessage() != null) {
/*  74 */         System.err.println(monitorException.getMessage());
/*     */       } else {
/*  76 */         Throwable throwable = monitorException.getCause();
/*  77 */         if (throwable != null && throwable.getMessage() != null) {
/*  78 */           System.err.println(throwable.getMessage());
/*     */         } else {
/*  80 */           monitorException.printStackTrace();
/*     */         } 
/*     */       } 
/*  83 */       System.exit(1);
/*     */     } 
/*  85 */     System.exit(0);
/*     */   }
/*     */   
/*     */   static void logNames() throws MonitorException {
/*  89 */     VmIdentifier vmIdentifier = arguments.vmId();
/*  90 */     int i = arguments.sampleInterval();
/*  91 */     MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/*  92 */     MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, i);
/*  93 */     JStatLogger jStatLogger = new JStatLogger(monitoredVm);
/*  94 */     jStatLogger.printNames(arguments.counterNames(), arguments.comparator(), arguments
/*  95 */         .showUnsupported(), System.out);
/*  96 */     monitoredHost.detach(monitoredVm);
/*     */   }
/*     */   
/*     */   static void logSnapShot() throws MonitorException {
/* 100 */     VmIdentifier vmIdentifier = arguments.vmId();
/* 101 */     int i = arguments.sampleInterval();
/* 102 */     MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/* 103 */     MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, i);
/* 104 */     JStatLogger jStatLogger = new JStatLogger(monitoredVm);
/* 105 */     jStatLogger.printSnapShot(arguments.counterNames(), arguments.comparator(), arguments
/* 106 */         .isVerbose(), arguments.showUnsupported(), System.out);
/*     */     
/* 108 */     monitoredHost.detach(monitoredVm);
/*     */   }
/*     */   static void logSamples() throws MonitorException {
/*     */     RawOutputFormatter rawOutputFormatter;
/* 112 */     final VmIdentifier vmId = arguments.vmId();
/* 113 */     int i = arguments.sampleInterval();
/*     */     
/* 115 */     final MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(vmIdentifier);
/* 116 */     MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(vmIdentifier, i);
/* 117 */     final JStatLogger logger = new JStatLogger(monitoredVm);
/* 118 */     OptionOutputFormatter optionOutputFormatter = null;
/*     */     
/* 120 */     if (arguments.isSpecialOption()) {
/* 121 */       OptionFormat optionFormat = arguments.optionFormat();
/* 122 */       optionOutputFormatter = new OptionOutputFormatter(monitoredVm, optionFormat);
/*     */     } else {
/* 124 */       List<Monitor> list = monitoredVm.findByPattern(arguments.counterNames());
/* 125 */       Collections.sort(list, arguments.comparator());
/* 126 */       ArrayList<Monitor> arrayList = new ArrayList();
/*     */       
/* 128 */       for (Iterator<Monitor> iterator = list.iterator(); iterator.hasNext(); ) {
/* 129 */         Monitor monitor = iterator.next();
/* 130 */         if (!monitor.isSupported() && !arguments.showUnsupported()) {
/* 131 */           iterator.remove();
/*     */           continue;
/*     */         } 
/* 134 */         if (monitor.getVariability() == Variability.CONSTANT) {
/* 135 */           iterator.remove();
/* 136 */           if (arguments.printConstants()) arrayList.add(monitor);  continue;
/* 137 */         }  if (monitor.getUnits() == Units.STRING && 
/* 138 */           !arguments.printStrings()) {
/* 139 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */       
/* 143 */       if (!arrayList.isEmpty()) {
/* 144 */         jStatLogger.printList(arrayList, arguments.isVerbose(), arguments
/* 145 */             .showUnsupported(), System.out);
/* 146 */         if (!list.isEmpty()) {
/* 147 */           System.out.println();
/*     */         }
/*     */       } 
/*     */       
/* 151 */       if (list.isEmpty()) {
/* 152 */         monitoredHost.detach(monitoredVm);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 157 */       rawOutputFormatter = new RawOutputFormatter(list, arguments.printStrings());
/*     */     } 
/*     */ 
/*     */     
/* 161 */     Runtime.getRuntime().addShutdownHook(new Thread() {
/*     */           public void run() {
/* 163 */             logger.stopLogging();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 168 */     HostListener hostListener = new HostListener() {
/*     */         public void vmStatusChanged(VmStatusChangeEvent param1VmStatusChangeEvent) {
/* 170 */           Integer integer = new Integer(vmId.getLocalVmId());
/* 171 */           if (param1VmStatusChangeEvent.getTerminated().contains(integer)) {
/* 172 */             logger.stopLogging();
/* 173 */           } else if (!param1VmStatusChangeEvent.getActive().contains(integer)) {
/* 174 */             logger.stopLogging();
/*     */           } 
/*     */         }
/*     */         
/*     */         public void disconnected(HostEvent param1HostEvent) {
/* 179 */           if (monitoredHost == param1HostEvent.getMonitoredHost()) {
/* 180 */             logger.stopLogging();
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 185 */     if (vmIdentifier.getLocalVmId() != 0) {
/* 186 */       monitoredHost.addHostListener(hostListener);
/*     */     }
/*     */     
/* 189 */     jStatLogger.logSamples(rawOutputFormatter, arguments.headerRate(), arguments
/* 190 */         .sampleInterval(), arguments.sampleCount(), System.out);
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (hostListener != null) {
/* 195 */       monitoredHost.removeHostListener(hostListener);
/*     */     }
/* 197 */     monitoredHost.detach(monitoredVm);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Jstat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */