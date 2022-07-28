/*     */ package com.sun.tools.internal.xjc.reader.dtd;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.model.TypeUse;
/*     */ import com.sun.tools.internal.xjc.model.TypeUseFactory;
/*     */ import com.sun.tools.internal.xjc.reader.ModelChecker;
/*     */ import com.sun.tools.internal.xjc.reader.Ring;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIAttribute;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIElement;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIInterface;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.dtdparser.DTDEventListener;
/*     */ import com.sun.xml.internal.dtdparser.DTDHandlerBase;
/*     */ import com.sun.xml.internal.dtdparser.DTDParser;
/*     */ import com.sun.xml.internal.dtdparser.InputEntity;
/*     */ import com.sun.xml.internal.xsom.XmlString;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ 
/*     */ public class TDTDReader
/*     */   extends DTDHandlerBase
/*     */ {
/*     */   private final EntityResolver entityResolver;
/*     */   final BindInfo bindInfo;
/*     */   final Model model;
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   private final Map<String, Element> elements;
/*     */   private final Stack<ModelGroup> modelGroups;
/*     */   private Locator locator;
/*     */   private static final Map<String, TypeUse> builtinConversions;
/*     */   
/*     */   public static Model parse(InputSource dtd, InputSource bindingInfo, ErrorReceiver errorReceiver, Options opts) {
/*     */     try {
/*  99 */       Ring old = Ring.begin();
/*     */       try {
/* 101 */         ErrorReceiverFilter ef = new ErrorReceiverFilter((ErrorListener)errorReceiver);
/*     */         
/* 103 */         JCodeModel cm = new JCodeModel();
/* 104 */         Model model = new Model(opts, cm, NameConverter.standard, opts.classNameAllocator, null);
/*     */         
/* 106 */         Ring.add(cm);
/* 107 */         Ring.add(model);
/* 108 */         Ring.add(ErrorReceiver.class, ef);
/*     */         
/* 110 */         TDTDReader reader = new TDTDReader((ErrorReceiver)ef, opts, bindingInfo);
/*     */         
/* 112 */         DTDParser parser = new DTDParser();
/* 113 */         parser.setDtdHandler((DTDEventListener)reader);
/* 114 */         if (opts.entityResolver != null) {
/* 115 */           parser.setEntityResolver(opts.entityResolver);
/*     */         }
/*     */         try {
/* 118 */           parser.parse(dtd);
/* 119 */         } catch (SAXParseException e) {
/* 120 */           return null;
/*     */         } 
/*     */         
/* 123 */         ((ModelChecker)Ring.get(ModelChecker.class)).check();
/*     */         
/* 125 */         if (ef.hadError()) return null; 
/* 126 */         return model;
/*     */       } finally {
/* 128 */         Ring.end(old);
/*     */       } 
/* 130 */     } catch (IOException e) {
/* 131 */       errorReceiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 132 */       return null;
/* 133 */     } catch (SAXException e) {
/* 134 */       errorReceiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 135 */       return null;
/* 136 */     } catch (AbortException e) {
/*     */       
/* 138 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TDTDReader(ErrorReceiver errorReceiver, Options opts, InputSource _bindInfo) throws AbortException {
/* 160 */     this.model = (Model)Ring.get(Model.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     this.elements = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     this.modelGroups = new Stack<>(); this.entityResolver = opts.entityResolver; this.errorReceiver = new ErrorReceiverFilter((ErrorListener)errorReceiver); this.bindInfo = new BindInfo(this.model, _bindInfo, (ErrorReceiver)this.errorReceiver); this.classFactory = new CodeModelClassFactory(errorReceiver);
/*     */   } public void startDTD(InputEntity entity) throws SAXException {} public void endDTD() throws SAXException { for (Element e : this.elements.values()) e.bind();  if (this.errorReceiver.hadError()) return;  processInterfaceDeclarations(); this.model.serialVersionUID = this.bindInfo.getSerialVersionUID(); if (this.model.serialVersionUID != null) this.model.serializable = true;  this.model.rootClass = this.bindInfo.getSuperClass(); this.model.rootInterface = this.bindInfo.getSuperInterface(); processConstructorDeclarations(); } private void processInterfaceDeclarations() { Map<String, InterfaceAcceptor> fromName = new HashMap<>(); Map<BIInterface, JClass> decls = new HashMap<>(); for (BIInterface decl : this.bindInfo.interfaces()) { final JDefinedClass intf = this.classFactory.createInterface((JClassContainer)this.bindInfo.getTargetPackage(), decl.name(), copyLocator()); decls.put(decl, intf); fromName.put(decl.name(), new InterfaceAcceptor() { public void implement(JClass c) { intf._implements(c); } }
/*     */         ); }  for (CClassInfo ci : this.model.beans().values()) { fromName.put(ci.getName(), new InterfaceAcceptor() { public void implement(JClass c) { ci._implements(c); } }
/* 364 */         ); }  for (Map.Entry<BIInterface, JClass> e : decls.entrySet()) { BIInterface decl = e.getKey(); JClass c = e.getValue(); for (String member : decl.members()) { InterfaceAcceptor acc = fromName.get(member); if (acc == null) { error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentInterfaceMember", new Object[] { member }); } else { acc.implement(c); }  }  }  } JPackage getTargetPackage() { return this.bindInfo.getTargetPackage(); } private void processConstructorDeclarations() { for (BIElement decl : this.bindInfo.elements()) { Element e = this.elements.get(decl.name()); if (e == null) { error(decl.getSourceLocation(), "TDTDReader.BindInfo.NonExistentElementDeclaration", new Object[] { decl.name() }); continue; }  if (!decl.isClass()) continue;  decl.declareConstructors(e.getClassInfo()); }  } public void startModelGroup() throws SAXException { this.modelGroups.push(new ModelGroup()); }
/*     */   public void attributeDecl(String elementName, String attributeName, String attributeType, String[] enumeration, short attributeUse, String defaultValue) throws SAXException { (getOrCreateElement(elementName)).attributes.add(createAttribute(elementName, attributeName, attributeType, enumeration, attributeUse, defaultValue)); }
/*     */   protected CPropertyInfo createAttribute(String elementName, String attributeName, String attributeType, String[] enums, short attributeUse, String defaultValue) throws SAXException { String propName; TypeUse use; boolean required = (attributeUse == 3); BIElement edecl = this.bindInfo.element(elementName); BIAttribute decl = null; if (edecl != null) decl = edecl.attribute(attributeName);  if (decl == null) { propName = this.model.getNameConverter().toPropertyName(attributeName); } else { propName = decl.getPropertyName(); }  QName qname = new QName("", attributeName); if (decl != null && decl.getConversion() != null) { use = decl.getConversion().getTransducer(); } else { use = builtinConversions.get(attributeType); }  CAttributePropertyInfo cAttributePropertyInfo = new CAttributePropertyInfo(propName, null, null, copyLocator(), qname, use, null, required); if (defaultValue != null) ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(use, new XmlString(defaultValue));  return (CPropertyInfo)cAttributePropertyInfo; }
/*     */   Element getOrCreateElement(String elementName) { Element r = this.elements.get(elementName); if (r == null) { r = new Element(this, elementName); this.elements.put(elementName, r); }  return r; }
/* 368 */   public void startContentModel(String elementName, short contentModelType) throws SAXException { assert this.modelGroups.isEmpty(); this.modelGroups.push(new ModelGroup()); } public void endContentModel(String elementName, short contentModelType) throws SAXException { assert this.modelGroups.size() == 1; Term term = ((ModelGroup)this.modelGroups.pop()).wrapUp(); Element e = getOrCreateElement(elementName); e.define(contentModelType, term, copyLocator()); } public void endModelGroup(short occurence) throws SAXException { Term t = Occurence.wrap(((ModelGroup)this.modelGroups.pop()).wrapUp(), occurence);
/* 369 */     ((ModelGroup)this.modelGroups.peek()).addTerm(t); }
/*     */ 
/*     */   
/*     */   public void connector(short connectorType) throws SAXException {
/* 373 */     ((ModelGroup)this.modelGroups.peek()).setKind(connectorType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void childElement(String elementName, short occurence) throws SAXException {
/* 380 */     Element child = getOrCreateElement(elementName);
/* 381 */     ((ModelGroup)this.modelGroups.peek()).addTerm(Occurence.wrap(child, occurence));
/* 382 */     child.isReferenced = true;
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
/*     */   public void setDocumentLocator(Locator loc) {
/* 398 */     this.locator = loc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator copyLocator() {
/* 405 */     return new LocatorImpl(this.locator);
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
/*     */   static {
/* 425 */     Map<String, TypeUse> m = new HashMap<>();
/*     */     
/* 427 */     m.put("CDATA", CBuiltinLeafInfo.NORMALIZED_STRING);
/* 428 */     m.put("ENTITY", CBuiltinLeafInfo.TOKEN);
/* 429 */     m.put("ENTITIES", CBuiltinLeafInfo.STRING.makeCollection());
/* 430 */     m.put("NMTOKEN", CBuiltinLeafInfo.TOKEN);
/* 431 */     m.put("NMTOKENS", CBuiltinLeafInfo.STRING.makeCollection());
/* 432 */     m.put("ID", CBuiltinLeafInfo.ID);
/* 433 */     m.put("IDREF", CBuiltinLeafInfo.IDREF);
/* 434 */     m.put("IDREFS", TypeUseFactory.makeCollection(CBuiltinLeafInfo.IDREF));
/* 435 */     m.put("ENUMERATION", CBuiltinLeafInfo.TOKEN);
/*     */     
/* 437 */     builtinConversions = Collections.unmodifiableMap(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(SAXParseException e) throws SAXException {
/* 447 */     this.errorReceiver.error(e);
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 451 */     this.errorReceiver.fatalError(e);
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException e) throws SAXException {
/* 455 */     this.errorReceiver.warning(e);
/*     */   }
/*     */   
/*     */   protected final void error(Locator loc, String prop, Object... args) {
/* 459 */     this.errorReceiver.error(loc, Messages.format(prop, args));
/*     */   }
/*     */   
/*     */   private static interface InterfaceAcceptor {
/*     */     void implement(JClass param1JClass);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\TDTDReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */