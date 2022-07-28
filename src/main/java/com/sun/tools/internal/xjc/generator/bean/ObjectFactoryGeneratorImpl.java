/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JCast;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JInvocation;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlElementDeclWriter;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlRegistryWriter;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Constructor;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
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
/*     */ abstract class ObjectFactoryGeneratorImpl
/*     */   extends ObjectFactoryGenerator
/*     */ {
/*     */   private final BeanGenerator outline;
/*     */   private final Model model;
/*     */   private final JCodeModel codeModel;
/*     */   private final JClass classRef;
/*     */   private final JDefinedClass objectFactory;
/*  86 */   private final HashMap<QName, JFieldVar> qnameMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private final Map<String, CElementInfo> elementFactoryNames = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private final Map<String, ClassOutlineImpl> valueFactoryNames = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDefinedClass getObjectFactory() {
/* 108 */     return this.objectFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectFactoryGeneratorImpl(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 115 */     this.outline = outline;
/* 116 */     this.model = model;
/* 117 */     this.codeModel = this.model.codeModel;
/* 118 */     this.classRef = this.codeModel.ref(Class.class);
/*     */ 
/*     */     
/* 121 */     this.objectFactory = this.outline.getClassFactory().createClass((JClassContainer)targetPackage, "ObjectFactory", null);
/*     */     
/* 123 */     this.objectFactory.annotate2(XmlRegistryWriter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     JMethod m1 = this.objectFactory.constructor(1);
/* 130 */     m1.javadoc().append("Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: " + targetPackage
/*     */         
/* 132 */         .name());
/*     */ 
/*     */     
/* 135 */     this.objectFactory.javadoc().append("This object contains factory methods for each \nJava content interface and Java element interface \ngenerated in the " + targetPackage
/*     */ 
/*     */         
/* 138 */         .name() + " package. \n<p>An ObjectFactory allows you to programatically \nconstruct new instances of the Java representation \nfor XML content. The Java representation of XML \ncontent can consist of schema derived interfaces \nand classes representing the binding of schema \ntype definitions, element declarations and model \ngroups.  Factory methods for each of these are \nprovided in this class.");
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
/*     */   protected final void populate(CElementInfo ei, Aspect impl, Aspect exposed) {
/*     */     JDefinedClass jDefinedClass;
/*     */     JExpression declaredType;
/* 154 */     JType exposedElementType = ei.toType(this.outline, exposed);
/* 155 */     JType exposedType = ei.getContentInMemoryType().toType(this.outline, exposed);
/* 156 */     JType implType = ei.getContentInMemoryType().toType(this.outline, impl);
/* 157 */     String namespaceURI = ei.getElementName().getNamespaceURI();
/* 158 */     String localPart = ei.getElementName().getLocalPart();
/*     */     
/* 160 */     JClass scope = null;
/* 161 */     if (ei.getScope() != null) {
/* 162 */       jDefinedClass = (this.outline.getClazz(ei.getScope())).implClass;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 167 */     if (ei.isAbstract())
/*     */     {
/*     */       
/* 170 */       TODO.checkSpec();
/*     */     }
/*     */ 
/*     */     
/* 174 */     CElementInfo existing = this.elementFactoryNames.put(ei.getSqueezedName(), ei);
/* 175 */     if (existing != null) {
/* 176 */       this.outline.getErrorReceiver().error(existing.getLocator(), Messages.OBJECT_FACTORY_CONFLICT
/* 177 */           .format(new Object[] { ei.getSqueezedName() }));
/* 178 */       this.outline.getErrorReceiver().error(ei.getLocator(), Messages.OBJECT_FACTORY_CONFLICT_RELATED
/* 179 */           .format(new Object[0]));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
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
/*     */ 
/*     */ 
/*     */     
/* 206 */     JMethod m = this.objectFactory.method(1, exposedElementType, "create" + ei.getSqueezedName());
/* 207 */     JVar $value = m.param(exposedType, "value");
/*     */ 
/*     */     
/* 210 */     if (implType.boxify().isParameterized() || !exposedType.equals(implType)) {
/* 211 */       JCast jCast = JExpr.cast((JType)this.classRef, implType.boxify().dotclass());
/*     */     } else {
/* 213 */       declaredType = implType.boxify().dotclass();
/* 214 */     }  JExpression scopeClass = (jDefinedClass == null) ? JExpr._null() : jDefinedClass.dotclass();
/*     */ 
/*     */     
/* 217 */     JInvocation exp = JExpr._new(exposedElementType);
/* 218 */     if (!ei.hasClass()) {
/* 219 */       exp.arg(getQNameInvocation(ei));
/* 220 */       exp.arg(declaredType);
/* 221 */       exp.arg(scopeClass);
/*     */     } 
/* 223 */     if (implType == exposedType) {
/* 224 */       exp.arg((JExpression)$value);
/*     */     } else {
/* 226 */       exp.arg((JExpression)JExpr.cast(implType, (JExpression)$value));
/*     */     } 
/* 228 */     m.body()._return((JExpression)exp);
/*     */     
/* 230 */     m.javadoc()
/* 231 */       .append("Create an instance of ")
/* 232 */       .append(exposedElementType)
/* 233 */       .append("}");
/*     */     
/* 235 */     XmlElementDeclWriter xemw = (XmlElementDeclWriter)m.annotate2(XmlElementDeclWriter.class);
/* 236 */     xemw.namespace(namespaceURI).name(localPart);
/* 237 */     if (jDefinedClass != null) {
/* 238 */       xemw.scope((JType)jDefinedClass);
/*     */     }
/* 240 */     if (ei.getSubstitutionHead() != null) {
/* 241 */       QName n = ei.getSubstitutionHead().getElementName();
/* 242 */       xemw.substitutionHeadNamespace(n.getNamespaceURI());
/* 243 */       xemw.substitutionHeadName(n.getLocalPart());
/*     */     } 
/*     */     
/* 246 */     if (ei.getDefaultValue() != null) {
/* 247 */       xemw.defaultValue(ei.getDefaultValue());
/*     */     }
/* 249 */     if (ei.getProperty().inlineBinaryData()) {
/* 250 */       m.annotate(XmlInlineBinaryData.class);
/*     */     }
/*     */     
/* 253 */     this.outline.generateAdapterIfNecessary((CPropertyInfo)ei.getProperty(), (JAnnotatable)m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JExpression getQNameInvocation(CElementInfo ei) {
/* 262 */     QName name = ei.getElementName();
/* 263 */     if (this.qnameMap.containsKey(name)) {
/* 264 */       return (JExpression)this.qnameMap.get(name);
/*     */     }
/*     */     
/* 267 */     if (this.qnameMap.size() > 1024)
/*     */     {
/* 269 */       return (JExpression)createQName(name);
/*     */     }
/*     */ 
/*     */     
/* 273 */     JFieldVar qnameField = this.objectFactory.field(28, QName.class, '_' + ei
/*     */ 
/*     */         
/* 276 */         .getSqueezedName() + "_QNAME", (JExpression)createQName(name));
/*     */     
/* 278 */     this.qnameMap.put(name, qnameField);
/*     */     
/* 280 */     return (JExpression)qnameField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JInvocation createQName(QName name) {
/* 287 */     return JExpr._new(this.codeModel.ref(QName.class)).arg(name.getNamespaceURI()).arg(name.getLocalPart());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void populate(ClassOutlineImpl cc, JClass sigType) {
/* 298 */     if (!cc.target.isAbstract()) {
/* 299 */       JMethod m = this.objectFactory.method(1, (JType)sigType, "create" + cc.target
/* 300 */           .getSqueezedName());
/* 301 */       m.body()._return((JExpression)JExpr._new(cc.implRef));
/*     */ 
/*     */       
/* 304 */       m.javadoc()
/* 305 */         .append("Create an instance of ")
/* 306 */         .append(cc.ref);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 311 */     Collection<? extends Constructor> consl = cc.target.getConstructors();
/* 312 */     if (consl.size() != 0)
/*     */     {
/*     */       
/* 315 */       cc.implClass.constructor(1);
/*     */     }
/*     */ 
/*     */     
/* 319 */     String name = cc.target.getSqueezedName();
/* 320 */     ClassOutlineImpl existing = this.valueFactoryNames.put(name, cc);
/* 321 */     if (existing != null) {
/* 322 */       this.outline.getErrorReceiver().error(existing.target.getLocator(), Messages.OBJECT_FACTORY_CONFLICT
/* 323 */           .format(new Object[] { name }));
/* 324 */       this.outline.getErrorReceiver().error(cc.target.getLocator(), Messages.OBJECT_FACTORY_CONFLICT_RELATED
/* 325 */           .format(new Object[0]));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 330 */     for (Constructor cons : consl) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 336 */       JMethod m = this.objectFactory.method(1, (JType)cc.ref, "create" + cc.target
/* 337 */           .getSqueezedName());
/* 338 */       JInvocation inv = JExpr._new(cc.implRef);
/* 339 */       m.body()._return((JExpression)inv);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 345 */       m.javadoc()
/* 346 */         .append("Create an instance of ")
/* 347 */         .append(cc.ref)
/* 348 */         .addThrows(JAXBException.class).append("if an error occurs");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 354 */       JMethod c = cc.implClass.constructor(1);
/*     */       
/* 356 */       for (String fieldName : cons.fields) {
/* 357 */         CPropertyInfo field = cc.target.getProperty(fieldName);
/* 358 */         if (field == null) {
/* 359 */           this.outline.getErrorReceiver().error(cc.target.getLocator(), Messages.ILLEGAL_CONSTRUCTOR_PARAM
/* 360 */               .format(new Object[] { fieldName }));
/*     */         }
/*     */         else {
/*     */           
/* 364 */           fieldName = camelize(fieldName);
/*     */           
/* 366 */           FieldOutline fo = this.outline.getField(field);
/* 367 */           FieldAccessor accessor = fo.create(JExpr._this());
/*     */ 
/*     */ 
/*     */           
/* 371 */           inv.arg((JExpression)m.param(fo.getRawType(), fieldName));
/*     */           
/* 373 */           JVar $var = c.param(fo.getRawType(), fieldName);
/* 374 */           accessor.fromRawValue(c.body(), '_' + fieldName, (JExpression)$var);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String camelize(String s) {
/* 382 */     return Character.toLowerCase(s.charAt(0)) + s.substring(1);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\ObjectFactoryGeneratorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */