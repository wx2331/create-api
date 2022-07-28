/*    */ package com.sun.tools.jdi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InternalEventHandler
/*    */   implements Runnable
/*    */ {
/*    */   EventQueueImpl queue;
/*    */   VirtualMachineImpl vm;
/*    */   
/*    */   InternalEventHandler(VirtualMachineImpl paramVirtualMachineImpl, EventQueueImpl paramEventQueueImpl) {
/* 39 */     this.vm = paramVirtualMachineImpl;
/* 40 */     this.queue = paramEventQueueImpl;
/* 41 */     Thread thread = new Thread(paramVirtualMachineImpl.threadGroupForJDI(), this, "JDI Internal Event Handler");
/*    */     
/* 43 */     thread.setDaemon(true);
/* 44 */     thread.start();
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   4: getfield traceFlags : I
/*    */     //   7: iconst_4
/*    */     //   8: iand
/*    */     //   9: ifeq -> 21
/*    */     //   12: aload_0
/*    */     //   13: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   16: ldc 'Internal event handler running'
/*    */     //   18: invokevirtual printTrace : (Ljava/lang/String;)V
/*    */     //   21: aload_0
/*    */     //   22: getfield queue : Lcom/sun/tools/jdi/EventQueueImpl;
/*    */     //   25: invokevirtual removeInternal : ()Lcom/sun/jdi/event/EventSet;
/*    */     //   28: astore_1
/*    */     //   29: aload_1
/*    */     //   30: invokeinterface eventIterator : ()Lcom/sun/jdi/event/EventIterator;
/*    */     //   35: astore_2
/*    */     //   36: aload_2
/*    */     //   37: invokeinterface hasNext : ()Z
/*    */     //   42: ifeq -> 204
/*    */     //   45: aload_2
/*    */     //   46: invokeinterface nextEvent : ()Lcom/sun/jdi/event/Event;
/*    */     //   51: astore_3
/*    */     //   52: aload_3
/*    */     //   53: instanceof com/sun/jdi/event/ClassUnloadEvent
/*    */     //   56: ifeq -> 126
/*    */     //   59: aload_3
/*    */     //   60: checkcast com/sun/jdi/event/ClassUnloadEvent
/*    */     //   63: astore #4
/*    */     //   65: aload_0
/*    */     //   66: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   69: aload #4
/*    */     //   71: invokeinterface classSignature : ()Ljava/lang/String;
/*    */     //   76: invokevirtual removeReferenceType : (Ljava/lang/String;)V
/*    */     //   79: aload_0
/*    */     //   80: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   83: getfield traceFlags : I
/*    */     //   86: iconst_4
/*    */     //   87: iand
/*    */     //   88: ifeq -> 123
/*    */     //   91: aload_0
/*    */     //   92: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   95: new java/lang/StringBuilder
/*    */     //   98: dup
/*    */     //   99: invokespecial <init> : ()V
/*    */     //   102: ldc 'Handled Unload Event for '
/*    */     //   104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   107: aload #4
/*    */     //   109: invokeinterface classSignature : ()Ljava/lang/String;
/*    */     //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   117: invokevirtual toString : ()Ljava/lang/String;
/*    */     //   120: invokevirtual printTrace : (Ljava/lang/String;)V
/*    */     //   123: goto -> 201
/*    */     //   126: aload_3
/*    */     //   127: instanceof com/sun/jdi/event/ClassPrepareEvent
/*    */     //   130: ifeq -> 201
/*    */     //   133: aload_3
/*    */     //   134: checkcast com/sun/jdi/event/ClassPrepareEvent
/*    */     //   137: astore #4
/*    */     //   139: aload #4
/*    */     //   141: invokeinterface referenceType : ()Lcom/sun/jdi/ReferenceType;
/*    */     //   146: checkcast com/sun/tools/jdi/ReferenceTypeImpl
/*    */     //   149: invokevirtual markPrepared : ()V
/*    */     //   152: aload_0
/*    */     //   153: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   156: getfield traceFlags : I
/*    */     //   159: iconst_4
/*    */     //   160: iand
/*    */     //   161: ifeq -> 201
/*    */     //   164: aload_0
/*    */     //   165: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   168: new java/lang/StringBuilder
/*    */     //   171: dup
/*    */     //   172: invokespecial <init> : ()V
/*    */     //   175: ldc 'Handled Prepare Event for '
/*    */     //   177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   180: aload #4
/*    */     //   182: invokeinterface referenceType : ()Lcom/sun/jdi/ReferenceType;
/*    */     //   187: invokeinterface name : ()Ljava/lang/String;
/*    */     //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   195: invokevirtual toString : ()Ljava/lang/String;
/*    */     //   198: invokevirtual printTrace : (Ljava/lang/String;)V
/*    */     //   201: goto -> 36
/*    */     //   204: goto -> 21
/*    */     //   207: astore_1
/*    */     //   208: aload_1
/*    */     //   209: invokevirtual printStackTrace : ()V
/*    */     //   212: goto -> 21
/*    */     //   215: astore_1
/*    */     //   216: aload_1
/*    */     //   217: invokevirtual printStackTrace : ()V
/*    */     //   220: goto -> 21
/*    */     //   223: astore_1
/*    */     //   224: aload_1
/*    */     //   225: invokevirtual printStackTrace : ()V
/*    */     //   228: goto -> 21
/*    */     //   231: astore_1
/*    */     //   232: aload_1
/*    */     //   233: invokevirtual printStackTrace : ()V
/*    */     //   236: goto -> 21
/*    */     //   239: astore_1
/*    */     //   240: goto -> 244
/*    */     //   243: astore_1
/*    */     //   244: aload_0
/*    */     //   245: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   248: getfield traceFlags : I
/*    */     //   251: iconst_4
/*    */     //   252: iand
/*    */     //   253: ifeq -> 265
/*    */     //   256: aload_0
/*    */     //   257: getfield vm : Lcom/sun/tools/jdi/VirtualMachineImpl;
/*    */     //   260: ldc 'Internal event handler exiting'
/*    */     //   262: invokevirtual printTrace : (Ljava/lang/String;)V
/*    */     //   265: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     //   #49	-> 12
/*    */     //   #54	-> 21
/*    */     //   #55	-> 29
/*    */     //   #56	-> 36
/*    */     //   #57	-> 45
/*    */     //   #58	-> 52
/*    */     //   #59	-> 59
/*    */     //   #60	-> 65
/*    */     //   #62	-> 79
/*    */     //   #63	-> 91
/*    */     //   #64	-> 109
/*    */     //   #63	-> 120
/*    */     //   #66	-> 123
/*    */     //   #67	-> 133
/*    */     //   #68	-> 139
/*    */     //   #69	-> 149
/*    */     //   #71	-> 152
/*    */     //   #72	-> 164
/*    */     //   #73	-> 182
/*    */     //   #72	-> 198
/*    */     //   #77	-> 201
/*    */     //   #104	-> 204
/*    */     //   #87	-> 207
/*    */     //   #88	-> 208
/*    */     //   #104	-> 212
/*    */     //   #89	-> 215
/*    */     //   #90	-> 216
/*    */     //   #104	-> 220
/*    */     //   #100	-> 223
/*    */     //   #101	-> 224
/*    */     //   #104	-> 228
/*    */     //   #102	-> 231
/*    */     //   #103	-> 232
/*    */     //   #104	-> 236
/*    */     //   #106	-> 239
/*    */     //   #108	-> 240
/*    */     //   #107	-> 243
/*    */     //   #109	-> 244
/*    */     //   #110	-> 256
/*    */     //   #112	-> 265
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   21	204	207	com/sun/jdi/VMOutOfMemoryException
/*    */     //   21	204	215	com/sun/jdi/InconsistentDebugInfoException
/*    */     //   21	204	223	com/sun/jdi/ObjectCollectedException
/*    */     //   21	204	231	com/sun/jdi/ClassNotPreparedException
/*    */     //   21	239	239	java/lang/InterruptedException
/*    */     //   21	239	243	com/sun/jdi/VMDisconnectedException
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\InternalEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */