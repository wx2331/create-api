/*      */ package com.sun.xml.internal.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*      */ import com.sun.xml.internal.xsom.XSComplexType;
/*      */ import com.sun.xml.internal.xsom.XSContentType;
/*      */ import com.sun.xml.internal.xsom.XSFacet;
/*      */ import com.sun.xml.internal.xsom.XSModelGroup;
/*      */ import com.sun.xml.internal.xsom.XSSimpleType;
/*      */ import com.sun.xml.internal.xsom.XSType;
/*      */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.internal.xsom.impl.AttributesHolder;
/*      */ import com.sun.xml.internal.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ContentTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ParticleImpl;
/*      */ import com.sun.xml.internal.xsom.impl.Ref;
/*      */ import com.sun.xml.internal.xsom.impl.RestrictionSimpleTypeImpl;
/*      */ import com.sun.xml.internal.xsom.impl.UName;
/*      */ import com.sun.xml.internal.xsom.impl.parser.BaseContentRef;
/*      */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*      */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*      */ import com.sun.xml.internal.xsom.impl.parser.SchemaDocumentImpl;
/*      */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*      */ import java.util.Collections;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ class complexType
/*      */   extends NGCCHandler
/*      */ {
/*      */   private Integer finalValue;
/*      */   private String name;
/*      */   private String abstractValue;
/*      */   private Integer blockValue;
/*      */   private XSFacet facet;
/*      */   private ForeignAttributesImpl fa;
/*      */   private AnnotationImpl annotation;
/*      */   private ContentTypeImpl explicitContent;
/*      */   private UName baseTypeName;
/*      */   private String mixedValue;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   private int $_ngcc_current_state;
/*      */   protected String $uri;
/*      */   protected String $localName;
/*      */   protected String $qname;
/*      */   private ComplexTypeImpl result;
/*      */   private Ref.Type baseType;
/*      */   private Ref.ContentType contentType;
/*      */   private Ref.SimpleType baseContentType;
/*      */   private RestrictionSimpleTypeImpl contentSimpleType;
/*      */   private Locator locator;
/*      */   private Locator locator2;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   58 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   
/*      */   public complexType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*   62 */     super(source, parent, cookie);
/*   63 */     this.$runtime = runtime;
/*   64 */     this.$_ngcc_current_state = 88;
/*      */   }
/*      */   
/*      */   public complexType(NGCCRuntimeEx runtime) {
/*   68 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action0() throws SAXException {
/*   73 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action1() throws SAXException {
/*   79 */     this.baseType = (Ref.Type)this.$runtime.parser.schemaSet.anyType;
/*   80 */     makeResult(2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action2() throws SAXException {
/*   86 */     this.result.setExplicitContent((XSContentType)this.explicitContent);
/*   87 */     this.result.setContentType(
/*   88 */         buildComplexExtensionContentModel((XSContentType)this.explicitContent));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action3() throws SAXException {
/*   94 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*   96 */     makeResult(1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action4() throws SAXException {
/*  101 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action5() throws SAXException {
/*  106 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action6() throws SAXException {
/*  112 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*  114 */     makeResult(2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action7() throws SAXException {
/*  119 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action8() throws SAXException {
/*  124 */     this.contentType = (Ref.ContentType)new BaseContentRef(this.$runtime, this.baseType);
/*  125 */     makeResult(1);
/*  126 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action9() throws SAXException {
/*  132 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action10() throws SAXException {
/*  138 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action11() throws SAXException {
/*  143 */     makeResult(2);
/*  144 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action12() throws SAXException {
/*  149 */     this.contentSimpleType.addFacet(this.facet);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action13() throws SAXException {
/*  154 */     if (this.baseContentType == null)
/*      */     {
/*  156 */       this.baseContentType = new BaseContentSimpleTypeRef(this.baseType);
/*      */     }
/*      */     
/*  159 */     this.contentSimpleType = new RestrictionSimpleTypeImpl(this.$runtime.document, null, this.locator2, null, null, true, Collections.EMPTY_SET, this.baseContentType);
/*      */ 
/*      */     
/*  162 */     this.contentType = (Ref.ContentType)this.contentSimpleType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action14() throws SAXException {
/*  168 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action15() throws SAXException {
/*  174 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   private void action16() throws SAXException {
/*  178 */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*  183 */     this.$uri = $__uri;
/*  184 */     this.$localName = $__local;
/*  185 */     this.$qname = $__qname;
/*  186 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 54:
/*  189 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  190 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 617, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  191 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  194 */           this.$_ngcc_current_state = 52;
/*  195 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/*  201 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  202 */           this.$runtime.consumeAttribute($ai);
/*  203 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  206 */           this.$_ngcc_current_state = 72;
/*  207 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/*  213 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  214 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 610);
/*  215 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  218 */           this.$_ngcc_current_state = 48;
/*  219 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 7:
/*  225 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  226 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  227 */           action7();
/*  228 */           this.$_ngcc_current_state = 24;
/*      */         
/*      */         }
/*  231 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  232 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  233 */           action4();
/*  234 */           this.$_ngcc_current_state = 15;
/*      */         } else {
/*      */           
/*  237 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 61:
/*  244 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  245 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 626, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  246 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  249 */           this.$_ngcc_current_state = 35;
/*  250 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  256 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  257 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 571, (AttributesHolder)this.result);
/*  258 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  261 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 12:
/*  267 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  268 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 564, this.fa);
/*  269 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  272 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  278 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  279 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 582, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  280 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  283 */           this.$_ngcc_current_state = 7;
/*  284 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/*  290 */         action8();
/*  291 */         this.$_ngcc_current_state = 37;
/*  292 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  297 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  298 */           this.$runtime.consumeAttribute($ai);
/*  299 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  302 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/*  308 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  309 */           this.$runtime.consumeAttribute($ai);
/*  310 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  313 */           this.$_ngcc_current_state = 67;
/*  314 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/*  320 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  321 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  322 */           action15();
/*  323 */           this.$_ngcc_current_state = 59;
/*      */         
/*      */         }
/*  326 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  327 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  328 */           action10();
/*  329 */           this.$_ngcc_current_state = 44;
/*      */         } else {
/*      */           
/*  332 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 80:
/*  339 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  340 */           this.$runtime.consumeAttribute($ai);
/*  341 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  344 */           this.$_ngcc_current_state = 76;
/*  345 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  351 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension"))) {
/*  352 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 628, this.fa);
/*  353 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  356 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 88:
/*  362 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  363 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  364 */           action16();
/*  365 */           this.$_ngcc_current_state = 84;
/*      */         } else {
/*      */           
/*  368 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/*  374 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  375 */           this.$runtime.consumeAttribute($ai);
/*  376 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  379 */           this.$_ngcc_current_state = 80;
/*  380 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 37:
/*  386 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  387 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 594, (AttributesHolder)this.result);
/*  388 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  391 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  397 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  398 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 560, (AttributesHolder)this.result);
/*  399 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  402 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/*  408 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  409 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 573, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  410 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  413 */           this.$_ngcc_current_state = 18;
/*  414 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/*  420 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  421 */           this.$runtime.consumeAttribute($ai);
/*  422 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  425 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  431 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  432 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 609);
/*  433 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  436 */           action11();
/*  437 */           this.$_ngcc_current_state = 47;
/*  438 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  444 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  445 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 606, (AttributesHolder)this.result);
/*  446 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  449 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  455 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  456 */           this.$runtime.consumeAttribute($ai);
/*  457 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  460 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  466 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction"))) {
/*  467 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 584, this.fa);
/*  468 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  471 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/*  477 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  478 */           this.$runtime.consumeAttribute($ai);
/*  479 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  482 */           this.$_ngcc_current_state = 28;
/*  483 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 67:
/*  489 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  490 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 636, this.fa);
/*  491 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  494 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  500 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  501 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 562, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  502 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  505 */           this.$_ngcc_current_state = 9;
/*  506 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 41:
/*  512 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  513 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 599, this.fa);
/*  514 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  517 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  523 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  524 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  525 */           this.$_ngcc_current_state = 63;
/*      */         
/*      */         }
/*  528 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  529 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  530 */           this.$_ngcc_current_state = 29;
/*      */         
/*      */         }
/*  533 */         else if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  534 */           action1();
/*  535 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 557, (AttributesHolder)this.result);
/*  536 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  539 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 21:
/*  547 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  548 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 575, this.fa);
/*  549 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  552 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/*  558 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  559 */           this.$runtime.consumeAttribute($ai);
/*  560 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  563 */           this.$_ngcc_current_state = 68;
/*  564 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  570 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  571 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 619, this.fa);
/*  572 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  575 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/*  581 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  582 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 597, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  583 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  586 */           this.$_ngcc_current_state = 38;
/*  587 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  593 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  594 */           this.$runtime.consumeAttribute($ai);
/*  595 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  598 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/*  604 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  605 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 614);
/*  606 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  609 */           this.$_ngcc_current_state = 51;
/*  610 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  616 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/*  621 */         action13();
/*  622 */         this.$_ngcc_current_state = 49;
/*  623 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/*  628 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  629 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 634, null, AnnotationContext.COMPLEXTYPE_DECL);
/*  630 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  633 */           this.$_ngcc_current_state = 2;
/*  634 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  640 */     unexpectedEnterElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*  648 */     this.$uri = $__uri;
/*  649 */     this.$localName = $__local;
/*  650 */     this.$qname = $__qname;
/*  651 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 54:
/*  654 */         this.$_ngcc_current_state = 52;
/*  655 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/*  660 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  661 */           this.$runtime.consumeAttribute($ai);
/*  662 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  665 */           this.$_ngcc_current_state = 72;
/*  666 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/*  672 */         this.$_ngcc_current_state = 48;
/*  673 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 6:
/*  678 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  679 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  680 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  683 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/*  689 */         this.$_ngcc_current_state = 35;
/*  690 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 46:
/*  695 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  696 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  697 */           this.$_ngcc_current_state = 34;
/*      */         } else {
/*      */           
/*  700 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  706 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  707 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  708 */           this.$_ngcc_current_state = 34;
/*      */         } else {
/*      */           
/*  711 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  717 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  718 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 571, (AttributesHolder)this.result);
/*  719 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  722 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 12:
/*  728 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  729 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 564, this.fa);
/*  730 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  733 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  739 */         this.$_ngcc_current_state = 7;
/*  740 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 34:
/*  745 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  746 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  747 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  750 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/*  756 */         action8();
/*  757 */         this.$_ngcc_current_state = 37;
/*  758 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  763 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  764 */           this.$runtime.consumeAttribute($ai);
/*  765 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  768 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/*  774 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  775 */           this.$runtime.consumeAttribute($ai);
/*  776 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  779 */           this.$_ngcc_current_state = 67;
/*  780 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  786 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  787 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  788 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  791 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/*  797 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  798 */           this.$runtime.consumeAttribute($ai);
/*  799 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  802 */           this.$_ngcc_current_state = 76;
/*  803 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 37:
/*  809 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  810 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 594, (AttributesHolder)this.result);
/*  811 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  814 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/*  820 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  821 */           this.$runtime.consumeAttribute($ai);
/*  822 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  825 */           this.$_ngcc_current_state = 80;
/*  826 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  832 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  833 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 560, (AttributesHolder)this.result);
/*  834 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  837 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/*  843 */         this.$_ngcc_current_state = 18;
/*  844 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/*  849 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  850 */           this.$runtime.consumeAttribute($ai);
/*  851 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  854 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  860 */         action11();
/*  861 */         this.$_ngcc_current_state = 47;
/*  862 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  867 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  868 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 606, (AttributesHolder)this.result);
/*  869 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  872 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  878 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  879 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  880 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  883 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  889 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  890 */           this.$runtime.consumeAttribute($ai);
/*  891 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  894 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/*  900 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  901 */           this.$runtime.consumeAttribute($ai);
/*  902 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  905 */           this.$_ngcc_current_state = 28;
/*  906 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 67:
/*  912 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  913 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 636, this.fa);
/*  914 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  917 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  923 */         this.$_ngcc_current_state = 9;
/*  924 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 41:
/*  929 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  930 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 599, this.fa);
/*  931 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  934 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  940 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  941 */           action1();
/*  942 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 557, (AttributesHolder)this.result);
/*  943 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  946 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  952 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  953 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 575, this.fa);
/*  954 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  957 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/*  963 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  964 */           this.$runtime.consumeAttribute($ai);
/*  965 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  968 */           this.$_ngcc_current_state = 68;
/*  969 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  975 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  976 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 619, this.fa);
/*  977 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  980 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/*  986 */         this.$_ngcc_current_state = 38;
/*  987 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  992 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  993 */           this.$runtime.consumeAttribute($ai);
/*  994 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  997 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1003 */         this.$_ngcc_current_state = 51;
/* 1004 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/* 1009 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 1010 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 1011 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/* 1014 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1020 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/* 1025 */         action13();
/* 1026 */         this.$_ngcc_current_state = 49;
/* 1027 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1032 */         this.$_ngcc_current_state = 2;
/* 1033 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1038 */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 1046 */     this.$uri = $__uri;
/* 1047 */     this.$localName = $__local;
/* 1048 */     this.$qname = $__qname;
/* 1049 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 29:
/* 1052 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1053 */           this.$_ngcc_current_state = 31;
/*      */         } else {
/*      */           
/* 1056 */           this.$_ngcc_current_state = 28;
/* 1057 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/* 1063 */         this.$_ngcc_current_state = 52;
/* 1064 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1069 */         this.$_ngcc_current_state = 9;
/* 1070 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/* 1075 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 1076 */           this.$_ngcc_current_state = 78;
/*      */         } else {
/*      */           
/* 1079 */           this.$_ngcc_current_state = 72;
/* 1080 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/* 1086 */         this.$_ngcc_current_state = 48;
/* 1087 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1092 */         this.$_ngcc_current_state = 35;
/* 1093 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/* 1098 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1099 */           this.$_ngcc_current_state = 74;
/*      */         } else {
/*      */           
/* 1102 */           this.$_ngcc_current_state = 68;
/* 1103 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/* 1109 */         this.$_ngcc_current_state = 38;
/* 1110 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/* 1115 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1116 */           this.$_ngcc_current_state = 58;
/*      */         } else {
/*      */           
/* 1119 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/* 1125 */         this.$_ngcc_current_state = 7;
/* 1126 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/* 1131 */         action8();
/* 1132 */         this.$_ngcc_current_state = 37;
/* 1133 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/* 1138 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1139 */           this.$_ngcc_current_state = 43;
/*      */         } else {
/*      */           
/* 1142 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/* 1148 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1149 */           this.$_ngcc_current_state = 70;
/*      */         } else {
/*      */           
/* 1152 */           this.$_ngcc_current_state = 67;
/* 1153 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1159 */         this.$_ngcc_current_state = 51;
/* 1160 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1165 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/* 1170 */         action13();
/* 1171 */         this.$_ngcc_current_state = 49;
/* 1172 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/* 1177 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1178 */           this.$_ngcc_current_state = 82;
/*      */         } else {
/*      */           
/* 1181 */           this.$_ngcc_current_state = 76;
/* 1182 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/* 1188 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/* 1189 */           this.$_ngcc_current_state = 86;
/*      */         } else {
/*      */           
/* 1192 */           this.$_ngcc_current_state = 80;
/* 1193 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/* 1199 */         this.$_ngcc_current_state = 18;
/* 1200 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/* 1205 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1206 */           this.$_ngcc_current_state = 14;
/*      */         } else {
/*      */           
/* 1209 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1215 */         this.$_ngcc_current_state = 2;
/* 1216 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1221 */         action11();
/* 1222 */         this.$_ngcc_current_state = 47;
/* 1223 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/* 1228 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1229 */           this.$_ngcc_current_state = 23;
/*      */         } else {
/*      */           
/* 1232 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1238 */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 1246 */     this.$uri = $__uri;
/* 1247 */     this.$localName = $__local;
/* 1248 */     this.$qname = $__qname;
/* 1249 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 54:
/* 1252 */         this.$_ngcc_current_state = 52;
/* 1253 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/* 1258 */         this.$_ngcc_current_state = 72;
/* 1259 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/* 1264 */         this.$_ngcc_current_state = 48;
/* 1265 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 30:
/* 1270 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1271 */           this.$_ngcc_current_state = 28;
/*      */         } else {
/*      */           
/* 1274 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/* 1280 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1281 */           this.$_ngcc_current_state = 68;
/*      */         } else {
/*      */           
/* 1284 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1290 */         this.$_ngcc_current_state = 35;
/* 1291 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/* 1296 */         this.$_ngcc_current_state = 7;
/* 1297 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/* 1302 */         action8();
/* 1303 */         this.$_ngcc_current_state = 37;
/* 1304 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/* 1309 */         this.$_ngcc_current_state = 67;
/* 1310 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/* 1315 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1316 */           this.$_ngcc_current_state = 12;
/*      */         } else {
/*      */           
/* 1319 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 85:
/* 1325 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/* 1326 */           this.$_ngcc_current_state = 80;
/*      */         } else {
/*      */           
/* 1329 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/* 1335 */         this.$_ngcc_current_state = 76;
/* 1336 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/* 1341 */         this.$_ngcc_current_state = 80;
/* 1342 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/* 1347 */         this.$_ngcc_current_state = 18;
/* 1348 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1353 */         action11();
/* 1354 */         this.$_ngcc_current_state = 47;
/* 1355 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/* 1360 */         this.$_ngcc_current_state = 28;
/* 1361 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1366 */         this.$_ngcc_current_state = 9;
/* 1367 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/* 1372 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 1373 */           this.$_ngcc_current_state = 72;
/*      */         } else {
/*      */           
/* 1376 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/* 1382 */         this.$_ngcc_current_state = 68;
/* 1383 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/* 1388 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1389 */           this.$_ngcc_current_state = 67;
/*      */         } else {
/*      */           
/* 1392 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/* 1398 */         this.$_ngcc_current_state = 38;
/* 1399 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 22:
/* 1404 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1405 */           this.$_ngcc_current_state = 21;
/*      */         } else {
/*      */           
/* 1408 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 81:
/* 1414 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1415 */           this.$_ngcc_current_state = 76;
/*      */         } else {
/*      */           
/* 1418 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 42:
/* 1424 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1425 */           this.$_ngcc_current_state = 41;
/*      */         } else {
/*      */           
/* 1428 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1434 */         this.$_ngcc_current_state = 51;
/* 1435 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1440 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/* 1445 */         action13();
/* 1446 */         this.$_ngcc_current_state = 49;
/* 1447 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 57:
/* 1452 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1453 */           this.$_ngcc_current_state = 56;
/*      */         } else {
/*      */           
/* 1456 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1462 */         this.$_ngcc_current_state = 2;
/* 1463 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1468 */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     NGCCHandler h;
/* 1476 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 58:
/* 1479 */         h = new qname(this, this._source, this.$runtime, 621);
/* 1480 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 54:
/* 1485 */         this.$_ngcc_current_state = 52;
/* 1486 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 31:
/* 1491 */         this.mixedValue = $value;
/* 1492 */         this.$_ngcc_current_state = 30;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 76:
/* 1497 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1498 */           this.$runtime.consumeAttribute($ai);
/* 1499 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1502 */         this.$_ngcc_current_state = 72;
/* 1503 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 49:
/* 1509 */         this.$_ngcc_current_state = 48;
/* 1510 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 61:
/* 1515 */         this.$_ngcc_current_state = 35;
/* 1516 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 26:
/* 1521 */         this.$_ngcc_current_state = 7;
/* 1522 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 38:
/* 1527 */         action8();
/* 1528 */         this.$_ngcc_current_state = 37;
/* 1529 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 44:
/* 1534 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1535 */           this.$runtime.consumeAttribute($ai);
/* 1536 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 68:
/* 1542 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1543 */           this.$runtime.consumeAttribute($ai);
/* 1544 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1547 */         this.$_ngcc_current_state = 67;
/* 1548 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 80:
/* 1554 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 1555 */           this.$runtime.consumeAttribute($ai);
/* 1556 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1559 */         this.$_ngcc_current_state = 76;
/* 1560 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 84:
/* 1566 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 1567 */           this.$runtime.consumeAttribute($ai);
/* 1568 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1571 */         this.$_ngcc_current_state = 80;
/* 1572 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 19:
/* 1578 */         this.$_ngcc_current_state = 18;
/* 1579 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 15:
/* 1584 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1585 */           this.$runtime.consumeAttribute($ai);
/* 1586 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 86:
/* 1592 */         this.abstractValue = $value;
/* 1593 */         this.$_ngcc_current_state = 85;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 48:
/* 1598 */         action11();
/* 1599 */         this.$_ngcc_current_state = 47;
/* 1600 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 24:
/* 1605 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1606 */           this.$runtime.consumeAttribute($ai);
/* 1607 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 29:
/* 1613 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1614 */           this.$runtime.consumeAttribute($ai);
/* 1615 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1618 */         this.$_ngcc_current_state = 28;
/* 1619 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 10:
/* 1625 */         this.$_ngcc_current_state = 9;
/* 1626 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 72:
/* 1631 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1632 */           this.$runtime.consumeAttribute($ai);
/* 1633 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1636 */         this.$_ngcc_current_state = 68;
/* 1637 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 43:
/* 1643 */         h = new qname(this, this._source, this.$runtime, 601);
/* 1644 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 39:
/* 1649 */         this.$_ngcc_current_state = 38;
/* 1650 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 59:
/* 1655 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1656 */           this.$runtime.consumeAttribute($ai);
/* 1657 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 23:
/* 1663 */         h = new qname(this, this._source, this.$runtime, 577);
/* 1664 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 52:
/* 1669 */         this.$_ngcc_current_state = 51;
/* 1670 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 78:
/* 1675 */         h = new erSet(this, this._source, this.$runtime, 648);
/* 1676 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 70:
/* 1681 */         this.name = WhiteSpaceProcessor.collapse($value);
/* 1682 */         this.$_ngcc_current_state = 69;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 82:
/* 1687 */         h = new erSet(this, this._source, this.$runtime, 653);
/* 1688 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 0:
/* 1693 */         revertToParentFromText(this.result, this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 51:
/* 1698 */         action13();
/* 1699 */         this.$_ngcc_current_state = 49;
/* 1700 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 65:
/* 1705 */         this.$_ngcc_current_state = 2;
/* 1706 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 74:
/* 1711 */         this.mixedValue = $value;
/* 1712 */         this.$_ngcc_current_state = 73;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 14:
/* 1717 */         h = new qname(this, this._source, this.$runtime, 566);
/* 1718 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 1725 */     switch ($__cookie__) {
/*      */       
/*      */       case 573:
/* 1728 */         this.annotation = (AnnotationImpl)$__result__;
/* 1729 */         this.$_ngcc_current_state = 18;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 636:
/* 1734 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1735 */         this.$_ngcc_current_state = 65;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 562:
/* 1740 */         this.annotation = (AnnotationImpl)$__result__;
/* 1741 */         this.$_ngcc_current_state = 9;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 577:
/* 1746 */         this.baseTypeName = (UName)$__result__;
/* 1747 */         action6();
/* 1748 */         this.$_ngcc_current_state = 22;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 648:
/* 1753 */         this.finalValue = (Integer)$__result__;
/* 1754 */         this.$_ngcc_current_state = 77;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 614:
/* 1759 */         this.baseContentType = (Ref.SimpleType)$__result__;
/* 1760 */         this.$_ngcc_current_state = 51;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 653:
/* 1765 */         this.blockValue = (Integer)$__result__;
/* 1766 */         this.$_ngcc_current_state = 81;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 566:
/* 1771 */         this.baseTypeName = (UName)$__result__;
/* 1772 */         action3();
/* 1773 */         this.$_ngcc_current_state = 13;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 621:
/* 1778 */         this.baseTypeName = (UName)$__result__;
/* 1779 */         action14();
/* 1780 */         this.$_ngcc_current_state = 57;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 617:
/* 1785 */         this.annotation = (AnnotationImpl)$__result__;
/* 1786 */         this.$_ngcc_current_state = 52;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 610:
/* 1791 */         this.facet = (XSFacet)$__result__;
/* 1792 */         action12();
/* 1793 */         this.$_ngcc_current_state = 48;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 626:
/* 1798 */         this.annotation = (AnnotationImpl)$__result__;
/* 1799 */         this.$_ngcc_current_state = 35;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 571:
/* 1804 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1805 */         action5();
/* 1806 */         this.$_ngcc_current_state = 17;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 564:
/* 1811 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1812 */         this.$_ngcc_current_state = 10;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 582:
/* 1817 */         this.annotation = (AnnotationImpl)$__result__;
/* 1818 */         this.$_ngcc_current_state = 7;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 628:
/* 1823 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1824 */         this.$_ngcc_current_state = 61;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 594:
/* 1829 */         this.$_ngcc_current_state = 36;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 560:
/* 1834 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1835 */         action2();
/* 1836 */         this.$_ngcc_current_state = 8;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 606:
/* 1841 */         this.$_ngcc_current_state = 46;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 609:
/* 1846 */         this.facet = (XSFacet)$__result__;
/* 1847 */         action12();
/* 1848 */         this.$_ngcc_current_state = 48;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 584:
/* 1853 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1854 */         this.$_ngcc_current_state = 26;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 599:
/* 1859 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1860 */         this.$_ngcc_current_state = 39;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 557:
/* 1865 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1866 */         action0();
/* 1867 */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 575:
/* 1872 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1873 */         this.$_ngcc_current_state = 19;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 601:
/* 1878 */         this.baseTypeName = (UName)$__result__;
/* 1879 */         action9();
/* 1880 */         this.$_ngcc_current_state = 42;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 619:
/* 1885 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1886 */         this.$_ngcc_current_state = 54;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 597:
/* 1891 */         this.annotation = (AnnotationImpl)$__result__;
/* 1892 */         this.$_ngcc_current_state = 38;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 634:
/* 1897 */         this.annotation = (AnnotationImpl)$__result__;
/* 1898 */         this.$_ngcc_current_state = 2;
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean accepted() {
/* 1905 */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class BaseContentSimpleTypeRef
/*      */     implements Ref.SimpleType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private BaseContentSimpleTypeRef(Ref.Type _baseType) {
/* 1921 */       this.baseType = _baseType;
/*      */     } public XSSimpleType getType() {
/* 1923 */       return (XSSimpleType)((XSComplexType)this.baseType.getType()).getContentType();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeResult(int derivationMethod) {
/* 1931 */     if (this.finalValue == null)
/* 1932 */       this.finalValue = Integer.valueOf(this.$runtime.finalDefault); 
/* 1933 */     if (this.blockValue == null) {
/* 1934 */       this.blockValue = Integer.valueOf(this.$runtime.blockDefault);
/*      */     }
/* 1936 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1948 */       .result = new ComplexTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, (this.name == null), this.$runtime.parseBoolean(this.abstractValue), derivationMethod, this.baseType, this.finalValue.intValue(), this.blockValue.intValue(), this.$runtime.parseBoolean(this.mixedValue));
/*      */   }
/*      */   
/*      */   private static class BaseComplexTypeContentRef
/*      */     implements Ref.ContentType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */     
/*      */     private BaseComplexTypeContentRef(Ref.Type _baseType) {
/* 1957 */       this.baseType = _baseType;
/*      */     } public XSContentType getContentType() {
/* 1959 */       return ((XSComplexType)this.baseType.getType()).getContentType();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class InheritBaseContentTypeRef implements Ref.ContentType {
/*      */     private final Ref.Type baseType;
/*      */     private final XSContentType empty;
/*      */     private final XSContentType expContent;
/*      */     private final SchemaDocumentImpl currentDocument;
/*      */     
/*      */     private InheritBaseContentTypeRef(Ref.Type _baseType, XSContentType _explicitContent, NGCCRuntimeEx $runtime) {
/* 1970 */       this.baseType = _baseType;
/* 1971 */       this.currentDocument = $runtime.document;
/* 1972 */       this.expContent = _explicitContent;
/* 1973 */       this.empty = (XSContentType)$runtime.parser.schemaSet.empty;
/*      */     }
/*      */     
/*      */     public XSContentType getContentType() {
/* 1977 */       XSContentType baseContentType = ((XSComplexType)this.baseType.getType()).getContentType();
/* 1978 */       if (baseContentType == this.empty) {
/* 1979 */         return this.expContent;
/*      */       }
/* 1981 */       return (XSContentType)new ParticleImpl(this.currentDocument, null, (Ref.Term)new ModelGroupImpl(this.currentDocument, null, null, null, XSModelGroup.SEQUENCE, new ParticleImpl[] { (ParticleImpl)baseContentType, (ParticleImpl)this.expContent }), null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Ref.ContentType buildComplexExtensionContentModel(XSContentType explicitContent) {
/* 1992 */     if (explicitContent == this.$runtime.parser.schemaSet.empty) {
/* 1993 */       return new BaseComplexTypeContentRef(this.baseType);
/*      */     }
/* 1995 */     return new InheritBaseContentTypeRef(this.baseType, explicitContent, this.$runtime);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\complexType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */