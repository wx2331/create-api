/*      */ package com.sun.xml.internal.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ElementDecl;
/*      */ import com.sun.xml.internal.xsom.impl.ModelGroupImpl;
/*      */ import com.sun.xml.internal.xsom.impl.ParticleImpl;
/*      */ import com.sun.xml.internal.xsom.impl.Ref;
/*      */ import com.sun.xml.internal.xsom.impl.UName;
/*      */ import com.sun.xml.internal.xsom.impl.WildcardImpl;
/*      */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*      */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*      */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class particle
/*      */   extends NGCCHandler
/*      */ {
/*      */   private AnnotationImpl annotation;
/*      */   private ElementDecl anonymousElementDecl;
/*      */   private WildcardImpl wcBody;
/*      */   private ModelGroupImpl term;
/*      */   private UName elementTypeName;
/*      */   private occurs occurs;
/*      */   private UName groupName;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   private int $_ngcc_current_state;
/*      */   protected String $uri;
/*      */   protected String $localName;
/*      */   protected String $qname;
/*      */   private Locator wloc;
/*      */   private Locator loc;
/*      */   private ParticleImpl result;
/*      */   private String compositorName;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   60 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   
/*      */   public particle(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*   64 */     super(source, parent, cookie);
/*   65 */     this.$runtime = runtime;
/*   66 */     this.$_ngcc_current_state = 1;
/*      */   }
/*      */   
/*      */   public particle(NGCCRuntimeEx runtime) {
/*   70 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action0() throws SAXException {
/*   75 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.wcBody, this.wloc, this.occurs.max, this.occurs.min);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action1() throws SAXException {
/*   80 */     this.wloc = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action2() throws SAXException {
/*   85 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.anonymousElementDecl, this.loc, this.occurs.max, this.occurs.min);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action3() throws SAXException {
/*   93 */     this.result = new ParticleImpl(this.$runtime.document, this.annotation, (Ref.Term)new DelayedRef.Element((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.elementTypeName), this.loc, this.occurs.max, this.occurs.min);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action4() throws SAXException {
/*  100 */     this.loc = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action5() throws SAXException {
/*  105 */     this.result = new ParticleImpl(this.$runtime.document, this.annotation, (Ref.Term)new DelayedRef.ModelGroup((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.groupName), this.loc, this.occurs.max, this.occurs.min);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action6() throws SAXException {
/*  112 */     this.loc = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action7() throws SAXException {
/*  117 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.term, this.loc, this.occurs.max, this.occurs.min);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action8() throws SAXException {
/*  123 */     this.compositorName = this.$localName;
/*  124 */     this.loc = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*  130 */     this.$uri = $__uri;
/*  131 */     this.$localName = $__local;
/*  132 */     this.$qname = $__qname;
/*  133 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 29:
/*  136 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  137 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 136, this.loc, this.compositorName);
/*  138 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  141 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 4:
/*  147 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/*  148 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 107);
/*  149 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  152 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  158 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/*  159 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 132);
/*  160 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  163 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  169 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  170 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  171 */           action8();
/*  172 */           this.$_ngcc_current_state = 30;
/*      */         
/*      */         }
/*  175 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*  176 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  177 */           action6();
/*  178 */           this.$_ngcc_current_state = 26;
/*      */         
/*      */         }
/*  181 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*  182 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  183 */           action4();
/*  184 */           this.$_ngcc_current_state = 16;
/*      */         
/*      */         }
/*  187 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/*  188 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  189 */           action1();
/*  190 */           this.$_ngcc_current_state = 4;
/*      */         } else {
/*      */           
/*  193 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*  202 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 20:
/*  207 */         action5();
/*  208 */         this.$_ngcc_current_state = 19;
/*  209 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  214 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  215 */           this.$runtime.consumeAttribute($ai);
/*  216 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         
/*      */         }
/*  219 */         else if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  220 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  221 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  224 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 11:
/*  231 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  232 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 115, null, AnnotationContext.PARTICLE);
/*  233 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  236 */           this.$_ngcc_current_state = 10;
/*  237 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 25:
/*  243 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  244 */           this.$runtime.consumeAttribute($ai);
/*  245 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  248 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  254 */         action3();
/*  255 */         this.$_ngcc_current_state = 7;
/*  256 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 3:
/*  261 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  262 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 106, this.wloc);
/*  263 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  266 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 16:
/*  272 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))))) {
/*  273 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 121);
/*  274 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  277 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  283 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  284 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 127, null, AnnotationContext.PARTICLE);
/*  285 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  288 */           this.$_ngcc_current_state = 20;
/*  289 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 30:
/*  295 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  296 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 137);
/*  297 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  300 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  306 */     unexpectedEnterElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*  314 */     this.$uri = $__uri;
/*  315 */     this.$localName = $__local;
/*  316 */     this.$qname = $__qname;
/*  317 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 29:
/*  320 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  321 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 136, this.loc, this.compositorName);
/*  322 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  325 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 4:
/*  331 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*  332 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 107);
/*  333 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  336 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  342 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  343 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 132);
/*  344 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  347 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  353 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/*  354 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  355 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  358 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  364 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  369 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  370 */           this.$runtime.consumeAttribute($ai);
/*  371 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         
/*      */         }
/*  374 */         else if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/*  375 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  376 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  379 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 20:
/*  386 */         action5();
/*  387 */         this.$_ngcc_current_state = 19;
/*  388 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  393 */         this.$_ngcc_current_state = 10;
/*  394 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 25:
/*  399 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  400 */           this.$runtime.consumeAttribute($ai);
/*  401 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  404 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/*  410 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*  411 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  412 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  415 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  421 */         action3();
/*  422 */         this.$_ngcc_current_state = 7;
/*  423 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 7:
/*  428 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*  429 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  430 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  433 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 3:
/*  439 */         if ((($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*  440 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 106, this.wloc);
/*  441 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  444 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 16:
/*  450 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/*  451 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 121);
/*  452 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  455 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  461 */         this.$_ngcc_current_state = 20;
/*  462 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 30:
/*  467 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  468 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 137);
/*  469 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  472 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  478 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  479 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  480 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  483 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  489 */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  497 */     this.$uri = $__uri;
/*  498 */     this.$localName = $__local;
/*  499 */     this.$qname = $__qname;
/*  500 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 0:
/*  503 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  508 */         if ($__uri.equals("") && $__local.equals("ref")) {
/*  509 */           this.$_ngcc_current_state = 14;
/*      */         
/*      */         }
/*  512 */         else if (($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("abstract"))) {
/*  513 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  514 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  517 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 20:
/*  524 */         action5();
/*  525 */         this.$_ngcc_current_state = 19;
/*  526 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  531 */         this.$_ngcc_current_state = 10;
/*  532 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 25:
/*  537 */         if ($__uri.equals("") && $__local.equals("ref")) {
/*  538 */           this.$_ngcc_current_state = 24;
/*      */         } else {
/*      */           
/*  541 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 4:
/*  547 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("namespace")) || ($__uri.equals("") && $__local.equals("processContents"))) {
/*  548 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 107);
/*  549 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  552 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  558 */         action3();
/*  559 */         this.$_ngcc_current_state = 7;
/*  560 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  565 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("ref")) || ($__uri.equals("") && $__local.equals("minOccurs"))) {
/*  566 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 132);
/*  567 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  570 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 3:
/*  576 */         if (($__uri.equals("") && $__local.equals("namespace")) || ($__uri.equals("") && $__local.equals("processContents"))) {
/*  577 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 106, this.wloc);
/*  578 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  581 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 16:
/*  587 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("ref")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("abstract"))) {
/*  588 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 121);
/*  589 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  592 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  598 */         this.$_ngcc_current_state = 20;
/*  599 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 30:
/*  604 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs"))) {
/*  605 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 137);
/*  606 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  609 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  615 */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  623 */     this.$uri = $__uri;
/*  624 */     this.$localName = $__local;
/*  625 */     this.$qname = $__qname;
/*  626 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 23:
/*  629 */         if ($__uri.equals("") && $__local.equals("ref")) {
/*  630 */           this.$_ngcc_current_state = 21;
/*      */         } else {
/*      */           
/*  633 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  639 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 20:
/*  644 */         action5();
/*  645 */         this.$_ngcc_current_state = 19;
/*  646 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  651 */         this.$_ngcc_current_state = 10;
/*  652 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  657 */         action3();
/*  658 */         this.$_ngcc_current_state = 7;
/*  659 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  664 */         this.$_ngcc_current_state = 20;
/*  665 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/*  670 */         if ($__uri.equals("") && $__local.equals("ref")) {
/*  671 */           this.$_ngcc_current_state = 11;
/*      */         } else {
/*      */           
/*  674 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  680 */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     NGCCHandler h;
/*  688 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 4:
/*  691 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  692 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 107);
/*  693 */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*  696 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  697 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 107);
/*  698 */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*  701 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/*  702 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 107);
/*  703 */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*  706 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/*  707 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 107);
/*  708 */           spawnChildFromText(nGCCHandler, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 26:
/*  717 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/*  718 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 132);
/*  719 */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*  722 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  723 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 132);
/*  724 */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*  727 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/*  728 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 132);
/*  729 */           spawnChildFromText(nGCCHandler, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 14:
/*  737 */         h = new qname(this, this._source, this.$runtime, 118);
/*  738 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 24:
/*  743 */         h = new qname(this, this._source, this.$runtime, 130);
/*  744 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 0:
/*  749 */         revertToParentFromText(this.result, this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 20:
/*  754 */         action5();
/*  755 */         this.$_ngcc_current_state = 19;
/*  756 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 8:
/*  761 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  762 */           this.$runtime.consumeAttribute($ai);
/*  763 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  766 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  767 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  768 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  771 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  772 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  773 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  776 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  777 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  778 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  781 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  782 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  783 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  786 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  787 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  788 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  791 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  792 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  793 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  796 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  797 */           h = new elementDeclBody(this, this._source, this.$runtime, 112, this.loc, false);
/*  798 */           spawnChildFromText(h, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 11:
/*  811 */         this.$_ngcc_current_state = 10;
/*  812 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 25:
/*  817 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  818 */           this.$runtime.consumeAttribute($ai);
/*  819 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 10:
/*  825 */         action3();
/*  826 */         this.$_ngcc_current_state = 7;
/*  827 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  832 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  833 */           h = new wildcardBody(this, this._source, this.$runtime, 106, this.wloc);
/*  834 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  837 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  838 */           h = new wildcardBody(this, this._source, this.$runtime, 106, this.wloc);
/*  839 */           spawnChildFromText(h, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 16:
/*  846 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  847 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  848 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  851 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  852 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  853 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  856 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/*  857 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  858 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  861 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/*  862 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  863 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  866 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  867 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  868 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  871 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  872 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  873 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  876 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  877 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  878 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  881 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  882 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  883 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  886 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  887 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  888 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  891 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/*  892 */           h = new occurs(this, this._source, this.$runtime, 121);
/*  893 */           spawnChildFromText(h, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 21:
/*  908 */         this.$_ngcc_current_state = 20;
/*  909 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 30:
/*  914 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/*  915 */           h = new occurs(this, this._source, this.$runtime, 137);
/*  916 */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*  919 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/*  920 */           h = new occurs(this, this._source, this.$runtime, 137);
/*  921 */           spawnChildFromText(h, $value);
/*      */         } 
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*  930 */     switch ($__cookie__) {
/*      */       
/*      */       case 136:
/*  933 */         this.term = (ModelGroupImpl)$__result__;
/*  934 */         action7();
/*  935 */         this.$_ngcc_current_state = 28;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 107:
/*  940 */         this.occurs = (occurs)$__result__;
/*  941 */         this.$_ngcc_current_state = 3;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 106:
/*  946 */         this.wcBody = (WildcardImpl)$__result__;
/*  947 */         action0();
/*  948 */         this.$_ngcc_current_state = 2;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 121:
/*  953 */         this.occurs = (occurs)$__result__;
/*  954 */         this.$_ngcc_current_state = 8;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 127:
/*  959 */         this.annotation = (AnnotationImpl)$__result__;
/*  960 */         this.$_ngcc_current_state = 20;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 137:
/*  965 */         this.occurs = (occurs)$__result__;
/*  966 */         this.$_ngcc_current_state = 29;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 132:
/*  971 */         this.occurs = (occurs)$__result__;
/*  972 */         this.$_ngcc_current_state = 25;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 118:
/*  977 */         this.elementTypeName = (UName)$__result__;
/*  978 */         this.$_ngcc_current_state = 13;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 130:
/*  983 */         this.groupName = (UName)$__result__;
/*  984 */         this.$_ngcc_current_state = 23;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 112:
/*  989 */         this.anonymousElementDecl = (ElementDecl)$__result__;
/*  990 */         action2();
/*  991 */         this.$_ngcc_current_state = 7;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 115:
/*  996 */         this.annotation = (AnnotationImpl)$__result__;
/*  997 */         this.$_ngcc_current_state = 10;
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean accepted() {
/* 1004 */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\particle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */