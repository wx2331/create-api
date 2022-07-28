/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.Type;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeImpl
/*    */   extends MirrorImpl
/*    */   implements Type
/*    */ {
/* 32 */   private String myName = null;
/*    */ 
/*    */   
/*    */   TypeImpl(VirtualMachine paramVirtualMachine) {
/* 36 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String name() {
/* 42 */     if (this.myName == null) {
/* 43 */       JNITypeParser jNITypeParser = new JNITypeParser(signature());
/* 44 */       this.myName = jNITypeParser.typeName();
/*    */     } 
/* 46 */     return this.myName;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 50 */     if (paramObject != null && paramObject instanceof Type) {
/* 51 */       Type type = (Type)paramObject;
/* 52 */       return (signature().equals(type.signature()) && super
/* 53 */         .equals(paramObject));
/*    */     } 
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return signature().hashCode();
/*    */   }
/*    */   
/*    */   public abstract String signature();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\TypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */