/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSNotation;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotationImpl
/*    */   extends DeclarationImpl
/*    */   implements XSNotation
/*    */ {
/*    */   private final String publicId;
/*    */   private final String systemId;
/*    */   
/*    */   public NotationImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, String _publicId, String _systemId) {
/* 44 */     super(owner, _annon, _loc, _fa, owner.getTargetNamespace(), _name, false);
/*    */     
/* 46 */     this.publicId = _publicId;
/* 47 */     this.systemId = _systemId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPublicId() {
/* 53 */     return this.publicId; } public String getSystemId() {
/* 54 */     return this.systemId;
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 57 */     visitor.notation(this);
/*    */   }
/*    */   
/*    */   public Object apply(XSFunction function) {
/* 61 */     return function.notation(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\NotationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */