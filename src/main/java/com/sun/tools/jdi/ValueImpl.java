/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ClassNotLoadedException;
/*    */ import com.sun.jdi.InvalidTypeException;
/*    */ import com.sun.jdi.Value;
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
/*    */ abstract class ValueImpl
/*    */   extends MirrorImpl
/*    */   implements Value
/*    */ {
/*    */   ValueImpl(VirtualMachine paramVirtualMachine) {
/* 33 */     super(paramVirtualMachine);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static ValueImpl prepareForAssignment(Value paramValue, ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException {
/* 39 */     if (paramValue == null) {
/*    */ 
/*    */ 
/*    */       
/* 43 */       if (paramValueContainer.signature().length() == 1) {
/* 44 */         throw new InvalidTypeException("Can't set a primitive type to null");
/*    */       }
/* 46 */       return null;
/*    */     } 
/* 48 */     return ((ValueImpl)paramValue).prepareForAssignmentTo(paramValueContainer);
/*    */   }
/*    */ 
/*    */   
/*    */   static byte typeValueKey(Value paramValue) {
/* 53 */     if (paramValue == null) {
/* 54 */       return 76;
/*    */     }
/* 56 */     return ((ValueImpl)paramValue).typeValueKey();
/*    */   }
/*    */   
/*    */   abstract ValueImpl prepareForAssignmentTo(ValueContainer paramValueContainer) throws InvalidTypeException, ClassNotLoadedException;
/*    */   
/*    */   abstract byte typeValueKey();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ValueImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */