/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ComplexTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.Messages;
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
/*     */ class redefine
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String schemaLocation;
/*     */   private ModelGroupDeclImpl newGrp;
/*     */   private AttGroupDeclImpl newAg;
/*     */   private SimpleTypeImpl newSt;
/*     */   private ComplexTypeImpl newCt;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  58 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public redefine(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  62 */     super(source, parent, cookie);
/*  63 */     this.$runtime = runtime;
/*  64 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public redefine(NGCCRuntimeEx runtime) {
/*  68 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  72 */     XSAttGroupDecl oldAg = this.$runtime.currentSchema.getAttGroupDecl(this.newAg.getName());
/*  73 */     if (oldAg == null) {
/*  74 */       this.$runtime.reportError(Messages.format("UndefinedAttributeGroup", new Object[] { this.newAg.getName() }));
/*     */     } else {
/*  76 */       this.newAg.redefine((AttGroupDeclImpl)oldAg);
/*  77 */       this.$runtime.currentSchema.addAttGroupDecl((XSAttGroupDecl)this.newAg, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  82 */     XSModelGroupDecl oldGrp = this.$runtime.currentSchema.getModelGroupDecl(this.newGrp.getName());
/*  83 */     if (oldGrp == null) {
/*  84 */       this.$runtime.reportError(Messages.format("UndefinedModelGroup", new Object[] { this.newGrp.getName() }));
/*     */     } else {
/*  86 */       this.newGrp.redefine((ModelGroupDeclImpl)oldGrp);
/*  87 */       this.$runtime.currentSchema.addModelGroupDecl((XSModelGroupDecl)this.newGrp, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*  92 */     XSComplexType oldCt = this.$runtime.currentSchema.getComplexType(this.newCt.getName());
/*  93 */     if (oldCt == null) {
/*  94 */       this.$runtime.reportError(Messages.format("UndefinedCompplexType", new Object[] { this.newCt.getName() }));
/*     */     } else {
/*  96 */       this.newCt.redefine((ComplexTypeImpl)oldCt);
/*  97 */       this.$runtime.currentSchema.addComplexType((XSComplexType)this.newCt, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/* 102 */     XSSimpleType oldSt = this.$runtime.currentSchema.getSimpleType(this.newSt.getName());
/* 103 */     if (oldSt == null) {
/* 104 */       this.$runtime.reportError(Messages.format("UndefinedSimpleType", new Object[] { this.newSt.getName() }));
/*     */     } else {
/* 106 */       this.newSt.redefine((SimpleTypeImpl)oldSt);
/* 107 */       this.$runtime.currentSchema.addSimpleType((XSSimpleType)this.newSt, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action4() throws SAXException {
/* 112 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 117 */     this.$uri = $__uri;
/* 118 */     this.$localName = $__local;
/* 119 */     this.$qname = $__qname;
/* 120 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 123 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 128 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 129 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 684, null, AnnotationContext.SCHEMA);
/* 130 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 133 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 134 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 685);
/* 135 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 138 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/* 139 */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 686);
/* 140 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 143 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 144 */           NGCCHandler h = new group(this, this._source, this.$runtime, 687);
/* 145 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 148 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 149 */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 688);
/* 150 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 153 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 14:
/* 163 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 164 */           this.$runtime.consumeAttribute($ai);
/* 165 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 168 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 174 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 175 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 689, null, AnnotationContext.SCHEMA);
/* 176 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 179 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 180 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 690);
/* 181 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 184 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/* 185 */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 691);
/* 186 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 189 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 190 */           NGCCHandler h = new group(this, this._source, this.$runtime, 692);
/* 191 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 194 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 195 */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 693);
/* 196 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 199 */           this.$_ngcc_current_state = 1;
/* 200 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 15:
/* 210 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 211 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 212 */           this.$_ngcc_current_state = 14;
/*     */         } else {
/*     */           
/* 215 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 221 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 229 */     this.$uri = $__uri;
/* 230 */     this.$localName = $__local;
/* 231 */     this.$qname = $__qname;
/* 232 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 235 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 240 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 241 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 242 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 245 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 251 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 252 */           this.$runtime.consumeAttribute($ai);
/* 253 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 256 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 262 */         this.$_ngcc_current_state = 1;
/* 263 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 268 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 276 */     this.$uri = $__uri;
/* 277 */     this.$localName = $__local;
/* 278 */     this.$qname = $__qname;
/* 279 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 282 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 287 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 288 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 291 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 297 */         this.$_ngcc_current_state = 1;
/* 298 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 303 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 311 */     this.$uri = $__uri;
/* 312 */     this.$localName = $__local;
/* 313 */     this.$qname = $__qname;
/* 314 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 317 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 322 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 323 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 326 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 332 */         this.$_ngcc_current_state = 1;
/* 333 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 338 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 346 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 349 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 354 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 355 */           this.$runtime.consumeAttribute($ai);
/* 356 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 362 */         this.$_ngcc_current_state = 1;
/* 363 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 368 */         this.schemaLocation = $value;
/* 369 */         this.$_ngcc_current_state = 12;
/* 370 */         action4();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 377 */     switch ($__cookie__) {
/*     */       
/*     */       case 689:
/* 380 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 690:
/* 385 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 386 */         action3();
/* 387 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 691:
/* 392 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 393 */         action2();
/* 394 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 692:
/* 399 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 400 */         action1();
/* 401 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 693:
/* 406 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 407 */         action0();
/* 408 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 684:
/* 413 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 685:
/* 418 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 419 */         action3();
/* 420 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 686:
/* 425 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 426 */         action2();
/* 427 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 687:
/* 432 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 433 */         action1();
/* 434 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 688:
/* 439 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 440 */         action0();
/* 441 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 448 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\redefine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */