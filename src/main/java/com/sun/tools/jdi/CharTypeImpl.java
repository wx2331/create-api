/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.CharType;
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
/*    */ public class CharTypeImpl
/*    */   extends PrimitiveTypeImpl
/*    */   implements CharType
/*    */ {
/*    */   CharTypeImpl(VirtualMachine paramVirtualMachine) {
/* 32 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */   
/*    */   public String signature() {
/* 37 */     return String.valueOf('C');
/*    */   }
/*    */   
/*    */   PrimitiveValue convert(PrimitiveValue paramPrimitiveValue) throws InvalidTypeException {
/* 41 */     return (PrimitiveValue)this.vm.mirrorOf(((PrimitiveValueImpl)paramPrimitiveValue).checkedCharValue());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\CharTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */