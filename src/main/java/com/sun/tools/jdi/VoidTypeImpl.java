/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import com.sun.jdi.VoidType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VoidTypeImpl
/*    */   extends TypeImpl
/*    */   implements VoidType
/*    */ {
/*    */   VoidTypeImpl(VirtualMachine paramVirtualMachine) {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */   
/*    */   public String signature() {
/* 36 */     return String.valueOf('V');
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     return name();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VoidTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */