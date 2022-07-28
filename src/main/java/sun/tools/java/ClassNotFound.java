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
/*    */ public class ClassNotFound
/*    */   extends Exception
/*    */ {
/*    */   public Identifier name;
/*    */   
/*    */   public ClassNotFound(Identifier paramIdentifier) {
/* 47 */     super(paramIdentifier.toString());
/* 48 */     this.name = paramIdentifier;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\ClassNotFound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */