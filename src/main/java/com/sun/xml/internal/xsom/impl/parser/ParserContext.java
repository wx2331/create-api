/*     */ package com.sun.xml.internal.xsom.impl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SchemaSetImpl;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParserFactory;
/*     */ import com.sun.xml.internal.xsom.parser.XMLParser;
/*     */ import com.sun.xml.internal.xsom.parser.XSOMParser;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParserContext
/*     */ {
/*  60 */   public final SchemaSetImpl schemaSet = new SchemaSetImpl();
/*     */ 
/*     */   
/*     */   private final XSOMParser owner;
/*     */   
/*     */   final XMLParser parser;
/*     */   
/*  67 */   private final Vector<Patch> patchers = new Vector<>();
/*  68 */   private final Vector<Patch> errorCheckers = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public final Map<SchemaDocumentImpl, SchemaDocumentImpl> parsedDocuments = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hadError;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final PatcherManager patcherManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ErrorHandler errorHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   final ErrorHandler noopHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 102 */     return this.owner.getEntityResolver();
/*     */   }
/*     */   
/*     */   public AnnotationParserFactory getAnnotationParserFactory() {
/* 106 */     return this.owner.getAnnotationParserFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException {
/* 113 */     newNGCCRuntime().parseEntity(source, false, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XSSchemaSet getResult() throws SAXException {
/* 119 */     for (Patch patcher : this.patchers)
/* 120 */       patcher.run(); 
/* 121 */     this.patchers.clear();
/*     */ 
/*     */     
/* 124 */     Iterator<ElementDecl> itr = this.schemaSet.iterateElementDecls();
/* 125 */     while (itr.hasNext()) {
/* 126 */       ((ElementDecl)itr.next()).updateSubstitutabilityMap();
/*     */     }
/*     */     
/* 129 */     for (Patch patcher : this.errorCheckers)
/* 130 */       patcher.run(); 
/* 131 */     this.errorCheckers.clear();
/*     */ 
/*     */     
/* 134 */     if (this.hadError) return null; 
/* 135 */     return (XSSchemaSet)this.schemaSet;
/*     */   }
/*     */   
/*     */   public NGCCRuntimeEx newNGCCRuntime() {
/* 139 */     return new NGCCRuntimeEx(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserContext(XSOMParser owner, XMLParser parser) {
/* 145 */     this.hadError = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     this.patcherManager = new PatcherManager() {
/*     */         public void addPatcher(Patch patch) {
/* 156 */           ParserContext.this.patchers.add(patch);
/*     */         }
/*     */         public void addErrorChecker(Patch patch) {
/* 159 */           ParserContext.this.errorCheckers.add(patch);
/*     */         }
/*     */         
/*     */         public void reportError(String msg, Locator src) throws SAXException {
/* 163 */           ParserContext.this.setErrorFlag();
/*     */           
/* 165 */           SAXParseException e = new SAXParseException(msg, src);
/* 166 */           if (ParserContext.this.errorHandler == null) {
/* 167 */             throw e;
/*     */           }
/* 169 */           ParserContext.this.errorHandler.error(e);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     this.errorHandler = new ErrorHandler() {
/*     */         private ErrorHandler getErrorHandler() {
/* 179 */           if (ParserContext.this.owner.getErrorHandler() == null) {
/* 180 */             return ParserContext.this.noopHandler;
/*     */           }
/* 182 */           return ParserContext.this.owner.getErrorHandler();
/*     */         }
/*     */         
/*     */         public void warning(SAXParseException e) throws SAXException {
/* 186 */           getErrorHandler().warning(e);
/*     */         }
/*     */         
/*     */         public void error(SAXParseException e) throws SAXException {
/* 190 */           ParserContext.this.setErrorFlag();
/* 191 */           getErrorHandler().error(e);
/*     */         }
/*     */         
/*     */         public void fatalError(SAXParseException e) throws SAXException {
/* 195 */           ParserContext.this.setErrorFlag();
/* 196 */           getErrorHandler().fatalError(e);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     this.noopHandler = new ErrorHandler() {
/*     */         public void warning(SAXParseException e) {}
/*     */         
/*     */         public void error(SAXParseException e) {}
/*     */         
/*     */         public void fatalError(SAXParseException e) {
/* 209 */           ParserContext.this.setErrorFlag();
/*     */         }
/*     */       };
/*     */     this.owner = owner;
/*     */     this.parser = parser;
/*     */     try {
/*     */       parse(new InputSource(ParserContext.class.getResource("datatypes.xsd").toExternalForm()));
/*     */       SchemaImpl xs = (SchemaImpl)this.schemaSet.getSchema("http://www.w3.org/2001/XMLSchema");
/*     */       xs.addSimpleType((XSSimpleType)this.schemaSet.anySimpleType, true);
/*     */       xs.addComplexType((XSComplexType)this.schemaSet.anyType, true);
/*     */     } catch (SAXException e) {
/*     */       if (e.getException() != null) {
/*     */         e.getException().printStackTrace();
/*     */       } else {
/*     */         e.printStackTrace();
/*     */       } 
/*     */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */   
/*     */   void setErrorFlag() {
/*     */     this.hadError = true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\ParserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */