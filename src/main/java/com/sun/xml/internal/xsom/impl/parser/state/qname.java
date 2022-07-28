/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
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
/*     */ class qname
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String qvalue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  44 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public qname(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  48 */     super(source, parent, cookie);
/*  49 */     this.$runtime = runtime;
/*  50 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public qname(NGCCRuntimeEx runtime) {
/*  54 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  59 */     this.$uri = $__uri;
/*  60 */     this.$localName = $__local;
/*  61 */     this.$qname = $__qname;
/*  62 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  65 */         revertToParentFromEnterElement(this.$runtime.parseUName(this.qvalue), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  70 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  78 */     this.$uri = $__uri;
/*  79 */     this.$localName = $__local;
/*  80 */     this.$qname = $__qname;
/*  81 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  84 */         revertToParentFromLeaveElement(this.$runtime.parseUName(this.qvalue), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  89 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  97 */     this.$uri = $__uri;
/*  98 */     this.$localName = $__local;
/*  99 */     this.$qname = $__qname;
/* 100 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 103 */         revertToParentFromEnterAttribute(this.$runtime.parseUName(this.qvalue), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 108 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 116 */     this.$uri = $__uri;
/* 117 */     this.$localName = $__local;
/* 118 */     this.$qname = $__qname;
/* 119 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 122 */         revertToParentFromLeaveAttribute(this.$runtime.parseUName(this.qvalue), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 135 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 138 */         revertToParentFromText(this.$runtime.parseUName(this.qvalue), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 143 */         this.qvalue = WhiteSpaceProcessor.collapse($value);
/* 144 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 156 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\qname.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */