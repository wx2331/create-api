/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import sun.jvmstat.monitor.HostIdentifier;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
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
/*     */ public class MonitoredHostProvider
/*     */   extends MonitoredHost
/*     */ {
/*     */   private static final String serverName = "/JStatRemoteHost";
/*     */   private static final int DEFAULT_POLLING_INTERVAL = 1000;
/*     */   private ArrayList<HostListener> listeners;
/*     */   private NotifierTask task;
/*     */   private HashSet<Integer> activeVms;
/*     */   private RemoteVmManager vmManager;
/*     */   private RemoteHost remoteHost;
/*     */   private Timer timer;
/*     */   
/*     */   public MonitoredHostProvider(HostIdentifier paramHostIdentifier) throws MonitorException {
/*     */     String str1;
/*  65 */     this.hostId = paramHostIdentifier;
/*  66 */     this.listeners = new ArrayList<>();
/*  67 */     this.interval = 1000;
/*  68 */     this.activeVms = new HashSet<>();
/*     */ 
/*     */     
/*  71 */     String str2 = "/JStatRemoteHost";
/*  72 */     String str3 = paramHostIdentifier.getPath();
/*     */     
/*  74 */     if (str3 != null && str3.length() > 0) {
/*  75 */       str2 = str3;
/*     */     }
/*     */     
/*  78 */     if (paramHostIdentifier.getPort() != -1) {
/*  79 */       str1 = "rmi://" + paramHostIdentifier.getHost() + ":" + paramHostIdentifier.getPort() + str2;
/*     */     } else {
/*  81 */       str1 = "rmi://" + paramHostIdentifier.getHost() + str2;
/*     */     } 
/*     */     
/*     */     try {
/*  85 */       this.remoteHost = (RemoteHost)Naming.lookup(str1);
/*     */     }
/*  87 */     catch (RemoteException remoteException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       String str = "RMI Registry not available at " + paramHostIdentifier.getHost();
/*     */       
/* 100 */       if (paramHostIdentifier.getPort() == -1) {
/* 101 */         str = str + ":" + 'ы';
/*     */       } else {
/*     */         
/* 104 */         str = str + ":" + paramHostIdentifier.getPort();
/*     */       } 
/*     */       
/* 107 */       if (remoteException.getMessage() != null) {
/* 108 */         throw new MonitorException(str + "\n" + remoteException.getMessage(), remoteException);
/*     */       }
/* 110 */       throw new MonitorException(str, remoteException);
/*     */     
/*     */     }
/* 113 */     catch (NotBoundException notBoundException) {
/*     */       
/* 115 */       String str = notBoundException.getMessage();
/* 116 */       if (str == null) str = str1; 
/* 117 */       throw new MonitorException("RMI Server " + str + " not available", notBoundException);
/*     */     }
/* 119 */     catch (MalformedURLException malformedURLException) {
/*     */       
/* 121 */       malformedURLException.printStackTrace();
/* 122 */       throw new IllegalArgumentException("Malformed URL: " + str1);
/*     */     } 
/* 124 */     this.vmManager = new RemoteVmManager(this.remoteHost);
/* 125 */     this.timer = new Timer(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier) throws MonitorException {
/* 133 */     return getMonitoredVm(paramVmIdentifier, 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoredVm getMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/* 141 */     VmIdentifier vmIdentifier = null;
/*     */     try {
/* 143 */       vmIdentifier = this.hostId.resolve(paramVmIdentifier);
/* 144 */       RemoteVm remoteVm = this.remoteHost.attachVm(paramVmIdentifier.getLocalVmId(), paramVmIdentifier
/* 145 */           .getMode());
/* 146 */       RemoteMonitoredVm remoteMonitoredVm = new RemoteMonitoredVm(remoteVm, vmIdentifier, this.timer, paramInt);
/*     */       
/* 148 */       remoteMonitoredVm.attach();
/* 149 */       return (MonitoredVm)remoteMonitoredVm;
/*     */     }
/* 151 */     catch (RemoteException remoteException) {
/* 152 */       throw new MonitorException("Remote Exception attaching to " + vmIdentifier
/* 153 */           .toString(), remoteException);
/* 154 */     } catch (URISyntaxException uRISyntaxException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 160 */       throw new IllegalArgumentException("Malformed URI: " + paramVmIdentifier
/* 161 */           .toString(), uRISyntaxException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void detach(MonitoredVm paramMonitoredVm) throws MonitorException {
/* 169 */     RemoteMonitoredVm remoteMonitoredVm = (RemoteMonitoredVm)paramMonitoredVm;
/* 170 */     remoteMonitoredVm.detach();
/*     */     try {
/* 172 */       this.remoteHost.detachVm(remoteMonitoredVm.getRemoteVm());
/*     */     }
/* 174 */     catch (RemoteException remoteException) {
/* 175 */       throw new MonitorException("Remote Exception detaching from " + paramMonitoredVm
/* 176 */           .getVmIdentifier().toString(), remoteException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHostListener(HostListener paramHostListener) {
/* 184 */     synchronized (this.listeners) {
/* 185 */       this.listeners.add(paramHostListener);
/* 186 */       if (this.task == null) {
/* 187 */         this.task = new NotifierTask();
/* 188 */         this.timer.schedule((TimerTask)this.task, 0L, this.interval);
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
/*     */   public void removeHostListener(HostListener paramHostListener) {
/* 202 */     synchronized (this.listeners) {
/* 203 */       this.listeners.remove(paramHostListener);
/* 204 */       if (this.listeners.isEmpty() && this.task != null) {
/* 205 */         this.task.cancel();
/* 206 */         this.task = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setInterval(int paramInt) {
/* 212 */     synchronized (this.listeners) {
/* 213 */       if (paramInt == this.interval) {
/*     */         return;
/*     */       }
/*     */       
/* 217 */       int i = this.interval;
/* 218 */       super.setInterval(paramInt);
/*     */       
/* 220 */       if (this.task != null) {
/* 221 */         this.task.cancel();
/* 222 */         NotifierTask notifierTask = this.task;
/* 223 */         this.task = new NotifierTask();
/* 224 */         CountedTimerTaskUtils.reschedule(this.timer, notifierTask, this.task, i, paramInt);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Integer> activeVms() throws MonitorException {
/* 234 */     return this.vmManager.activeVms();
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
/*     */ 
/*     */   
/*     */   private void fireVmStatusChangedEvents(Set paramSet1, Set paramSet2, Set paramSet3) {
/* 251 */     ArrayList arrayList = null;
/* 252 */     VmStatusChangeEvent vmStatusChangeEvent = null;
/*     */     
/* 254 */     synchronized (this.listeners) {
/* 255 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 258 */     for (HostListener hostListener : arrayList) {
/*     */       
/* 260 */       if (vmStatusChangeEvent == null) {
/* 261 */         vmStatusChangeEvent = new VmStatusChangeEvent(this, paramSet1, paramSet2, paramSet3);
/*     */       }
/* 263 */       hostListener.vmStatusChanged(vmStatusChangeEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void fireDisconnectedEvents() {
/* 271 */     ArrayList arrayList = null;
/* 272 */     HostEvent hostEvent = null;
/*     */     
/* 274 */     synchronized (this.listeners) {
/* 275 */       arrayList = (ArrayList)this.listeners.clone();
/*     */     } 
/*     */     
/* 278 */     for (HostListener hostListener : arrayList) {
/*     */       
/* 280 */       if (hostEvent == null) {
/* 281 */         hostEvent = new HostEvent(this);
/*     */       }
/* 283 */       hostListener.disconnected(hostEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class NotifierTask
/*     */     extends CountedTimerTask {
/*     */     private NotifierTask() {}
/*     */     
/*     */     public void run() {
/* 292 */       super.run();
/*     */ 
/*     */       
/* 295 */       HashSet hashSet1 = MonitoredHostProvider.this.activeVms;
/*     */ 
/*     */       
/*     */       try {
/* 299 */         MonitoredHostProvider.this.activeVms = (HashSet)MonitoredHostProvider.this.vmManager.activeVms();
/*     */       }
/* 301 */       catch (MonitorException monitorException) {
/*     */         
/* 303 */         System.err.println("MonitoredHostProvider: polling task caught MonitorException:");
/*     */         
/* 305 */         monitorException.printStackTrace();
/*     */ 
/*     */         
/* 308 */         MonitoredHostProvider.this.setLastException((Exception)monitorException);
/* 309 */         MonitoredHostProvider.this.fireDisconnectedEvents();
/*     */       } 
/*     */       
/* 312 */       if (MonitoredHostProvider.this.activeVms.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 316 */       HashSet<Integer> hashSet = new HashSet();
/* 317 */       HashSet hashSet2 = new HashSet();
/*     */       
/* 319 */       for (Integer integer : MonitoredHostProvider.this.activeVms) {
/*     */         
/* 321 */         if (!hashSet1.contains(integer))
/*     */         {
/* 323 */           hashSet.add(integer);
/*     */         }
/*     */       } 
/*     */       
/* 327 */       for (Object object : hashSet1) {
/*     */ 
/*     */         
/* 330 */         if (!MonitoredHostProvider.this.activeVms.contains(object))
/*     */         {
/* 332 */           hashSet2.add(object);
/*     */         }
/*     */       } 
/*     */       
/* 336 */       if (!hashSet.isEmpty() || !hashSet2.isEmpty())
/* 337 */         MonitoredHostProvider.this.fireVmStatusChangedEvents(MonitoredHostProvider.this.activeVms, hashSet, hashSet2); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\rmi\MonitoredHostProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */