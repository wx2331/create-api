/*     */ package sun.jvmstat.perfdata.monitor.protocol.file;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostListener;
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
/*     */ public class MonitoredHostProvider
/*     */   extends MonitoredHost
/*     */ {
/*     */   public static final int DEFAULT_POLLING_INTERVAL = 0;
/*     */   
/*     */   public MonitoredHostProvider(HostIdentifier paramHostIdentifier) {
/*  54 */     this.hostId = paramHostIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier) throws MonitorException {
/*  62 */     return getMonitoredVm(paramVmIdentifier, 0);
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
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/*  74 */     return (MonitoredVm)new FileMonitoredVm(paramVmIdentifier, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach(MonitoredVm paramMonitoredVm) {
/*  81 */     paramMonitoredVm.detach();
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
/*     */   
/*     */   public void addHostListener(HostListener paramHostListener) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHostListener(HostListener paramHostListener) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Integer> activeVms() {
/* 114 */     return new HashSet<>(0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\file\MonitoredHostProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */