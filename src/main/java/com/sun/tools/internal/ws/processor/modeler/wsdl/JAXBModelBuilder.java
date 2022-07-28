/*     */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.ModelException;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaSimpleType;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBMapping;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBModel;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*     */ import com.sun.tools.internal.ws.processor.util.ClassNameCollector;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.DOMForest;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.DOMForestScanner;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.tools.internal.xjc.api.JAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.SchemaCompiler;
/*     */ import com.sun.tools.internal.xjc.api.TypeAndAnnotation;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class JAXBModelBuilder
/*     */ {
/*     */   private final ErrorReceiver errReceiver;
/*     */   private final WsimportOptions options;
/*     */   private final MetadataFinder forest;
/*     */   private JAXBModel jaxbModel;
/*     */   private SchemaCompiler schemaCompiler;
/*     */   private final ClassNameAllocatorImpl _classNameAllocator;
/*     */   
/*     */   public JAXBModelBuilder(WsimportOptions options, ClassNameCollector classNameCollector, MetadataFinder finder, ErrorReceiver errReceiver) {
/*  61 */     this._classNameAllocator = new ClassNameAllocatorImpl(classNameCollector);
/*  62 */     this.errReceiver = errReceiver;
/*  63 */     this.options = options;
/*  64 */     this.forest = finder;
/*     */     
/*  66 */     internalBuildJAXBModel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void internalBuildJAXBModel() {
/*     */     try {
/*  78 */       this.schemaCompiler = this.options.getSchemaCompiler();
/*  79 */       this.schemaCompiler.resetSchema();
/*  80 */       if (this.options.entityResolver != null)
/*     */       {
/*  82 */         this.schemaCompiler.setEntityResolver(this.options.entityResolver);
/*     */       }
/*  84 */       this.schemaCompiler.setClassNameAllocator(this._classNameAllocator);
/*  85 */       this.schemaCompiler.setErrorListener((ErrorListener)this.errReceiver);
/*  86 */       int schemaElementCount = 1;
/*     */       
/*  88 */       for (Element element : this.forest.getInlinedSchemaElement()) {
/*  89 */         String location = element.getOwnerDocument().getDocumentURI();
/*  90 */         String systemId = location + "#types?schema" + schemaElementCount++;
/*  91 */         if (this.forest.isMexMetadata) {
/*  92 */           this.schemaCompiler.parseSchema(systemId, element); continue;
/*     */         } 
/*  94 */         (new DOMForestScanner((DOMForest)this.forest)).scan(element, this.schemaCompiler.getParserHandler(systemId));
/*     */       } 
/*     */ 
/*     */       
/*  98 */       InputSource[] externalBindings = this.options.getSchemaBindings();
/*  99 */       if (externalBindings != null) {
/* 100 */         for (InputSource jaxbBinding : externalBindings) {
/* 101 */           this.schemaCompiler.parseSchema(jaxbBinding);
/*     */         }
/*     */       }
/* 104 */     } catch (Exception e) {
/* 105 */       throw new ModelException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public JAXBType getJAXBType(QName qname) {
/* 110 */     JAXBMapping mapping = this.jaxbModel.get(qname);
/* 111 */     if (mapping == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     JavaSimpleType javaSimpleType = new JavaSimpleType(mapping.getType());
/* 115 */     return new JAXBType(qname, (JavaType)javaSimpleType, mapping, this.jaxbModel);
/*     */   }
/*     */   
/*     */   public TypeAndAnnotation getElementTypeAndAnn(QName qname) {
/* 119 */     JAXBMapping mapping = this.jaxbModel.get(qname);
/* 120 */     if (mapping == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     return mapping.getType().getTypeAnn();
/*     */   }
/*     */   
/*     */   protected void bind() {
/* 127 */     S2JJAXBModel rawJaxbModel = this.schemaCompiler.bind();
/* 128 */     if (rawJaxbModel == null)
/* 129 */       throw new AbortException(); 
/* 130 */     this.options.setCodeModel(rawJaxbModel.generateCode(null, (ErrorListener)this.errReceiver));
/* 131 */     this.jaxbModel = new JAXBModel((JAXBModel)rawJaxbModel);
/* 132 */     this.jaxbModel.setGeneratedClassNames(this._classNameAllocator.getJaxbGeneratedClasses());
/*     */   }
/*     */   
/*     */   protected SchemaCompiler getJAXBSchemaCompiler() {
/* 136 */     return this.schemaCompiler;
/*     */   }
/*     */   
/*     */   public JAXBModel getJAXBModel() {
/* 140 */     return this.jaxbModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\JAXBModelBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */