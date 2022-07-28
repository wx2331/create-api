/*     */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JConditional;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.model.CAdapter;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.ClassSelector;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import java.util.Collection;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*     */ public abstract class BIConversion
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @Deprecated
/*     */   public BIConversion(Locator loc) {
/*  73 */     super(loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BIConversion() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/*  92 */     return NAME;
/*     */   }
/*     */   
/*  95 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "conversion");
/*     */ 
/*     */   
/*     */   public abstract TypeUse getTypeUse(XSSimpleType paramXSSimpleType);
/*     */ 
/*     */   
/*     */   public static final class Static
/*     */     extends BIConversion
/*     */   {
/*     */     private final TypeUse transducer;
/*     */ 
/*     */     
/*     */     public Static(Locator loc, TypeUse transducer) {
/* 108 */       super(loc);
/* 109 */       this.transducer = transducer;
/*     */     }
/*     */     
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 113 */       return this.transducer;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlRootElement(name = "javaType")
/*     */   public static class User
/*     */     extends BIConversion
/*     */   {
/*     */     @XmlAttribute
/*     */     private String parseMethod;
/*     */     
/*     */     @XmlAttribute
/*     */     private String printMethod;
/*     */     
/*     */     @XmlAttribute(name = "name")
/* 130 */     private String type = "java.lang.String";
/*     */ 
/*     */     
/*     */     private JType inMemoryType;
/*     */ 
/*     */     
/*     */     private TypeUse typeUse;
/*     */ 
/*     */     
/*     */     public User(Locator loc, String parseMethod, String printMethod, JType inMemoryType) {
/* 140 */       super(loc);
/* 141 */       this.parseMethod = parseMethod;
/* 142 */       this.printMethod = printMethod;
/* 143 */       this.inMemoryType = inMemoryType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/* 155 */       if (this.typeUse != null) {
/* 156 */         return this.typeUse;
/*     */       }
/* 158 */       JCodeModel cm = getCodeModel();
/*     */       
/* 160 */       if (this.inMemoryType == null) {
/* 161 */         this.inMemoryType = TypeUtil.getType(cm, this.type, (ErrorReceiver)Ring.get(ErrorReceiver.class), getLocation());
/*     */       }
/* 163 */       JDefinedClass adapter = generateAdapter(parseMethodFor(owner), printMethodFor(owner), owner);
/*     */ 
/*     */       
/* 166 */       this.typeUse = TypeUseFactory.adapt((TypeUse)CBuiltinLeafInfo.STRING, new CAdapter((JClass)adapter));
/*     */       
/* 168 */       return this.typeUse;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private JDefinedClass generateAdapter(String parseMethod, String printMethod, XSSimpleType owner) {
/*     */       JExpression inv;
/* 175 */       JDefinedClass adapter = null;
/*     */       
/* 177 */       int id = 1;
/* 178 */       while (adapter == null) {
/*     */         try {
/* 180 */           JPackage pkg = ((ClassSelector)Ring.get(ClassSelector.class)).getClassScope().getOwnerPackage();
/* 181 */           adapter = pkg._class("Adapter" + id);
/* 182 */         } catch (JClassAlreadyExistsException e) {
/*     */ 
/*     */ 
/*     */           
/* 186 */           id++;
/*     */         } 
/*     */       } 
/*     */       
/* 190 */       JClass bim = this.inMemoryType.boxify();
/*     */       
/* 192 */       adapter._extends(getCodeModel().ref(XmlAdapter.class).narrow(String.class).narrow(bim));
/*     */       
/* 194 */       JMethod unmarshal = adapter.method(1, (JType)bim, "unmarshal");
/* 195 */       JVar $value = unmarshal.param(String.class, "value");
/*     */ 
/*     */ 
/*     */       
/* 199 */       if (parseMethod.equals("new")) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 204 */         JInvocation jInvocation = JExpr._new(bim).arg((JExpression)$value);
/*     */       } else {
/* 206 */         int i = parseMethod.lastIndexOf('.');
/* 207 */         if (i < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 213 */           JInvocation jInvocation = bim.staticInvoke(parseMethod).arg((JExpression)$value);
/*     */         } else {
/* 215 */           inv = JExpr.direct(parseMethod + "(value)");
/*     */         } 
/*     */       } 
/* 218 */       unmarshal.body()._return(inv);
/*     */ 
/*     */       
/* 221 */       JMethod marshal = adapter.method(1, String.class, "marshal");
/* 222 */       $value = marshal.param((JType)bim, "value");
/*     */       
/* 224 */       if (printMethod.startsWith("javax.xml.bind.DatatypeConverter."))
/*     */       {
/*     */         
/* 227 */         marshal.body()._if($value.eq(JExpr._null()))._then()._return(JExpr._null());
/*     */       }
/*     */       
/* 230 */       int idx = printMethod.lastIndexOf('.');
/* 231 */       if (idx < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 236 */         JInvocation jInvocation = $value.invoke(printMethod);
/*     */ 
/*     */         
/* 239 */         JConditional jcon = marshal.body()._if($value.eq(JExpr._null()));
/* 240 */         jcon._then()._return(JExpr._null());
/*     */       
/*     */       }
/* 243 */       else if (this.printMethod == null) {
/*     */         
/* 245 */         JType t = this.inMemoryType.unboxify();
/* 246 */         inv = JExpr.direct(printMethod + "((" + findBaseConversion(owner).toLowerCase() + ")(" + t.fullName() + ")value)");
/*     */       } else {
/* 248 */         inv = JExpr.direct(printMethod + "(value)");
/*     */       } 
/* 250 */       marshal.body()._return(inv);
/*     */       
/* 252 */       return adapter;
/*     */     }
/*     */     
/*     */     private String printMethodFor(XSSimpleType owner) {
/* 256 */       if (this.printMethod != null) return this.printMethod;
/*     */       
/* 258 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 259 */         String method = getConversionMethod("print", owner);
/* 260 */         if (method != null) {
/* 261 */           return method;
/*     */         }
/*     */       } 
/* 264 */       return "toString";
/*     */     }
/*     */     
/*     */     private String parseMethodFor(XSSimpleType owner) {
/* 268 */       if (this.parseMethod != null) return this.parseMethod;
/*     */       
/* 270 */       if (this.inMemoryType.unboxify().isPrimitive()) {
/* 271 */         String method = getConversionMethod("parse", owner);
/* 272 */         if (method != null)
/*     */         {
/* 274 */           return '(' + this.inMemoryType.unboxify().fullName() + ')' + method;
/*     */         }
/*     */       } 
/*     */       
/* 278 */       return "new";
/*     */     }
/*     */     
/* 281 */     private static final String[] knownBases = new String[] { "Float", "Double", "Byte", "Short", "Int", "Long", "Boolean" };
/*     */ 
/*     */ 
/*     */     
/*     */     private String getConversionMethod(String methodPrefix, XSSimpleType owner) {
/* 286 */       String bc = findBaseConversion(owner);
/* 287 */       if (bc == null) return null;
/*     */       
/* 289 */       return DatatypeConverter.class.getName() + '.' + methodPrefix + bc;
/*     */     }
/*     */ 
/*     */     
/*     */     private String findBaseConversion(XSSimpleType owner) {
/* 294 */       for (XSSimpleType st = owner; st != null; st = st.getSimpleBaseType()) {
/* 295 */         if ("http://www.w3.org/2001/XMLSchema".equals(st.getTargetNamespace())) {
/*     */ 
/*     */           
/* 298 */           String name = st.getName().intern();
/* 299 */           for (String s : knownBases) {
/* 300 */             if (name.equalsIgnoreCase(s))
/* 301 */               return s; 
/*     */           } 
/*     */         } 
/* 304 */       }  return null;
/*     */     }
/*     */     public QName getName() {
/* 307 */       return NAME;
/*     */     }
/*     */     
/* 310 */     public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "javaType");
/*     */     public User() {} }
/*     */   @XmlRootElement(name = "javaType", namespace = "http://java.sun.com/xml/ns/jaxb/xjc")
/*     */   public static class UserAdapter extends BIConversion { @XmlAttribute(name = "name")
/*     */     private String type;
/*     */     public UserAdapter() {
/* 316 */       this.type = null;
/*     */ 
/*     */       
/* 319 */       this.adapter = null;
/*     */     }
/*     */     @XmlAttribute
/*     */     private String adapter; private TypeUse typeUse;
/*     */     public TypeUse getTypeUse(XSSimpleType owner) {
/*     */       JDefinedClass a;
/* 325 */       if (this.typeUse != null) {
/* 326 */         return this.typeUse;
/*     */       }
/* 328 */       JCodeModel cm = getCodeModel();
/*     */ 
/*     */       
/*     */       try {
/* 332 */         a = cm._class(this.adapter);
/* 333 */         a.hide();
/* 334 */         a._extends(cm.ref(XmlAdapter.class).narrow(String.class).narrow(cm
/* 335 */               .ref(this.type)));
/* 336 */       } catch (JClassAlreadyExistsException e) {
/* 337 */         a = e.getExistingClass();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 342 */       this.typeUse = TypeUseFactory.adapt((TypeUse)CBuiltinLeafInfo.STRING, new CAdapter((JClass)a));
/*     */ 
/*     */ 
/*     */       
/* 346 */       return this.typeUse;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIConversion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */