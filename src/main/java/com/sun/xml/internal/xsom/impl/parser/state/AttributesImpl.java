/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
/*     */ import org.xml.sax.Attributes;
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
/*     */ 
/*     */ public class AttributesImpl
/*     */   implements Attributes
/*     */ {
/*     */   int length;
/*     */   String[] data;
/*     */   
/*     */   public AttributesImpl() {
/*  86 */     this.length = 0;
/*  87 */     this.data = null;
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
/*     */ 
/*     */   
/*     */   public AttributesImpl(Attributes atts) {
/* 101 */     setAttributes(atts);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 119 */     return this.length;
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
/*     */ 
/*     */   
/*     */   public String getURI(int index) {
/* 133 */     if (index >= 0 && index < this.length) {
/* 134 */       return this.data[index * 5];
/*     */     }
/* 136 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalName(int index) {
/* 151 */     if (index >= 0 && index < this.length) {
/* 152 */       return this.data[index * 5 + 1];
/*     */     }
/* 154 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQName(int index) {
/* 169 */     if (index >= 0 && index < this.length) {
/* 170 */       return this.data[index * 5 + 2];
/*     */     }
/* 172 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(int index) {
/* 187 */     if (index >= 0 && index < this.length) {
/* 188 */       return this.data[index * 5 + 3];
/*     */     }
/* 190 */     return null;
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
/*     */ 
/*     */   
/*     */   public String getValue(int index) {
/* 204 */     if (index >= 0 && index < this.length) {
/* 205 */       return this.data[index * 5 + 4];
/*     */     }
/* 207 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex(String uri, String localName) {
/* 227 */     int max = this.length * 5;
/* 228 */     for (int i = 0; i < max; i += 5) {
/* 229 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 230 */         return i / 5;
/*     */       }
/*     */     } 
/* 233 */     return -1;
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
/*     */   
/*     */   public int getIndex(String qName) {
/* 246 */     int max = this.length * 5;
/* 247 */     for (int i = 0; i < max; i += 5) {
/* 248 */       if (this.data[i + 2].equals(qName)) {
/* 249 */         return i / 5;
/*     */       }
/*     */     } 
/* 252 */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(String uri, String localName) {
/* 268 */     int max = this.length * 5;
/* 269 */     for (int i = 0; i < max; i += 5) {
/* 270 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 271 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 274 */     return null;
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
/*     */ 
/*     */   
/*     */   public String getType(String qName) {
/* 288 */     int max = this.length * 5;
/* 289 */     for (int i = 0; i < max; i += 5) {
/* 290 */       if (this.data[i + 2].equals(qName)) {
/* 291 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 294 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue(String uri, String localName) {
/* 310 */     int max = this.length * 5;
/* 311 */     for (int i = 0; i < max; i += 5) {
/* 312 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 313 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 316 */     return null;
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
/*     */ 
/*     */   
/*     */   public String getValue(String qName) {
/* 330 */     int max = this.length * 5;
/* 331 */     for (int i = 0; i < max; i += 5) {
/* 332 */       if (this.data[i + 2].equals(qName)) {
/* 333 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 336 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 355 */     this.length = 0;
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
/*     */ 
/*     */   
/*     */   public void setAttributes(Attributes atts) {
/* 369 */     clear();
/* 370 */     this.length = atts.getLength();
/* 371 */     this.data = new String[this.length * 5];
/* 372 */     for (int i = 0; i < this.length; i++) {
/* 373 */       this.data[i * 5] = atts.getURI(i);
/* 374 */       this.data[i * 5 + 1] = atts.getLocalName(i);
/* 375 */       this.data[i * 5 + 2] = atts.getQName(i);
/* 376 */       this.data[i * 5 + 3] = atts.getType(i);
/* 377 */       this.data[i * 5 + 4] = atts.getValue(i);
/*     */     } 
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
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value) {
/* 402 */     ensureCapacity(this.length + 1);
/* 403 */     this.data[this.length * 5] = uri;
/* 404 */     this.data[this.length * 5 + 1] = localName;
/* 405 */     this.data[this.length * 5 + 2] = qName;
/* 406 */     this.data[this.length * 5 + 3] = type;
/* 407 */     this.data[this.length * 5 + 4] = value;
/* 408 */     this.length++;
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
/*     */   public void setAttribute(int index, String uri, String localName, String qName, String type, String value) {
/* 436 */     if (index >= 0 && index < this.length) {
/* 437 */       this.data[index * 5] = uri;
/* 438 */       this.data[index * 5 + 1] = localName;
/* 439 */       this.data[index * 5 + 2] = qName;
/* 440 */       this.data[index * 5 + 3] = type;
/* 441 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 443 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public void removeAttribute(int index) {
/* 458 */     if (index >= 0 && index < this.length) {
/* 459 */       if (index < this.length - 1) {
/* 460 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */       
/* 463 */       this.length--;
/*     */     } else {
/* 465 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setURI(int index, String uri) {
/* 482 */     if (index >= 0 && index < this.length) {
/* 483 */       this.data[index * 5] = uri;
/*     */     } else {
/* 485 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalName(int index, String localName) {
/* 502 */     if (index >= 0 && index < this.length) {
/* 503 */       this.data[index * 5 + 1] = localName;
/*     */     } else {
/* 505 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQName(int index, String qName) {
/* 522 */     if (index >= 0 && index < this.length) {
/* 523 */       this.data[index * 5 + 2] = qName;
/*     */     } else {
/* 525 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int index, String type) {
/* 541 */     if (index >= 0 && index < this.length) {
/* 542 */       this.data[index * 5 + 3] = type;
/*     */     } else {
/* 544 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int index, String value) {
/* 560 */     if (index >= 0 && index < this.length) {
/* 561 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 563 */       badIndex(index);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureCapacity(int n) {
/* 582 */     if (n > 0 && (this.data == null || this.data.length == 0)) {
/* 583 */       this.data = new String[25];
/*     */     }
/*     */     
/* 586 */     int max = this.data.length;
/* 587 */     if (max >= n * 5) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 592 */     while (max < n * 5) {
/* 593 */       max *= 2;
/*     */     }
/* 595 */     String[] newData = new String[max];
/* 596 */     System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/* 597 */     this.data = newData;
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
/*     */   
/*     */   private void badIndex(int index) throws ArrayIndexOutOfBoundsException {
/* 610 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */     
/* 612 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\AttributesImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */