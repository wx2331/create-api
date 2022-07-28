/*     */ package com.sun.tools.internal.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.Plugin;
/*     */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.generator.bean.BeanGenerator;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy;
/*     */ import com.sun.tools.internal.xjc.model.nav.NClass;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.tools.internal.xjc.model.nav.NavigatorImpl;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.Messages;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.util.FlattenIterator;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Model
/*     */   implements TypeInfoSet<NType, NClass, Void, Void>, CCustomizable
/*     */ {
/*  84 */   private final Map<NClass, CClassInfo> beans = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private final Map<NClass, CEnumLeafInfo> enums = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private final Map<NClass, Map<QName, CElementInfo>> elementMappings = new HashMap<>();
/*     */ 
/*     */   
/*  97 */   private final Iterable<? extends CElementInfo> allElements = new Iterable<CElementInfo>()
/*     */     {
/*     */       public Iterator<CElementInfo> iterator() {
/* 100 */         return new FlattenIterator<>(Model.this.elementMappings.values());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private final Map<QName, TypeUse> typeUses = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameConverter nameConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CCustomizations customizations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean packageLevelAnnotations = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XSSchemaSet schemaComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private CCustomizations gloablCustomizations = new CCustomizations();
/*     */   
/*     */   @XmlTransient
/*     */   public final JCodeModel codeModel;
/*     */   
/*     */   public final Options options;
/*     */   
/*     */   @XmlAttribute
/*     */   public boolean serializable;
/*     */   
/*     */   @XmlAttribute
/*     */   public Long serialVersionUID;
/*     */   
/*     */   @XmlTransient
/*     */   public JClass rootClass;
/*     */   @XmlTransient
/*     */   public JClass rootInterface;
/*     */   public ImplStructureStrategy strategy;
/*     */   final ClassNameAllocatorWrapper allocator;
/*     */   @XmlTransient
/*     */   public final SymbolSpace defaultSymbolSpace;
/*     */   private final Map<String, SymbolSpace> symbolSpaces;
/*     */   private final Map<JPackage, CClassInfoParent.Package> cache;
/*     */   static final Locator EMPTY_LOCATOR;
/*     */   
/*     */   public void setNameConverter(NameConverter nameConverter) {
/* 165 */     assert this.nameConverter == null;
/* 166 */     assert nameConverter != null;
/* 167 */     this.nameConverter = nameConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final NameConverter getNameConverter() {
/* 174 */     return this.nameConverter;
/*     */   }
/*     */   
/*     */   public boolean isPackageLevelAnnotations() {
/* 178 */     return this.packageLevelAnnotations;
/*     */   }
/*     */   
/*     */   public void setPackageLevelAnnotations(boolean packageLevelAnnotations) {
/* 182 */     this.packageLevelAnnotations = packageLevelAnnotations;
/*     */   }
/*     */   public SymbolSpace getSymbolSpace(String name) { SymbolSpace ss = this.symbolSpaces.get(name);
/*     */     if (ss == null)
/*     */       this.symbolSpaces.put(name, ss = new SymbolSpace(this.codeModel)); 
/*     */     return ss; }
/*     */   public Outline generateCode(Options opt, ErrorReceiver receiver) { ErrorReceiverFilter ehf = new ErrorReceiverFilter((ErrorListener)receiver);
/*     */     Outline o = BeanGenerator.generate(this, (ErrorReceiver)ehf);
/*     */     try {
/*     */       for (Plugin ma : opt.activePlugins)
/*     */         ma.run(o, opt, (ErrorHandler)ehf); 
/*     */     } catch (SAXException e) {
/*     */       return null;
/*     */     } 
/*     */     Set<CCustomizations> check = new HashSet<>();
/*     */     for (CCustomizations c = this.customizations; c != null; c = c.next) {
/*     */       if (!check.add(c))
/*     */         throw new AssertionError(); 
/*     */       for (CPluginCustomization p : c) {
/*     */         if (!p.isAcknowledged()) {
/*     */           ehf.error(p.locator, Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization", new Object[] { p.element.getNodeName() }));
/*     */           ehf.error(c.getOwner().getLocator(), Messages.format("UnusedCustomizationChecker.UnacknolwedgedCustomization.Relevant", new Object[0]));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     if (ehf.hadError())
/*     */       o = null; 
/*     */     return o; }
/*     */   public final Map<QName, CClassInfo> createTopLevelBindings() { Map<QName, CClassInfo> r = new HashMap<>();
/*     */     for (CClassInfo b : beans().values()) {
/*     */       if (b.isElement())
/*     */         r.put(b.getElementName(), b); 
/*     */     } 
/*     */     return r; }
/*     */   public Navigator<NType, NClass, Void, Void> getNavigator() { return (Navigator<NType, NClass, Void, Void>)NavigatorImpl.theInstance; }
/*     */   public CNonElement getTypeInfo(NType type) { CBuiltinLeafInfo leaf = CBuiltinLeafInfo.LEAVES.get(type);
/*     */     if (leaf != null)
/*     */       return leaf; 
/*     */     return (CNonElement)getClassInfo(getNavigator().asDecl(type)); }
/*     */   public CBuiltinLeafInfo getAnyTypeInfo() { return CBuiltinLeafInfo.ANYTYPE; }
/*     */   public CNonElement getTypeInfo(Ref<NType, NClass> ref) { assert !ref.valueList;
/*     */     return getTypeInfo((NType)ref.type); }
/*     */   public Map<NClass, CClassInfo> beans() { return this.beans; }
/*     */   public Map<NClass, CEnumLeafInfo> enums() { return this.enums; }
/* 226 */   public Map<QName, TypeUse> typeUses() { return this.typeUses; } public Model(Options opts, JCodeModel cm, NameConverter nc, ClassNameAllocator allocator, XSSchemaSet schemaComponent) { this.strategy = ImplStructureStrategy.BEAN_ONLY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     this.symbolSpaces = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     this.cache = new HashMap<>(); this.options = opts; this.codeModel = cm; this.nameConverter = nc; this.defaultSymbolSpace = new SymbolSpace(this.codeModel); this.defaultSymbolSpace.setType((JType)this.codeModel.ref(Object.class)); this.elementMappings.put(null, new HashMap<>()); if (opts.automaticNameConflictResolution) allocator = new AutoClassNameAllocator(allocator);  this.allocator = new ClassNameAllocatorWrapper(allocator); this.schemaComponent = schemaComponent; this.gloablCustomizations.setParent(this, this); }
/*     */   public Map<NType, ? extends CArrayInfo> arrays() { return Collections.emptyMap(); }
/*     */   public Map<NType, ? extends CBuiltinLeafInfo> builtins() { return CBuiltinLeafInfo.LEAVES; }
/* 477 */   public CClassInfo getClassInfo(NClass t) { return this.beans.get(t); } public CClassInfoParent.Package getPackage(JPackage pkg) { CClassInfoParent.Package r = this.cache.get(pkg);
/* 478 */     if (r == null)
/* 479 */       this.cache.put(pkg, r = new CClassInfoParent.Package(pkg)); 
/* 480 */     return r; } public CElementInfo getElementInfo(NClass scope, QName name) { Map<QName, CElementInfo> m = this.elementMappings.get(scope); if (m != null) { CElementInfo r = m.get(name); if (r != null) return r;  }  return (CElementInfo)((Map)this.elementMappings.get(null)).get(name); } public Map<QName, CElementInfo> getElementMappings(NClass scope) { return this.elementMappings.get(scope); }
/*     */   public Iterable<? extends CElementInfo> getAllElements() { return this.allElements; }
/*     */   public XSComponent getSchemaComponent() { return null; }
/*     */   public Locator getLocator() { LocatorImpl r = new LocatorImpl(); r.setLineNumber(-1); r.setColumnNumber(-1); return r; }
/*     */   public CCustomizations getCustomizations() { return this.gloablCustomizations; }
/*     */   public Map<String, String> getXmlNs(String namespaceUri) { return Collections.emptyMap(); }
/* 486 */   static { LocatorImpl l = new LocatorImpl();
/* 487 */     l.setColumnNumber(-1);
/* 488 */     l.setLineNumber(-1);
/* 489 */     EMPTY_LOCATOR = l; }
/*     */ 
/*     */   
/*     */   public Map<String, String> getSchemaLocations() {
/*     */     return Collections.emptyMap();
/*     */   }
/*     */   
/*     */   public XmlNsForm getElementFormDefault(String nsUri) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public XmlNsForm getAttributeFormDefault(String nsUri) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void dump(Result out) {
/*     */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   void add(CEnumLeafInfo e) {
/*     */     this.enums.put(e.getClazz(), e);
/*     */   }
/*     */   
/*     */   void add(CClassInfo ci) {
/*     */     this.beans.put(ci.getClazz(), ci);
/*     */   }
/*     */   
/*     */   void add(CElementInfo ei) {
/*     */     NClass clazz = null;
/*     */     if (ei.getScope() != null)
/*     */       clazz = ei.getScope().getClazz(); 
/*     */     Map<QName, CElementInfo> m = this.elementMappings.get(clazz);
/*     */     if (m == null)
/*     */       this.elementMappings.put(clazz, m = new HashMap<>()); 
/*     */     m.put(ei.getElementName(), ei);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\Model.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */