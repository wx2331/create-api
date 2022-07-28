/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.MonitorStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.event.VmEvent;
/*     */ import sun.jvmstat.monitor.event.VmListener;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
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
/*     */ public class RemoteMonitoredVm
/*     */   extends AbstractMonitoredVm
/*     */ {
/*     */   private ArrayList<VmListener> listeners;
/*     */   private NotifierTask notifierTask;
/*     */   private SamplerTask samplerTask;
/*     */   private Timer timer;
/*     */   private RemoteVm rvm;
/*     */   private ByteBuffer updateBuffer;
/*     */   
/*     */   public RemoteMonitoredVm(RemoteVm paramRemoteVm, VmIdentifier paramVmIdentifier, Timer paramTimer, int paramInt) throws MonitorException {
/*  69 */     super(paramVmIdentifier, paramInt);
/*  70 */     this.rvm = paramRemoteVm;
/*  71 */     this.pdb = new PerfDataBuffer(paramRemoteVm, paramVmIdentifier.getLocalVmId());
/*  72 */     this.listeners = new ArrayList<>();
/*  73 */     this.timer = paramTimer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attach() throws MonitorException {
/*  80 */     this.updateBuffer = this.pdb.getByteBuffer().duplicate();
/*     */ 
/*     */     
/*  83 */     if (this.interval > 0) {
/*  84 */       this.samplerTask = new SamplerTask();
/*  85 */       this.timer.schedule((TimerTask)this.samplerTask, 0L, this.interval);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach() {
/*     */     try {
/*  94 */       if (this.interval > 0) {
/*  95 */         if (this.samplerTask != null) {
/*  96 */           this.samplerTask.cancel();
/*  97 */           this.samplerTask = null;
/*     */         } 
/*  99 */         if (this.notifierTask != null) {
/* 100 */           this.notifierTask.cancel();
/* 101 */           this.notifierTask = null;
/*     */         } 
/* 103 */         sample();
/*     */       } 
/* 105 */     } catch (RemoteException remoteException) {
/*     */       
/* 107 */       System.err.println("Could not read data for remote JVM " + this.vmid);
/* 108 */       remoteException.printStackTrace();
/*     */     } finally {
/*     */       
/* 111 */       super.detach();
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
/*     */   
/*     */   public void sample() throws RemoteException {
/* 125 */     assert this.updateBuffer != null;
/* 126 */     ((PerfDataBuffer)this.pdb).sample(this.updateBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteVm getRemoteVm() {
/* 135 */     return this.rvm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVmListener(VmListener paramVmListener) {
/* 142 */     synchronized (this.listeners) {
/* 143 */       this.listeners.add(paramVmListener);
/* 144 */       if (this.notifierTask == null) {
/* 145 */         this.notifierTask = new NotifierTask();
/* 146 */         this.timer.schedule((TimerTask)this.notifierTask, 0L, this.interval);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeVmListener(VmListener paramVmListener) {
/* 155 */     synchronized (this.listeners) {
/* 156 */       this.listeners.remove(paramVmListener);
/* 157 */       if (this.listeners.isEmpty() && this.notifierTask != null) {
/* 158 */         this.notifierTask.cancel();
/* 159 */         this.notifierTask = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 168 */     synchronized (this.listeners) {
/* 169 */       if (paramInt == this.interval) {
/*     */         return;
/*     */       }
/*     */       
/* 173 */       int i = this.interval;
/* 174 */       super.setInterval(paramInt);
/*     */       
/* 176 */       if (this.samplerTask != null) {
/* 177 */         this.samplerTask.cancel();
/* 178 */         SamplerTask samplerTask = this.samplerTask;
/* 179 */         this.samplerTask = new SamplerTask();
/* 180 */         CountedTimerTaskUtils.reschedule(this.timer, samplerTask, this.samplerTask, i, paramInt);
/*     */       } 
/*     */ 
/*     */       
/* 184 */       if (this.notifierTask != null) {
/* 185 */         this.notifierTask.cancel();
/* 186 */         NotifierTask notifierTask = this.notifierTask;
/* 187 */         this.notifierTask = new NotifierTask();
/* 188 */         CountedTimerTaskUtils.reschedule(this.timer, notifierTask, this.notifierTask, i, paramInt);
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
/*     */   void fireMonitorStatusChangedEvents(List paramList1, List paramList2) {
/* 202 */     ArrayList arrayList = null;
/* 203 */     MonitorStatusChangeEvent monitorStatusChangeEvent = null;
/*     */     
/* 205 */     synchronized (this.listeners) {
/* 206 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 209 */     for (VmListener vmListener : arrayList) {
/*     */       
/* 211 */       if (monitorStatusChangeEvent == null) {
/* 212 */         monitorStatusChangeEvent = new MonitorStatusChangeEvent((MonitoredVm)this, paramList1, paramList2);
/*     */       }
/* 214 */       vmListener.monitorStatusChanged(monitorStatusChangeEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void fireMonitorsUpdatedEvents() {
/* 222 */     ArrayList arrayList = null;
/* 223 */     VmEvent vmEvent = null;
/*     */     
/* 225 */     synchronized (this.listeners) {
/* 226 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 229 */     for (VmListener vmListener : arrayList) {
/*     */       
/* 231 */       if (vmEvent == null) {
/* 232 */         vmEvent = new VmEvent((MonitoredVm)this);
/*     */       }
/* 234 */       vmListener.monitorsUpdated(vmEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class NotifierTask
/*     */     extends CountedTimerTask
/*     */   {
/*     */     private NotifierTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 255 */       super.run();
/*     */       try {
/* 257 */         MonitorStatus monitorStatus = RemoteMonitoredVm.this.getMonitorStatus();
/*     */         
/* 259 */         List list1 = monitorStatus.getInserted();
/* 260 */         List list2 = monitorStatus.getRemoved();
/*     */         
/* 262 */         if (!list1.isEmpty() || !list2.isEmpty()) {
/* 263 */           RemoteMonitoredVm.this.fireMonitorStatusChangedEvents(list1, list2);
/*     */         }
/* 265 */       } catch (MonitorException monitorException) {
/*     */ 
/*     */         
/* 268 */         System.err.println("Exception updating monitors for " + RemoteMonitoredVm.this
/* 269 */             .getVmIdentifier());
/* 270 */         monitorException.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class SamplerTask
/*     */     extends CountedTimerTask
/*     */   {
/*     */     private SamplerTask() {}
/*     */ 
/*     */     
/*     */     public void run() {
/* 284 */       super.run();
/*     */       try {
/* 286 */         RemoteMonitoredVm.this.sample();
/* 287 */         RemoteMonitoredVm.this.fireMonitorsUpdatedEvents();
/*     */       }
/* 289 */       catch (RemoteException remoteException) {
/*     */         
/* 291 */         System.err.println("Exception taking sample for " + RemoteMonitoredVm.this
/* 292 */             .getVmIdentifier());
/* 293 */         remoteException.printStackTrace();
/* 294 */         cancel();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\rmi\RemoteMonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */