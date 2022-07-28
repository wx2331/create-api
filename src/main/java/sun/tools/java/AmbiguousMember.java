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
/*    */ public class AmbiguousMember
/*    */   extends Exception
/*    */ {
/*    */   public MemberDefinition field1;
/*    */   public MemberDefinition field2;
/*    */   
/*    */   public AmbiguousMember(MemberDefinition paramMemberDefinition1, MemberDefinition paramMemberDefinition2) {
/* 50 */     super(paramMemberDefinition1.getName() + " + " + paramMemberDefinition2.getName());
/* 51 */     this.field1 = paramMemberDefinition1;
/* 52 */     this.field2 = paramMemberDefinition2;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\AmbiguousMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */