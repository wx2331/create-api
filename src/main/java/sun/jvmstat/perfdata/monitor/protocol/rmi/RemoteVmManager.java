/*     */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*     */ 
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.jvmstat.monitor.MonitorException;
/*     */ import sun.jvmstat.monitor.remote.RemoteHost;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteVmManager
/*     */ {
/*     */   private RemoteHost remoteHost;
/*     */   private String user;
/*     */   
/*     */   public RemoteVmManager(RemoteHost paramRemoteHost) {
/*  63 */     this(paramRemoteHost, null);
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
/*     */   public RemoteVmManager(RemoteHost paramRemoteHost, String paramString) {
/*  79 */     this.user = paramString;
/*  80 */     this.remoteHost = paramRemoteHost;
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
/*     */   public Set<Integer> activeVms() throws MonitorException {
/*  96 */     int[] arrayOfInt = null;
/*     */     
/*     */     try {
/*  99 */       arrayOfInt = this.remoteHost.activeVms();
/*     */     }
/* 101 */     catch (RemoteException remoteException) {
/* 102 */       throw new MonitorException("Error communicating with remote host: " + remoteException
/* 103 */           .getMessage(), remoteException);
/*     */     } 
/*     */     
/* 106 */     HashSet<Integer> hashSet = new HashSet(arrayOfInt.length);
/*     */     
/* 108 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 109 */       hashSet.add(new Integer(arrayOfInt[b]));
/*     */     }
/*     */     
/* 112 */     return hashSet;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\rmi\RemoteVmManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */