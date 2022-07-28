/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
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
/*     */ class attributeGroupDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private AttGroupDeclImpl result;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  48 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public attributeGroupDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  52 */     super(source, parent, cookie);
/*  53 */     this.$runtime = runtime;
/*  54 */     this.$_ngcc_current_state = 14;
/*     */   }
/*     */   
/*     */   public attributeGroupDecl(NGCCRuntimeEx runtime) {
/*  58 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  63 */     this.result = new AttGroupDeclImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  69 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  74 */     this.$uri = $__uri;
/*  75 */     this.$localName = $__local;
/*  76 */     this.$qname = $__qname;
/*  77 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/*  80 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  81 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 246, this.fa);
/*  82 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/*  91 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  92 */           this.$runtime.consumeAttribute($ai);
/*  93 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  96 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 102 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 107 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 108 */           this.$runtime.consumeAttribute($ai);
/* 109 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 112 */           this.$_ngcc_current_state = 6;
/* 113 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 119 */         action0();
/* 120 */         this.$_ngcc_current_state = 2;
/* 121 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 126 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/* 127 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 241, (AttributesHolder)this.result);
/* 128 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 131 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 137 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 138 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 139 */           action1();
/* 140 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 143 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 149 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 150 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 244, null, AnnotationContext.ATTRIBUTE_GROUP);
/* 151 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 154 */           this.$_ngcc_current_state = 3;
/* 155 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 161 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 169 */     this.$uri = $__uri;
/* 170 */     this.$localName = $__local;
/* 171 */     this.$qname = $__qname;
/* 172 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 175 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 176 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 246, this.fa);
/* 177 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 180 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 186 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 187 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 188 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 191 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 197 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 198 */           this.$runtime.consumeAttribute($ai);
/* 199 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 202 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 208 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 213 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 214 */           this.$runtime.consumeAttribute($ai);
/* 215 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 218 */           this.$_ngcc_current_state = 6;
/* 219 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 225 */         action0();
/* 226 */         this.$_ngcc_current_state = 2;
/* 227 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 232 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 233 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 241, (AttributesHolder)this.result);
/* 234 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 237 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 243 */         this.$_ngcc_current_state = 3;
/* 244 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 249 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 257 */     this.$uri = $__uri;
/* 258 */     this.$localName = $__local;
/* 259 */     this.$qname = $__qname;
/* 260 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 263 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 264 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 267 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 273 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 278 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 279 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 282 */           this.$_ngcc_current_state = 6;
/* 283 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 289 */         action0();
/* 290 */         this.$_ngcc_current_state = 2;
/* 291 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 296 */         this.$_ngcc_current_state = 3;
/* 297 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 302 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 310 */     this.$uri = $__uri;
/* 311 */     this.$localName = $__local;
/* 312 */     this.$qname = $__qname;
/* 313 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 316 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 321 */         this.$_ngcc_current_state = 6;
/* 322 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 327 */         action0();
/* 328 */         this.$_ngcc_current_state = 2;
/* 329 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 334 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 335 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 338 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 344 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 345 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 348 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 354 */         this.$_ngcc_current_state = 3;
/* 355 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 360 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 368 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 371 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 372 */           this.$runtime.consumeAttribute($ai);
/* 373 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 379 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 384 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 385 */           this.$runtime.consumeAttribute($ai);
/* 386 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 389 */         this.$_ngcc_current_state = 6;
/* 390 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 12:
/* 396 */         this.name = WhiteSpaceProcessor.collapse($value);
/* 397 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 402 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 407 */         action0();
/* 408 */         this.$_ngcc_current_state = 2;
/* 409 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 414 */         this.$_ngcc_current_state = 3;
/* 415 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 422 */     switch ($__cookie__) {
/*     */       
/*     */       case 241:
/* 425 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 246:
/* 430 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 431 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 244:
/* 436 */         this.annotation = (AnnotationImpl)$__result__;
/* 437 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 444 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\attributeGroupDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */