/*     */ package sun.tools.jstatd;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.MonitoredHost;
/*     */ import sun.jvmstat.monitor.MonitoredVm;
/*     */ import sun.jvmstat.monitor.VmIdentifier;
/*     */ import sun.jvmstat.monitor.event.HostEvent;
/*     */ import sun.jvmstat.monitor.event.HostListener;
/*     */ import sun.jvmstat.monitor.event.VmStatusChangeEvent;
/*     */ import sun.jvmstat.monitor.remote.BufferedMonitoredVm;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
/*     */ import sun.jvmstat.monitor.remote.RemoteVm;
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
/*     */ public class RemoteHostImpl
/*     */   implements RemoteHost, HostListener
/*     */ {
/*     */   private MonitoredHost monitoredHost;
/*     */   private Set<Integer> activeVms;
/*     */   
/*     */   public RemoteHostImpl() throws MonitorException {
/*     */     try {
/*  56 */       this.monitoredHost = MonitoredHost.getMonitoredHost("localhost");
/*  57 */     } catch (URISyntaxException uRISyntaxException) {}
/*     */     
/*  59 */     this.activeVms = this.monitoredHost.activeVms();
/*  60 */     this.monitoredHost.addHostListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public RemoteVm attachVm(int paramInt, String paramString) throws RemoteException, MonitorException {
/*  65 */     Integer integer = new Integer(paramInt);
/*  66 */     RemoteVm remoteVm = null;
/*  67 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/*  69 */     stringBuffer.append("local://").append(paramInt).append("@localhost");
/*  70 */     if (paramString != null) {
/*  71 */       stringBuffer.append("?mode=" + paramString);
/*     */     }
/*     */     
/*  74 */     String str = stringBuffer.toString();
/*     */     
/*     */     try {
/*  77 */       VmIdentifier vmIdentifier = new VmIdentifier(str);
/*  78 */       MonitoredVm monitoredVm = this.monitoredHost.getMonitoredVm(vmIdentifier);
/*  79 */       RemoteVmImpl remoteVmImpl = new RemoteVmImpl((BufferedMonitoredVm)monitoredVm);
/*  80 */       remoteVm = (RemoteVm)UnicastRemoteObject.exportObject((Remote)remoteVmImpl, 0);
/*     */     }
/*  82 */     catch (URISyntaxException uRISyntaxException) {
/*  83 */       throw new RuntimeException("Malformed VmIdentifier URI: " + str, uRISyntaxException);
/*     */     } 
/*     */     
/*  86 */     return remoteVm;
/*     */   }
/*     */   
/*     */   public void detachVm(RemoteVm paramRemoteVm) throws RemoteException {
/*  90 */     paramRemoteVm.detach();
/*     */   }
/*     */   
/*     */   public int[] activeVms() throws MonitorException {
/*  94 */     Object[] arrayOfObject = null;
/*  95 */     int[] arrayOfInt = null;
/*     */     
/*  97 */     arrayOfObject = this.monitoredHost.activeVms().toArray();
/*  98 */     arrayOfInt = new int[arrayOfObject.length];
/*     */     
/* 100 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 101 */       arrayOfInt[b] = ((Integer)arrayOfObject[b]).intValue();
/*     */     }
/* 103 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   public void vmStatusChanged(VmStatusChangeEvent paramVmStatusChangeEvent) {
/* 107 */     synchronized (this.activeVms) {
/* 108 */       this.activeVms.retainAll(paramVmStatusChangeEvent.getActive());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnected(HostEvent paramHostEvent) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstatd\RemoteHostImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */