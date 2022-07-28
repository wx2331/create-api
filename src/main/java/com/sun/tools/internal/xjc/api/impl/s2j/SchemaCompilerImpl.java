/*     */ package com.sun.tools.internal.xjc.api.impl.s2j;
/*     */ 
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.ModelLoader;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.api.ClassNameAllocator;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.SchemaCompiler;
/*     */ import com.sun.tools.internal.xjc.api.SpecVersion;
/*     */ import com.sun.tools.internal.xjc.model.Model;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
/*     */ import com.sun.tools.internal.xjc.reader.internalizer.SCDBasedBindingSet;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.LSInputSAXWrapper;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic;
/*     */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.internal.bind.v2.util.XmlFactory;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SchemaCompilerImpl
/*     */   extends ErrorReceiver
/*     */   implements SchemaCompiler
/*     */ {
/*     */   private ErrorListener errorListener;
/*  88 */   protected final Options opts = new Options();
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   protected DOMForest forest;
/*     */   
/*     */   private boolean hadError;
/*     */ 
/*     */   
/*     */   public SchemaCompilerImpl() {
/*  98 */     this.opts.compatibilityMode = 2;
/*  99 */     resetSchema();
/*     */     
/* 101 */     if (System.getProperty("xjc-api.test") != null) {
/* 102 */       this.opts.debugMode = true;
/* 103 */       this.opts.verbose = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Options getOptions() {
/* 109 */     return this.opts;
/*     */   }
/*     */   
/*     */   public ContentHandler getParserHandler(String systemId) {
/* 113 */     return (ContentHandler)this.forest.getParserHandler(systemId, true);
/*     */   }
/*     */   
/*     */   public void parseSchema(String systemId, Element element) {
/* 117 */     checkAbsoluteness(systemId);
/*     */     try {
/* 119 */       DOMScanner scanner = new DOMScanner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       LocatorImpl loc = new LocatorImpl();
/* 126 */       loc.setSystemId(systemId);
/* 127 */       scanner.setLocator(loc);
/*     */       
/* 129 */       scanner.setContentHandler(getParserHandler(systemId));
/* 130 */       scanner.scan(element);
/* 131 */     } catch (SAXException e) {
/*     */ 
/*     */ 
/*     */       
/* 135 */       fatalError(new SAXParseException2(e
/* 136 */             .getMessage(), null, systemId, -1, -1, e));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void parseSchema(InputSource source) {
/* 141 */     checkAbsoluteness(source.getSystemId());
/*     */     try {
/* 143 */       this.forest.parse(source, true);
/* 144 */     } catch (SAXException e) {
/*     */ 
/*     */       
/* 147 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTargetVersion(SpecVersion version) {
/* 152 */     if (version == null)
/* 153 */       version = SpecVersion.LATEST; 
/* 154 */     this.opts.target = version;
/*     */   }
/*     */   
/*     */   public void parseSchema(String systemId, XMLStreamReader reader) throws XMLStreamException {
/* 158 */     checkAbsoluteness(systemId);
/* 159 */     this.forest.parse(systemId, reader, true);
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
/*     */   private void checkAbsoluteness(String systemId) {
/*     */     try {
/* 172 */       new URL(systemId);
/* 173 */     } catch (MalformedURLException mue) {
/*     */       try {
/* 175 */         new URI(systemId);
/* 176 */       } catch (URISyntaxException e) {
/* 177 */         throw new IllegalArgumentException("system ID '" + systemId + "' isn't absolute", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setEntityResolver(EntityResolver entityResolver) {
/* 183 */     this.forest.setEntityResolver(entityResolver);
/* 184 */     this.opts.entityResolver = entityResolver;
/*     */   }
/*     */   
/*     */   public void setDefaultPackageName(String packageName) {
/* 188 */     this.opts.defaultPackage2 = packageName;
/*     */   }
/*     */   
/*     */   public void forcePackageName(String packageName) {
/* 192 */     this.opts.defaultPackage = packageName;
/*     */   }
/*     */   
/*     */   public void setClassNameAllocator(ClassNameAllocator allocator) {
/* 196 */     this.opts.classNameAllocator = allocator;
/*     */   }
/*     */   
/*     */   public void resetSchema() {
/* 200 */     this.forest = new DOMForest((InternalizationLogic)new XMLSchemaInternalizationLogic(), this.opts);
/* 201 */     this.forest.setErrorHandler(this);
/* 202 */     this.forest.setEntityResolver(this.opts.entityResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBModelImpl bind() {
/* 212 */     for (InputSource is : this.opts.getBindFiles()) {
/* 213 */       parseSchema(is);
/*     */     }
/*     */     
/* 216 */     SCDBasedBindingSet scdBasedBindingSet = this.forest.transform(this.opts.isExtensionMode());
/*     */     
/* 218 */     if (!NO_CORRECTNESS_CHECK) {
/*     */       
/* 220 */       SchemaFactory sf = XmlFactory.createSchemaFactory("http://www.w3.org/2001/XMLSchema", this.opts.disableXmlSecurity);
/*     */ 
/*     */ 
/*     */       
/* 224 */       if (this.opts.entityResolver != null) {
/* 225 */         sf.setResourceResolver(new LSResourceResolver()
/*     */             {
/*     */               public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI)
/*     */               {
/*     */                 try {
/* 230 */                   InputSource is = SchemaCompilerImpl.this.opts.entityResolver.resolveEntity(namespaceURI, systemId);
/* 231 */                   if (is == null) return null; 
/* 232 */                   return (LSInput)new LSInputSAXWrapper(is);
/* 233 */                 } catch (SAXException e) {
/*     */                   
/* 235 */                   return null;
/* 236 */                 } catch (IOException e) {
/*     */                   
/* 238 */                   return null;
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 244 */       sf.setErrorHandler(new DowngradingErrorHandler((ErrorHandler)this));
/* 245 */       this.forest.weakSchemaCorrectnessCheck(sf);
/* 246 */       if (this.hadError) {
/* 247 */         return null;
/*     */       }
/*     */     } 
/* 250 */     JCodeModel codeModel = new JCodeModel();
/*     */     
/* 252 */     ModelLoader gl = new ModelLoader(this.opts, codeModel, this);
/*     */     try {
/* 254 */       XSSchemaSet result = gl.createXSOM(this.forest, scdBasedBindingSet);
/* 255 */       if (result == null) {
/* 256 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 262 */       Model model = gl.annotateXMLSchema(result);
/* 263 */       if (model == null) return null;
/*     */       
/* 265 */       if (this.hadError) return null;
/*     */       
/* 267 */       model.setPackageLevelAnnotations(this.opts.packageLevelAnnotations);
/*     */       
/* 269 */       Outline context = model.generateCode(this.opts, this);
/* 270 */       if (context == null) return null;
/*     */       
/* 272 */       if (this.hadError) return null;
/*     */       
/* 274 */       return new JAXBModelImpl(context);
/* 275 */     } catch (SAXException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 283 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setErrorListener(ErrorListener errorListener) {
/* 288 */     this.errorListener = errorListener;
/*     */   }
/*     */   
/*     */   public void info(SAXParseException exception) {
/* 292 */     if (this.errorListener != null)
/* 293 */       this.errorListener.info(exception); 
/*     */   }
/*     */   public void warning(SAXParseException exception) {
/* 296 */     if (this.errorListener != null)
/* 297 */       this.errorListener.warning(exception); 
/*     */   }
/*     */   public void error(SAXParseException exception) {
/* 300 */     this.hadError = true;
/* 301 */     if (this.errorListener != null)
/* 302 */       this.errorListener.error(exception); 
/*     */   }
/*     */   public void fatalError(SAXParseException exception) {
/* 305 */     this.hadError = true;
/* 306 */     if (this.errorListener != null) {
/* 307 */       this.errorListener.fatalError(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean NO_CORRECTNESS_CHECK = false;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 319 */       NO_CORRECTNESS_CHECK = Boolean.getBoolean(SchemaCompilerImpl.class.getName() + ".noCorrectnessCheck");
/* 320 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\impl\s2j\SchemaCompilerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */