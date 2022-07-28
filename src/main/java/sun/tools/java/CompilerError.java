/*    */ package sun.tools.java;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompilerError
/*    */   extends Error
/*    */ {
/*    */   Throwable e;
/*    */   
/*    */   public CompilerError(String paramString) {
/* 44 */     super(paramString);
/* 45 */     this.e = this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CompilerError(Exception paramException) {
/* 52 */     super(paramException.getMessage());
/* 53 */     this.e = paramException;
/*    */   }
/*    */   
/*    */   public void printStackTrace() {
/* 57 */     if (this.e == this) {
/* 58 */       super.printStackTrace();
/*    */     } else {
/* 60 */       this.e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\CompilerError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */