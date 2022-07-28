/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class annotation
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationContext context;
/*     */   private AnnotationImpl existing;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private AnnotationParser parser;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  56 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public annotation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _existing, AnnotationContext _context) {
/*  60 */     super(source, parent, cookie);
/*  61 */     this.$runtime = runtime;
/*  62 */     this.existing = _existing;
/*  63 */     this.context = _context;
/*  64 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public annotation(NGCCRuntimeEx runtime, AnnotationImpl _existing, AnnotationContext _context) {
/*  68 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _existing, _context);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  73 */     this.locator = this.$runtime.copyLocator();
/*  74 */     this.parser = this.$runtime.createAnnotationParser();
/*  75 */     this.$runtime.redirectSubtree(this.parser.getContentHandler(this.context, this.$runtime
/*     */           
/*  77 */           .getAnnotationContextElementName(), this.$runtime
/*  78 */           .getErrorHandler(), this.$runtime.parser
/*  79 */           .getEntityResolver()), this.$uri, this.$localName, this.$qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  86 */     this.$uri = $__uri;
/*  87 */     this.$localName = $__local;
/*  88 */     this.$qname = $__qname;
/*  89 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  92 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  97 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  98 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  99 */           action0();
/* 100 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 103 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 109 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/* 117 */     this.$uri = $__uri;
/* 118 */     this.$localName = $__local;
/* 119 */     this.$qname = $__qname;
/* 120 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 123 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 124 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 125 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 128 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 134 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 147 */     this.$uri = $__uri;
/* 148 */     this.$localName = $__local;
/* 149 */     this.$qname = $__qname;
/* 150 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 153 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 166 */     this.$uri = $__uri;
/* 167 */     this.$localName = $__local;
/* 168 */     this.$qname = $__qname;
/* 169 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 172 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 177 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 185 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 188 */         revertToParentFromText(makeResult(), this._cookie, $value);
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
/* 200 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationImpl makeResult() {
/* 208 */     Object e = null;
/* 209 */     if (this.existing != null) e = this.existing.getAnnotation();
/*     */     
/* 211 */     return new AnnotationImpl(this.parser.getResult(e), this.locator);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\annotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */