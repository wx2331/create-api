/*     */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.TimerTask;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.MonitorStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.event.VmEvent;
/*     */ import sun.jvmstat.monitor.event.VmListener;
/*     */ import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTask;
/*     */ import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;
/*     */ import sun.jvmstat.perfdata.monitor.MonitorStatus;
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
/*     */ public class LocalMonitoredVm
/*     */   extends AbstractMonitoredVm
/*     */ {
/*     */   private ArrayList<VmListener> listeners;
/*     */   private NotifierTask task;
/*     */   
/*     */   public LocalMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/*  67 */     super(paramVmIdentifier, paramInt);
/*  68 */     this.pdb = new PerfDataBuffer(paramVmIdentifier);
/*  69 */     this.listeners = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach() {
/*  76 */     if (this.interval > 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       if (this.task != null) {
/*  83 */         this.task.cancel();
/*  84 */         this.task = null;
/*     */       } 
/*     */     }
/*  87 */     super.detach();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVmListener(VmListener paramVmListener) {
/*  94 */     synchronized (this.listeners) {
/*  95 */       this.listeners.add(paramVmListener);
/*  96 */       if (this.task == null) {
/*  97 */         this.task = new NotifierTask();
/*  98 */         LocalEventTimer localEventTimer = LocalEventTimer.getInstance();
/*  99 */         localEventTimer.schedule((TimerTask)this.task, this.interval, this.interval);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeVmListener(VmListener paramVmListener) {
/* 108 */     synchronized (this.listeners) {
/* 109 */       this.listeners.remove(paramVmListener);
/* 110 */       if (this.listeners.isEmpty() && this.task != null) {
/* 111 */         this.task.cancel();
/* 112 */         this.task = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 121 */     synchronized (this.listeners) {
/* 122 */       if (paramInt == this.interval) {
/*     */         return;
/*     */       }
/*     */       
/* 126 */       int i = this.interval;
/* 127 */       super.setInterval(paramInt);
/*     */       
/* 129 */       if (this.task != null) {
/* 130 */         this.task.cancel();
/* 131 */         NotifierTask notifierTask = this.task;
/* 132 */         this.task = new NotifierTask();
/* 133 */         LocalEventTimer localEventTimer = LocalEventTimer.getInstance();
/* 134 */         CountedTimerTaskUtils.reschedule(localEventTimer, notifierTask, this.task, i, paramInt);
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
/*     */   void fireMonitorStatusChangedEvents(List paramList1, List paramList2) {
/* 147 */     MonitorStatusChangeEvent monitorStatusChangeEvent = null;
/* 148 */     ArrayList arrayList = null;
/*     */     
/* 150 */     synchronized (this.listeners) {
/* 151 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 154 */     for (VmListener vmListener : arrayList) {
/*     */ 
/*     */       
/* 157 */       if (monitorStatusChangeEvent == null) {
/* 158 */         monitorStatusChangeEvent = new MonitorStatusChangeEvent((MonitoredVm)this, paramList1, paramList2);
/*     */       }
/* 160 */       vmListener.monitorStatusChanged(monitorStatusChangeEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void fireMonitorsUpdatedEvents() {
/* 168 */     VmEvent vmEvent = null;
/* 169 */     ArrayList arrayList = null;
/*     */     
/* 171 */     synchronized (this.listeners) {
/* 172 */       arrayList = cast(this.listeners.clone());
/*     */     } 
/*     */     
/* 175 */     for (VmListener vmListener : arrayList) {
/*     */       
/* 177 */       if (vmEvent == null) {
/* 178 */         vmEvent = new VmEvent((MonitoredVm)this);
/*     */       }
/* 180 */       vmListener.monitorsUpdated(vmEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class NotifierTask
/*     */     extends CountedTimerTask
/*     */   {
/*     */     private NotifierTask() {}
/*     */     
/*     */     public void run() {
/* 190 */       super.run();
/*     */       try {
/* 192 */         MonitorStatus monitorStatus = LocalMonitoredVm.this.getMonitorStatus();
/* 193 */         List list1 = monitorStatus.getInserted();
/* 194 */         List list2 = monitorStatus.getRemoved();
/*     */         
/* 196 */         if (!list1.isEmpty() || !list2.isEmpty()) {
/* 197 */           LocalMonitoredVm.this.fireMonitorStatusChangedEvents(list1, list2);
/*     */         }
/* 199 */         LocalMonitoredVm.this.fireMonitorsUpdatedEvents();
/* 200 */       } catch (MonitorException monitorException) {
/*     */         
/* 202 */         System.err.println("Exception updating monitors for " + LocalMonitoredVm.this
/* 203 */             .getVmIdentifier());
/* 204 */         monitorException.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> T cast(Object paramObject) {
/* 211 */     return (T)paramObject;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\LocalMonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */