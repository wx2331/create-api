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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClassType
/*    */   extends Type
/*    */ {
/*    */   Identifier className;
/*    */   
/*    */   ClassType(String paramString, Identifier paramIdentifier) {
/* 50 */     super(10, paramString);
/* 51 */     this.className = paramIdentifier;
/*    */   }
/*    */   
/*    */   public Identifier getClassName() {
/* 55 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String typeString(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/* 61 */     String str = (paramBoolean1 ? getClassName().getFlatName() : Identifier.lookup(getClassName().getQualifier(), getClassName().getFlatName())).toString();
/* 62 */     return (paramString.length() > 0) ? (str + " " + paramString) : str;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */