/*    */ package sun.jvmstat.perfdata.monitor.protocol.file;
/*    */ 
/*    */ import sun.jvmstat.monitor.MonitorException;
/*    */ import sun.jvmstat.monitor.VmIdentifier;
/*    */ import sun.jvmstat.monitor.event.VmListener;
/*    */ import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
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
/*    */ public class FileMonitoredVm
/*    */   extends AbstractMonitoredVm
/*    */ {
/*    */   public FileMonitoredVm(VmIdentifier paramVmIdentifier, int paramInt) throws MonitorException {
/* 56 */     super(paramVmIdentifier, paramInt);
/* 57 */     this.pdb = new PerfDataBuffer(paramVmIdentifier);
/*    */   }
/*    */   
/*    */   public void addVmListener(VmListener paramVmListener) {}
/*    */   
/*    */   public void removeVmListener(VmListener paramVmListener) {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\file\FileMonitoredVm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */