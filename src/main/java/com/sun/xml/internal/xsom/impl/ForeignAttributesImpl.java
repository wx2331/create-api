/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.ForeignAttributes;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.AttributesImpl;
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
/*    */ public final class ForeignAttributesImpl
/*    */   extends AttributesImpl
/*    */   implements ForeignAttributes
/*    */ {
/*    */   private final ValidationContext context;
/*    */   private final Locator locator;
/*    */   final ForeignAttributesImpl next;
/*    */   
/*    */   public ForeignAttributesImpl(ValidationContext context, Locator locator, ForeignAttributesImpl next) {
/* 47 */     this.context = context;
/* 48 */     this.locator = locator;
/* 49 */     this.next = next;
/*    */   }
/*    */   
/*    */   public ValidationContext getContext() {
/* 53 */     return this.context;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 57 */     return this.locator;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\ForeignAttributesImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */