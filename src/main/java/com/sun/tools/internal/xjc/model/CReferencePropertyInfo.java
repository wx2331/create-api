/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
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
/*     */ public final class CReferencePropertyInfo
/*     */   extends CPropertyInfo
/*     */   implements ReferencePropertyInfo<NType, NClass>
/*     */ {
/*     */   private final boolean required;
/*  63 */   private final Set<CElement> elements = new HashSet<>();
/*     */   
/*     */   private final boolean isMixed;
/*     */   
/*     */   private WildcardMode wildcard;
/*     */   private boolean dummy;
/*     */   private boolean content;
/*     */   private boolean isMixedExtendedCust = false;
/*     */   
/*     */   public CReferencePropertyInfo(String name, boolean collection, boolean required, boolean isMixed, XSComponent source, CCustomizations customizations, Locator locator, boolean dummy, boolean content, boolean isMixedExtended) {
/*  73 */     super(name, ((collection || isMixed) && !dummy), source, customizations, locator);
/*  74 */     this.isMixed = isMixed;
/*  75 */     this.required = required;
/*  76 */     this.dummy = dummy;
/*  77 */     this.content = content;
/*  78 */     this.isMixedExtendedCust = isMixedExtended;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<? extends CTypeInfo> ref() {
/*     */     final class RefList
/*     */       extends HashSet<CTypeInfo>
/*     */     {
/*     */       RefList() {
/*  92 */         super(CReferencePropertyInfo.this.elements.size());
/*  93 */         addAll((Collection)CReferencePropertyInfo.this.elements);
/*     */       }
/*     */       
/*     */       public boolean addAll(Collection<? extends CTypeInfo> col) {
/*  97 */         boolean r = false;
/*  98 */         for (CTypeInfo e : col) {
/*  99 */           if (e instanceof CElementInfo)
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 104 */             r |= addAll((Collection)((CElementInfo)e).getSubstitutionMembers());
/*     */           }
/* 106 */           r |= add(e);
/*     */         } 
/* 108 */         return r;
/*     */       }
/*     */     };
/*     */     
/* 112 */     RefList r = new RefList();
/* 113 */     if (this.wildcard != null) {
/* 114 */       if (this.wildcard.allowDom)
/* 115 */         r.add(CWildcardTypeInfo.INSTANCE); 
/* 116 */       if (this.wildcard.allowTypedObject)
/*     */       {
/*     */         
/* 119 */         r.add(CBuiltinLeafInfo.ANYTYPE); } 
/*     */     } 
/* 121 */     if (isMixed()) {
/* 122 */       r.add(CBuiltinLeafInfo.STRING);
/*     */     }
/* 124 */     return r;
/*     */   }
/*     */   
/*     */   public Set<CElement> getElements() {
/* 128 */     return this.elements;
/*     */   }
/*     */   
/*     */   public boolean isMixed() {
/* 132 */     return this.isMixed;
/*     */   }
/*     */   
/*     */   public boolean isDummy() {
/* 136 */     return this.dummy;
/*     */   }
/*     */   
/*     */   public boolean isContent() {
/* 140 */     return this.content;
/*     */   }
/*     */   
/*     */   public boolean isMixedExtendedCust() {
/* 144 */     return this.isMixedExtendedCust;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public QName getXmlName() {
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable() {
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 171 */     return visitor.onReference(this);
/*     */   }
/*     */   
/*     */   public CAdapter getAdapter() {
/* 175 */     return null;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 179 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ID id() {
/* 187 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public WildcardMode getWildcard() {
/* 191 */     return this.wildcard;
/*     */   }
/*     */   
/*     */   public void setWildcard(WildcardMode mode) {
/* 195 */     this.wildcard = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public NClass getDOMHandler() {
/* 200 */     if (getWildcard() != null) {
/* 201 */       return NavigatorImpl.create(W3CDomHandler.class);
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionNillable() {
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionRequired() {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getSchemaType() {
/* 222 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 226 */     return this.required;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 231 */     for (CElement e : this.elements) {
/* 232 */       QName n = e.getElementName();
/* 233 */       if (table.containsKey(n))
/* 234 */         return n; 
/* 235 */       table.put(n, this);
/*     */     } 
/* 237 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CReferencePropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */