/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
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
/*     */ 
/*     */ 
/*     */ class foreignAttributes
/*     */   extends NGCCHandler
/*     */ {
/*     */   private ForeignAttributesImpl current;
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
/*     */   public foreignAttributes(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, ForeignAttributesImpl _current) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.current = _current;
/*  61 */     this.$_ngcc_current_state = 0;
/*     */   }
/*     */   
/*     */   public foreignAttributes(NGCCRuntimeEx runtime, ForeignAttributesImpl _current) {
/*  65 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _current);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  70 */     this.$uri = $__uri;
/*  71 */     this.$localName = $__local;
/*  72 */     this.$qname = $__qname;
/*  73 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  76 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  81 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  89 */     this.$uri = $__uri;
/*  90 */     this.$localName = $__local;
/*  91 */     this.$qname = $__qname;
/*  92 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  95 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 100 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 108 */     this.$uri = $__uri;
/* 109 */     this.$localName = $__local;
/* 110 */     this.$qname = $__qname;
/* 111 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 114 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 119 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 127 */     this.$uri = $__uri;
/* 128 */     this.$localName = $__local;
/* 129 */     this.$qname = $__qname;
/* 130 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 133 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 138 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 146 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 149 */         revertToParentFromText(makeResult(), this._cookie, $value);
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
/* 161 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   ForeignAttributesImpl makeResult() {
/* 166 */     return this.$runtime.parseForeignAttributes(this.current);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\foreignAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */