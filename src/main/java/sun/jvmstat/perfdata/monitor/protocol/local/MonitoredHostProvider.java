/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.TimerTask;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
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
/*     */   private static final int DEFAULT_POLLING_INTERVAL = 1000;
/*     */   private ArrayList<HostListener> listeners;
/*     */   private NotifierTask task;
/*     */   private HashSet<Integer> activeVms;
/*     */   private LocalVmManager vmManager;
/*     */   
/*     */   public MonitoredHostProvider(HostIdentifier paramHostIdentifier) {
/*  55 */     this.hostId = paramHostIdentifier;
/*  56 */     this.listeners = new ArrayList<>();
/*  57 */     this.interval = 1000;
/*  58 */     this.activeVms = new HashSet<>();
/*  59 */     this.vmManager = new LocalVmManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier) throws MonitorException {
/*  67 */     return getMonitoredVm(paramVmIdentifier, 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/*     */     try {
/*  76 */       VmIdentifier vmIdentifier = this.hostId.resolve(paramVmIdentifier);
/*  77 */       return (MonitoredVm)new LocalMonitoredVm(vmIdentifier, paramInt);
/*  78 */     } catch (URISyntaxException uRISyntaxException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       throw new IllegalArgumentException("Malformed URI: " + paramVmIdentifier
/*  85 */           .toString(), uRISyntaxException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach(MonitoredVm paramMonitoredVm) {
/*  93 */     paramMonitoredVm.detach();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHostListener(HostListener paramHostListener) {
/* 100 */     synchronized (this.listeners) {
/* 101 */       this.listeners.add(paramHostListener);
/* 102 */       if (this.task == null) {
/* 103 */         this.task = new NotifierTask();
/* 104 */         LocalEventTimer localEventTimer = LocalEventTimer.getInstance();
/* 105 */         localEventTimer.schedule((TimerTask)this.task, this.interval, this.interval);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHostListener(HostListener paramHostListener) {
/* 114 */     synchronized (this.listeners) {
/* 115 */       this.listeners.remove(paramHostListener);
/* 116 */       if (this.listeners.isEmpty() && this.task != null) {
/* 117 */         this.task.cancel();
/* 118 */         this.task = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 127 */     synchronized (this.listeners) {
/* 128 */       if (paramInt == this.interval) {
/*     */         return;
/*     */       }
/*     */       
/* 132 */       int i = this.interval;
/* 133 */       super.setInterval(paramInt);
/*     */       
/* 135 */       if (this.task != null) {
/* 136 */         this.task.cancel();
/* 137 */         NotifierTask notifierTask = this.task;
/* 138 */         this.task = new NotifierTask();
/* 139 */         LocalEventTimer localEventTimer = LocalEventTimer.getInstance();
/* 140 */         CountedTimerTaskUtils.reschedule(localEventTimer, notifierTask, this.task, i, paramInt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Integer> activeVms() {
/* 150 */     return this.vmManager.activeVms();
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
/*     */ 
/*     */   
/*     */   private void fireVmStatusChangedEvents(Set paramSet1, Set paramSet2, Set paramSet3) {
/* 165 */     ArrayList arrayList = null;
/* 166 */     VmStatusChangeEvent vmStatusChangeEvent = null;
/*     */     
/* 168 */     synchronized (this.listeners) {
/* 169 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 172 */     for (HostListener hostListener : arrayList) {
/*     */       
/* 174 */       if (vmStatusChangeEvent == null) {
/* 175 */         vmStatusChangeEvent = new VmStatusChangeEvent(this, paramSet1, paramSet2, paramSet3);
/*     */       }
/* 177 */       hostListener.vmStatusChanged(vmStatusChangeEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class NotifierTask
/*     */     extends CountedTimerTask {
/*     */     private NotifierTask() {}
/*     */     
/*     */     public void run() {
/* 186 */       super.run();
/*     */ 
/*     */       
/* 189 */       HashSet hashSet1 = MonitoredHostProvider.this.activeVms;
/*     */ 
/*     */       
/* 192 */       MonitoredHostProvider.this.activeVms = (HashSet)MonitoredHostProvider.this.vmManager.activeVms();
/*     */       
/* 194 */       if (MonitoredHostProvider.this.activeVms.isEmpty()) {
/*     */         return;
/*     */       }
/* 197 */       HashSet<Integer> hashSet = new HashSet();
/* 198 */       HashSet hashSet2 = new HashSet();
/*     */       
/* 200 */       for (Integer integer : MonitoredHostProvider.this.activeVms) {
/*     */         
/* 202 */         if (!hashSet1.contains(integer))
/*     */         {
/* 204 */           hashSet.add(integer);
/*     */         }
/*     */       } 
/*     */       
/* 208 */       for (Object object : hashSet1) {
/*     */ 
/*     */         
/* 211 */         if (!MonitoredHostProvider.this.activeVms.contains(object))
/*     */         {
/* 213 */           hashSet2.add(object);
/*     */         }
/*     */       } 
/*     */       
/* 217 */       if (!hashSet.isEmpty() || !hashSet2.isEmpty())
/* 218 */         MonitoredHostProvider.this.fireVmStatusChangedEvents(MonitoredHostProvider.this.activeVms, hashSet, hashSet2); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\MonitoredHostProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */