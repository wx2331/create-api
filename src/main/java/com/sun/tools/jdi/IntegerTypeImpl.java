/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.IntegerType;
/*    */ import com.sun.jdi.InvalidTypeException;
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
/*    */ public class IntegerTypeImpl
/*    */   extends PrimitiveTypeImpl
/*    */   implements IntegerType
/*    */ {
/*    */   IntegerTypeImpl(VirtualMachine paramVirtualMachine) {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   
/*    */   public String signature() {
/* 37 */     return String.valueOf('I');
/*    */   }
/*    */   
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return (PrimitiveValue)this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedIntValue());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\IntegerTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */