/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ import com.sun.jdi.ClassNotLoadedException;
/*    */ import com.sun.jdi.Location;
/*    */ import com.sun.jdi.Type;
/*    */ import com.sun.jdi.VirtualMachine;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ObsoleteMethodImpl
/*    */   extends NonConcreteMethodImpl
/*    */ {
/* 38 */   private Location location = null;
/*    */ 
/*    */   
/*    */   ObsoleteMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl) {
/* 42 */     super(paramVirtualMachine, paramReferenceTypeImpl, 0L, "<obsolete>", "", null, 0);
/*    */   }
/*    */   
/*    */   public boolean isObsolete() {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public String returnTypeName() {
/* 50 */     return "<unknown>";
/*    */   }
/*    */   
/*    */   public Type returnType() throws ClassNotLoadedException {
/* 54 */     throw new ClassNotLoadedException("type unknown");
/*    */   }
/*    */   
/*    */   public List<String> argumentTypeNames() {
/* 58 */     return new ArrayList<>();
/*    */   }
/*    */   
/*    */   public List<String> argumentSignatures() {
/* 62 */     return new ArrayList<>();
/*    */   }
/*    */   
/*    */   Type argumentType(int paramInt) throws ClassNotLoadedException {
/* 66 */     throw new ClassNotLoadedException("type unknown");
/*    */   }
/*    */   
/*    */   public List<Type> argumentTypes() throws ClassNotLoadedException {
/* 70 */     return new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ObsoleteMethodImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */