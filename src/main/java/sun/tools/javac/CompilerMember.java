/*    */ package sun.tools.javac;
/*    */ 
/*    */ import sun.tools.asm.Assembler;
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
/*    */ @Deprecated
/*    */ final class CompilerMember
/*    */   implements Comparable
/*    */ {
/*    */   MemberDefinition field;
/*    */   Assembler asm;
/*    */   Object value;
/*    */   String name;
/*    */   String sig;
/*    */   String key;
/*    */   
/*    */   CompilerMember(MemberDefinition paramMemberDefinition, Assembler paramAssembler) {
/* 50 */     this.field = paramMemberDefinition;
/* 51 */     this.asm = paramAssembler;
/* 52 */     this.name = paramMemberDefinition.getName().toString();
/* 53 */     this.sig = paramMemberDefinition.getType().getTypeSignature();
/*    */   }
/*    */   
/*    */   public int compareTo(Object paramObject) {
/* 57 */     CompilerMember compilerMember = (CompilerMember)paramObject;
/* 58 */     return getKey().compareTo(compilerMember.getKey());
/*    */   }
/*    */   
/*    */   String getKey() {
/* 62 */     if (this.key == null)
/* 63 */       this.key = this.name + this.sig; 
/* 64 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\javac\CompilerMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */