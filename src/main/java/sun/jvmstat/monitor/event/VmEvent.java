/*    */ package sun.jvmstat.monitor.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import sun.jvmstat.monitor.MonitoredVm;
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
/*    */ public class VmEvent
/*    */   extends EventObject
/*    */ {
/*    */   public VmEvent(MonitoredVm paramMonitoredVm) {
/* 45 */     super(paramMonitoredVm);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MonitoredVm getMonitoredVm() {
/* 54 */     return (MonitoredVm)this.source;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\event\VmEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */