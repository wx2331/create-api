/*    */ package sun.tools.jstatd;
/*    */ 
/*    */ import sun.jvmstat.monitor.remote.BufferedMonitoredVm;
/*    */ import sun.jvmstat.monitor.remote.RemoteVm;
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
/*    */ public class RemoteVmImpl
/*    */   implements RemoteVm
/*    */ {
/*    */   private BufferedMonitoredVm mvm;
/*    */   
/*    */   RemoteVmImpl(BufferedMonitoredVm paramBufferedMonitoredVm) {
/* 47 */     this.mvm = paramBufferedMonitoredVm;
/*    */   }
/*    */   
/*    */   public byte[] getBytes() {
/* 51 */     return this.mvm.getBytes();
/*    */   }
/*    */   
/*    */   public int getCapacity() {
/* 55 */     return this.mvm.getCapacity();
/*    */   }
/*    */   
/*    */   public void detach() {
/* 59 */     this.mvm.detach();
/*    */   }
/*    */   
/*    */   public int getLocalVmId() {
/* 63 */     return this.mvm.getVmIdentifier().getLocalVmId();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstatd\RemoteVmImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */