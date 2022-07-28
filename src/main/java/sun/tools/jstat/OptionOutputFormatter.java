/*    */ package sun.tools.jstat;
/*    */ 
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
/*    */ 
/*    */ public class OptionOutputFormatter
/*    */   implements OutputFormatter
/*    */ {
/*    */   private OptionFormat format;
/*    */   private String header;
/*    */   private MonitoredVm vm;
/*    */   
/*    */   public OptionOutputFormatter(MonitoredVm paramMonitoredVm, OptionFormat paramOptionFormat) throws MonitorException {
/* 44 */     this.vm = paramMonitoredVm;
/* 45 */     this.format = paramOptionFormat;
/* 46 */     resolve();
/*    */   }
/*    */   
/*    */   private void resolve() throws MonitorException {
/* 50 */     ExpressionResolver expressionResolver = new ExpressionResolver(this.vm);
/* 51 */     SymbolResolutionClosure symbolResolutionClosure = new SymbolResolutionClosure(expressionResolver);
/* 52 */     this.format.apply(symbolResolutionClosure);
/*    */   }
/*    */   
/*    */   public String getHeader() throws MonitorException {
/* 56 */     if (this.header == null) {
/* 57 */       HeaderClosure headerClosure = new HeaderClosure();
/* 58 */       this.format.apply(headerClosure);
/* 59 */       this.header = headerClosure.getHeader();
/*    */     } 
/* 61 */     return this.header;
/*    */   }
/*    */   
/*    */   public String getRow() throws MonitorException {
/* 65 */     RowClosure rowClosure = new RowClosure(this.vm);
/* 66 */     this.format.apply(rowClosure);
/* 67 */     return rowClosure.getRow();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\OptionOutputFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */