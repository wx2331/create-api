/*    */ package org.relaxng.datatype;
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
/*    */ public class DatatypeException
/*    */   extends Exception
/*    */ {
/*    */   private final int index;
/*    */   public static final int UNKNOWN = -1;
/*    */   
/*    */   public DatatypeException(int index, String msg) {
/* 45 */     super(msg);
/* 46 */     this.index = index;
/*    */   }
/*    */   public DatatypeException(String msg) {
/* 49 */     this(-1, msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DatatypeException() {
/* 56 */     this(-1, null);
/*    */   }
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
/*    */   public int getIndex() {
/* 70 */     return this.index;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\DatatypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */