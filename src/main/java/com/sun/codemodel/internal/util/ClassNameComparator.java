/*    */ package com.sun.codemodel.internal.util;
/*    */ 
/*    */ import com.sun.codemodel.internal.JClass;
/*    */ import java.util.Comparator;
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
/*    */ public class ClassNameComparator
/*    */   implements Comparator<JClass>
/*    */ {
/*    */   public int compare(JClass l, JClass r) {
/* 42 */     return l.fullName().compareTo(r.fullName());
/*    */   }
/*    */   
/* 45 */   public static final Comparator<JClass> theInstance = new ClassNameComparator();
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\ClassNameComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */