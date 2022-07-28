/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
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
/*     */ class group
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ModelGroupImpl term;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ModelGroupDeclImpl result;
/*     */   private Locator loc;
/*     */   private Locator mloc;
/*     */   private String compositorName;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  50 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public group(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  54 */     super(source, parent, cookie);
/*  55 */     this.$runtime = runtime;
/*  56 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public group(NGCCRuntimeEx runtime) {
/*  60 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  65 */     this
/*     */       
/*  67 */       .result = new ModelGroupDeclImpl(this.$runtime.document, this.annotation, this.loc, this.fa, this.$runtime.currentSchema.getTargetNamespace(), this.name, this.term);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  76 */     this.mloc = this.$runtime.copyLocator();
/*  77 */     this.compositorName = this.$localName;
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  82 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  87 */     this.$uri = $__uri;
/*  88 */     this.$localName = $__local;
/*  89 */     this.$qname = $__qname;
/*  90 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/*  93 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  94 */           this.$runtime.consumeAttribute($ai);
/*  95 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  98 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 104 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 105 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 357, null);
/* 106 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 109 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 115 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 116 */           this.$runtime.consumeAttribute($ai);
/* 117 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 120 */           this.$_ngcc_current_state = 10;
/* 121 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 127 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 128 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 359, null, AnnotationContext.MODELGROUP_DECL);
/* 129 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 132 */           this.$_ngcc_current_state = 5;
/* 133 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 139 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 144 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 145 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 146 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 149 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 155 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 156 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 157 */           action2();
/* 158 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 161 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 167 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 168 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 355, this.mloc, this.compositorName);
/* 169 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 172 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 178 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 186 */     this.$uri = $__uri;
/* 187 */     this.$localName = $__local;
/* 188 */     this.$qname = $__qname;
/* 189 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 192 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 193 */           this.$runtime.consumeAttribute($ai);
/* 194 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 197 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 203 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 204 */           this.$runtime.consumeAttribute($ai);
/* 205 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 208 */           this.$_ngcc_current_state = 10;
/* 209 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 215 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 216 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 217 */           this.$_ngcc_current_state = 0;
/* 218 */           action0();
/*     */         } else {
/*     */           
/* 221 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 227 */         this.$_ngcc_current_state = 5;
/* 228 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 233 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 238 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 239 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 240 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 243 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 249 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 250 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 355, this.mloc, this.compositorName);
/* 251 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 254 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 260 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 268 */     this.$uri = $__uri;
/* 269 */     this.$localName = $__local;
/* 270 */     this.$qname = $__qname;
/* 271 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 274 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 275 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 278 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 284 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 285 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 288 */           this.$_ngcc_current_state = 10;
/* 289 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 295 */         this.$_ngcc_current_state = 5;
/* 296 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 301 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 306 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 314 */     this.$uri = $__uri;
/* 315 */     this.$localName = $__local;
/* 316 */     this.$qname = $__qname;
/* 317 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 11:
/* 320 */         this.$_ngcc_current_state = 10;
/* 321 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 326 */         this.$_ngcc_current_state = 5;
/* 327 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 332 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 337 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 338 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 341 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 347 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 348 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 351 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 357 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 365 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 368 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 369 */           this.$runtime.consumeAttribute($ai);
/* 370 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 376 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 377 */           this.$runtime.consumeAttribute($ai);
/* 378 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 381 */         this.$_ngcc_current_state = 10;
/* 382 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 6:
/* 388 */         this.$_ngcc_current_state = 5;
/* 389 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 394 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 399 */         this.name = WhiteSpaceProcessor.collapse($value);
/* 400 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 405 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 412 */     switch ($__cookie__) {
/*     */       
/*     */       case 357:
/* 415 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 416 */         action1();
/* 417 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 359:
/* 422 */         this.annotation = (AnnotationImpl)$__result__;
/* 423 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 355:
/* 428 */         this.term = (ModelGroupImpl)$__result__;
/* 429 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 436 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */