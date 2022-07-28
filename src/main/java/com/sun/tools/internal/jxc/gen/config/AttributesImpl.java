/*     */ package com.sun.tools.internal.jxc.gen.config;
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
/*  89 */     this.length = 0;
/*  90 */     this.data = null;
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
/* 104 */     setAttributes(atts);
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
/* 122 */     return this.length;
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
/* 136 */     if (index >= 0 && index < this.length) {
/* 137 */       return this.data[index * 5];
/*     */     }
/* 139 */     return null;
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
/* 154 */     if (index >= 0 && index < this.length) {
/* 155 */       return this.data[index * 5 + 1];
/*     */     }
/* 157 */     return null;
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
/* 172 */     if (index >= 0 && index < this.length) {
/* 173 */       return this.data[index * 5 + 2];
/*     */     }
/* 175 */     return null;
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
/* 190 */     if (index >= 0 && index < this.length) {
/* 191 */       return this.data[index * 5 + 3];
/*     */     }
/* 193 */     return null;
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
/* 207 */     if (index >= 0 && index < this.length) {
/* 208 */       return this.data[index * 5 + 4];
/*     */     }
/* 210 */     return null;
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
/* 230 */     int max = this.length * 5;
/* 231 */     for (int i = 0; i < max; i += 5) {
/* 232 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 233 */         return i / 5;
/*     */       }
/*     */     } 
/* 236 */     return -1;
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
/* 249 */     int max = this.length * 5;
/* 250 */     for (int i = 0; i < max; i += 5) {
/* 251 */       if (this.data[i + 2].equals(qName)) {
/* 252 */         return i / 5;
/*     */       }
/*     */     } 
/* 255 */     return -1;
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
/* 271 */     int max = this.length * 5;
/* 272 */     for (int i = 0; i < max; i += 5) {
/* 273 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 274 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 277 */     return null;
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
/* 291 */     int max = this.length * 5;
/* 292 */     for (int i = 0; i < max; i += 5) {
/* 293 */       if (this.data[i + 2].equals(qName)) {
/* 294 */         return this.data[i + 3];
/*     */       }
/*     */     } 
/* 297 */     return null;
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
/* 313 */     int max = this.length * 5;
/* 314 */     for (int i = 0; i < max; i += 5) {
/* 315 */       if (this.data[i].equals(uri) && this.data[i + 1].equals(localName)) {
/* 316 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 319 */     return null;
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
/* 333 */     int max = this.length * 5;
/* 334 */     for (int i = 0; i < max; i += 5) {
/* 335 */       if (this.data[i + 2].equals(qName)) {
/* 336 */         return this.data[i + 4];
/*     */       }
/*     */     } 
/* 339 */     return null;
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
/* 358 */     this.length = 0;
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
/* 372 */     clear();
/* 373 */     this.length = atts.getLength();
/* 374 */     this.data = new String[this.length * 5];
/* 375 */     for (int i = 0; i < this.length; i++) {
/* 376 */       this.data[i * 5] = atts.getURI(i);
/* 377 */       this.data[i * 5 + 1] = atts.getLocalName(i);
/* 378 */       this.data[i * 5 + 2] = atts.getQName(i);
/* 379 */       this.data[i * 5 + 3] = atts.getType(i);
/* 380 */       this.data[i * 5 + 4] = atts.getValue(i);
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
/* 405 */     ensureCapacity(this.length + 1);
/* 406 */     this.data[this.length * 5] = uri;
/* 407 */     this.data[this.length * 5 + 1] = localName;
/* 408 */     this.data[this.length * 5 + 2] = qName;
/* 409 */     this.data[this.length * 5 + 3] = type;
/* 410 */     this.data[this.length * 5 + 4] = value;
/* 411 */     this.length++;
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
/* 439 */     if (index >= 0 && index < this.length) {
/* 440 */       this.data[index * 5] = uri;
/* 441 */       this.data[index * 5 + 1] = localName;
/* 442 */       this.data[index * 5 + 2] = qName;
/* 443 */       this.data[index * 5 + 3] = type;
/* 444 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 446 */       badIndex(index);
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
/* 461 */     if (index >= 0 && index < this.length) {
/* 462 */       if (index < this.length - 1) {
/* 463 */         System.arraycopy(this.data, (index + 1) * 5, this.data, index * 5, (this.length - index - 1) * 5);
/*     */       }
/*     */       
/* 466 */       this.length--;
/*     */     } else {
/* 468 */       badIndex(index);
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
/* 485 */     if (index >= 0 && index < this.length) {
/* 486 */       this.data[index * 5] = uri;
/*     */     } else {
/* 488 */       badIndex(index);
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
/* 505 */     if (index >= 0 && index < this.length) {
/* 506 */       this.data[index * 5 + 1] = localName;
/*     */     } else {
/* 508 */       badIndex(index);
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
/* 525 */     if (index >= 0 && index < this.length) {
/* 526 */       this.data[index * 5 + 2] = qName;
/*     */     } else {
/* 528 */       badIndex(index);
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
/* 544 */     if (index >= 0 && index < this.length) {
/* 545 */       this.data[index * 5 + 3] = type;
/*     */     } else {
/* 547 */       badIndex(index);
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
/* 563 */     if (index >= 0 && index < this.length) {
/* 564 */       this.data[index * 5 + 4] = value;
/*     */     } else {
/* 566 */       badIndex(index);
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
/* 585 */     if (n > 0 && (this.data == null || this.data.length == 0)) {
/* 586 */       this.data = new String[25];
/*     */     }
/*     */     
/* 589 */     int max = this.data.length;
/* 590 */     if (max >= n * 5) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 595 */     while (max < n * 5) {
/* 596 */       max *= 2;
/*     */     }
/* 598 */     String[] newData = new String[max];
/* 599 */     System.arraycopy(this.data, 0, newData, 0, this.length * 5);
/* 600 */     this.data = newData;
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
/* 613 */     String msg = "Attempt to modify attribute at illegal index: " + index;
/*     */     
/* 615 */     throw new ArrayIndexOutOfBoundsException(msg);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\AttributesImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */