/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
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
/*     */ class importDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String ns;
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  55 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  59 */     super(source, parent, cookie);
/*  60 */     this.$runtime = runtime;
/*  61 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCRuntimeEx runtime) {
/*  65 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  70 */     if (this.ns == null) this.ns = ""; 
/*  71 */     this.$runtime.importSchema(this.ns, this.schemaLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  77 */     this.$uri = $__uri;
/*  78 */     this.$localName = $__local;
/*  79 */     this.$qname = $__qname;
/*  80 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/*  83 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  84 */           this.$runtime.consumeAttribute($ai);
/*  85 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  88 */           this.$_ngcc_current_state = 2;
/*  89 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/*  95 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  96 */           this.$runtime.consumeAttribute($ai);
/*  97 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 100 */           this.$_ngcc_current_state = 4;
/* 101 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 107 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/* 108 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 109 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 112 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 118 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 119 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 340, null, AnnotationContext.SCHEMA);
/* 120 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 123 */           this.$_ngcc_current_state = 1;
/* 124 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 130 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 135 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 143 */     this.$uri = $__uri;
/* 144 */     this.$localName = $__local;
/* 145 */     this.$qname = $__qname;
/* 146 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 149 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 150 */           this.$runtime.consumeAttribute($ai);
/* 151 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 154 */           this.$_ngcc_current_state = 2;
/* 155 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 161 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 162 */           this.$runtime.consumeAttribute($ai);
/* 163 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 166 */           this.$_ngcc_current_state = 4;
/* 167 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 173 */         this.$_ngcc_current_state = 1;
/* 174 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 179 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/* 180 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 181 */           this.$_ngcc_current_state = 0;
/* 182 */           action0();
/*     */         } else {
/*     */           
/* 185 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 191 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 196 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 204 */     this.$uri = $__uri;
/* 205 */     this.$localName = $__local;
/* 206 */     this.$qname = $__qname;
/* 207 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 210 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 211 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 214 */           this.$_ngcc_current_state = 2;
/* 215 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 221 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 222 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 225 */           this.$_ngcc_current_state = 4;
/* 226 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 232 */         this.$_ngcc_current_state = 1;
/* 233 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 238 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 243 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 251 */     this.$uri = $__uri;
/* 252 */     this.$localName = $__local;
/* 253 */     this.$qname = $__qname;
/* 254 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 257 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 258 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 261 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 267 */         this.$_ngcc_current_state = 2;
/* 268 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 273 */         this.$_ngcc_current_state = 4;
/* 274 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 279 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 280 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 283 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 289 */         this.$_ngcc_current_state = 1;
/* 290 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 295 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 300 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 308 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 311 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 312 */           this.$runtime.consumeAttribute($ai);
/* 313 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 316 */         this.$_ngcc_current_state = 2;
/* 317 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 323 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 324 */           this.$runtime.consumeAttribute($ai);
/* 325 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 328 */         this.$_ngcc_current_state = 4;
/* 329 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 10:
/* 335 */         this.ns = $value;
/* 336 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 341 */         this.$_ngcc_current_state = 1;
/* 342 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 347 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 352 */         this.schemaLocation = $value;
/* 353 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 360 */     switch ($__cookie__) {
/*     */       
/*     */       case 340:
/* 363 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 370 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\importDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */