/*    */ package sun.jvmstat.perfdata.monitor.protocol.local;
/*    */ 
/*    */ import java.util.Timer;
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
/*    */ public class LocalEventTimer
/*    */   extends Timer
/*    */ {
/*    */   private static LocalEventTimer instance;
/*    */   
/*    */   private LocalEventTimer() {
/* 48 */     super(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static synchronized LocalEventTimer getInstance() {
/* 57 */     if (instance == null) {
/* 58 */       instance = new LocalEventTimer();
/*    */     }
/* 60 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\protocol\local\LocalEventTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */