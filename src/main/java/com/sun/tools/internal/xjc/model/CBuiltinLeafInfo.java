/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;
/*     */ import com.sun.tools.internal.xjc.util.NamespaceContextAdapter;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.LeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.awt.Image;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
/*     */ import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CBuiltinLeafInfo
/*     */   implements CNonElement, BuiltinLeafInfo<NType, NClass>, LeafInfo<NType, NClass>, Location
/*     */ {
/*     */   private final NType type;
/*     */   private final QName typeName;
/*     */   private final QName[] typeNames;
/*     */   private final ID id;
/*     */   
/*     */   private CBuiltinLeafInfo(NType typeToken, ID id, QName... typeNames) {
/* 112 */     this.type = typeToken;
/* 113 */     this.typeName = (typeNames.length > 0) ? typeNames[0] : null;
/* 114 */     this.typeNames = typeNames;
/* 115 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType toType(Outline o, Aspect aspect) {
/* 122 */     return getType().toType(o, aspect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isCollection() {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/* 143 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final CAdapter getAdapterUse() {
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 159 */     return Model.EMPTY_LOCATOR;
/*     */   }
/*     */   
/*     */   public final XSComponent getSchemaComponent() {
/* 163 */     throw new UnsupportedOperationException("TODO. If you hit this, let us know.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeCollection() {
/* 170 */     return TypeUseFactory.makeCollection(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeAdapted(Class<? extends XmlAdapter> adapter, boolean copy) {
/* 177 */     return TypeUseFactory.adapt(this, adapter, copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeUse makeMimeTyped(MimeType mt) {
/* 184 */     return TypeUseFactory.makeMimeTyped(this, mt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isElement() {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final QName getElementName() {
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Element<NType, NClass> asElement() {
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NType getType() {
/* 212 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final QName[] getTypeNames() {
/* 222 */     return this.typeNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/* 232 */     return false;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 236 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/* 247 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Builtin
/*     */     extends CBuiltinLeafInfo
/*     */   {
/*     */     protected Builtin(Class c, String typeName) {
/* 260 */       this(c, typeName, ID.NONE);
/*     */     }
/*     */     protected Builtin(Class c, String typeName, ID id) {
/* 263 */       super((NType)NavigatorImpl.theInstance.ref(c), id, new QName[] { new QName("http://www.w3.org/2001/XMLSchema", typeName) });
/* 264 */       LEAVES.put(getType(), this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CCustomizations getCustomizations() {
/* 271 */       return CCustomizations.EMPTY;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NoConstantBuiltin extends Builtin {
/*     */     public NoConstantBuiltin(Class c, String typeName) {
/* 277 */       super(c, typeName);
/*     */     }
/*     */     public JExpression createConstant(Outline outline, XmlString lexical) {
/* 280 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 287 */   public static final Map<NType, CBuiltinLeafInfo> LEAVES = new HashMap<>();
/*     */ 
/*     */   
/* 290 */   public static final CBuiltinLeafInfo ANYTYPE = new NoConstantBuiltin(Object.class, "anyType");
/* 291 */   public static final CBuiltinLeafInfo STRING = new Builtin(String.class, "string") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 293 */         return JExpr.lit(lexical.value);
/*     */       }
/*     */     };
/* 296 */   public static final CBuiltinLeafInfo BOOLEAN = new Builtin(Boolean.class, "boolean") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 298 */         return JExpr.lit(DatatypeConverter.parseBoolean(lexical.value));
/*     */       }
/*     */     };
/* 301 */   public static final CBuiltinLeafInfo INT = new Builtin(Integer.class, "int") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 303 */         return JExpr.lit(DatatypeConverter.parseInt(lexical.value));
/*     */       }
/*     */     };
/* 306 */   public static final CBuiltinLeafInfo LONG = new Builtin(Long.class, "long") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 308 */         return JExpr.lit(DatatypeConverter.parseLong(lexical.value));
/*     */       }
/*     */     };
/* 311 */   public static final CBuiltinLeafInfo BYTE = new Builtin(Byte.class, "byte") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 313 */         return (JExpression)JExpr.cast(
/* 314 */             (JType)(outline.getCodeModel()).BYTE, 
/* 315 */             JExpr.lit(DatatypeConverter.parseByte(lexical.value)));
/*     */       }
/*     */     };
/* 318 */   public static final CBuiltinLeafInfo SHORT = new Builtin(Short.class, "short") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 320 */         return (JExpression)JExpr.cast(
/* 321 */             (JType)(outline.getCodeModel()).SHORT, 
/* 322 */             JExpr.lit(DatatypeConverter.parseShort(lexical.value)));
/*     */       }
/*     */     };
/* 325 */   public static final CBuiltinLeafInfo FLOAT = new Builtin(Float.class, "float") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 327 */         return JExpr.lit(DatatypeConverter.parseFloat(lexical.value));
/*     */       }
/*     */     };
/* 330 */   public static final CBuiltinLeafInfo DOUBLE = new Builtin(Double.class, "double") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 332 */         return JExpr.lit(DatatypeConverter.parseDouble(lexical.value));
/*     */       }
/*     */     };
/* 335 */   public static final CBuiltinLeafInfo QNAME = new Builtin(QName.class, "QName") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 337 */         QName qn = DatatypeConverter.parseQName(lexical.value, (NamespaceContext)new NamespaceContextAdapter(lexical));
/* 338 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(QName.class))
/* 339 */           .arg(qn.getNamespaceURI())
/* 340 */           .arg(qn.getLocalPart())
/* 341 */           .arg(qn.getPrefix());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 347 */   public static final CBuiltinLeafInfo CALENDAR = new NoConstantBuiltin(XMLGregorianCalendar.class, "\000");
/* 348 */   public static final CBuiltinLeafInfo DURATION = new NoConstantBuiltin(Duration.class, "duration");
/*     */   
/* 350 */   public static final CBuiltinLeafInfo BIG_INTEGER = new Builtin(BigInteger.class, "integer") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 352 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(BigInteger.class)).arg(lexical.value.trim());
/*     */       }
/*     */     };
/*     */   
/* 356 */   public static final CBuiltinLeafInfo BIG_DECIMAL = new Builtin(BigDecimal.class, "decimal") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 358 */         return (JExpression)JExpr._new(outline.getCodeModel().ref(BigDecimal.class)).arg(lexical.value.trim());
/*     */       }
/*     */     };
/*     */   
/* 362 */   public static final CBuiltinLeafInfo BASE64_BYTE_ARRAY = new Builtin(byte[].class, "base64Binary") {
/*     */       public JExpression createConstant(Outline outline, XmlString lexical) {
/* 364 */         return (JExpression)outline.getCodeModel().ref(DatatypeConverter.class).staticInvoke("parseBase64Binary").arg(lexical.value);
/*     */       }
/*     */     };
/*     */   
/* 368 */   public static final CBuiltinLeafInfo DATA_HANDLER = new NoConstantBuiltin(DataHandler.class, "base64Binary");
/* 369 */   public static final CBuiltinLeafInfo IMAGE = new NoConstantBuiltin(Image.class, "base64Binary");
/* 370 */   public static final CBuiltinLeafInfo XML_SOURCE = new NoConstantBuiltin(Source.class, "base64Binary");
/*     */   
/* 372 */   public static final TypeUse HEXBIN_BYTE_ARRAY = STRING
/* 373 */     .makeAdapted((Class)HexBinaryAdapter.class, false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 378 */   public static final TypeUse TOKEN = STRING
/* 379 */     .makeAdapted((Class)CollapsedStringAdapter.class, false);
/*     */   
/* 381 */   public static final TypeUse NORMALIZED_STRING = STRING
/* 382 */     .makeAdapted((Class)NormalizedStringAdapter.class, false);
/*     */   
/* 384 */   public static final TypeUse ID = TypeUseFactory.makeID(TOKEN, ID.ID);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 389 */   public static final TypeUse BOOLEAN_ZERO_OR_ONE = STRING
/* 390 */     .makeAdapted((Class)ZeroOneBooleanAdapter.class, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 398 */   public static final TypeUse IDREF = TypeUseFactory.makeID(ANYTYPE, ID.IDREF);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 403 */   public static final TypeUse STRING_LIST = STRING
/* 404 */     .makeCollection();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CBuiltinLeafInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */