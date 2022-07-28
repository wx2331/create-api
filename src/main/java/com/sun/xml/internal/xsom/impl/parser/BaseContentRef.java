/*    */ package com.sun.xml.internal.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSContentType;
/*    */ import com.sun.xml.internal.xsom.XSType;
/*    */ import com.sun.xml.internal.xsom.impl.Ref;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public final class BaseContentRef
/*    */   implements Ref.ContentType, Patch
/*    */ {
/*    */   private final Ref.Type baseType;
/*    */   private final Locator loc;
/*    */   
/*    */   public BaseContentRef(final NGCCRuntimeEx $runtime, Ref.Type _baseType) {
/* 39 */     this.baseType = _baseType;
/* 40 */     $runtime.addPatcher(this);
/* 41 */     $runtime.addErrorChecker(new Patch() {
/*    */           public void run() throws SAXException {
/* 43 */             XSType t = BaseContentRef.this.baseType.getType();
/* 44 */             if (t.isComplexType() && t.asComplexType().getContentType().asParticle() != null) {
/* 45 */               $runtime.reportError(
/* 46 */                   Messages.format("SimpleContentExpected", new Object[] {
/* 47 */                       t.getTargetNamespace(), t.getName() }), BaseContentRef.this.loc);
/*    */             }
/*    */           }
/*    */         });
/* 51 */     this.loc = $runtime.copyLocator();
/*    */   }
/*    */   
/*    */   public XSContentType getContentType() {
/* 55 */     XSType t = this.baseType.getType();
/* 56 */     if (t.asComplexType() != null) {
/* 57 */       return t.asComplexType().getContentType();
/*    */     }
/* 59 */     return (XSContentType)t.asSimpleType();
/*    */   }
/*    */   
/*    */   public void run() throws SAXException {
/* 63 */     if (this.baseType instanceof Patch)
/* 64 */       ((Patch)this.baseType).run(); 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\BaseContentRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */