/*    */ package sun.tools.jstat;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HeaderClosure
/*    */   implements Closure
/*    */ {
/*    */   private static final char ALIGN_CHAR = '^';
/* 41 */   private StringBuilder header = new StringBuilder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(Object paramObject, boolean paramBoolean) throws MonitorException {
/* 49 */     if (!(paramObject instanceof ColumnFormat)) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     ColumnFormat columnFormat = (ColumnFormat)paramObject;
/*    */     
/* 55 */     String str = columnFormat.getHeader();
/*    */ 
/*    */     
/* 58 */     if (str.indexOf('^') >= 0) {
/* 59 */       int i = str.length();
/* 60 */       if (str.charAt(0) == '^' && str
/* 61 */         .charAt(i - 1) == '^') {
/*    */         
/* 63 */         columnFormat.setWidth(Math.max(columnFormat.getWidth(), 
/* 64 */               Math.max(columnFormat.getFormat().length(), i - 2)));
/* 65 */         str = str.substring(1, i - 1);
/* 66 */         str = Alignment.CENTER.align(str, columnFormat.getWidth());
/* 67 */       } else if (str.charAt(0) == '^') {
/*    */         
/* 69 */         columnFormat.setWidth(Math.max(columnFormat.getWidth(), 
/* 70 */               Math.max(columnFormat.getFormat().length(), i - 1)));
/* 71 */         str = str.substring(1, i);
/* 72 */         str = Alignment.LEFT.align(str, columnFormat.getWidth());
/* 73 */       } else if (str.charAt(i - 1) == '^') {
/*    */         
/* 75 */         columnFormat.setWidth(Math.max(columnFormat.getWidth(), 
/* 76 */               Math.max(columnFormat.getFormat().length(), i - 1)));
/* 77 */         str = str.substring(0, i - 1);
/* 78 */         str = Alignment.RIGHT.align(str, columnFormat.getWidth());
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 86 */     this.header.append(str);
/* 87 */     if (paramBoolean) {
/* 88 */       this.header.append(" ");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHeader() {
/* 96 */     return this.header.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\HeaderClosure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */