/*    */ package sun.jvmstat.perfdata.monitor.protocol.rmi;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.rmi.RemoteException;
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.remote.RemoteVm;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PerfDataBuffer
/*    */   extends AbstractPerfDataBuffer
/*    */ {
/*    */   private RemoteVm rvm;
/*    */   
/*    */   public PerfDataBuffer(RemoteVm paramRemoteVm, int paramInt) throws MonitorException {
/* 61 */     this.rvm = paramRemoteVm;
/*    */     try {
/* 63 */       ByteBuffer byteBuffer = ByteBuffer.allocate(paramRemoteVm.getCapacity());
/* 64 */       sample(byteBuffer);
/* 65 */       createPerfDataBuffer(byteBuffer, paramInt);
/*    */     }
/* 67 */     catch (RemoteException remoteException) {
/* 68 */       throw new MonitorException("Could not read data for remote JVM " + paramInt, remoteException);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void sample(ByteBuffer paramByteBuffer) throws RemoteException {
/* 85 */     assert paramByteBuffer != null;
/* 86 */     assert this.rvm != null;
/* 87 */     synchronized (paramByteBuffer) {
/* 88 */       paramByteBuffer.clear();
/* 89 */       paramByteBuffer.put(this.rvm.getBytes());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\rmi\PerfDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */