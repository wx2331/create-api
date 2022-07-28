/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*     */ 
/*     */ public abstract class CPropertyInfo
/*     */   implements PropertyInfo<NType, NClass>, CCustomizable
/*     */ {
/*     */   @XmlTransient
/*     */   private CClassInfo parent;
/*     */   private String privateName;
/*     */   private String publicName;
/*     */   private final boolean isCollection;
/*     */   @XmlTransient
/*     */   public final Locator locator;
/*     */   private final XSComponent source;
/*     */   public JType baseType;
/*  82 */   public String javadoc = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inlineBinaryData;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/*     */   public FieldRenderer realization;
/*     */ 
/*     */ 
/*     */   
/*     */   public CDefaultValue defaultValue;
/*     */ 
/*     */ 
/*     */   
/*     */   private final CCustomizations customizations;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CPropertyInfo(String name, boolean collection, XSComponent source, CCustomizations customizations, Locator locator) {
/* 107 */     this.publicName = name;
/* 108 */     String n = null;
/*     */     
/* 110 */     Model m = (Model)Ring.get(Model.class);
/* 111 */     if (m != null) {
/* 112 */       n = m.getNameConverter().toVariableName(name);
/*     */     } else {
/* 114 */       n = NameConverter.standard.toVariableName(name);
/*     */     } 
/*     */     
/* 117 */     if (!JJavaName.isJavaIdentifier(n))
/* 118 */       n = '_' + n; 
/* 119 */     this.privateName = n;
/*     */     
/* 121 */     this.isCollection = collection;
/* 122 */     this.locator = locator;
/* 123 */     if (customizations == null) {
/* 124 */       this.customizations = CCustomizations.EMPTY;
/*     */     } else {
/* 126 */       this.customizations = customizations;
/* 127 */     }  this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   final void setParent(CClassInfo parent) {
/* 132 */     assert this.parent == null;
/* 133 */     assert parent != null;
/* 134 */     this.parent = parent;
/* 135 */     this.customizations.setParent(parent.model, this);
/*     */   }
/*     */   
/*     */   public CTypeInfo parent() {
/* 139 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 143 */     return this.locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XSComponent getSchemaComponent() {
/* 155 */     return this.source;
/*     */   }
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
/*     */   public String getName() {
/* 185 */     return getName(false);
/*     */   }
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
/*     */   public String getName(boolean isPublic) {
/* 204 */     return isPublic ? this.publicName : this.privateName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(boolean isPublic, String newName) {
/* 215 */     if (isPublic) {
/* 216 */       this.publicName = newName;
/*     */     } else {
/* 218 */       this.privateName = newName;
/*     */     } 
/*     */   }
/*     */   public String displayName() {
/* 222 */     return this.parent.toString() + '#' + getName(false);
/*     */   }
/*     */   
/*     */   public boolean isCollection() {
/* 226 */     return this.isCollection;
/*     */   }
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
/*     */   public boolean isUnboxable() {
/* 261 */     Collection<? extends CTypeInfo> ts = ref();
/* 262 */     if (ts.size() != 1)
/*     */     {
/*     */       
/* 265 */       return false;
/*     */     }
/* 267 */     if (this.baseType != null && this.baseType instanceof com.sun.codemodel.internal.JClass) {
/* 268 */       return false;
/*     */     }
/* 270 */     CTypeInfo t = ts.iterator().next();
/*     */     
/* 272 */     return t.getType().isBoxedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/* 280 */     return false;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/* 284 */     return this.customizations;
/*     */   }
/*     */   
/*     */   public boolean inlineBinaryData() {
/* 288 */     return this.inlineBinaryData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean needsExplicitTypeName(TypeUse type, QName typeName) {
/* 298 */     if (typeName == null)
/*     */     {
/* 300 */       return false;
/*     */     }
/* 302 */     if (!"http://www.w3.org/2001/XMLSchema".equals(typeName.getNamespaceURI()))
/*     */     {
/*     */       
/* 305 */       return false;
/*     */     }
/* 307 */     if (type.isCollection())
/*     */     {
/*     */       
/* 310 */       return true;
/*     */     }
/* 312 */     QName itemType = type.getInfo().getTypeName();
/* 313 */     if (itemType == null)
/*     */     {
/*     */ 
/*     */       
/* 317 */       return true;
/*     */     }
/*     */     
/* 320 */     return !itemType.equals(typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName collectElementNames(Map<QName, CPropertyInfo> table) {
/* 332 */     return null;
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 336 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 340 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public abstract CAdapter getAdapter();
/*     */   
/*     */   public abstract Collection<? extends CTypeInfo> ref();
/*     */   
/*     */   public abstract <V> V accept(CPropertyVisitor<V> paramCPropertyVisitor);
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CPropertyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */