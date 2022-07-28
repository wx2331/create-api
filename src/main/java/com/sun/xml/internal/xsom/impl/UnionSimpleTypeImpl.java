/*    */ package com.sun.xml.internal.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.internal.xsom.XSFacet;
/*    */ import com.sun.xml.internal.xsom.XSSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*    */ import com.sun.xml.internal.xsom.XSVariety;
/*    */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
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
/*    */ public class UnionSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSUnionSimpleType
/*    */ {
/*    */   private final Ref.SimpleType[] memberTypes;
/*    */   
/*    */   public UnionSimpleTypeImpl(SchemaDocumentImpl _parent, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String _name, boolean _anonymous, Set<XSVariety> finalSet, Ref.SimpleType[] _members) {
/* 49 */     super(_parent, _annon, _loc, _fa, _name, _anonymous, finalSet, 
/* 50 */         (_parent.getSchema()).parent.anySimpleType);
/*    */     
/* 52 */     this.memberTypes = _members;
/*    */   }
/*    */   
/*    */   public XSSimpleType getMember(int idx) {
/* 56 */     return this.memberTypes[idx].getType(); } public int getMemberSize() {
/* 57 */     return this.memberTypes.length;
/*    */   }
/*    */   public Iterator<XSSimpleType> iterator() {
/* 60 */     return new Iterator<XSSimpleType>() {
/* 61 */         int idx = 0;
/*    */         public boolean hasNext() {
/* 63 */           return (this.idx < UnionSimpleTypeImpl.this.memberTypes.length);
/*    */         }
/*    */         
/*    */         public XSSimpleType next() {
/* 67 */           return UnionSimpleTypeImpl.this.memberTypes[this.idx++].getType();
/*    */         }
/*    */         
/*    */         public void remove() {
/* 71 */           throw new UnsupportedOperationException();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 77 */     visitor.unionSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 80 */     return function.unionSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSUnionSimpleType getBaseUnionType() {
/* 84 */     return this;
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 88 */     return null; } public List<XSFacet> getFacets(String name) {
/* 89 */     return Collections.EMPTY_LIST;
/*    */   } public XSVariety getVariety() {
/* 91 */     return XSVariety.UNION;
/*    */   } public XSSimpleType getPrimitiveType() {
/* 93 */     return null;
/*    */   }
/* 95 */   public boolean isUnion() { return true; } public XSUnionSimpleType asUnion() {
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\UnionSimpleTypeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */