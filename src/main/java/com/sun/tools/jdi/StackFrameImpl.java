/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.ClassNotLoadedException;
/*     */ import com.sun.jdi.IncompatibleThreadStateException;
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.InvalidStackFrameException;
/*     */ import com.sun.jdi.InvalidTypeException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.Mirror;
/*     */ import com.sun.jdi.ObjectReference;
/*     */ import com.sun.jdi.StackFrame;
/*     */ import com.sun.jdi.ThreadReference;
/*     */ import com.sun.jdi.Value;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StackFrameImpl
/*     */   extends MirrorImpl
/*     */   implements StackFrame, ThreadListener
/*     */ {
/*     */   private boolean isValid = true;
/*     */   private final ThreadReferenceImpl thread;
/*     */   private final long id;
/*     */   private final Location location;
/*  49 */   private Map<String, LocalVariable> visibleVariables = null;
/*  50 */   private ObjectReference thisObject = null;
/*     */ 
/*     */   
/*     */   StackFrameImpl(VirtualMachine paramVirtualMachine, ThreadReferenceImpl paramThreadReferenceImpl, long paramLong, Location paramLocation) {
/*  54 */     super(paramVirtualMachine);
/*  55 */     this.thread = paramThreadReferenceImpl;
/*  56 */     this.id = paramLong;
/*  57 */     this.location = paramLocation;
/*  58 */     paramThreadReferenceImpl.addListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean threadResumable(ThreadAction paramThreadAction) {
/*  67 */     synchronized (this.vm.state()) {
/*  68 */       if (this.isValid) {
/*  69 */         this.isValid = false;
/*  70 */         return false;
/*     */       } 
/*  72 */       throw new InternalException("Invalid stack frame thread listener");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void validateStackFrame() {
/*  79 */     if (!this.isValid) {
/*  80 */       throw new InvalidStackFrameException("Thread has been resumed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location location() {
/*  89 */     validateStackFrame();
/*  90 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadReference thread() {
/*  98 */     validateStackFrame();
/*  99 */     return this.thread;
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 103 */     if (paramObject != null && paramObject instanceof StackFrameImpl) {
/* 104 */       StackFrameImpl stackFrameImpl = (StackFrameImpl)paramObject;
/* 105 */       return (this.id == stackFrameImpl.id && 
/* 106 */         thread().equals(stackFrameImpl.thread()) && 
/* 107 */         location().equals(stackFrameImpl.location()) && super
/* 108 */         .equals(paramObject));
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     return (thread().hashCode() << 4) + (int)this.id;
/*     */   }
/*     */   
/*     */   public ObjectReference thisObject() {
/* 119 */     validateStackFrame();
/* 120 */     MethodImpl methodImpl = (MethodImpl)this.location.method();
/* 121 */     if (methodImpl.isStatic() || methodImpl.isNative()) {
/* 122 */       return null;
/*     */     }
/* 124 */     if (this.thisObject == null) {
/*     */       PacketStream packetStream;
/*     */ 
/*     */       
/* 128 */       synchronized (this.vm.state()) {
/* 129 */         validateStackFrame();
/*     */         
/* 131 */         packetStream = JDWP.StackFrame.ThisObject.enqueueCommand(this.vm, this.thread, this.id);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 136 */         this
/* 137 */           .thisObject = (JDWP.StackFrame.ThisObject.waitForReply(this.vm, packetStream)).objectThis;
/* 138 */       } catch (JDWPException jDWPException) {
/* 139 */         switch (jDWPException.errorCode()) {
/*     */           case 10:
/*     */           case 13:
/*     */           case 30:
/* 143 */             throw new InvalidStackFrameException();
/*     */         } 
/* 145 */         throw jDWPException.toJDIException();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 150 */     return this.thisObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createVisibleVariables() throws AbsentInformationException {
/* 158 */     if (this.visibleVariables == null) {
/* 159 */       List list = this.location.method().variables();
/* 160 */       HashMap<Object, Object> hashMap = new HashMap<>(list.size());
/*     */       
/* 162 */       for (LocalVariable localVariable : list) {
/* 163 */         String str = localVariable.name();
/* 164 */         if (localVariable.isVisible(this)) {
/* 165 */           LocalVariable localVariable1 = (LocalVariable)hashMap.get(str);
/* 166 */           if (localVariable1 == null || ((LocalVariableImpl)localVariable)
/* 167 */             .hides(localVariable1)) {
/* 168 */             hashMap.put(str, localVariable);
/*     */           }
/*     */         } 
/*     */       } 
/* 172 */       this.visibleVariables = (Map)hashMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<LocalVariable> visibleVariables() throws AbsentInformationException {
/* 181 */     validateStackFrame();
/* 182 */     createVisibleVariables();
/* 183 */     ArrayList<Comparable> arrayList = new ArrayList(this.visibleVariables.values());
/* 184 */     Collections.sort(arrayList);
/* 185 */     return (List)arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalVariable visibleVariableByName(String paramString) throws AbsentInformationException {
/* 193 */     validateStackFrame();
/* 194 */     createVisibleVariables();
/* 195 */     return this.visibleVariables.get(paramString);
/*     */   }
/*     */   
/*     */   public Value getValue(LocalVariable paramLocalVariable) {
/* 199 */     ArrayList<LocalVariable> arrayList = new ArrayList(1);
/* 200 */     arrayList.add(paramLocalVariable);
/* 201 */     return getValues(arrayList).get(paramLocalVariable);
/*     */   } public Map<LocalVariable, Value> getValues(List<? extends LocalVariable> paramList) {
/*     */     PacketStream packetStream;
/*     */     ValueImpl[] arrayOfValueImpl;
/* 205 */     validateStackFrame();
/* 206 */     validateMirrors((Collection)paramList);
/*     */     
/* 208 */     int i = paramList.size();
/* 209 */     JDWP.StackFrame.GetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.GetValues.SlotInfo[i];
/*     */ 
/*     */     
/* 212 */     for (byte b1 = 0; b1 < i; b1++) {
/* 213 */       LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramList.get(b1);
/* 214 */       if (!localVariableImpl.isVisible(this)) {
/* 215 */         throw new IllegalArgumentException(localVariableImpl.name() + " is not valid at this frame location");
/*     */       }
/*     */       
/* 218 */       arrayOfSlotInfo[b1] = new JDWP.StackFrame.GetValues.SlotInfo(localVariableImpl.slot(), 
/* 219 */           (byte)localVariableImpl.signature().charAt(0));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     synchronized (this.vm.state()) {
/* 226 */       validateStackFrame();
/* 227 */       packetStream = JDWP.StackFrame.GetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 233 */       arrayOfValueImpl = (JDWP.StackFrame.GetValues.waitForReply(this.vm, packetStream)).values;
/* 234 */     } catch (JDWPException jDWPException) {
/* 235 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/*     */         case 30:
/* 239 */           throw new InvalidStackFrameException();
/*     */       } 
/* 241 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */     
/* 245 */     if (i != arrayOfValueImpl.length) {
/* 246 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */     
/* 249 */     HashMap<Object, Object> hashMap = new HashMap<>(i);
/* 250 */     for (byte b2 = 0; b2 < i; b2++) {
/* 251 */       LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramList.get(b2);
/* 252 */       hashMap.put(localVariableImpl, arrayOfValueImpl[b2]);
/*     */     } 
/* 254 */     return (Map)hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(LocalVariable paramLocalVariable, Value paramValue) throws InvalidTypeException, ClassNotLoadedException {
/* 260 */     validateStackFrame();
/* 261 */     validateMirror((Mirror)paramLocalVariable);
/* 262 */     validateMirrorOrNull((Mirror)paramValue);
/*     */     
/* 264 */     LocalVariableImpl localVariableImpl = (LocalVariableImpl)paramLocalVariable;
/* 265 */     ValueImpl valueImpl = (ValueImpl)paramValue;
/*     */     
/* 267 */     if (!localVariableImpl.isVisible(this)) {
/* 268 */       throw new IllegalArgumentException(localVariableImpl.name() + " is not valid at this frame location");
/*     */     }
/*     */     
/*     */     try {
/*     */       PacketStream packetStream;
/*     */       
/* 274 */       valueImpl = ValueImpl.prepareForAssignment(valueImpl, localVariableImpl);
/*     */       
/* 276 */       JDWP.StackFrame.SetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.SetValues.SlotInfo[1];
/*     */       
/* 278 */       arrayOfSlotInfo[0] = new JDWP.StackFrame.SetValues.SlotInfo(localVariableImpl
/* 279 */           .slot(), valueImpl);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 284 */       synchronized (this.vm.state()) {
/* 285 */         validateStackFrame();
/*     */         
/* 287 */         packetStream = JDWP.StackFrame.SetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 292 */         JDWP.StackFrame.SetValues.waitForReply(this.vm, packetStream);
/* 293 */       } catch (JDWPException jDWPException) {
/* 294 */         switch (jDWPException.errorCode()) {
/*     */           case 10:
/*     */           case 13:
/*     */           case 30:
/* 298 */             throw new InvalidStackFrameException();
/*     */         } 
/* 300 */         throw jDWPException.toJDIException();
/*     */       }
/*     */     
/* 303 */     } catch (ClassNotLoadedException classNotLoadedException) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 312 */       if (valueImpl != null)
/* 313 */         throw classNotLoadedException; 
/*     */     } 
/*     */   } public List<Value> getArgumentValues() {
/*     */     byte b1;
/*     */     PacketStream packetStream;
/*     */     ValueImpl[] arrayOfValueImpl;
/* 319 */     validateStackFrame();
/* 320 */     MethodImpl methodImpl = (MethodImpl)this.location.method();
/* 321 */     List<String> list = methodImpl.argumentSignatures();
/* 322 */     int i = list.size();
/* 323 */     JDWP.StackFrame.GetValues.SlotInfo[] arrayOfSlotInfo = new JDWP.StackFrame.GetValues.SlotInfo[i];
/*     */ 
/*     */ 
/*     */     
/* 327 */     if (methodImpl.isStatic()) {
/* 328 */       b1 = 0;
/*     */     } else {
/* 330 */       b1 = 1;
/*     */     } 
/* 332 */     for (byte b2 = 0; b2 < i; b2++) {
/* 333 */       char c = ((String)list.get(b2)).charAt(0);
/* 334 */       arrayOfSlotInfo[b2] = new JDWP.StackFrame.GetValues.SlotInfo(b1++, (byte)c);
/* 335 */       if (c == 'J' || c == 'D') {
/* 336 */         b1++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     synchronized (this.vm.state()) {
/* 344 */       validateStackFrame();
/* 345 */       packetStream = JDWP.StackFrame.GetValues.enqueueCommand(this.vm, this.thread, this.id, arrayOfSlotInfo);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 350 */       arrayOfValueImpl = (JDWP.StackFrame.GetValues.waitForReply(this.vm, packetStream)).values;
/* 351 */     } catch (JDWPException jDWPException) {
/* 352 */       switch (jDWPException.errorCode()) {
/*     */         case 10:
/*     */         case 13:
/*     */         case 30:
/* 356 */           throw new InvalidStackFrameException();
/*     */       } 
/* 358 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */     
/* 362 */     if (i != arrayOfValueImpl.length) {
/* 363 */       throw new InternalException("Wrong number of values returned from target VM");
/*     */     }
/*     */     
/* 366 */     return Arrays.asList((Value[])arrayOfValueImpl);
/*     */   }
/*     */   
/*     */   void pop() throws IncompatibleThreadStateException {
/* 370 */     validateStackFrame();
/*     */     
/* 372 */     CommandSender commandSender = new CommandSender()
/*     */       {
/*     */         public PacketStream send() {
/* 375 */           return JDWP.StackFrame.PopFrames.enqueueCommand(StackFrameImpl.this.vm, StackFrameImpl.this
/* 376 */               .thread, StackFrameImpl.this.id);
/*     */         }
/*     */       };
/*     */     try {
/* 380 */       PacketStream packetStream = this.thread.sendResumingCommand(commandSender);
/* 381 */       JDWP.StackFrame.PopFrames.waitForReply(this.vm, packetStream);
/* 382 */     } catch (JDWPException jDWPException) {
/* 383 */       switch (jDWPException.errorCode()) {
/*     */         case 13:
/* 385 */           throw new IncompatibleThreadStateException("Thread not current or suspended");
/*     */         
/*     */         case 10:
/* 388 */           throw new IncompatibleThreadStateException("zombie");
/*     */         case 31:
/* 390 */           throw new InvalidStackFrameException("No more frames on the stack");
/*     */       } 
/*     */       
/* 393 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 398 */     this.vm.state().freeze();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 402 */     return this.location.toString() + " in thread " + this.thread.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\StackFrameImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */