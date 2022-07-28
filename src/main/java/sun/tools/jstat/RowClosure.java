/*    */ package sun.tools.jstat;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import sun.jvmstat.monitor.MonitorException;
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
/*    */ public class RowClosure
/*    */   implements Closure
/*    */ {
/*    */   private MonitoredVm vm;
/* 41 */   private StringBuilder row = new StringBuilder();
/*    */   
/*    */   public RowClosure(MonitoredVm paramMonitoredVm) {
/* 44 */     this.vm = paramMonitoredVm;
/*    */   }
/*    */   
/*    */   public void visit(Object paramObject, boolean paramBoolean) throws MonitorException {
/* 48 */     if (!(paramObject instanceof ColumnFormat)) {
/*    */       return;
/*    */     }
/*    */     
/* 52 */     ColumnFormat columnFormat = (ColumnFormat)paramObject;
/* 53 */     String str = null;
/*    */     
/* 55 */     Expression expression = columnFormat.getExpression();
/* 56 */     ExpressionExecuter expressionExecuter = new ExpressionExecuter(this.vm);
/* 57 */     Object object = expressionExecuter.evaluate(expression);
/*    */     
/* 59 */     if (object instanceof String) {
/* 60 */       str = (String)object;
/* 61 */     } else if (object instanceof Number) {
/* 62 */       double d1 = ((Number)object).doubleValue();
/* 63 */       double d2 = columnFormat.getScale().scale(d1);
/* 64 */       DecimalFormat decimalFormat = new DecimalFormat(columnFormat.getFormat());
/* 65 */       DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
/* 66 */       decimalFormatSymbols.setNaN("-");
/* 67 */       decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
/* 68 */       str = decimalFormat.format(d2);
/*    */     } 
/*    */     
/* 71 */     columnFormat.setPreviousValue(object);
/* 72 */     str = columnFormat.getAlignment().align(str, columnFormat.getWidth());
/* 73 */     this.row.append(str);
/* 74 */     if (paramBoolean) {
/* 75 */       this.row.append(" ");
/*    */     }
/*    */   }
/*    */   
/*    */   public String getRow() {
/* 80 */     return this.row.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\RowClosure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */