/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.PrimitiveType;
/*    */ import com.sun.jdi.PrimitiveValue;
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
/*    */ abstract class PrimitiveTypeImpl
/*    */   extends TypeImpl
/*    */   implements PrimitiveType
/*    */ {
/*    */   PrimitiveTypeImpl(VirtualMachine paramVirtualMachine) {
/* 33 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   abstract PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException;
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return name();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\PrimitiveTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */