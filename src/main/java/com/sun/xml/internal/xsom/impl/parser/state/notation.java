/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.NotationImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class notation
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private String pub;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String sys;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  52 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public notation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  56 */     super(source, parent, cookie);
/*  57 */     this.$runtime = runtime;
/*  58 */     this.$_ngcc_current_state = 16;
/*     */   }
/*     */   
/*     */   public notation(NGCCRuntimeEx runtime) {
/*  62 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  66 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  71 */     this.$uri = $__uri;
/*  72 */     this.$localName = $__local;
/*  73 */     this.$qname = $__qname;
/*  74 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/*  77 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  78 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 209, null, AnnotationContext.NOTATION);
/*  79 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  82 */           this.$_ngcc_current_state = 1;
/*  83 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/*  89 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*  90 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  91 */           action0();
/*  92 */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           
/*  95 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 101 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 102 */           this.$runtime.consumeAttribute($ai);
/* 103 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 106 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 112 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 113 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 114 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 117 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 123 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 124 */           this.$runtime.consumeAttribute($ai);
/* 125 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 128 */           this.$_ngcc_current_state = 2;
/* 129 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 135 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 136 */           this.$runtime.consumeAttribute($ai);
/* 137 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 140 */           this.$_ngcc_current_state = 4;
/* 141 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 147 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 152 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 160 */     this.$uri = $__uri;
/* 161 */     this.$localName = $__local;
/* 162 */     this.$qname = $__qname;
/* 163 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 166 */         this.$_ngcc_current_state = 1;
/* 167 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 172 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 173 */           this.$runtime.consumeAttribute($ai);
/* 174 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 177 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 183 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 184 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 185 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 188 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 194 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 195 */           this.$runtime.consumeAttribute($ai);
/* 196 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 199 */           this.$_ngcc_current_state = 2;
/* 200 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 206 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 207 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 208 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 211 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 217 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 218 */           this.$runtime.consumeAttribute($ai);
/* 219 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 222 */           this.$_ngcc_current_state = 4;
/* 223 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 229 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 234 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 242 */     this.$uri = $__uri;
/* 243 */     this.$localName = $__local;
/* 244 */     this.$qname = $__qname;
/* 245 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 248 */         this.$_ngcc_current_state = 1;
/* 249 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 254 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 255 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 258 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 264 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 265 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 266 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 269 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 275 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 276 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 279 */           this.$_ngcc_current_state = 2;
/* 280 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 286 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 287 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 290 */           this.$_ngcc_current_state = 4;
/* 291 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 297 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 2:
/* 316 */         this.$_ngcc_current_state = 1;
/* 317 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 322 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 323 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 326 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 332 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 333 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 336 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 342 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 343 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 346 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 352 */         this.$_ngcc_current_state = 2;
/* 353 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 358 */         this.$_ngcc_current_state = 4;
/* 359 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 364 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 369 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 377 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 380 */         this.$_ngcc_current_state = 1;
/* 381 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 386 */         this.pub = $value;
/* 387 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 392 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 393 */           this.$runtime.consumeAttribute($ai);
/* 394 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 15:
/* 400 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 401 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 224, null);
/* 402 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 408 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 409 */           this.$runtime.consumeAttribute($ai);
/* 410 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 413 */         this.$_ngcc_current_state = 2;
/* 414 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 420 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 421 */           this.$runtime.consumeAttribute($ai);
/* 422 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 425 */         this.$_ngcc_current_state = 4;
/* 426 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 13:
/* 432 */         this.name = WhiteSpaceProcessor.collapse($value);
/* 433 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 438 */         this.sys = $value;
/* 439 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 444 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 451 */     switch ($__cookie__) {
/*     */       
/*     */       case 209:
/* 454 */         this.ann = (AnnotationImpl)$__result__;
/* 455 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 224:
/* 460 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 461 */         this.$_ngcc_current_state = 14;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 468 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private XSNotation makeResult() {
/* 474 */     return (XSNotation)new NotationImpl(this.$runtime.document, this.ann, this.loc, this.fa, this.name, this.pub, this.sys);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\notation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */