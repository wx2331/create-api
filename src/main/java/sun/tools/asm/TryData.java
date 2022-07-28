/*    */ package sun.tools.asm;
/*    */ 
/*    */ import java.util.Vector;
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
/*    */ public final class TryData
/*    */ {
/* 38 */   Vector<CatchData> catches = new Vector<>();
/* 39 */   Label endLabel = new Label();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CatchData add(Object paramObject) {
/* 45 */     CatchData catchData = new CatchData(paramObject);
/* 46 */     this.catches.addElement(catchData);
/* 47 */     return catchData;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CatchData getCatch(int paramInt) {
/* 54 */     return this.catches.elementAt(paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Label getEndLabel() {
/* 61 */     return this.endLabel;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\TryData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */