/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.AbortException;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.SchemaCache;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.model.CCustomizations;
/*     */ import com.sun.tools.internal.xjc.model.CPluginCustomization;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.util.CodeModelClassFactory;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.xjc.util.ForkContentHandler;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindInfo
/*     */ {
/*     */   protected final ErrorReceiver errorReceiver;
/*     */   final Model model;
/*     */   private final String defaultPackage;
/*     */   final JCodeModel codeModel;
/*     */   final CodeModelClassFactory classFactory;
/*     */   private final Element dom;
/*     */   private final Map<String, BIConversion> conversions;
/*     */   private final Map<String, BIElement> elements;
/*     */   private final Map<String, BIInterface> interfaces;
/*     */   private static final String XJC_NS = "http://java.sun.com/xml/ns/jaxb/xjc";
/*     */   
/*     */   public BindInfo(Model model, InputSource source, ErrorReceiver _errorReceiver) throws AbortException {
/*  81 */     this(model, parse(model, source, _errorReceiver), _errorReceiver);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindInfo(Model model, Document _dom, ErrorReceiver _errorReceiver) {
/* 136 */     this.conversions = new HashMap<>();
/*     */ 
/*     */     
/* 139 */     this.elements = new HashMap<>();
/*     */ 
/*     */     
/* 142 */     this.interfaces = new HashMap<>(); this.model = model; this.dom = _dom.getDocumentElement(); this.codeModel = model.codeModel; this.errorReceiver = _errorReceiver; this.classFactory = new CodeModelClassFactory(_errorReceiver); this.defaultPackage = model.options.defaultPackage; model.getCustomizations().addAll((Collection)getGlobalCustomizations()); for (Element ele : DOMUtil.getChildElements(this.dom, "element")) {
/*     */       BIElement e = new BIElement(this, ele); this.elements.put(e.name(), e);
/*     */     }  BIUserConversion.addBuiltinConversions(this, this.conversions); for (Element cnv : DOMUtil.getChildElements(this.dom, "conversion")) {
/*     */       BIConversion c = new BIUserConversion(this, cnv);
/*     */       this.conversions.put(c.name(), c);
/*     */     } 
/*     */     for (Element en : DOMUtil.getChildElements(this.dom, "enumeration")) {
/*     */       BIConversion c = BIEnumeration.create(en, this);
/*     */       this.conversions.put(c.name(), c);
/*     */     } 
/*     */     for (Element itf : DOMUtil.getChildElements(this.dom, "interface")) {
/*     */       BIInterface c = new BIInterface(itf);
/*     */       this.interfaces.put(c.name(), c);
/* 155 */     }  } public Long getSerialVersionUID() { Element serial = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "serializable");
/* 156 */     if (serial == null) return null;
/*     */     
/* 158 */     String v = DOMUtil.getAttribute(serial, "uid");
/* 159 */     if (v == null) v = "1"; 
/* 160 */     return new Long(v); }
/*     */ 
/*     */   
/*     */   public JClass getSuperClass() {
/*     */     JDefinedClass c;
/* 165 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superClass");
/* 166 */     if (sc == null) return null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 171 */       String v = DOMUtil.getAttribute(sc, "name");
/* 172 */       if (v == null) return null; 
/* 173 */       c = this.codeModel._class(v);
/* 174 */       c.hide();
/* 175 */     } catch (JClassAlreadyExistsException e) {
/* 176 */       c = e.getExistingClass();
/*     */     } 
/*     */     
/* 179 */     return (JClass)c;
/*     */   }
/*     */   
/*     */   public JClass getSuperInterface() {
/*     */     JDefinedClass c;
/* 184 */     Element sc = DOMUtil.getElement(this.dom, "http://java.sun.com/xml/ns/jaxb/xjc", "superInterface");
/* 185 */     if (sc == null) return null;
/*     */     
/* 187 */     String name = DOMUtil.getAttribute(sc, "name");
/* 188 */     if (name == null) return null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 193 */       c = this.codeModel._class(name, ClassType.INTERFACE);
/* 194 */       c.hide();
/* 195 */     } catch (JClassAlreadyExistsException e) {
/* 196 */       c = e.getExistingClass();
/*     */     } 
/*     */     
/* 199 */     return (JClass)c;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JPackage getTargetPackage() {
/*     */     String p;
/* 206 */     if (this.model.options.defaultPackage != null)
/*     */     {
/* 208 */       return this.codeModel._package(this.model.options.defaultPackage);
/*     */     }
/*     */     
/* 211 */     if (this.defaultPackage != null) {
/* 212 */       p = this.defaultPackage;
/*     */     } else {
/* 214 */       p = getOption("package", "");
/* 215 */     }  return this.codeModel._package(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIConversion conversion(String name) {
/* 225 */     BIConversion r = this.conversions.get(name);
/* 226 */     if (r == null)
/* 227 */       throw new AssertionError("undefined conversion name: this should be checked by the validator before we read it"); 
/* 228 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIElement element(String name) {
/* 239 */     return this.elements.get(name);
/*     */   }
/*     */   
/*     */   public Collection<BIElement> elements() {
/* 243 */     return this.elements.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<BIInterface> interfaces() {
/* 248 */     return this.interfaces.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CCustomizations getGlobalCustomizations() {
/* 255 */     CCustomizations r = null;
/* 256 */     for (Element e : DOMUtil.getChildElements(this.dom)) {
/* 257 */       if (!this.model.options.pluginURIs.contains(e.getNamespaceURI()))
/*     */         continue; 
/* 259 */       if (r == null)
/* 260 */         r = new CCustomizations(); 
/* 261 */       r.add(new CPluginCustomization(e, DOMLocator.getLocationInfo(e)));
/*     */     } 
/*     */     
/* 264 */     if (r == null) r = CCustomizations.EMPTY; 
/* 265 */     return new CCustomizations((Collection)r);
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
/*     */   private String getOption(String attName, String defaultValue) {
/* 280 */     Element opt = DOMUtil.getElement(this.dom, "options");
/* 281 */     if (opt != null) {
/* 282 */       String s = DOMUtil.getAttribute(opt, attName);
/* 283 */       if (s != null)
/* 284 */         return s; 
/*     */     } 
/* 286 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 292 */   private static SchemaCache bindingFileSchema = new SchemaCache(BindInfo.class.getResource("bindingfile.xsd"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Document parse(Model model, InputSource is, ErrorReceiver receiver) throws AbortException {
/*     */     try {
/* 300 */       ValidatorHandler validator = bindingFileSchema.newValidator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 306 */       SAXParserFactory pf = XmlFactory.createParserFactory(model.options.disableXmlSecurity);
/* 307 */       DocumentBuilderFactory domFactory = XmlFactory.createDocumentBuilderFactory(model.options.disableXmlSecurity);
/* 308 */       DOMBuilder builder = new DOMBuilder(domFactory);
/*     */       
/* 310 */       ErrorReceiverFilter controller = new ErrorReceiverFilter((ErrorListener)receiver);
/* 311 */       validator.setErrorHandler((ErrorHandler)controller);
/* 312 */       XMLReader reader = pf.newSAXParser().getXMLReader();
/* 313 */       reader.setErrorHandler((ErrorHandler)controller);
/*     */       
/* 315 */       DTDExtensionBindingChecker checker = new DTDExtensionBindingChecker("", model.options, (ErrorHandler)controller);
/* 316 */       checker.setContentHandler(validator);
/*     */       
/* 318 */       reader.setContentHandler((ContentHandler)new ForkContentHandler((ContentHandler)checker, builder));
/*     */       
/* 320 */       reader.parse(is);
/*     */       
/* 322 */       if (controller.hadError()) throw new AbortException(); 
/* 323 */       return (Document)builder.getDOM();
/* 324 */     } catch (IOException e) {
/* 325 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 326 */     } catch (SAXException e) {
/* 327 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/* 328 */     } catch (ParserConfigurationException e) {
/* 329 */       receiver.error(new SAXParseException2(e.getMessage(), null, e));
/*     */     } 
/*     */     
/* 332 */     throw new AbortException();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BindInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */