/*    */ package sun.tools.asm;
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
/*    */ public final class CatchData
/*    */ {
/*    */   Object type;
/*    */   Label label;
/*    */   
/*    */   CatchData(Object paramObject) {
/* 45 */     this.type = paramObject;
/* 46 */     this.label = new Label();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Label getLabel() {
/* 53 */     return this.label;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getType() {
/* 60 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\CatchData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */