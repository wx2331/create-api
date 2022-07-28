/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.internal.xsom.impl.ContentTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
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
/*     */ class complexType_complexContent_body
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AttributesHolder owner;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ContentTypeImpl particle;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  54 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.owner = _owner;
/*  61 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  65 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  69 */     if (this.particle == null) {
/*  70 */       this.particle = (ContentTypeImpl)this.$runtime.parser.schemaSet.empty;
/*     */     }
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  75 */     this.$uri = $__uri;
/*  76 */     this.$localName = $__local;
/*  77 */     this.$qname = $__qname;
/*  78 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  81 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  82 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/*  83 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  86 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/*  87 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  93 */         revertToParentFromEnterElement(this.particle, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  98 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*  99 */           NGCCHandler h = new particle(this, this._source, this.$runtime, 3);
/* 100 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 103 */           this.$_ngcc_current_state = 1;
/* 104 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 110 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 118 */     this.$uri = $__uri;
/* 119 */     this.$localName = $__local;
/* 120 */     this.$qname = $__qname;
/* 121 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 124 */         h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/* 125 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 130 */         revertToParentFromLeaveElement(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 135 */         this.$_ngcc_current_state = 1;
/* 136 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 141 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 149 */     this.$uri = $__uri;
/* 150 */     this.$localName = $__local;
/* 151 */     this.$qname = $__qname;
/* 152 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 155 */         h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/* 156 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 161 */         revertToParentFromEnterAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 166 */         this.$_ngcc_current_state = 1;
/* 167 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 172 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 180 */     this.$uri = $__uri;
/* 181 */     this.$localName = $__local;
/* 182 */     this.$qname = $__qname;
/* 183 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 186 */         h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/* 187 */         spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 192 */         revertToParentFromLeaveAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 197 */         this.$_ngcc_current_state = 1;
/* 198 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 203 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     NGCCHandler h;
/* 211 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 214 */         h = new attributeUses(this, this._source, this.$runtime, 1, this.owner);
/* 215 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 220 */         revertToParentFromText(this.particle, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 225 */         this.$_ngcc_current_state = 1;
/* 226 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 233 */     switch ($__cookie__) {
/*     */       
/*     */       case 1:
/* 236 */         action0();
/* 237 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 242 */         this.particle = (ContentTypeImpl)$__result__;
/* 243 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 250 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\complexType_complexContent_body.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */