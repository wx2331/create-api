/*    */ package sun.jvmstat.monitor.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import sun.jvmstat.monitor.MonitoredHost;
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
/*    */ public class HostEvent
/*    */   extends EventObject
/*    */ {
/*    */   public HostEvent(MonitoredHost paramMonitoredHost) {
/* 45 */     super(paramMonitoredHost);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MonitoredHost getMonitoredHost() {
/* 54 */     return (MonitoredHost)this.source;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\event\HostEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */