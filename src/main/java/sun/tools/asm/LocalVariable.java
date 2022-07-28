/*    */ package sun.tools.asm;
/*    */ 
/*    */ import sun.tools.java.MemberDefinition;
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
/*    */ public final class LocalVariable
/*    */ {
/*    */   MemberDefinition field;
/*    */   int slot;
/*    */   int from;
/*    */   int to;
/*    */   
/*    */   public LocalVariable(MemberDefinition paramMemberDefinition, int paramInt) {
/* 48 */     if (paramMemberDefinition == null) {
/* 49 */       (new Exception()).printStackTrace();
/*    */     }
/* 51 */     this.field = paramMemberDefinition;
/* 52 */     this.slot = paramInt;
/* 53 */     this.to = -1;
/*    */   }
/*    */   
/*    */   LocalVariable(MemberDefinition paramMemberDefinition, int paramInt1, int paramInt2, int paramInt3) {
/* 57 */     this.field = paramMemberDefinition;
/* 58 */     this.slot = paramInt1;
/* 59 */     this.from = paramInt2;
/* 60 */     this.to = paramInt3;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 64 */     return this.field + "/" + this.slot;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\LocalVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */