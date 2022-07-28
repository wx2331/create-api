/*    */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.nc.AnyNameExceptNameClass;
/*    */ import com.sun.xml.internal.rngom.nc.ChoiceNameClass;
/*    */ import com.sun.xml.internal.rngom.nc.NameClass;
/*    */ import com.sun.xml.internal.rngom.nc.NsNameClass;
/*    */ import com.sun.xml.internal.xsom.XSWildcard;
/*    */ import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ public final class WildcardNameClassBuilder
/*    */   implements XSWildcardFunction<NameClass>
/*    */ {
/* 50 */   private static final XSWildcardFunction<NameClass> theInstance = new WildcardNameClassBuilder();
/*    */ 
/*    */   
/*    */   public static NameClass build(XSWildcard wc) {
/* 54 */     return (NameClass)wc.apply(theInstance);
/*    */   }
/*    */   
/*    */   public NameClass any(XSWildcard.Any wc) {
/* 58 */     return NameClass.ANY;
/*    */   }
/*    */   
/*    */   public NameClass other(XSWildcard.Other wc) {
/* 62 */     return (NameClass)new AnyNameExceptNameClass((NameClass)new ChoiceNameClass((NameClass)new NsNameClass(""), (NameClass)new NsNameClass(wc
/*    */ 
/*    */             
/* 65 */             .getOtherNamespace())));
/*    */   }
/*    */   public NameClass union(XSWildcard.Union wc) {
/*    */     ChoiceNameClass choiceNameClass;
/* 69 */     NameClass nc = null;
/* 70 */     for (Iterator<String> itr = wc.iterateNamespaces(); itr.hasNext(); ) {
/* 71 */       NsNameClass nsNameClass; String ns = itr.next();
/*    */       
/* 73 */       if (nc == null) { nsNameClass = new NsNameClass(ns); continue; }
/*    */       
/* 75 */       choiceNameClass = new ChoiceNameClass((NameClass)nsNameClass, (NameClass)new NsNameClass(ns));
/*    */     } 
/*    */ 
/*    */     
/* 79 */     assert choiceNameClass != null;
/*    */     
/* 81 */     return (NameClass)choiceNameClass;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\WildcardNameClassBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */