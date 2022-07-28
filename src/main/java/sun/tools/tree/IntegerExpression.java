/*    */ package sun.tools.tree;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
/*    */ import sun.tools.java.Environment;
/*    */ import sun.tools.java.Type;
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
/*    */ public class IntegerExpression
/*    */   extends ConstantExpression
/*    */ {
/*    */   int value;
/*    */   
/*    */   IntegerExpression(int paramInt1, long paramLong, Type paramType, int paramInt2) {
/* 44 */     super(paramInt1, paramLong, paramType);
/* 45 */     this.value = paramInt2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean fitsType(Environment paramEnvironment, Context paramContext, Type paramType) {
/* 52 */     if (this.type.isType(2))
/*    */     {
/*    */ 
/*    */       
/* 56 */       return super.fitsType(paramEnvironment, paramContext, paramType);
/*    */     }
/* 58 */     switch (paramType.getTypeCode()) {
/*    */       case 1:
/* 60 */         return (this.value == (byte)this.value);
/*    */       case 3:
/* 62 */         return (this.value == (short)this.value);
/*    */       case 2:
/* 64 */         return (this.value == (char)this.value);
/*    */     } 
/* 66 */     return super.fitsType(paramEnvironment, paramContext, paramType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 73 */     return new Integer(this.value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(int paramInt) {
/* 80 */     return (this.value == paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equalsDefault() {
/* 87 */     return (this.value == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 94 */     paramAssembler.add(this.where, 18, new Integer(this.value));
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\IntegerExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */