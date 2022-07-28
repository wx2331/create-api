/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.LongType;
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
/*    */ public class LongTypeImpl
/*    */   extends PrimitiveTypeImpl
/*    */   implements LongType
/*    */ {
/*    */   LongTypeImpl(VirtualMachine paramVirtualMachine) {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   
/*    */   public String signature() {
/* 37 */     return String.valueOf('J');
/*    */   }
/*    */   
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return (PrimitiveValue)this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedLongValue());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\LongTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */