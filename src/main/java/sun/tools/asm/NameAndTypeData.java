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
/*    */ final class NameAndTypeData
/*    */ {
/*    */   MemberDefinition field;
/*    */   
/*    */   NameAndTypeData(MemberDefinition paramMemberDefinition) {
/* 45 */     this.field = paramMemberDefinition;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     return this.field.getName().hashCode() * this.field.getType().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 59 */     if (paramObject != null && paramObject instanceof NameAndTypeData) {
/* 60 */       NameAndTypeData nameAndTypeData = (NameAndTypeData)paramObject;
/* 61 */       return (this.field.getName().equals(nameAndTypeData.field.getName()) && this.field
/* 62 */         .getType().equals(nameAndTypeData.field.getType()));
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "%%" + this.field.toString() + "%%";
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\asm\NameAndTypeData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */