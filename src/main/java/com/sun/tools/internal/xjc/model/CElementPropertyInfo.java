/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CElementPropertyInfo
/*     */   extends CPropertyInfo
/*     */   implements ElementPropertyInfo<NType, NClass>
/*     */ {
/*     */   private final boolean required;
/*     */   private final MimeType expectedMimeType;
/*     */   private CAdapter adapter;
/*     */   private final boolean isValueList;
/*     */   private ID id;
/*  74 */   private final List<CTypeRef> types = new ArrayList<>();
/*     */   
/*  76 */   private final List<CNonElement> ref = new AbstractList<CNonElement>() {
/*     */       public CNonElement get(int index) {
/*  78 */         return ((CTypeRef)CElementPropertyInfo.this.getTypes().get(index)).getTarget();
/*     */       }
/*     */       public int size() {
/*  81 */         return CElementPropertyInfo.this.getTypes().size();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public CElementPropertyInfo(String name, CollectionMode collection, ID id, MimeType expectedMimeType, XSComponent source, CCustomizations customizations, Locator locator, boolean required) {
/*  88 */     super(name, collection.col, source, customizations, locator);
/*  89 */     this.required = required;
/*  90 */     this.id = id;
/*  91 */     this.expectedMimeType = expectedMimeType;
/*  92 */     this.isValueList = collection.val;
/*     */   }
/*     */   
/*     */   public ID id() {
/*  96 */     return this.id;
/*     */   }
/*     */   
/*     */   public List<CTypeRef> getTypes() {
/* 100 */     return this.types;
/*     */   }
/*     */   
/*     */   public List<CNonElement> ref() {
/* 104 */     return this.ref;
/*     */   }
/*     */   
/*     */   public QName getSchemaType() {
/* 108 */     if (this.types.size() != 1)
/*     */     {
/*     */       
/* 111 */       return null;
/*     */     }
/* 113 */     CTypeRef t = this.types.get(0);
/* 114 */     if (needsExplicitTypeName(t.getTarget(), t.typeName)) {
/* 115 */       return t.typeName;
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public QName getXmlName() {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionRequired() {
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollectionNillable() {
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 139 */     return this.required;
/*     */   }
/*     */   
/*     */   public boolean isValueList() {
/* 143 */     return this.isValueList;
/*     */   }
/*     */   
/*     */   public boolean isUnboxable() {
/* 147 */     if (!isCollection() && !this.required)
/*     */     {
/* 149 */       return false;
/*     */     }
/* 151 */     for (CTypeRef t : getTypes()) {
/* 152 */       if (t.isNillable())
/* 153 */         return false; 
/*     */     } 
/* 155 */     return super.isUnboxable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 161 */     for (CTypeRef t : getTypes()) {
/* 162 */       if (t.isNillable())
/* 163 */         return false; 
/*     */     } 
/* 165 */     return (!isCollection() && !this.required && super.isUnboxable());
/*     */   }
/*     */   
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/* 169 */     return visitor.onElement(this);
/*     */   }
/*     */   
/*     */   public CAdapter getAdapter() {
/* 173 */     return this.adapter;
/*     */   }
/*     */   
/*     */   public void setAdapter(CAdapter a) {
/* 177 */     assert this.adapter == null;
/* 178 */     this.adapter = a;
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 182 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 186 */     return this.expectedMimeType;
/*     */   }
/*     */   
/*     */   public enum CollectionMode {
/* 190 */     NOT_REPEATED(false, false),
/* 191 */     REPEATED_ELEMENT(true, false),
/* 192 */     REPEATED_VALUE(true, true);
/*     */     private final boolean col;
/*     */     private final boolean val;
/*     */     
/*     */     CollectionMode(boolean col, boolean val) {
/* 197 */       this.col = col;
/* 198 */       this.val = val;
/*     */     }
/*     */     public boolean isRepeated() {
/* 201 */       return this.col;
/*     */     }
/*     */   }
/*     */   
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 206 */     for (CTypeRef t : this.types) {
/* 207 */       QName n = t.getTagName();
/* 208 */       if (table.containsKey(n)) return n; 
/* 209 */       table.put(n, this);
/*     */     } 
/* 211 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CElementPropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */