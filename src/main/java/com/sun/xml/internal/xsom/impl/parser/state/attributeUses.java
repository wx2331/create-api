/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttributeDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttributeUseImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.Ref;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class attributeUses
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String use;
/*     */   private AttributesHolder owner;
/*     */   private ForeignAttributesImpl fa;
/*     */   private WildcardImpl wildcard;
/*     */   private AnnotationImpl annotation;
/*     */   private UName attDeclName;
/*     */   private AttributeDeclImpl anonymousDecl;
/*     */   private String defaultValue;
/*     */   private String fixedValue;
/*     */   private UName groupName;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Ref.Attribute decl;
/*     */   private Locator wloc;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  63 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public attributeUses(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  67 */     super(source, parent, cookie);
/*  68 */     this.$runtime = runtime;
/*  69 */     this.owner = _owner;
/*  70 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */   
/*     */   public attributeUses(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  74 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  79 */     this.owner.setWildcard(this.wildcard);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  84 */     this.wloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  89 */     this.owner.addAttGroup((Ref.AttGroup)new DelayedRef.AttGroup((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.groupName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  95 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/* 100 */     if ("prohibited".equals(this.use)) {
/* 101 */       this.owner.addProhibitedAttribute(this.attDeclName);
/*     */     } else {
/* 103 */       this.owner.addAttributeUse(this.attDeclName, new AttributeUseImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.decl, this.$runtime
/*     */             
/* 105 */             .createXmlString(this.defaultValue), this.$runtime
/* 106 */             .createXmlString(this.fixedValue), "required"
/* 107 */             .equals(this.use)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void action5() throws SAXException {
/* 113 */     this.decl = (Ref.Attribute)new DelayedRef.Attribute((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.attDeclName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action6() throws SAXException {
/* 120 */     this.decl = (Ref.Attribute)this.anonymousDecl;
/* 121 */     this
/*     */       
/* 123 */       .attDeclName = new UName(this.anonymousDecl.getTargetNamespace(), this.anonymousDecl.getName());
/* 124 */     this.defaultValue = null;
/* 125 */     this.fixedValue = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action7() throws SAXException {
/* 131 */     this.locator = this.$runtime.copyLocator();
/* 132 */     this.use = null;
/* 133 */     this.defaultValue = null;
/* 134 */     this.fixedValue = null;
/* 135 */     this.decl = null;
/* 136 */     this.annotation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 142 */     this.$uri = $__uri;
/* 143 */     this.$localName = $__local;
/* 144 */     this.$qname = $__qname;
/* 145 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 148 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 149 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 150 */           action7();
/* 151 */           this.$_ngcc_current_state = 33;
/*     */         
/*     */         }
/* 154 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 155 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 156 */           action3();
/* 157 */           this.$_ngcc_current_state = 13;
/*     */         
/*     */         }
/* 160 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 161 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 162 */           action1();
/* 163 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 166 */           this.$_ngcc_current_state = 0;
/* 167 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 175 */         action2();
/* 176 */         this.$_ngcc_current_state = 7;
/* 177 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 182 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 183 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 290, this.wloc);
/* 184 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 187 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 193 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 194 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 315, this.locator, true, this.defaultValue, this.fixedValue);
/* 195 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 198 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 199 */           this.$runtime.consumeAttribute($ai);
/* 200 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 203 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 33:
/* 210 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 211 */           this.$runtime.consumeAttribute($ai);
/* 212 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 215 */           this.$_ngcc_current_state = 29;
/* 216 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 222 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 223 */           this.$runtime.consumeAttribute($ai);
/* 224 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 227 */           this.$_ngcc_current_state = 17;
/* 228 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 234 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 235 */           this.$runtime.consumeAttribute($ai);
/* 236 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 239 */           this.$_ngcc_current_state = 25;
/* 240 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 246 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 247 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 297, null, AnnotationContext.ATTRIBUTE_USE);
/* 248 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 251 */           this.$_ngcc_current_state = 8;
/* 252 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 258 */         action4();
/* 259 */         this.$_ngcc_current_state = 15;
/* 260 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 265 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 266 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 267 */           action7();
/* 268 */           this.$_ngcc_current_state = 33;
/*     */         
/*     */         }
/* 271 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 272 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 273 */           action3();
/* 274 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 277 */           this.$_ngcc_current_state = 1;
/* 278 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 13:
/* 285 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 286 */           this.$runtime.consumeAttribute($ai);
/* 287 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 290 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 296 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 297 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 308, null, AnnotationContext.ATTRIBUTE_USE);
/* 298 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 301 */           this.$_ngcc_current_state = 18;
/* 302 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 308 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 313 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 321 */     this.$uri = $__uri;
/* 322 */     this.$localName = $__local;
/* 323 */     this.$qname = $__qname;
/* 324 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 327 */         this.$_ngcc_current_state = 0;
/* 328 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 333 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 334 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 335 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 338 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 344 */         action2();
/* 345 */         this.$_ngcc_current_state = 7;
/* 346 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 351 */         if ((($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/* 352 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 290, this.wloc);
/* 353 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 356 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 362 */         if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/* 363 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 315, this.locator, true, this.defaultValue, this.fixedValue);
/* 364 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 367 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 368 */           this.$runtime.consumeAttribute($ai);
/* 369 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 372 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 33:
/* 379 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 380 */           this.$runtime.consumeAttribute($ai);
/* 381 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 384 */           this.$_ngcc_current_state = 29;
/* 385 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 391 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 392 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 393 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 396 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 402 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 403 */           this.$runtime.consumeAttribute($ai);
/* 404 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 407 */           this.$_ngcc_current_state = 17;
/* 408 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 414 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 415 */           this.$runtime.consumeAttribute($ai);
/* 416 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 419 */           this.$_ngcc_current_state = 25;
/* 420 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 426 */         this.$_ngcc_current_state = 8;
/* 427 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 432 */         action4();
/* 433 */         this.$_ngcc_current_state = 15;
/* 434 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 439 */         this.$_ngcc_current_state = 1;
/* 440 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 445 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 446 */           this.$runtime.consumeAttribute($ai);
/* 447 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 450 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 456 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 457 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 458 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 461 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 467 */         this.$_ngcc_current_state = 18;
/* 468 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 473 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 478 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 479 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 306, null);
/* 480 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 483 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 489 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 497 */     this.$uri = $__uri;
/* 498 */     this.$localName = $__local;
/* 499 */     this.$qname = $__qname;
/* 500 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 503 */         this.$_ngcc_current_state = 0;
/* 504 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 509 */         action2();
/* 510 */         this.$_ngcc_current_state = 7;
/* 511 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 516 */         if (($__uri.equals("") && $__local.equals("namespace")) || ($__uri.equals("") && $__local.equals("processContents"))) {
/* 517 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 290, this.wloc);
/* 518 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 521 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 527 */         if (($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("form"))) {
/* 528 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 315, this.locator, true, this.defaultValue, this.fixedValue);
/* 529 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 532 */         else if ($__uri.equals("") && $__local.equals("ref")) {
/* 533 */           this.$_ngcc_current_state = 22;
/*     */         } else {
/*     */           
/* 536 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 33:
/* 543 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 544 */           this.$_ngcc_current_state = 35;
/*     */         } else {
/*     */           
/* 547 */           this.$_ngcc_current_state = 29;
/* 548 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 554 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 555 */           this.$_ngcc_current_state = 27;
/*     */         } else {
/*     */           
/* 558 */           this.$_ngcc_current_state = 17;
/* 559 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 565 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 566 */           this.$_ngcc_current_state = 31;
/*     */         } else {
/*     */           
/* 569 */           this.$_ngcc_current_state = 25;
/* 570 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 576 */         this.$_ngcc_current_state = 8;
/* 577 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 582 */         action4();
/* 583 */         this.$_ngcc_current_state = 15;
/* 584 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 589 */         this.$_ngcc_current_state = 1;
/* 590 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 595 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 596 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 599 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 605 */         this.$_ngcc_current_state = 18;
/* 606 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 611 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 616 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 624 */     this.$uri = $__uri;
/* 625 */     this.$localName = $__local;
/* 626 */     this.$qname = $__qname;
/* 627 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 630 */         this.$_ngcc_current_state = 0;
/* 631 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 636 */         action2();
/* 637 */         this.$_ngcc_current_state = 7;
/* 638 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 34:
/* 643 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 644 */           this.$_ngcc_current_state = 29;
/*     */         } else {
/*     */           
/* 647 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 653 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 654 */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           
/* 657 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 663 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 664 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 667 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 673 */         this.$_ngcc_current_state = 29;
/* 674 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 679 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 680 */           this.$_ngcc_current_state = 19;
/*     */         } else {
/*     */           
/* 683 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 689 */         this.$_ngcc_current_state = 17;
/* 690 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 695 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 696 */           this.$_ngcc_current_state = 25;
/*     */         } else {
/*     */           
/* 699 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 705 */         this.$_ngcc_current_state = 25;
/* 706 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 711 */         this.$_ngcc_current_state = 8;
/* 712 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 717 */         action4();
/* 718 */         this.$_ngcc_current_state = 15;
/* 719 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 724 */         this.$_ngcc_current_state = 1;
/* 725 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 730 */         this.$_ngcc_current_state = 18;
/* 731 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 736 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 741 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 749 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 31:
/* 752 */         this.defaultValue = $value;
/* 753 */         this.$_ngcc_current_state = 30;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 758 */         this.$_ngcc_current_state = 0;
/* 759 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 764 */         action2();
/* 765 */         this.$_ngcc_current_state = 7;
/* 766 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 771 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 772 */           NGCCHandler nGCCHandler = new wildcardBody(this, this._source, this.$runtime, 290, this.wloc);
/* 773 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 776 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 777 */           NGCCHandler nGCCHandler = new wildcardBody(this, this._source, this.$runtime, 290, this.wloc);
/* 778 */           spawnChildFromText(nGCCHandler, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 17:
/* 785 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 786 */           NGCCHandler nGCCHandler = new attributeDeclBody(this, this._source, this.$runtime, 315, this.locator, true, this.defaultValue, this.fixedValue);
/* 787 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 790 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 791 */           NGCCHandler nGCCHandler = new attributeDeclBody(this, this._source, this.$runtime, 315, this.locator, true, this.defaultValue, this.fixedValue);
/* 792 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 795 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 796 */           this.$runtime.consumeAttribute($ai);
/* 797 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 33:
/* 805 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 806 */           this.$runtime.consumeAttribute($ai);
/* 807 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 810 */         this.$_ngcc_current_state = 29;
/* 811 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 25:
/* 817 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 818 */           this.$runtime.consumeAttribute($ai);
/* 819 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 822 */         this.$_ngcc_current_state = 17;
/* 823 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 22:
/* 829 */         h = new qname(this, this._source, this.$runtime, 311);
/* 830 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 29:
/* 835 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 836 */           this.$runtime.consumeAttribute($ai);
/* 837 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 840 */         this.$_ngcc_current_state = 25;
/* 841 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 12:
/* 847 */         h = new qname(this, this._source, this.$runtime, 300);
/* 848 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 35:
/* 853 */         this.use = $value;
/* 854 */         this.$_ngcc_current_state = 34;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 27:
/* 859 */         this.fixedValue = $value;
/* 860 */         this.$_ngcc_current_state = 26;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 865 */         this.$_ngcc_current_state = 8;
/* 866 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 16:
/* 871 */         action4();
/* 872 */         this.$_ngcc_current_state = 15;
/* 873 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 878 */         this.$_ngcc_current_state = 1;
/* 879 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 884 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 885 */           this.$runtime.consumeAttribute($ai);
/* 886 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 19:
/* 892 */         this.$_ngcc_current_state = 18;
/* 893 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 898 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 905 */     switch ($__cookie__) {
/*     */       
/*     */       case 300:
/* 908 */         this.groupName = (UName)$__result__;
/* 909 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 297:
/* 914 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 306:
/* 919 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 920 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 290:
/* 925 */         this.wildcard = (WildcardImpl)$__result__;
/* 926 */         action0();
/* 927 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 315:
/* 932 */         this.anonymousDecl = (AttributeDeclImpl)$__result__;
/* 933 */         action6();
/* 934 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 311:
/* 939 */         this.attDeclName = (UName)$__result__;
/* 940 */         action5();
/* 941 */         this.$_ngcc_current_state = 21;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 308:
/* 946 */         this.annotation = (AnnotationImpl)$__result__;
/* 947 */         this.$_ngcc_current_state = 18;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 954 */     return (this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 5);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\attributeUses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */