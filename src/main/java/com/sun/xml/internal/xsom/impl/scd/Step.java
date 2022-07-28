/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import java.util.Iterator;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public abstract class Step<T extends XSComponent>
/*     */ {
/*     */   public final Axis<? extends T> axis;
/*  54 */   int predicate = -1;
/*     */
/*     */   protected Step(Axis<? extends T> axis) {
/*  57 */     this.axis = axis;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   protected abstract Iterator<? extends T> filter(Iterator<? extends T> paramIterator);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public final Iterator<T> evaluate(Iterator<XSComponent> nodeSet) {
/*  71 */     Iterator<T> r = new Iterators.Map<T, XSComponent>(nodeSet) {
/*     */         protected Iterator<? extends T> apply(XSComponent contextNode) {
/*  73 */           return Step.this.filter(Step.this.axis.iterator(contextNode));
/*     */         }
/*     */       };
/*     */
/*     */
/*  78 */     r = new Iterators.Unique<>(r);
/*     */
/*  80 */     if (this.predicate >= 0) {
/*  81 */       XSComponent xSComponent; T item = null;
/*  82 */       for (int i = this.predicate; i > 0; i--) {
/*  83 */         if (!r.hasNext())
/*  84 */           return Iterators.empty();
/*  85 */         xSComponent = (XSComponent)r.next();
/*     */       }
/*  87 */       return new Iterators.Singleton<>((T)xSComponent);
/*     */     }
/*     */
/*  90 */     return r;
/*     */   }
/*     */
/*     */
/*     */   static final class Any
/*     */     extends Step<XSComponent>
/*     */   {
/*     */     public Any(Axis<? extends XSComponent> axis) {
/*  98 */       super(axis);
/*     */     }
/*     */
/*     */
/*     */     protected Iterator<? extends XSComponent> filter(Iterator<? extends XSComponent> base) {
/* 103 */       return base;
/*     */     }
/*     */   }
/*     */
/*     */   private static abstract class Filtered<T extends XSComponent> extends Step<T> {
/*     */     protected Filtered(Axis<? extends T> axis) {
/* 109 */       super(axis);
/*     */     }
/*     */
/*     */     protected Iterator<T> filter(Iterator<? extends T> base) {
/* 113 */       return new Iterators.Filter<T>(base) {
/*     */           protected boolean matches(T d) {
/* 115 */             return Filtered.this.match(d);
/*     */           }
/*     */         };
/*     */     }
/*     */
/*     */
/*     */     protected abstract boolean match(T param1T);
/*     */   }
/*     */
/*     */   static final class Named
/*     */     extends Filtered<XSDeclaration>
/*     */   {
/*     */     private final String nsUri;
/*     */     private final String localName;
/*     */
/*     */     public Named(Axis<? extends XSDeclaration> axis, UName n) {
/* 131 */       this(axis, n.getNamespaceURI(), n.getName());
/*     */     }
/*     */
/*     */     public Named(Axis<? extends XSDeclaration> axis, String nsUri, String localName) {
/* 135 */       super(axis);
/* 136 */       this.nsUri = nsUri;
/* 137 */       this.localName = localName;
/*     */     }
/*     */
/*     */     protected boolean match(XSDeclaration d) {
/* 141 */       return (d.getName().equals(this.localName) && d.getTargetNamespace().equals(this.nsUri));
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   static final class AnonymousType
/*     */     extends Filtered<XSType>
/*     */   {
/*     */     public AnonymousType(Axis<? extends XSType> axis) {
/* 150 */       super(axis);
/*     */     }
/*     */
/*     */     protected boolean match(XSType node) {
/* 154 */       return node.isLocal();
/*     */     }
/*     */   }
/*     */
/*     */   static final class Facet
/*     */     extends Filtered<XSFacet>
/*     */   {
/*     */     private final String name;
/*     */
/*     */     public Facet(Axis<XSFacet> axis, String facetName) {
/* 164 */       super(axis);
/* 165 */       this.name = facetName;
/*     */     }
/*     */
/*     */     protected boolean match(XSFacet f) {
/* 169 */       return f.getName().equals(this.name);
/*     */     }
/*     */   }
/*     */
/*     */   static final class Schema
/*     */     extends Filtered<XSSchema>
/*     */   {
/*     */     private final String uri;
/*     */
/*     */     public Schema(Axis<XSSchema> axis, String uri) {
/* 179 */       super(axis);
/* 180 */       this.uri = uri;
/*     */     }
/*     */
/*     */     protected boolean match(XSSchema d) {
/* 184 */       return d.getTargetNamespace().equals(this.uri);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
