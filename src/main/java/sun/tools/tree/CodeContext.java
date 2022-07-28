/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Label;
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
/*    */ class CodeContext
/*    */   extends Context
/*    */ {
/*    */   Label breakLabel;
/*    */   Label contLabel;
/*    */   
/*    */   CodeContext(Context paramContext, Node paramNode) {
/* 44 */     super(paramContext, paramNode);
/* 45 */     switch (paramNode.op) {
/*    */       case 92:
/*    */       case 93:
/*    */       case 94:
/*    */       case 103:
/*    */       case 126:
/* 51 */         this.breakLabel = new Label();
/* 52 */         this.contLabel = new Label();
/*    */         return;
/*    */       case 95:
/*    */       case 101:
/*    */       case 150:
/*    */       case 151:
/* 58 */         this.breakLabel = new Label();
/*    */         return;
/*    */     } 
/* 61 */     if (paramNode instanceof Statement && ((Statement)paramNode).labels != null)
/* 62 */       this.breakLabel = new Label(); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\CodeContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */