/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
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
/*     */ 
/*     */ class qualification
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String text;
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
/*     */   public qualification(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  58 */     super(source, parent, cookie);
/*  59 */     this.$runtime = runtime;
/*  60 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public qualification(NGCCRuntimeEx runtime) {
/*  64 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  69 */     this.$uri = $__uri;
/*  70 */     this.$localName = $__local;
/*  71 */     this.$qname = $__qname;
/*  72 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  75 */         revertToParentFromEnterElement(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  80 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  88 */     this.$uri = $__uri;
/*  89 */     this.$localName = $__local;
/*  90 */     this.$qname = $__qname;
/*  91 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  94 */         revertToParentFromLeaveElement(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  99 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 107 */     this.$uri = $__uri;
/* 108 */     this.$localName = $__local;
/* 109 */     this.$qname = $__qname;
/* 110 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 113 */         revertToParentFromEnterAttribute(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 126 */     this.$uri = $__uri;
/* 127 */     this.$localName = $__local;
/* 128 */     this.$qname = $__qname;
/* 129 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 132 */         revertToParentFromLeaveAttribute(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 145 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 148 */         revertToParentFromText(new Boolean(this.text.trim().equals("qualified")), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 153 */         if ($value.equals("qualified")) {
/* 154 */           this.text = $value;
/* 155 */           this.$_ngcc_current_state = 0;
/*     */           break;
/*     */         } 
/* 158 */         if ($value.equals("unqualified")) {
/* 159 */           this.text = $value;
/* 160 */           this.$_ngcc_current_state = 0;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 174 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\qualification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */