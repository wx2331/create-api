/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.Ref;
/*     */ import com.sun.xml.internal.xsom.impl.RestrictionSimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.UName;
/*     */ import com.sun.xml.internal.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.Set;
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
/*     */ class SimpleType_Restriction
/*     */   extends NGCCHandler
/*     */ {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName baseTypeName;
/*     */   private Set finalSet;
/*     */   private ForeignAttributesImpl fa;
/*     */   private XSFacet facet;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private RestrictionSimpleTypeImpl result;
/*     */   private Ref.SimpleType baseType;
/*     */   private Locator rloc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  60 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public SimpleType_Restriction(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  64 */     super(source, parent, cookie);
/*  65 */     this.$runtime = runtime;
/*  66 */     this.annotation = _annotation;
/*  67 */     this.locator = _locator;
/*  68 */     this.fa = _fa;
/*  69 */     this.name = _name;
/*  70 */     this.finalSet = _finalSet;
/*  71 */     this.$_ngcc_current_state = 13;
/*     */   }
/*     */   
/*     */   public SimpleType_Restriction(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  75 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _fa, _name, _finalSet);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  80 */     this.result.addFacet(this.facet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  86 */     this.result = new RestrictionSimpleTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, (this.name == null), this.finalSet, this.baseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  93 */     this.baseType = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.rloc, this.$runtime.currentSchema, this.baseTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  99 */     this.rloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 104 */     this.$uri = $__uri;
/* 105 */     this.$localName = $__local;
/* 106 */     this.$qname = $__qname;
/* 107 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 110 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType"))) {
/* 111 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 166, this.fa);
/* 112 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 115 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 121 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 122 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 164, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/* 123 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 126 */           this.$_ngcc_current_state = 5;
/* 127 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 133 */         action1();
/* 134 */         this.$_ngcc_current_state = 2;
/* 135 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 140 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 145 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 146 */           this.$runtime.consumeAttribute($ai);
/* 147 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 150 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 151 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 158);
/* 152 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 155 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 162 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 163 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 153);
/* 164 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 167 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 173 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 174 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 154);
/* 175 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 178 */           this.$_ngcc_current_state = 1;
/* 179 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 185 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 186 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 187 */           action3();
/* 188 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 191 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 197 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 205 */     this.$uri = $__uri;
/* 206 */     this.$localName = $__local;
/* 207 */     this.$qname = $__qname;
/* 208 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 211 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 212 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 166, this.fa);
/* 213 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 216 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 222 */         this.$_ngcc_current_state = 5;
/* 223 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 228 */         action1();
/* 229 */         this.$_ngcc_current_state = 2;
/* 230 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 235 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 240 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 241 */           this.$runtime.consumeAttribute($ai);
/* 242 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 245 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 251 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 252 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 253 */           this.$_ngcc_current_state = 0;
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
/*     */       case 12:
/* 282 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 283 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 166, this.fa);
/* 284 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 287 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 293 */         this.$_ngcc_current_state = 5;
/* 294 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 299 */         action1();
/* 300 */         this.$_ngcc_current_state = 2;
/* 301 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 306 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 311 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 312 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 315 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 321 */         this.$_ngcc_current_state = 1;
/* 322 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 327 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 335 */     this.$uri = $__uri;
/* 336 */     this.$localName = $__local;
/* 337 */     this.$qname = $__qname;
/* 338 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 341 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 342 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 345 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 351 */         this.$_ngcc_current_state = 5;
/* 352 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 357 */         action1();
/* 358 */         this.$_ngcc_current_state = 2;
/* 359 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 364 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 369 */         this.$_ngcc_current_state = 1;
/* 370 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 375 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 383 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 386 */         h = new qname(this, this._source, this.$runtime, 160);
/* 387 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 392 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 393 */           h = new foreignAttributes(this, this._source, this.$runtime, 166, this.fa);
/* 394 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 400 */         this.$_ngcc_current_state = 5;
/* 401 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 406 */         action1();
/* 407 */         this.$_ngcc_current_state = 2;
/* 408 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 413 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 418 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 419 */           this.$runtime.consumeAttribute($ai);
/* 420 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 426 */         this.$_ngcc_current_state = 1;
/* 427 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 434 */     switch ($__cookie__) {
/*     */       
/*     */       case 160:
/* 437 */         this.baseTypeName = (UName)$__result__;
/* 438 */         action2();
/* 439 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 164:
/* 444 */         this.annotation = (AnnotationImpl)$__result__;
/* 445 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 154:
/* 450 */         this.facet = (XSFacet)$__result__;
/* 451 */         action0();
/* 452 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 166:
/* 457 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 458 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 158:
/* 463 */         this.baseType = (Ref.SimpleType)$__result__;
/* 464 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 153:
/* 469 */         this.facet = (XSFacet)$__result__;
/* 470 */         action0();
/* 471 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 478 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\SimpleType_Restriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */