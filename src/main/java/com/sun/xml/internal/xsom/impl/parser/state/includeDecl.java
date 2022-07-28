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
/*     */ class includeDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  54 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public includeDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.$_ngcc_current_state = 7;
/*     */   }
/*     */   
/*     */   public includeDecl(NGCCRuntimeEx runtime) {
/*  64 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  68 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  73 */     this.$uri = $__uri;
/*  74 */     this.$localName = $__local;
/*  75 */     this.$qname = $__qname;
/*  76 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/*  79 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  80 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 372, null, AnnotationContext.SCHEMA);
/*  81 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  84 */           this.$_ngcc_current_state = 1;
/*  85 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  91 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/*  96 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*  97 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  98 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 101 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 107 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 108 */           this.$runtime.consumeAttribute($ai);
/* 109 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 112 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 126 */     this.$uri = $__uri;
/* 127 */     this.$localName = $__local;
/* 128 */     this.$qname = $__qname;
/* 129 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 132 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/* 133 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 134 */           this.$_ngcc_current_state = 0;
/* 135 */           action0();
/*     */         } else {
/*     */           
/* 138 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 144 */         this.$_ngcc_current_state = 1;
/* 145 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 150 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 155 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 156 */           this.$runtime.consumeAttribute($ai);
/* 157 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 160 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 166 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 174 */     this.$uri = $__uri;
/* 175 */     this.$localName = $__local;
/* 176 */     this.$qname = $__qname;
/* 177 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 180 */         this.$_ngcc_current_state = 1;
/* 181 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 186 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 191 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 192 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 195 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 201 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 209 */     this.$uri = $__uri;
/* 210 */     this.$localName = $__local;
/* 211 */     this.$qname = $__qname;
/* 212 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 215 */         this.$_ngcc_current_state = 1;
/* 216 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 221 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 226 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 227 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 230 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 244 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 247 */         this.$_ngcc_current_state = 1;
/* 248 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 253 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 258 */         this.schemaLocation = $value;
/* 259 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 264 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 265 */           this.$runtime.consumeAttribute($ai);
/* 266 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 274 */     switch ($__cookie__) {
/*     */       
/*     */       case 372:
/* 277 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 284 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\includeDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */