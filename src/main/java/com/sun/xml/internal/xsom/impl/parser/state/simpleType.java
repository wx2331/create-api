/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.xsom.XSVariety;
/*     */ import com.sun.xml.internal.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.internal.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.internal.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.internal.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.internal.xsom.parser.AnnotationContext;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
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
/*     */ class simpleType
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String finalValue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private SimpleTypeImpl result;
/*     */   private Locator locator;
/*     */   private Set finalSet;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  56 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public simpleType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  60 */     super(source, parent, cookie);
/*  61 */     this.$runtime = runtime;
/*  62 */     this.$_ngcc_current_state = 19;
/*     */   }
/*     */   
/*     */   public simpleType(NGCCRuntimeEx runtime) {
/*  66 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  70 */     this.finalSet = makeFinalSet(this.finalValue);
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  74 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  79 */     this.$uri = $__uri;
/*  80 */     this.$localName = $__local;
/*  81 */     this.$qname = $__qname;
/*  82 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/*  85 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  86 */           this.$runtime.consumeAttribute($ai);
/*  87 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  90 */           this.$_ngcc_current_state = 11;
/*  91 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/*  97 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  98 */           this.$runtime.consumeAttribute($ai);
/*  99 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 102 */           this.$_ngcc_current_state = 10;
/* 103 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 109 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 110 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 111 */           action1();
/* 112 */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           
/* 115 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 121 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 122 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 89, null, AnnotationContext.SIMPLETYPE_DECL);
/* 123 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 126 */           this.$_ngcc_current_state = 7;
/* 127 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 133 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list"))) {
/* 134 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 91, this.fa);
/* 135 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 138 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 144 */         action0();
/* 145 */         this.$_ngcc_current_state = 2;
/* 146 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 151 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 156 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 157 */           NGCCHandler h = new SimpleType_Restriction(this, this._source, this.$runtime, 85, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 158 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 161 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/* 162 */           NGCCHandler h = new SimpleType_List(this, this._source, this.$runtime, 86, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 163 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 166 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) {
/* 167 */           NGCCHandler h = new SimpleType_Union(this, this._source, this.$runtime, 80, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 168 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 171 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 187 */     this.$uri = $__uri;
/* 188 */     this.$localName = $__local;
/* 189 */     this.$qname = $__qname;
/* 190 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/* 193 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 194 */           this.$runtime.consumeAttribute($ai);
/* 195 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 198 */           this.$_ngcc_current_state = 11;
/* 199 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 205 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 206 */           this.$runtime.consumeAttribute($ai);
/* 207 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 210 */           this.$_ngcc_current_state = 10;
/* 211 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 217 */         this.$_ngcc_current_state = 7;
/* 218 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 223 */         action0();
/* 224 */         this.$_ngcc_current_state = 2;
/* 225 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 230 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 231 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 232 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 235 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 241 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 246 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 254 */     this.$uri = $__uri;
/* 255 */     this.$localName = $__local;
/* 256 */     this.$qname = $__qname;
/* 257 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/* 260 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 261 */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           
/* 264 */           this.$_ngcc_current_state = 11;
/* 265 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 271 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 272 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 275 */           this.$_ngcc_current_state = 10;
/* 276 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 282 */         this.$_ngcc_current_state = 7;
/* 283 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 288 */         action0();
/* 289 */         this.$_ngcc_current_state = 2;
/* 290 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 295 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 300 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 308 */     this.$uri = $__uri;
/* 309 */     this.$localName = $__local;
/* 310 */     this.$qname = $__qname;
/* 311 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/* 314 */         this.$_ngcc_current_state = 11;
/* 315 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 320 */         this.$_ngcc_current_state = 10;
/* 321 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 326 */         this.$_ngcc_current_state = 7;
/* 327 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 332 */         action0();
/* 333 */         this.$_ngcc_current_state = 2;
/* 334 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 339 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 340 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 343 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 349 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 350 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 353 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 359 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 364 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 372 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 375 */         this.name = WhiteSpaceProcessor.collapse($value);
/* 376 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 15:
/* 381 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 382 */           this.$runtime.consumeAttribute($ai);
/* 383 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 386 */         this.$_ngcc_current_state = 11;
/* 387 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 393 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 394 */           this.$runtime.consumeAttribute($ai);
/* 395 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 398 */         this.$_ngcc_current_state = 10;
/* 399 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 405 */         this.$_ngcc_current_state = 7;
/* 406 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 411 */         action0();
/* 412 */         this.$_ngcc_current_state = 2;
/* 413 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 17:
/* 418 */         this.finalValue = $value;
/* 419 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 424 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 431 */     switch ($__cookie__) {
/*     */       
/*     */       case 89:
/* 434 */         this.annotation = (AnnotationImpl)$__result__;
/* 435 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 91:
/* 440 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 441 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 85:
/* 446 */         this.result = (SimpleTypeImpl)$__result__;
/* 447 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 86:
/* 452 */         this.result = (SimpleTypeImpl)$__result__;
/* 453 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 80:
/* 458 */         this.result = (SimpleTypeImpl)$__result__;
/* 459 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 466 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set makeFinalSet(String finalValue) {
/* 478 */     if (finalValue == null) {
/* 479 */       return Collections.EMPTY_SET;
/*     */     }
/* 481 */     Set<XSVariety> s = new HashSet();
/* 482 */     StringTokenizer tokens = new StringTokenizer(finalValue);
/* 483 */     while (tokens.hasMoreTokens()) {
/* 484 */       String token = tokens.nextToken();
/* 485 */       if (token.equals("#all")) {
/* 486 */         s.add(XSVariety.ATOMIC);
/* 487 */         s.add(XSVariety.UNION);
/* 488 */         s.add(XSVariety.LIST);
/*     */       } 
/* 490 */       if (token.equals("list")) {
/* 491 */         s.add(XSVariety.LIST);
/*     */       }
/* 493 */       if (token.equals("union")) {
/* 494 */         s.add(XSVariety.UNION);
/*     */       }
/* 496 */       if (token.equals("restriction")) {
/* 497 */         s.add(XSVariety.ATOMIC);
/*     */       }
/*     */     } 
/* 500 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\simpleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */