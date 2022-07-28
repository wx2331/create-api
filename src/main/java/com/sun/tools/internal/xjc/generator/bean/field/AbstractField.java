/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAnyElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAttributeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementRefsWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementsWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlSchemaTypeWriter;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlValue;
/*     */ import javax.xml.namespace.QName;
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
/*     */ abstract class AbstractField
/*     */   implements FieldOutline
/*     */ {
/*     */   protected final ClassOutlineImpl outline;
/*     */   protected final CPropertyInfo prop;
/*     */   protected final JCodeModel codeModel;
/*     */   protected final JType implType;
/*     */   protected final JType exposedType;
/*     */   private XmlElementsWriter xesw;
/*     */   
/*     */   protected AbstractField(ClassOutlineImpl outline, CPropertyInfo prop) {
/* 329 */     this.xesw = null; this.outline = outline; this.prop = prop; this.codeModel = outline.parent().getCodeModel(); this.implType = getType(Aspect.IMPLEMENTATION); this.exposedType = getType(Aspect.EXPOSED);
/*     */   } public final ClassOutline parent() { return (ClassOutline)this.outline; } public final CPropertyInfo getPropertyInfo() { return this.prop; }
/*     */   protected void annotate(JAnnotatable field) { assert field != null; if (this.prop instanceof CAttributePropertyInfo) { annotateAttribute(field); } else if (this.prop instanceof CElementPropertyInfo) { annotateElement(field); } else if (this.prop instanceof com.sun.tools.internal.xjc.model.CValuePropertyInfo) { field.annotate(XmlValue.class); } else if (this.prop instanceof CReferencePropertyInfo) { annotateReference(field); }  this.outline.parent().generateAdapterIfNecessary(this.prop, field); QName st = this.prop.getSchemaType(); if (st != null) ((XmlSchemaTypeWriter)field.annotate2(XmlSchemaTypeWriter.class)).name(st.getLocalPart()).namespace(st.getNamespaceURI());  if (this.prop.inlineBinaryData())
/*     */       field.annotate(XmlInlineBinaryData.class);  }
/* 333 */   private XmlElementWriter getXew(boolean checkWrapper, JAnnotatable field) { XmlElementWriter xew; if (checkWrapper) {
/* 334 */       if (this.xesw == null) {
/* 335 */         this.xesw = (XmlElementsWriter)field.annotate2(XmlElementsWriter.class);
/*     */       }
/* 337 */       xew = this.xesw.value();
/*     */     } else {
/* 339 */       xew = (XmlElementWriter)field.annotate2(XmlElementWriter.class);
/*     */     } 
/* 341 */     return xew; } private void annotateReference(JAnnotatable field) { CReferencePropertyInfo rp = (CReferencePropertyInfo)this.prop; TODO.prototype(); Collection<CElement> elements = rp.getElements(); if (elements.size() == 1) { XmlElementRefWriter refw = (XmlElementRefWriter)field.annotate2(XmlElementRefWriter.class); CElement e = elements.iterator().next(); refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI()).type(((NType)e.getType()).toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION)); if ((getOptions()).target.isLaterThan(SpecVersion.V2_2)) refw.required(rp.isRequired());  } else if (elements.size() > 1) { XmlElementRefsWriter refsw = (XmlElementRefsWriter)field.annotate2(XmlElementRefsWriter.class); for (CElement e : elements) { XmlElementRefWriter refw = refsw.value(); refw.name(e.getElementName().getLocalPart()).namespace(e.getElementName().getNamespaceURI()).type(((NType)e.getType()).toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION)); if ((getOptions()).target.isLaterThan(SpecVersion.V2_2))
/*     */           refw.required(rp.isRequired());  }  }  if (rp.isMixed())
/*     */       field.annotate(XmlMixed.class);  NClass dh = rp.getDOMHandler(); if (dh != null) { XmlAnyElementWriter xaew = (XmlAnyElementWriter)field.annotate2(XmlAnyElementWriter.class); xaew.lax((rp.getWildcard()).allowTypedObject); JClass value = dh.toType((Outline)this.outline.parent(), Aspect.IMPLEMENTATION); if (!value.equals(this.codeModel.ref(W3CDomHandler.class)))
/*     */         xaew.value((JType)value);  }  }
/*     */   private void annotateElement(JAnnotatable field) { CElementPropertyInfo ep = (CElementPropertyInfo)this.prop; List<CTypeRef> types = ep.getTypes(); if (ep.isValueList())
/*     */       field.annotate(XmlList.class);  assert ep.getXmlName() == null; if (types.size() == 1) { CTypeRef t = types.get(0); writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), false); } else { for (CTypeRef t : types)
/*     */         writeXmlElementAnnotation(field, t, resolve(t, Aspect.IMPLEMENTATION), true);  this.xesw = null; }  }
/* 348 */   private void annotateAttribute(JAnnotatable field) { CAttributePropertyInfo ap = (CAttributePropertyInfo)this.prop;
/* 349 */     QName attName = ap.getXmlName();
/*     */ 
/*     */ 
/*     */     
/* 353 */     XmlAttributeWriter xaw = (XmlAttributeWriter)field.annotate2(XmlAttributeWriter.class);
/*     */     
/* 355 */     String generatedName = attName.getLocalPart();
/* 356 */     String generatedNS = attName.getNamespaceURI();
/*     */ 
/*     */ 
/*     */     
/* 360 */     if (!generatedName.equals(ap.getName(false)) || !generatedName.equals(ap.getName(true)) || this.outline.parent().getModel().getNameConverter() != NameConverter.standard) {
/* 361 */       xaw.name(generatedName);
/*     */     }
/*     */ 
/*     */     
/* 365 */     if (!generatedNS.equals("")) {
/* 366 */       xaw.namespace(generatedNS);
/*     */     }
/*     */ 
/*     */     
/* 370 */     if (ap.isRequired())
/* 371 */       xaw.required(true);  } private void writeXmlElementAnnotation(JAnnotatable field, CTypeRef ctype, JType jtype, boolean checkWrapper) { String enclosingTypeNS; XmlElementWriter xew = null; XmlNsForm formDefault = parent()._package().getElementFormDefault(); String propName = this.prop.getName(false); if ((parent()).target.getTypeName() == null) { enclosingTypeNS = parent()._package().getMostUsedNamespaceURI(); } else { enclosingTypeNS = (parent()).target.getTypeName().getNamespaceURI(); }  String generatedName = ctype.getTagName().getLocalPart(); if (!generatedName.equals(propName)) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.name(generatedName); }
/*     */      String generatedNS = ctype.getTagName().getNamespaceURI(); if ((formDefault == XmlNsForm.QUALIFIED && !generatedNS.equals(enclosingTypeNS)) || (formDefault == XmlNsForm.UNQUALIFIED && !generatedNS.equals(""))) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.namespace(generatedNS); }
/*     */      CElementPropertyInfo ep = (CElementPropertyInfo)this.prop; if (ep.isRequired() && this.exposedType.isReference()) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.required(true); }
/*     */      if (ep.isRequired() && !this.prop.isCollection())
/*     */       jtype = jtype.unboxify();  if (!jtype.equals(this.exposedType) || ((getOptions()).runtime14 && this.prop.isCollection())) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.type(jtype); }
/*     */      String defaultValue = ctype.getDefaultValue(); if (defaultValue != null) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.defaultValue(defaultValue); }
/*     */      if (ctype.isNillable()) { if (xew == null)
/*     */         xew = getXew(checkWrapper, field);  xew.nillable(true); }
/*     */      } protected final Options getOptions() { return (parent().parent().getModel()).options; } protected abstract class Accessor implements FieldAccessor
/*     */   {
/* 386 */     protected Accessor(JExpression $target) { this.$target = $target; }
/*     */     
/*     */     protected final JExpression $target;
/*     */     public final FieldOutline owner() {
/* 390 */       return AbstractField.this;
/*     */     }
/*     */     
/*     */     public final CPropertyInfo getPropertyInfo() {
/* 394 */       return AbstractField.this.prop;
/*     */     }
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
/*     */   protected final JFieldVar generateField(JType type) {
/* 409 */     return this.outline.implClass.field(2, type, this.prop.getName(false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JExpression castToImplType(JExpression exp) {
/* 416 */     if (this.implType == this.exposedType) {
/* 417 */       return exp;
/*     */     }
/* 419 */     return (JExpression)JExpr.cast(this.implType, exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JType getType(final Aspect aspect) {
/*     */     JType t;
/* 427 */     if (this.prop.getAdapter() != null)
/* 428 */       return ((NType)(this.prop.getAdapter()).customType).toType((Outline)this.outline.parent(), aspect); 
/*     */     final class TypeList
/*     */       extends ArrayList<JType> {
/*     */       void add(CTypeInfo t) {
/* 432 */         add(((NType)t.getType()).toType((Outline)AbstractField.this.outline.parent(), aspect));
/* 433 */         if (t instanceof CElementInfo)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 438 */           add(((CElementInfo)t).getSubstitutionMembers());
/*     */         }
/*     */       }
/*     */       
/*     */       void add(Collection<? extends CTypeInfo> col) {
/* 443 */         for (CTypeInfo typeInfo : col)
/* 444 */           add(typeInfo); 
/*     */       }
/*     */     };
/* 447 */     TypeList r = new TypeList();
/* 448 */     r.add(this.prop.ref());
/*     */ 
/*     */     
/* 451 */     if (this.prop.baseType != null) {
/* 452 */       t = this.prop.baseType;
/*     */     } else {
/* 454 */       t = TypeUtil.getCommonBaseType(this.codeModel, r);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (this.prop.isUnboxable())
/* 462 */       t = t.unboxify(); 
/* 463 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<Object> listPossibleTypes(CPropertyInfo prop) {
/* 470 */     List<Object> r = new ArrayList();
/* 471 */     for (CTypeInfo tt : prop.ref()) {
/* 472 */       JType t = ((NType)tt.getType()).toType((Outline)this.outline.parent(), Aspect.EXPOSED);
/* 473 */       if (t.isPrimitive() || t.isArray()) {
/* 474 */         r.add(t.fullName()); continue;
/*     */       } 
/* 476 */       r.add(t);
/* 477 */       r.add("\n");
/*     */     } 
/*     */ 
/*     */     
/* 481 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JType resolve(CTypeRef typeRef, Aspect a) {
/* 488 */     return this.outline.parent().resolve(typeRef, a);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\AbstractField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */