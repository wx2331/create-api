/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSVariety;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public class ListSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSListSimpleType
/*    */ {
/*    */   private final Ref.SimpleType itemType;
/*    */   
/*    */   public ListSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType _itemType) {
/* 48 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, 
/* 49 */         (_parent.getSchema()).parent.anySimpleType);
/*    */     
/* 51 */     this.itemType = _itemType;
/*    */   }
/*    */   
/*    */   public XSSimpleType getItemType() {
/* 55 */     return this.itemType.getType();
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 58 */     visitor.listSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 61 */     return function.listSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 65 */     return null; } public List<XSFacet> getFacets(String name) {
/* 66 */     return Collections.EMPTY_LIST;
/*    */   } public XSVariety getVariety() {
/* 68 */     return XSVariety.LIST;
/*    */   } public XSSimpleType getPrimitiveType() {
/* 70 */     return null;
/*    */   } public XSListSimpleType getBaseListType() {
/* 72 */     return this;
/*    */   }
/* 74 */   public boolean isList() { return true; } public XSListSimpleType asList() {
/* 75 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\ListSimpleTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */