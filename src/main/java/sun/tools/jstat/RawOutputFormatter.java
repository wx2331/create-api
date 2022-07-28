/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.util.List;
/*    */ import sun.jvmstat.monitor.Monitor;
/*    */ import sun.jvmstat.monitor.MonitorException;
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
/*    */ public class RawOutputFormatter
/*    */   implements OutputFormatter
/*    */ {
/*    */   private List logged;
/*    */   private String header;
/*    */   private boolean printStrings;
/*    */   
/*    */   public RawOutputFormatter(List paramList, boolean paramBoolean) {
/* 43 */     this.logged = paramList;
/* 44 */     this.printStrings = paramBoolean;
/*    */   }
/*    */   
/*    */   public String getHeader() throws MonitorException {
/* 48 */     if (this.header == null) {
/*    */       
/* 50 */       StringBuilder stringBuilder = new StringBuilder();
/* 51 */       for (Monitor monitor : this.logged)
/*    */       {
/* 53 */         stringBuilder.append(monitor.getName() + " ");
/*    */       }
/* 55 */       this.header = stringBuilder.toString();
/*    */     } 
/* 57 */     return this.header;
/*    */   }
/*    */   
/*    */   public String getRow() throws MonitorException {
/* 61 */     StringBuilder stringBuilder = new StringBuilder();
/* 62 */     byte b = 0;
/* 63 */     for (Monitor monitor : this.logged) {
/*    */       
/* 65 */       if (b++ > 0) {
/* 66 */         stringBuilder.append(" ");
/*    */       }
/* 68 */       if (this.printStrings && monitor instanceof sun.jvmstat.monitor.StringMonitor) {
/* 69 */         stringBuilder.append("\"").append(monitor.getValue()).append("\""); continue;
/*    */       } 
/* 71 */       stringBuilder.append(monitor.getValue());
/*    */     } 
/*    */     
/* 74 */     return stringBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\RawOutputFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */