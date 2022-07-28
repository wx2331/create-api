/*    */ package sun.jvmstat.perfdata.monitor;
/*    */ 
/*    */ import java.util.TimerTask;
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
/*    */ public class CountedTimerTask
/*    */   extends TimerTask
/*    */ {
/*    */   volatile long executionCount;
/*    */   
/*    */   public long executionCount() {
/* 42 */     return this.executionCount;
/*    */   }
/*    */   
/*    */   public void run() {
/* 46 */     this.executionCount++;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\perfdata\monitor\CountedTimerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */