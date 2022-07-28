/*     */ package com.sun.tools.internal.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JJavaName;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.util.JavadocEscapeWriter;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClass;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CElementInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.LocalScoping;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSDeclaration;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.impl.util.SchemaWriter;
/*     */ import com.sun.xml.internal.xsom.util.ComponentNameFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
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
/*     */ public final class ClassSelector
/*     */   extends BindingComponent
/*     */ {
/*  80 */   private final BGMBuilder builder = (BGMBuilder)Ring.get(BGMBuilder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private final Map<XSComponent, Binding> bindMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   final Map<XSComponent, CElementInfo> boundElements = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   private final Stack<Binding> bindQueue = new Stack<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private final Set<CClassInfo> built = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassBinder classBinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final Stack<CClassInfoParent> classScopes = new Stack<>();
/*     */ 
/*     */   
/*     */   private XSComponent currentRoot;
/*     */ 
/*     */   
/*     */   private CClassInfo currentBean;
/*     */ 
/*     */   
/*     */   private final class Binding
/*     */   {
/*     */     private final XSComponent sc;
/*     */     
/*     */     private final CTypeInfo bean;
/*     */ 
/*     */     
/*     */     public Binding(XSComponent sc, CTypeInfo bean) {
/* 141 */       this.sc = sc;
/* 142 */       this.bean = bean;
/*     */     }
/*     */     
/*     */     void build() {
/* 146 */       if (!(this.bean instanceof CClassInfo)) {
/*     */         return;
/*     */       }
/* 149 */       CClassInfo bean = (CClassInfo)this.bean;
/*     */       
/* 151 */       if (!ClassSelector.this.built.add(bean)) {
/*     */         return;
/*     */       }
/* 154 */       for (String reservedClassName : ClassSelector.reservedClassNames) {
/* 155 */         if (bean.getName().equals(reservedClassName)) {
/* 156 */           ClassSelector.this.getErrorReporter().error(this.sc.getLocator(), "ClassSelector.ReservedClassName", new Object[] { reservedClassName });
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 165 */       if (ClassSelector.this.needValueConstructor(this.sc))
/*     */       {
/*     */         
/* 168 */         bean.addConstructor(new String[] { "value" });
/*     */       }
/*     */       
/* 171 */       if (bean.javadoc == null) {
/* 172 */         ClassSelector.this.addSchemaFragmentJavadoc(bean, this.sc);
/*     */       }
/*     */       
/* 175 */       if (ClassSelector.this.builder.getGlobalBinding().getFlattenClasses() == LocalScoping.NESTED) {
/* 176 */         ClassSelector.this.pushClassScope((CClassInfoParent)bean);
/*     */       } else {
/* 178 */         ClassSelector.this.pushClassScope(bean.parent());
/* 179 */       }  XSComponent oldRoot = ClassSelector.this.currentRoot;
/* 180 */       CClassInfo oldBean = ClassSelector.this.currentBean;
/* 181 */       ClassSelector.this.currentRoot = this.sc;
/* 182 */       ClassSelector.this.currentBean = bean;
/* 183 */       this.sc.visit((XSVisitor)Ring.get(BindRed.class));
/* 184 */       ClassSelector.this.currentBean = oldBean;
/* 185 */       ClassSelector.this.currentRoot = oldRoot;
/* 186 */       ClassSelector.this.popClassScope();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       BIProperty prop = (BIProperty)ClassSelector.this.builder.getBindInfo(this.sc).get(BIProperty.class);
/* 192 */       if (prop != null) prop.markAsAcknowledged();
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassSelector() {
/* 199 */     this.classBinder = new Abstractifier(new DefaultClassBinder());
/* 200 */     Ring.add(ClassBinder.class, this.classBinder);
/*     */     
/* 202 */     this.classScopes.push((CClassInfoParent)null);
/*     */     
/* 204 */     XSComplexType anyType = ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getComplexType("http://www.w3.org/2001/XMLSchema", "anyType");
/* 205 */     this.bindMap.put(anyType, new Binding((XSComponent)anyType, (CTypeInfo)CBuiltinLeafInfo.ANYTYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public final CClassInfoParent getClassScope() {
/* 210 */     assert !this.classScopes.isEmpty();
/* 211 */     return this.classScopes.peek();
/*     */   }
/*     */   
/*     */   public final void pushClassScope(CClassInfoParent clsFctry) {
/* 215 */     assert clsFctry != null;
/* 216 */     this.classScopes.push(clsFctry);
/*     */   }
/*     */   
/*     */   public final void popClassScope() {
/* 220 */     this.classScopes.pop();
/*     */   }
/*     */   
/*     */   public XSComponent getCurrentRoot() {
/* 224 */     return this.currentRoot;
/*     */   }
/*     */   
/*     */   public CClassInfo getCurrentBean() {
/* 228 */     return this.currentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CElement isBound(XSElementDecl x, XSComponent referer) {
/* 235 */     CElementInfo r = this.boundElements.get(x);
/* 236 */     if (r != null)
/* 237 */       return (CElement)r; 
/* 238 */     return bindToType(x, referer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CTypeInfo bindToType(XSComponent sc, XSComponent referer) {
/* 247 */     return _bindToClass(sc, referer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CElement bindToType(XSElementDecl e, XSComponent referer) {
/* 257 */     return (CElement)_bindToClass((XSComponent)e, referer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CClass bindToType(XSComplexType t, XSComponent referer, boolean cannotBeDelayed) {
/* 265 */     return (CClass)_bindToClass((XSComponent)t, referer, cannotBeDelayed);
/*     */   }
/*     */   
/*     */   public TypeUse bindToType(XSType t, XSComponent referer) {
/* 269 */     if (t instanceof XSSimpleType) {
/* 270 */       return ((SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class)).build((XSSimpleType)t);
/*     */     }
/* 272 */     return (TypeUse)_bindToClass((XSComponent)t, referer, false);
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
/*     */   CTypeInfo _bindToClass(@NotNull XSComponent sc, XSComponent referer, boolean cannotBeDelayed) {
/* 291 */     if (!this.bindMap.containsKey(sc)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 296 */       boolean isGlobal = false;
/* 297 */       if (sc instanceof XSDeclaration) {
/* 298 */         isGlobal = ((XSDeclaration)sc).isGlobal();
/* 299 */         if (isGlobal) {
/* 300 */           pushClassScope((CClassInfoParent)new CClassInfoParent.Package(
/* 301 */                 getPackage(((XSDeclaration)sc).getTargetNamespace())));
/*     */         }
/*     */       } 
/*     */       
/* 305 */       CElement bean = (CElement)sc.apply(this.classBinder);
/*     */       
/* 307 */       if (isGlobal) {
/* 308 */         popClassScope();
/*     */       }
/* 310 */       if (bean == null) {
/* 311 */         return null;
/*     */       }
/*     */       
/* 314 */       if (bean instanceof CClassInfo) {
/* 315 */         XSSchema os = sc.getOwnerSchema();
/* 316 */         BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)os).get(BISchemaBinding.class);
/* 317 */         if (sb != null && !sb.map) {
/*     */           
/* 319 */           getErrorReporter().error(sc.getLocator(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS", new Object[] { sc
/* 320 */                 .apply((XSFunction)new ComponentNameFunction()) });
/* 321 */           getErrorReporter().error(sb.getLocation(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS_MAP_FALSE", new Object[] { os
/* 322 */                 .getTargetNamespace() });
/* 323 */           if (referer != null) {
/* 324 */             getErrorReporter().error(referer.getLocator(), "ERR_REFERENCE_TO_NONEXPORTED_CLASS_REFERER", new Object[] { referer
/* 325 */                   .apply((XSFunction)new ComponentNameFunction()) });
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 330 */       queueBuild(sc, bean);
/*     */     } 
/*     */     
/* 333 */     Binding bind = this.bindMap.get(sc);
/* 334 */     if (cannotBeDelayed) {
/* 335 */       bind.build();
/*     */     }
/* 337 */     return bind.bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void executeTasks() {
/* 344 */     while (this.bindQueue.size() != 0) {
/* 345 */       ((Binding)this.bindQueue.pop()).build();
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
/*     */   private boolean needValueConstructor(XSComponent sc) {
/* 360 */     if (!(sc instanceof XSElementDecl)) return false;
/*     */     
/* 362 */     XSElementDecl decl = (XSElementDecl)sc;
/* 363 */     if (!decl.getType().isSimpleType()) return false;
/*     */     
/* 365 */     return true;
/*     */   }
/*     */   
/* 368 */   private static final String[] reservedClassNames = new String[] { "ObjectFactory" };
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueBuild(XSComponent sc, CElement bean) {
/* 373 */     Binding b = new Binding(sc, (CTypeInfo)bean);
/* 374 */     this.bindQueue.push(b);
/* 375 */     Binding old = this.bindMap.put(sc, b);
/* 376 */     assert old == null || old.bean == bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSchemaFragmentJavadoc(CClassInfo bean, XSComponent sc) {
/* 386 */     String doc = this.builder.getBindInfo(sc).getDocumentation();
/* 387 */     if (doc != null) {
/* 388 */       append(bean, doc);
/*     */     }
/*     */     
/* 391 */     Locator loc = sc.getLocator();
/* 392 */     String fileName = null;
/* 393 */     if (loc != null) {
/* 394 */       fileName = loc.getPublicId();
/* 395 */       if (fileName == null)
/* 396 */         fileName = loc.getSystemId(); 
/*     */     } 
/* 398 */     if (fileName == null) fileName = "";
/*     */     
/* 400 */     String lineNumber = Messages.format("ClassSelector.JavadocLineUnknown", new Object[0]);
/* 401 */     if (loc != null && loc.getLineNumber() != -1) {
/* 402 */       lineNumber = String.valueOf(loc.getLineNumber());
/*     */     }
/* 404 */     String componentName = (String)sc.apply((XSFunction)new ComponentNameFunction());
/* 405 */     String jdoc = Messages.format("ClassSelector.JavadocHeading", new Object[] { componentName, fileName, lineNumber });
/* 406 */     append(bean, jdoc);
/*     */ 
/*     */     
/* 409 */     StringWriter out = new StringWriter();
/* 410 */     out.write("<pre>\n");
/* 411 */     SchemaWriter sw = new SchemaWriter((Writer)new JavadocEscapeWriter(out));
/* 412 */     sc.visit((XSVisitor)sw);
/* 413 */     out.write("</pre>");
/* 414 */     append(bean, out.toString());
/*     */   }
/*     */   
/*     */   private void append(CClassInfo bean, String doc) {
/* 418 */     if (bean.javadoc == null) {
/* 419 */       bean.javadoc = doc + '\n';
/*     */     } else {
/* 421 */       bean.javadoc += '\n' + doc + '\n';
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 431 */   private static Set<String> checkedPackageNames = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage getPackage(String targetNamespace) {
/* 442 */     XSSchema s = ((XSSchemaSet)Ring.get(XSSchemaSet.class)).getSchema(targetNamespace);
/*     */ 
/*     */     
/* 445 */     BISchemaBinding sb = (BISchemaBinding)this.builder.getBindInfo((XSComponent)s).get(BISchemaBinding.class);
/* 446 */     if (sb != null) sb.markAsAcknowledged();
/*     */     
/* 448 */     String name = null;
/*     */ 
/*     */     
/* 451 */     if (this.builder.defaultPackage1 != null) {
/* 452 */       name = this.builder.defaultPackage1;
/*     */     }
/*     */     
/* 455 */     if (name == null && sb != null && sb.getPackageName() != null) {
/* 456 */       name = sb.getPackageName();
/*     */     }
/*     */     
/* 459 */     if (name == null && this.builder.defaultPackage2 != null) {
/* 460 */       name = this.builder.defaultPackage2;
/*     */     }
/*     */     
/* 463 */     if (name == null) {
/* 464 */       name = this.builder.getNameConverter().toPackageName(targetNamespace);
/*     */     }
/*     */ 
/*     */     
/* 468 */     if (name == null) {
/* 469 */       name = "generated";
/*     */     }
/*     */ 
/*     */     
/* 473 */     if (checkedPackageNames.add(name))
/*     */     {
/* 475 */       if (!JJavaName.isJavaPackageName(name))
/*     */       {
/*     */ 
/*     */         
/* 479 */         getErrorReporter().error(s.getLocator(), "ClassSelector.IncorrectPackageName", new Object[] { targetNamespace, name });
/*     */       }
/*     */     }
/*     */     
/* 483 */     return ((JCodeModel)Ring.get(JCodeModel.class))._package(name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\ClassSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */