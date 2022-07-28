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
/*    */ public class Cover
/*    */ {
/*    */   public int Type;
/*    */   public long Addr;
/*    */   public int NumCommand;
/*    */   
/*    */   public Cover(int paramInt1, long paramLong, int paramInt2) {
/* 42 */     this.Type = paramInt1;
/* 43 */     this.Addr = paramLong;
/* 44 */     this.NumCommand = paramInt2;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\Cover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */