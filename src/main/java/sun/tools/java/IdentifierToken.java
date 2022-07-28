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
/*    */ 
/*    */ 
/*    */ public class IdentifierToken
/*    */ {
/*    */   long where;
/*    */   int modifiers;
/*    */   Identifier id;
/*    */   
/*    */   public IdentifierToken(long paramLong, Identifier paramIdentifier) {
/* 47 */     this.where = paramLong;
/* 48 */     this.id = paramIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IdentifierToken(Identifier paramIdentifier) {
/* 55 */     this.where = 0L;
/* 56 */     this.id = paramIdentifier;
/*    */   }
/*    */   
/*    */   public IdentifierToken(long paramLong, Identifier paramIdentifier, int paramInt) {
/* 60 */     this.where = paramLong;
/* 61 */     this.id = paramIdentifier;
/* 62 */     this.modifiers = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getWhere() {
/* 67 */     return this.where;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getName() {
/* 72 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getModifiers() {
/* 77 */     return this.modifiers;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 81 */     return this.id.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long getWhere(IdentifierToken paramIdentifierToken, long paramLong) {
/* 89 */     return (paramIdentifierToken != null && paramIdentifierToken.where != 0L) ? paramIdentifierToken.where : paramLong;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\IdentifierToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */