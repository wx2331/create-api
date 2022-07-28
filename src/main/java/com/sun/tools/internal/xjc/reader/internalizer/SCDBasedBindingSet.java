/*     */ package com.sun.tools.internal.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.SAXParseException2;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.internal.xjc.util.DOMUtils;
/*     */ import com.sun.tools.internal.xjc.util.ForkContentHandler;
/*     */ import com.sun.xml.internal.xsom.SCD;
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SCDBasedBindingSet
/*     */ {
/*     */   private Target topLevel;
/*     */   private final DOMForest forest;
/*     */   private ErrorReceiver errorReceiver;
/*     */   private UnmarshallerHandler unmarshaller;
/*     */   private ForkContentHandler loader;
/*     */   
/*     */   final class Target
/*     */   {
/*     */     private Target firstChild;
/*     */     private final Target nextSibling;
/*     */     @NotNull
/*     */     private final SCD scd;
/*     */     @NotNull
/*     */     private final Element src;
/* 100 */     private final List<Element> bindings = new ArrayList<>();
/*     */     
/*     */     private Target(Target parent, Element src, SCD scd) {
/* 103 */       if (parent == null) {
/* 104 */         this.nextSibling = SCDBasedBindingSet.this.topLevel;
/* 105 */         SCDBasedBindingSet.this.topLevel = this;
/*     */       } else {
/* 107 */         this.nextSibling = parent.firstChild;
/* 108 */         parent.firstChild = this;
/*     */       } 
/* 110 */       this.src = src;
/* 111 */       this.scd = scd;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void addBinidng(Element binding) {
/* 119 */       this.bindings.add(binding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void applyAll(Collection<? extends XSComponent> contextNode) {
/* 126 */       for (Target self = this; self != null; self = self.nextSibling) {
/* 127 */         self.apply(contextNode);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void apply(Collection<? extends XSComponent> contextNode) {
/* 135 */       Collection<XSComponent> childNodes = this.scd.select(contextNode);
/* 136 */       if (childNodes.isEmpty()) {
/*     */         
/* 138 */         if (this.src.getAttributeNode("if-exists") != null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 143 */         SCDBasedBindingSet.this.reportError(this.src, Messages.format("ERR_SCD_EVALUATED_EMPTY", new Object[] { this.scd }));
/*     */         
/*     */         return;
/*     */       } 
/* 147 */       if (this.firstChild != null) {
/* 148 */         this.firstChild.applyAll(childNodes);
/*     */       }
/* 150 */       if (!this.bindings.isEmpty()) {
/*     */         
/* 152 */         Iterator<XSComponent> itr = childNodes.iterator();
/* 153 */         XSComponent target = itr.next();
/* 154 */         if (itr.hasNext()) {
/* 155 */           SCDBasedBindingSet.this.reportError(this.src, Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES", new Object[] { this.scd, Integer.valueOf(childNodes.size()) }));
/* 156 */           SCDBasedBindingSet.this.errorReceiver.error(target.getLocator(), Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES_FIRST", new Object[0]));
/* 157 */           SCDBasedBindingSet.this.errorReceiver.error(((XSComponent)itr.next()).getLocator(), Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES_SECOND", new Object[0]));
/*     */         } 
/*     */ 
/*     */         
/* 161 */         for (Element binding : this.bindings) {
/* 162 */           for (Element item : DOMUtils.getChildElements(binding)) {
/* 163 */             String localName = item.getLocalName();
/*     */             
/* 165 */             if (!"bindings".equals(localName)) {
/*     */               
/*     */               try {
/*     */                 
/* 169 */                 (new DOMForestScanner(SCDBasedBindingSet.this.forest)).scan(item, (ContentHandler)SCDBasedBindingSet.this.loader);
/* 170 */                 BIDeclaration decl = (BIDeclaration)SCDBasedBindingSet.this.unmarshaller.getResult();
/*     */ 
/*     */                 
/* 173 */                 XSAnnotation ann = target.getAnnotation(true);
/* 174 */                 BindInfo bi = (BindInfo)ann.getAnnotation();
/* 175 */                 if (bi == null) {
/* 176 */                   bi = new BindInfo();
/* 177 */                   ann.setAnnotation(bi);
/*     */                 } 
/* 179 */                 bi.addDecl(decl);
/* 180 */               } catch (SAXException sAXException) {
/*     */               
/* 182 */               } catch (JAXBException e) {
/*     */                 
/* 184 */                 throw new AssertionError(e);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */   SCDBasedBindingSet(DOMForest forest) {
/* 207 */     this.forest = forest;
/*     */   }
/*     */   
/*     */   Target createNewTarget(Target parent, Element src, SCD scd) {
/* 211 */     return new Target(parent, src, scd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(XSSchemaSet schema, ErrorReceiver errorReceiver) {
/* 218 */     if (this.topLevel != null) {
/* 219 */       this.errorReceiver = errorReceiver;
/* 220 */       Unmarshaller u = BindInfo.getCustomizationUnmarshaller();
/* 221 */       this.unmarshaller = u.getUnmarshallerHandler();
/* 222 */       ValidatorHandler v = BindInfo.bindingFileSchema.newValidator();
/* 223 */       v.setErrorHandler((ErrorHandler)errorReceiver);
/* 224 */       this.loader = new ForkContentHandler(v, this.unmarshaller);
/*     */       
/* 226 */       this.topLevel.applyAll(schema.getSchemas());
/*     */       
/* 228 */       this.loader = null;
/* 229 */       this.unmarshaller = null;
/* 230 */       this.errorReceiver = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) {
/* 235 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) {
/* 242 */     SAXParseException e = new SAXParseException2(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */     
/* 244 */     this.errorReceiver.error(e);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\internalizer\SCDBasedBindingSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */