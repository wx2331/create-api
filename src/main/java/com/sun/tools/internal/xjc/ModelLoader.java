/*     */ package com.sun.tools.internal.xjc;
/*     */
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.reader.ExtensionBindingChecker;
/*     */ import com.sun.tools.internal.xjc.reader.dtd.TDTDReader;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForestScanner;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.SCDBasedBindingSet;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.VersionChecker;
/*     */ import com.sun.tools.internal.xjc.reader.relaxng.RELAXNGCompiler;
/*     */ import com.sun.tools.internal.xjc.reader.relaxng.RELAXNGInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.AnnotationParserFactoryImpl;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.CustomizationContextChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.IncorrectNamespaceURIChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.SchemaConstraintChecker;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.util.ErrorReceiverFilter;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.ast.util.CheckingSchemaBuilder;
/*     */ import com.sun.xml.internal.rngom.digested.DPattern;
/*     */ import com.sun.xml.internal.rngom.digested.DSchemaBuilderImpl;
/*     */ import com.sun.xml.internal.rngom.parse.IllegalSchemaException;
/*     */ import com.sun.xml.internal.rngom.parse.Parseable;
/*     */ import com.sun.xml.internal.rngom.parse.compact.CompactParseable;
/*     */ import com.sun.xml.internal.rngom.parse.xml.SAXParseable;
/*     */ import com.sun.xml.internal.rngom.xml.sax.XMLReaderCreator;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.internal.xsom.parser.JAXPParser;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import com.sun.xml.internal.xsom.parser.XSOMParser;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLFilter;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public final class ModelLoader
/*     */ {
/*     */   private final Options opt;
/*     */   private final ErrorReceiverFilter errorReceiver;
/*     */   private final JCodeModel codeModel;
/*     */   private SCDBasedBindingSet scdBasedBindingSet;
/*     */
/*     */   public static Model load(Options opt, JCodeModel codeModel, ErrorReceiver er) {
/* 104 */     return (new ModelLoader(opt, codeModel, er)).load();
/*     */   }
/*     */
/*     */
/*     */   public ModelLoader(Options _opt, JCodeModel _codeModel, ErrorReceiver er) {
/* 109 */     this.opt = _opt;
/* 110 */     this.codeModel = _codeModel;
/* 111 */     this.errorReceiver = new ErrorReceiverFilter(er);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private Model load() {
/* 118 */     if (!sanityCheck())
/* 119 */       return null;
/*     */     try {
/*     */       Model grammar;
/*     */       InputSource bindFile;
/* 123 */       switch (this.opt.getSchemaLanguage()) {
/*     */
/*     */         case DTD:
/* 126 */           bindFile = null;
/* 127 */           if ((this.opt.getBindFiles()).length > 0) {
/* 128 */             bindFile = this.opt.getBindFiles()[0];
/*     */           }
/* 130 */           if (bindFile == null)
/*     */           {
/* 132 */             bindFile = new InputSource(new StringReader("<?xml version='1.0'?><xml-java-binding-schema><options package='" + ((this.opt.defaultPackage == null) ? "generated" : this.opt.defaultPackage) + "'/></xml-java-binding-schema>"));
/*     */           }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 140 */           checkTooManySchemaErrors();
/* 141 */           grammar = loadDTD(this.opt.getGrammars()[0], bindFile);
/*     */           break;
/*     */
/*     */         case RELAXNG:
/* 145 */           checkTooManySchemaErrors();
/* 146 */           grammar = loadRELAXNG();
/*     */           break;
/*     */
/*     */         case RELAXNG_COMPACT:
/* 150 */           checkTooManySchemaErrors();
/* 151 */           grammar = loadRELAXNGCompact();
/*     */           break;
/*     */
/*     */         case WSDL:
/* 155 */           grammar = annotateXMLSchema(loadWSDL());
/*     */           break;
/*     */
/*     */         case XMLSCHEMA:
/* 159 */           grammar = annotateXMLSchema(loadXMLSchema());
/*     */           break;
/*     */
/*     */         default:
/* 163 */           throw new AssertionError();
/*     */       }
/*     */
/* 166 */       if (this.errorReceiver.hadError()) {
/* 167 */         grammar = null;
/*     */       } else {
/* 169 */         grammar.setPackageLevelAnnotations(this.opt.packageLevelAnnotations);
/*     */       }
/*     */
/* 172 */       return grammar;
/*     */     }
/* 174 */     catch (SAXException e) {
/*     */
/*     */
/*     */
/* 178 */       if (this.opt.verbose)
/*     */       {
/*     */
/*     */
/* 182 */         if (e.getException() != null) {
/* 183 */           e.getException().printStackTrace();
/*     */         } else {
/* 185 */           e.printStackTrace();
/*     */         }  }
/* 187 */       return null;
/* 188 */     } catch (AbortException e) {
/*     */
/* 190 */       return null;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private boolean sanityCheck() {
/* 201 */     if (this.opt.getSchemaLanguage() == Language.XMLSCHEMA) {
/* 202 */       Language guess = this.opt.guessSchemaLanguage();
/*     */
/* 204 */       String[] msg = null;
/* 205 */       switch (guess) {
/*     */         case DTD:
/* 207 */           msg = new String[] { "DTD", "-dtd" };
/*     */           break;
/*     */         case RELAXNG:
/* 210 */           msg = new String[] { "RELAX NG", "-relaxng" };
/*     */           break;
/*     */         case RELAXNG_COMPACT:
/* 213 */           msg = new String[] { "RELAX NG compact syntax", "-relaxng-compact" };
/*     */           break;
/*     */         case WSDL:
/* 216 */           msg = new String[] { "WSDL", "-wsdl" };
/*     */           break;
/*     */       }
/* 219 */       if (msg != null) {
/* 220 */         this.errorReceiver.warning(null,
/* 221 */             Messages.format("Driver.ExperimentalLanguageWarning", new Object[] { msg[0], msg[1] }));
/*     */       }
/*     */     }
/*     */
/* 225 */     return true;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private class XMLSchemaParser
/*     */     implements XMLParser
/*     */   {
/*     */     private final XMLParser baseParser;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private XMLSchemaParser(XMLParser baseParser) {
/* 243 */       this.baseParser = baseParser;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 249 */       handler = wrapBy((XMLFilterImpl)new ExtensionBindingChecker("http://www.w3.org/2001/XMLSchema", ModelLoader.this.opt, (ErrorHandler)ModelLoader.this.errorReceiver), handler);
/* 250 */       handler = wrapBy((XMLFilterImpl)new IncorrectNamespaceURIChecker((ErrorHandler)ModelLoader.this.errorReceiver), handler);
/* 251 */       handler = wrapBy((XMLFilterImpl)new CustomizationContextChecker((ErrorHandler)ModelLoader.this.errorReceiver), handler);
/*     */
/*     */
/* 254 */       this.baseParser.parse(source, handler, errorHandler, entityResolver);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler) {
/* 262 */       filter.setContentHandler(handler);
/* 263 */       return filter;
/*     */     }
/*     */   }
/*     */
/*     */   private void checkTooManySchemaErrors() {
/* 268 */     if ((this.opt.getGrammars()).length != 1) {
/* 269 */       this.errorReceiver.error(null, Messages.format("ModelLoader.TooManySchema", new Object[0]));
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
/*     */   private Model loadDTD(InputSource source, InputSource bindFile) {
/* 283 */     return TDTDReader.parse(source, bindFile, (ErrorReceiver)this.errorReceiver, this.opt);
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
/*     */   public DOMForest buildDOMForest(InternalizationLogic logic) throws SAXException {
/* 300 */     DOMForest forest = new DOMForest(logic, this.opt);
/*     */
/* 302 */     forest.setErrorHandler((ErrorReceiver)this.errorReceiver);
/* 303 */     if (this.opt.entityResolver != null) {
/* 304 */       forest.setEntityResolver(this.opt.entityResolver);
/*     */     }
/*     */
/* 307 */     for (InputSource value : this.opt.getGrammars()) {
/* 308 */       this.errorReceiver.pollAbort();
/* 309 */       forest.parse(value, true);
/*     */     }
/*     */
/*     */
/* 313 */     for (InputSource value : this.opt.getBindFiles()) {
/* 314 */       this.errorReceiver.pollAbort();
/* 315 */       Document dom = forest.parse(value, true);
/* 316 */       if (dom != null) {
/* 317 */         Element root = dom.getDocumentElement();
/*     */
/*     */
/* 320 */         if (!fixNull(root.getNamespaceURI()).equals("http://java.sun.com/xml/ns/jaxb") ||
/* 321 */           !root.getLocalName().equals("bindings")) {
/* 322 */           this.errorReceiver.error(new SAXParseException(Messages.format("Driver.NotABindingFile", new Object[] { root
/* 323 */                     .getNamespaceURI(), root
/* 324 */                     .getLocalName()
/*     */
/* 326 */                   }), null, value.getSystemId(), -1, -1));
/*     */         }
/*     */       }
/*     */     }
/* 330 */     this.scdBasedBindingSet = forest.transform(this.opt.isExtensionMode());
/*     */
/* 332 */     return forest;
/*     */   }
/*     */
/*     */   private String fixNull(String s) {
/* 336 */     if (s == null) return "";
/* 337 */     return s;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public XSSchemaSet loadXMLSchema() throws SAXException {
/* 345 */     if (this.opt.strictCheck && !SchemaConstraintChecker.check(this.opt.getGrammars(), (ErrorReceiver)this.errorReceiver, this.opt.entityResolver, this.opt.disableXmlSecurity))
/*     */     {
/* 347 */       return null;
/*     */     }
/*     */
/* 350 */     if ((this.opt.getBindFiles()).length == 0) {
/*     */
/*     */       try {
/*     */
/* 354 */         return createXSOMSpeculative();
/* 355 */       } catch (SpeculationFailure speculationFailure) {}
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/* 362 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/* 363 */     return createXSOM(forest, this.scdBasedBindingSet);
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
/*     */   private XSSchemaSet loadWSDL() throws SAXException {
/* 376 */     DOMForest forest = buildDOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic());
/*     */
/* 378 */     DOMForestScanner scanner = new DOMForestScanner(forest);
/*     */
/* 380 */     XSOMParser xsomParser = createXSOMParser(forest);
/*     */
/*     */
/* 383 */     for (InputSource grammar : this.opt.getGrammars()) {
/* 384 */       Document wsdlDom = forest.get(grammar.getSystemId());
/* 385 */       if (wsdlDom == null) {
/* 386 */         String systemId = Options.normalizeSystemId(grammar.getSystemId());
/* 387 */         if (forest.get(systemId) != null) {
/* 388 */           grammar.setSystemId(systemId);
/* 389 */           wsdlDom = forest.get(grammar.getSystemId());
/*     */         }
/*     */       }
/*     */
/* 393 */       NodeList schemas = wsdlDom.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
/* 394 */       for (int i = 0; i < schemas.getLength(); i++)
/* 395 */         scanner.scan((Element)schemas.item(i), xsomParser.getParserHandler());
/*     */     }
/* 397 */     return xsomParser.getResult();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public Model annotateXMLSchema(XSSchemaSet xs) {
/* 408 */     if (xs == null)
/* 409 */       return null;
/* 410 */     return BGMBuilder.build(xs, this.codeModel, (ErrorReceiver)this.errorReceiver, this.opt);
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
/*     */   public XSOMParser createXSOMParser(XMLParser parser) {
/* 422 */     XSOMParser reader = new XSOMParser(new XMLSchemaParser(parser));
/* 423 */     reader.setAnnotationParser((AnnotationParserFactory)new AnnotationParserFactoryImpl(this.opt));
/* 424 */     reader.setErrorHandler((ErrorHandler)this.errorReceiver);
/* 425 */     reader.setEntityResolver(this.opt.entityResolver);
/* 426 */     return reader;
/*     */   }
/*     */
/*     */   public XSOMParser createXSOMParser(final DOMForest forest) {
/* 430 */     XSOMParser p = createXSOMParser(forest.createParser());
/* 431 */     p.setEntityResolver(new EntityResolver()
/*     */         {
/*     */
/*     */
/*     */
/*     */
/*     */           public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
/*     */           {
/* 439 */             if (systemId != null && forest.get(systemId) != null)
/* 440 */               return new InputSource(systemId);
/* 441 */             if (ModelLoader.this.opt.entityResolver != null) {
/* 442 */               return ModelLoader.this.opt.entityResolver.resolveEntity(publicId, systemId);
/*     */             }
/* 444 */             return null;
/*     */           }
/*     */         });
/* 447 */     return p;
/*     */   }
/*     */
/*     */   private static final class SpeculationFailure extends Error {
/*     */     private SpeculationFailure() {} }
/*     */
/*     */   private static final class SpeculationChecker extends XMLFilterImpl { private SpeculationChecker() {}
/*     */
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 456 */       if (localName.equals("bindings") && uri.equals("http://java.sun.com/xml/ns/jaxb"))
/* 457 */         throw new SpeculationFailure();
/* 458 */       super.startElement(uri, localName, qName, attributes);
/*     */     } }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private XSSchemaSet createXSOMSpeculative() throws SAXException, SpeculationFailure {
/* 473 */     XMLParser parser = new XMLParser() {
/* 474 */         private final JAXPParser base = new JAXPParser(XmlFactory.createParserFactory(ModelLoader.this.opt.disableXmlSecurity));
/*     */
/*     */
/*     */
/*     */         public void parse(InputSource source, ContentHandler handler, ErrorHandler errorHandler, EntityResolver entityResolver) throws SAXException, IOException {
/* 479 */           handler = wrapBy(new SpeculationChecker(), handler);
/* 480 */           handler = wrapBy((XMLFilterImpl)new VersionChecker(null, (ErrorHandler)ModelLoader.this.errorReceiver, entityResolver), handler);
/*     */
/* 482 */           this.base.parse(source, handler, errorHandler, entityResolver);
/*     */         }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */         private ContentHandler wrapBy(XMLFilterImpl filter, ContentHandler handler) {
/* 490 */           filter.setContentHandler(handler);
/* 491 */           return filter;
/*     */         }
/*     */       };
/*     */
/* 495 */     XSOMParser reader = createXSOMParser(parser);
/*     */
/*     */
/* 498 */     for (InputSource value : this.opt.getGrammars()) {
/* 499 */       reader.parse(value);
/*     */     }
/* 501 */     return reader.getResult();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public XSSchemaSet createXSOM(DOMForest forest, SCDBasedBindingSet scdBasedBindingSet) throws SAXException {
/* 512 */     XSOMParser reader = createXSOMParser(forest);
/*     */
/*     */
/* 515 */     for (String systemId : forest.getRootDocuments()) {
/* 516 */       this.errorReceiver.pollAbort();
/* 517 */       Document dom = forest.get(systemId);
/* 518 */       if (!dom.getDocumentElement().getNamespaceURI().equals("http://java.sun.com/xml/ns/jaxb")) {
/* 519 */         reader.parse(systemId);
/*     */       }
/*     */     }
/*     */
/* 523 */     XSSchemaSet result = reader.getResult();
/*     */
/* 525 */     if (result != null) {
/* 526 */       scdBasedBindingSet.apply(result, (ErrorReceiver)this.errorReceiver);
/*     */     }
/* 528 */     return result;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Model loadRELAXNG() throws SAXException {
/* 537 */     final DOMForest forest = buildDOMForest((InternalizationLogic)new RELAXNGInternalizationLogic());
/*     */
/*     */
/*     */
/*     */
/* 542 */     XMLReaderCreator xrc = new XMLReaderCreator()
/*     */       {
/*     */
/*     */         public XMLReader createXMLReader()
/*     */         {
/* 547 */           XMLFilter buffer = new XMLFilterImpl()
/*     */             {
/*     */               public void parse(InputSource source) throws IOException, SAXException {
/* 550 */                 forest.createParser().parse(source, this, this, this);
/*     */               }
/*     */             };
/*     */
/* 554 */           ExtensionBindingChecker extensionBindingChecker = new ExtensionBindingChecker("http://relaxng.org/ns/structure/1.0", ModelLoader.this.opt, (ErrorHandler)ModelLoader.this.errorReceiver);
/* 555 */           extensionBindingChecker.setParent(buffer);
/*     */
/* 557 */           extensionBindingChecker.setEntityResolver(ModelLoader.this.opt.entityResolver);
/*     */
/* 559 */           return (XMLReader)extensionBindingChecker;
/*     */         }
/*     */       };
/*     */
/* 563 */     SAXParseable sAXParseable = new SAXParseable(this.opt.getGrammars()[0], (ErrorHandler)this.errorReceiver, xrc);
/*     */
/* 565 */     return loadRELAXNG((Parseable)sAXParseable);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Model loadRELAXNGCompact() {
/* 573 */     if ((this.opt.getBindFiles()).length > 0) {
/* 574 */       this.errorReceiver.error(new SAXParseException(
/* 575 */             Messages.format("ModelLoader.BindingFileNotSupportedForRNC", new Object[0]), null));
/*     */     }
/*     */
/* 578 */     CompactParseable compactParseable = new CompactParseable(this.opt.getGrammars()[0], (ErrorHandler)this.errorReceiver);
/*     */
/* 580 */     return loadRELAXNG((Parseable)compactParseable);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private Model loadRELAXNG(Parseable p) {
/* 588 */     CheckingSchemaBuilder checkingSchemaBuilder = new CheckingSchemaBuilder((SchemaBuilder)new DSchemaBuilderImpl(), (ErrorHandler)this.errorReceiver);
/*     */
/*     */     try {
/* 591 */       DPattern out = (DPattern)p.parse((SchemaBuilder)checkingSchemaBuilder);
/* 592 */       return RELAXNGCompiler.build(out, this.codeModel, this.opt);
/* 593 */     } catch (IllegalSchemaException e) {
/* 594 */       this.errorReceiver.error(e.getMessage(), (Exception)e);
/* 595 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ModelLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
