/*     */ package com.sun.xml.internal.xsom.impl.parser.state;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NGCCInterleaveFilter
/*     */   implements NGCCEventSource, NGCCEventReceiver
/*     */ {
/*     */   protected NGCCEventReceiver[] _receivers;
/*     */   private final NGCCHandler _parent;
/*     */   private final int _cookie;
/*     */   private int lockedReceiver;
/*     */   private int lockCount;
/*     */   private boolean isJoining;
/*     */   
/*     */   protected void setHandlers(NGCCEventReceiver[] receivers) {
/*     */     this._receivers = receivers;
/*     */   }
/*     */   
/*     */   public int replace(NGCCEventReceiver oldHandler, NGCCEventReceiver newHandler) {
/*     */     for (int i = 0; i < this._receivers.length; i++) {
/*     */       if (this._receivers[i] == oldHandler) {
/*     */         this._receivers[i] = newHandler;
/*     */         return i;
/*     */       } 
/*     */     } 
/*     */     throw new InternalError();
/*     */   }
/*     */   
/*     */   public void enterElement(String uri, String localName, String qname, Attributes atts) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount++ == 0) {
/*     */       this.lockedReceiver = findReceiverOfElement(uri, localName);
/*     */       if (this.lockedReceiver == -1) {
/*     */         joinByEnterElement(null, uri, localName, qname, atts);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     this._receivers[this.lockedReceiver].enterElement(uri, localName, qname, atts);
/*     */   }
/*     */   
/*     */   protected NGCCInterleaveFilter(NGCCHandler parent, int cookie) {
/*  89 */     this.lockCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     this.isJoining = false;
/*     */     this._parent = parent;
/*     */     this._cookie = cookie; } public void leaveElement(String uri, String localName, String qname) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount-- == 0) {
/*     */       joinByLeaveElement(null, uri, localName, qname);
/*     */     } else {
/*     */       this._receivers[this.lockedReceiver].leaveElement(uri, localName, qname);
/*     */     } 
/*     */   } public void enterAttribute(String uri, String localName, String qname) throws SAXException {
/*     */     if (this.isJoining)
/*     */       return; 
/*     */     if (this.lockCount++ == 0) {
/*     */       this.lockedReceiver = findReceiverOfAttribute(uri, localName);
/*     */       if (this.lockedReceiver == -1) {
/*     */         joinByEnterAttribute(null, uri, localName, qname);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     this._receivers[this.lockedReceiver].enterAttribute(uri, localName, qname);
/*     */   }
/* 207 */   public void joinByEnterElement(NGCCEventReceiver source, String uri, String local, String qname, Attributes atts) throws SAXException { if (this.isJoining)
/* 208 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     for (int i = 0; i < this._receivers.length; i++) {
/* 216 */       if (this._receivers[i] != source) {
/* 217 */         this._receivers[i].enterElement(uri, local, qname, atts);
/*     */       }
/*     */     } 
/* 220 */     this._parent._source.replace(this, this._parent);
/* 221 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 223 */     this._parent.enterElement(uri, local, qname, atts); }
/*     */   public void leaveAttribute(String uri, String localName, String qname) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount-- == 0) {
/*     */       joinByLeaveAttribute(null, uri, localName, qname);
/*     */     } else {
/*     */       this._receivers[this.lockedReceiver].leaveAttribute(uri, localName, qname);
/* 229 */     }  } public void joinByLeaveElement(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 230 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     for (int i = 0; i < this._receivers.length; i++) {
/* 238 */       if (this._receivers[i] != source) {
/* 239 */         this._receivers[i].leaveElement(uri, local, qname);
/*     */       }
/*     */     } 
/* 242 */     this._parent._source.replace(this, this._parent);
/* 243 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 245 */     this._parent.leaveElement(uri, local, qname); }
/*     */   public void text(String value) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount != 0) { this._receivers[this.lockedReceiver].text(value); }
/*     */     else { int receiver = findReceiverOfText(); if (receiver != -1) { this._receivers[receiver].text(value); }
/*     */       else { joinByText(null, value); }
/*     */        }
/* 251 */      } protected abstract int findReceiverOfElement(String paramString1, String paramString2); protected abstract int findReceiverOfAttribute(String paramString1, String paramString2); protected abstract int findReceiverOfText(); public void joinByEnterAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 252 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     for (int i = 0; i < this._receivers.length; i++) {
/* 260 */       if (this._receivers[i] != source) {
/* 261 */         this._receivers[i].enterAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 264 */     this._parent._source.replace(this, this._parent);
/* 265 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 267 */     this._parent.enterAttribute(uri, local, qname); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByLeaveAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException {
/* 273 */     if (this.isJoining)
/* 274 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     for (int i = 0; i < this._receivers.length; i++) {
/* 282 */       if (this._receivers[i] != source) {
/* 283 */         this._receivers[i].leaveAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 286 */     this._parent._source.replace(this, this._parent);
/* 287 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 289 */     this._parent.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByText(NGCCEventReceiver source, String value) throws SAXException {
/* 295 */     if (this.isJoining)
/* 296 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     for (int i = 0; i < this._receivers.length; i++) {
/* 304 */       if (this._receivers[i] != source) {
/* 305 */         this._receivers[i].text(value);
/*     */       }
/*     */     } 
/* 308 */     this._parent._source.replace(this, this._parent);
/* 309 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 311 */     this._parent.text(value);
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
/*     */   public void sendEnterAttribute(int threadId, String uri, String local, String qname) throws SAXException {
/* 325 */     this._receivers[threadId].enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts) throws SAXException {
/* 331 */     this._receivers[threadId].enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname) throws SAXException {
/* 337 */     this._receivers[threadId].leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname) throws SAXException {
/* 343 */     this._receivers[threadId].leaveElement(uri, local, qname);
/*     */   }
/*     */   
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 347 */     this._receivers[threadId].text(value);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\state\NGCCInterleaveFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */