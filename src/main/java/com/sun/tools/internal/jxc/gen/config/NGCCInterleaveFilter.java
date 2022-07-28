/*     */ package com.sun.tools.internal.jxc.gen.config;
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
/*  92 */     this.lockCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     this.isJoining = false;
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
/* 210 */   public void joinByEnterElement(NGCCEventReceiver source, String uri, String local, String qname, Attributes atts) throws SAXException { if (this.isJoining)
/* 211 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     for (int i = 0; i < this._receivers.length; i++) {
/* 219 */       if (this._receivers[i] != source) {
/* 220 */         this._receivers[i].enterElement(uri, local, qname, atts);
/*     */       }
/*     */     } 
/* 223 */     this._parent._source.replace(this, this._parent);
/* 224 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 226 */     this._parent.enterElement(uri, local, qname, atts); }
/*     */   public void leaveAttribute(String uri, String localName, String qname) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount-- == 0) {
/*     */       joinByLeaveAttribute(null, uri, localName, qname);
/*     */     } else {
/*     */       this._receivers[this.lockedReceiver].leaveAttribute(uri, localName, qname);
/* 232 */     }  } public void joinByLeaveElement(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 233 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     for (int i = 0; i < this._receivers.length; i++) {
/* 241 */       if (this._receivers[i] != source) {
/* 242 */         this._receivers[i].leaveElement(uri, local, qname);
/*     */       }
/*     */     } 
/* 245 */     this._parent._source.replace(this, this._parent);
/* 246 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 248 */     this._parent.leaveElement(uri, local, qname); }
/*     */   public void text(String value) throws SAXException { if (this.isJoining)
/*     */       return;  if (this.lockCount != 0) { this._receivers[this.lockedReceiver].text(value); }
/*     */     else { int receiver = findReceiverOfText(); if (receiver != -1) { this._receivers[receiver].text(value); }
/*     */       else { joinByText(null, value); }
/*     */        }
/* 254 */      } protected abstract int findReceiverOfElement(String paramString1, String paramString2); protected abstract int findReceiverOfAttribute(String paramString1, String paramString2); protected abstract int findReceiverOfText(); public void joinByEnterAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException { if (this.isJoining)
/* 255 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     for (int i = 0; i < this._receivers.length; i++) {
/* 263 */       if (this._receivers[i] != source) {
/* 264 */         this._receivers[i].enterAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 267 */     this._parent._source.replace(this, this._parent);
/* 268 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 270 */     this._parent.enterAttribute(uri, local, qname); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByLeaveAttribute(NGCCEventReceiver source, String uri, String local, String qname) throws SAXException {
/* 276 */     if (this.isJoining)
/* 277 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     for (int i = 0; i < this._receivers.length; i++) {
/* 285 */       if (this._receivers[i] != source) {
/* 286 */         this._receivers[i].leaveAttribute(uri, local, qname);
/*     */       }
/*     */     } 
/* 289 */     this._parent._source.replace(this, this._parent);
/* 290 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 292 */     this._parent.leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void joinByText(NGCCEventReceiver source, String value) throws SAXException {
/* 298 */     if (this.isJoining)
/* 299 */       return;  this.isJoining = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     for (int i = 0; i < this._receivers.length; i++) {
/* 307 */       if (this._receivers[i] != source) {
/* 308 */         this._receivers[i].text(value);
/*     */       }
/*     */     } 
/* 311 */     this._parent._source.replace(this, this._parent);
/* 312 */     this._parent.onChildCompleted((Object)null, this._cookie, true);
/*     */     
/* 314 */     this._parent.text(value);
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
/* 328 */     this._receivers[threadId].enterAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEnterElement(int threadId, String uri, String local, String qname, Attributes atts) throws SAXException {
/* 334 */     this._receivers[threadId].enterElement(uri, local, qname, atts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveAttribute(int threadId, String uri, String local, String qname) throws SAXException {
/* 340 */     this._receivers[threadId].leaveAttribute(uri, local, qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendLeaveElement(int threadId, String uri, String local, String qname) throws SAXException {
/* 346 */     this._receivers[threadId].leaveElement(uri, local, qname);
/*     */   }
/*     */   
/*     */   public void sendText(int threadId, String value) throws SAXException {
/* 350 */     this._receivers[threadId].text(value);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\NGCCInterleaveFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */