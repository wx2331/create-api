/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSDeclaration;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import org.xml.sax.Locator;
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
/*    */ abstract class DeclarationImpl
/*    */   extends ComponentImpl
/*    */   implements XSDeclaration
/*    */ {
/*    */   private final String name;
/*    */   private final String targetNamespace;
/*    */   private final boolean anonymous;
/*    */   
/*    */   DeclarationImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator loc, ForeignAttributesImpl fa, String _targetNamespace, String _name, boolean _anonymous) {
/* 39 */     super(owner, _annon, loc, fa);
/* 40 */     this.targetNamespace = _targetNamespace;
/* 41 */     this.name = _name;
/* 42 */     this.anonymous = _anonymous;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   public String getTargetNamespace() {
/* 49 */     return this.targetNamespace;
/*    */   }
/*    */   
/*    */   public boolean isAnonymous() {
/* 53 */     return this.anonymous;
/*    */   }
/* 55 */   public final boolean isGlobal() { return !isAnonymous(); } public final boolean isLocal() {
/* 56 */     return isAnonymous();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\DeclarationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */