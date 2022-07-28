/*      */ package com.sun.tools.jdi;
/*      */
/*      */ import com.sun.jdi.Location;
/*      */ import java.util.List;
/*      */
/*      */
/*      */
/*      */
/*      */ class JDWP
/*      */ {
/*      */   static class VirtualMachine
/*      */   {
/*      */     static final int COMMAND_SET = 1;
/*      */
/*      */     static class Version
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final String description;
/*      */       final int jdwpMajor;
/*      */       final int jdwpMinor;
/*      */       final String vmVersion;
/*      */       final String vmName;
/*      */
/*      */       static Version process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*   25 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*   26 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*   30 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 1);
/*   31 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*   32 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Version" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*   34 */         packetStream.send();
/*   35 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Version waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*   40 */         param2PacketStream.waitForReply();
/*   41 */         return new Version(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Version(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*   71 */         if (param2VirtualMachineImpl.traceReceives) {
/*   72 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Version" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*   74 */         this.description = param2PacketStream.readString();
/*   75 */         if (param2VirtualMachineImpl.traceReceives) {
/*   76 */           param2VirtualMachineImpl.printReceiveTrace(4, "description(String): " + this.description);
/*      */         }
/*   78 */         this.jdwpMajor = param2PacketStream.readInt();
/*   79 */         if (param2VirtualMachineImpl.traceReceives) {
/*   80 */           param2VirtualMachineImpl.printReceiveTrace(4, "jdwpMajor(int): " + this.jdwpMajor);
/*      */         }
/*   82 */         this.jdwpMinor = param2PacketStream.readInt();
/*   83 */         if (param2VirtualMachineImpl.traceReceives) {
/*   84 */           param2VirtualMachineImpl.printReceiveTrace(4, "jdwpMinor(int): " + this.jdwpMinor);
/*      */         }
/*   86 */         this.vmVersion = param2PacketStream.readString();
/*   87 */         if (param2VirtualMachineImpl.traceReceives) {
/*   88 */           param2VirtualMachineImpl.printReceiveTrace(4, "vmVersion(String): " + this.vmVersion);
/*      */         }
/*   90 */         this.vmName = param2PacketStream.readString();
/*   91 */         if (param2VirtualMachineImpl.traceReceives) {
/*   92 */           param2VirtualMachineImpl.printReceiveTrace(4, "vmName(String): " + this.vmName);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class ClassesBySignature
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */
/*      */       final ClassInfo[] classes;
/*      */
/*      */
/*      */
/*      */       static ClassesBySignature process(VirtualMachineImpl param2VirtualMachineImpl, String param2String) throws JDWPException {
/*  111 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2String);
/*  112 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, String param2String) {
/*  117 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 2);
/*  118 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  119 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.ClassesBySignature" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  121 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/*  122 */           packetStream.vm.printTrace("Sending:                 signature(String): " + param2String);
/*      */         }
/*  124 */         packetStream.writeString(param2String);
/*  125 */         packetStream.send();
/*  126 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClassesBySignature waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  131 */         param2PacketStream.waitForReply();
/*  132 */         return new ClassesBySignature(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */
/*      */
/*      */
/*      */         final long typeID;
/*      */
/*      */
/*      */
/*      */         final int status;
/*      */
/*      */
/*      */
/*      */
/*      */         private ClassInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/*  155 */           this.refTypeTag = param3PacketStream.readByte();
/*  156 */           if (param3VirtualMachineImpl.traceReceives) {
/*  157 */             param3VirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/*  159 */           this.typeID = param3PacketStream.readClassRef();
/*  160 */           if (param3VirtualMachineImpl.traceReceives) {
/*  161 */             param3VirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*  163 */           this.status = param3PacketStream.readInt();
/*  164 */           if (param3VirtualMachineImpl.traceReceives) {
/*  165 */             param3VirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ClassesBySignature(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  177 */         if (param2VirtualMachineImpl.traceReceives) {
/*  178 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.ClassesBySignature" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  180 */         if (param2VirtualMachineImpl.traceReceives) {
/*  181 */           param2VirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/*  183 */         int i = param2PacketStream.readInt();
/*  184 */         this.classes = new ClassInfo[i];
/*  185 */         for (byte b = 0; b < i; b++) {
/*  186 */           if (param2VirtualMachineImpl.traceReceives) {
/*  187 */             param2VirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/*  189 */           this.classes[b] = new ClassInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class AllClasses
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */       final ClassInfo[] classes;
/*      */
/*      */
/*      */       static AllClasses process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  203 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  204 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  208 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 3);
/*  209 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  210 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.AllClasses" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  212 */         packetStream.send();
/*  213 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static AllClasses waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  218 */         param2PacketStream.waitForReply();
/*  219 */         return new AllClasses(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */
/*      */
/*      */
/*      */
/*      */         final long typeID;
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */         final int status;
/*      */
/*      */
/*      */
/*      */
/*      */         private ClassInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/*  247 */           this.refTypeTag = param3PacketStream.readByte();
/*  248 */           if (param3VirtualMachineImpl.traceReceives) {
/*  249 */             param3VirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/*  251 */           this.typeID = param3PacketStream.readClassRef();
/*  252 */           if (param3VirtualMachineImpl.traceReceives) {
/*  253 */             param3VirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*  255 */           this.signature = param3PacketStream.readString();
/*  256 */           if (param3VirtualMachineImpl.traceReceives) {
/*  257 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/*  259 */           this.status = param3PacketStream.readInt();
/*  260 */           if (param3VirtualMachineImpl.traceReceives) {
/*  261 */             param3VirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private AllClasses(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  273 */         if (param2VirtualMachineImpl.traceReceives) {
/*  274 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.AllClasses" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  276 */         if (param2VirtualMachineImpl.traceReceives) {
/*  277 */           param2VirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/*  279 */         int i = param2PacketStream.readInt();
/*  280 */         this.classes = new ClassInfo[i];
/*  281 */         for (byte b = 0; b < i; b++) {
/*  282 */           if (param2VirtualMachineImpl.traceReceives) {
/*  283 */             param2VirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/*  285 */           this.classes[b] = new ClassInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class AllThreads
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */
/*      */
/*      */
/*      */       final ThreadReferenceImpl[] threads;
/*      */
/*      */
/*      */
/*      */       static AllThreads process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  304 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  305 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  309 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 4);
/*  310 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  311 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.AllThreads" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  313 */         packetStream.send();
/*  314 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static AllThreads waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  319 */         param2PacketStream.waitForReply();
/*  320 */         return new AllThreads(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private AllThreads(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  330 */         if (param2VirtualMachineImpl.traceReceives) {
/*  331 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.AllThreads" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  333 */         if (param2VirtualMachineImpl.traceReceives) {
/*  334 */           param2VirtualMachineImpl.printReceiveTrace(4, "threads(ThreadReferenceImpl[]): ");
/*      */         }
/*  336 */         int i = param2PacketStream.readInt();
/*  337 */         this.threads = new ThreadReferenceImpl[i];
/*  338 */         for (byte b = 0; b < i; b++) {
/*  339 */           this.threads[b] = param2PacketStream.readThreadReference();
/*  340 */           if (param2VirtualMachineImpl.traceReceives) {
/*  341 */             param2VirtualMachineImpl.printReceiveTrace(5, "threads[i](ThreadReferenceImpl): " + ((this.threads[b] == null) ? "NULL" : ("ref=" + this.threads[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class TopLevelThreadGroups
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */
/*      */       final ThreadGroupReferenceImpl[] groups;
/*      */
/*      */
/*      */       static TopLevelThreadGroups process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  357 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  358 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  362 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 5);
/*  363 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  364 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.TopLevelThreadGroups" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  366 */         packetStream.send();
/*  367 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static TopLevelThreadGroups waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  372 */         param2PacketStream.waitForReply();
/*  373 */         return new TopLevelThreadGroups(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private TopLevelThreadGroups(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  383 */         if (param2VirtualMachineImpl.traceReceives) {
/*  384 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.TopLevelThreadGroups" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  386 */         if (param2VirtualMachineImpl.traceReceives) {
/*  387 */           param2VirtualMachineImpl.printReceiveTrace(4, "groups(ThreadGroupReferenceImpl[]): ");
/*      */         }
/*  389 */         int i = param2PacketStream.readInt();
/*  390 */         this.groups = new ThreadGroupReferenceImpl[i];
/*  391 */         for (byte b = 0; b < i; b++) {
/*  392 */           this.groups[b] = param2PacketStream.readThreadGroupReference();
/*  393 */           if (param2VirtualMachineImpl.traceReceives) {
/*  394 */             param2VirtualMachineImpl.printReceiveTrace(5, "groups[i](ThreadGroupReferenceImpl): " + ((this.groups[b] == null) ? "NULL" : ("ref=" + this.groups[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class Dispose
/*      */     {
/*      */       static final int COMMAND = 6;
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
/*      */       static Dispose process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  430 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  431 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  435 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 6);
/*  436 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  437 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Dispose" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  439 */         packetStream.send();
/*  440 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Dispose waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  445 */         param2PacketStream.waitForReply();
/*  446 */         return new Dispose(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Dispose(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  451 */         if (param2VirtualMachineImpl.traceReceives) {
/*  452 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Dispose" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     static class IDSizes
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */       final int fieldIDSize;
/*      */       final int methodIDSize;
/*      */       final int objectIDSize;
/*      */       final int referenceTypeIDSize;
/*      */       final int frameIDSize;
/*      */
/*      */       static IDSizes process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  467 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  468 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  472 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 7);
/*  473 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  474 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.IDSizes" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  476 */         packetStream.send();
/*  477 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static IDSizes waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  482 */         param2PacketStream.waitForReply();
/*  483 */         return new IDSizes(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private IDSizes(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  513 */         if (param2VirtualMachineImpl.traceReceives) {
/*  514 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.IDSizes" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  516 */         this.fieldIDSize = param2PacketStream.readInt();
/*  517 */         if (param2VirtualMachineImpl.traceReceives) {
/*  518 */           param2VirtualMachineImpl.printReceiveTrace(4, "fieldIDSize(int): " + this.fieldIDSize);
/*      */         }
/*  520 */         this.methodIDSize = param2PacketStream.readInt();
/*  521 */         if (param2VirtualMachineImpl.traceReceives) {
/*  522 */           param2VirtualMachineImpl.printReceiveTrace(4, "methodIDSize(int): " + this.methodIDSize);
/*      */         }
/*  524 */         this.objectIDSize = param2PacketStream.readInt();
/*  525 */         if (param2VirtualMachineImpl.traceReceives) {
/*  526 */           param2VirtualMachineImpl.printReceiveTrace(4, "objectIDSize(int): " + this.objectIDSize);
/*      */         }
/*  528 */         this.referenceTypeIDSize = param2PacketStream.readInt();
/*  529 */         if (param2VirtualMachineImpl.traceReceives) {
/*  530 */           param2VirtualMachineImpl.printReceiveTrace(4, "referenceTypeIDSize(int): " + this.referenceTypeIDSize);
/*      */         }
/*  532 */         this.frameIDSize = param2PacketStream.readInt();
/*  533 */         if (param2VirtualMachineImpl.traceReceives) {
/*  534 */           param2VirtualMachineImpl.printReceiveTrace(4, "frameIDSize(int): " + this.frameIDSize);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Suspend
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Suspend process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  555 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  556 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  560 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 8);
/*  561 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  562 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Suspend" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  564 */         packetStream.send();
/*  565 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Suspend waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  570 */         param2PacketStream.waitForReply();
/*  571 */         return new Suspend(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Suspend(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  576 */         if (param2VirtualMachineImpl.traceReceives) {
/*  577 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Suspend" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Resume
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */
/*      */
/*      */
/*      */
/*      */       static Resume process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  594 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  595 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  599 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 9);
/*  600 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  601 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Resume" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  603 */         packetStream.send();
/*  604 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Resume waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  609 */         param2PacketStream.waitForReply();
/*  610 */         return new Resume(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Resume(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  615 */         if (param2VirtualMachineImpl.traceReceives) {
/*  616 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Resume" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Exit
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Exit process(VirtualMachineImpl param2VirtualMachineImpl, int param2Int) throws JDWPException {
/*  636 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2Int);
/*  637 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, int param2Int) {
/*  642 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 10);
/*  643 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  644 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Exit" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  646 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/*  647 */           packetStream.vm.printTrace("Sending:                 exitCode(int): " + param2Int);
/*      */         }
/*  649 */         packetStream.writeInt(param2Int);
/*  650 */         packetStream.send();
/*  651 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Exit waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  656 */         param2PacketStream.waitForReply();
/*  657 */         return new Exit(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Exit(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  662 */         if (param2VirtualMachineImpl.traceReceives) {
/*  663 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Exit" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class CreateString
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */
/*      */       final StringReferenceImpl stringObject;
/*      */
/*      */
/*      */       static CreateString process(VirtualMachineImpl param2VirtualMachineImpl, String param2String) throws JDWPException {
/*  678 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2String);
/*  679 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, String param2String) {
/*  684 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 11);
/*  685 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  686 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.CreateString" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  688 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/*  689 */           packetStream.vm.printTrace("Sending:                 utf(String): " + param2String);
/*      */         }
/*  691 */         packetStream.writeString(param2String);
/*  692 */         packetStream.send();
/*  693 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static CreateString waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  698 */         param2PacketStream.waitForReply();
/*  699 */         return new CreateString(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private CreateString(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  709 */         if (param2VirtualMachineImpl.traceReceives) {
/*  710 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.CreateString" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  712 */         this.stringObject = param2PacketStream.readStringReference();
/*  713 */         if (param2VirtualMachineImpl.traceReceives) {
/*  714 */           param2VirtualMachineImpl.printReceiveTrace(4, "stringObject(StringReferenceImpl): " + ((this.stringObject == null) ? "NULL" : ("ref=" + this.stringObject.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */     static class Capabilities
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */       final boolean canWatchFieldModification;
/*      */       final boolean canWatchFieldAccess;
/*      */       final boolean canGetBytecodes;
/*      */       final boolean canGetSyntheticAttribute;
/*      */       final boolean canGetOwnedMonitorInfo;
/*      */       final boolean canGetCurrentContendedMonitor;
/*      */       final boolean canGetMonitorInfo;
/*      */
/*      */       static Capabilities process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  731 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  732 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  736 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 12);
/*  737 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  738 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.Capabilities" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  740 */         packetStream.send();
/*  741 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Capabilities waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  746 */         param2PacketStream.waitForReply();
/*  747 */         return new Capabilities(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private Capabilities(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  792 */         if (param2VirtualMachineImpl.traceReceives) {
/*  793 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.Capabilities" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  795 */         this.canWatchFieldModification = param2PacketStream.readBoolean();
/*  796 */         if (param2VirtualMachineImpl.traceReceives) {
/*  797 */           param2VirtualMachineImpl.printReceiveTrace(4, "canWatchFieldModification(boolean): " + this.canWatchFieldModification);
/*      */         }
/*  799 */         this.canWatchFieldAccess = param2PacketStream.readBoolean();
/*  800 */         if (param2VirtualMachineImpl.traceReceives) {
/*  801 */           param2VirtualMachineImpl.printReceiveTrace(4, "canWatchFieldAccess(boolean): " + this.canWatchFieldAccess);
/*      */         }
/*  803 */         this.canGetBytecodes = param2PacketStream.readBoolean();
/*  804 */         if (param2VirtualMachineImpl.traceReceives) {
/*  805 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetBytecodes(boolean): " + this.canGetBytecodes);
/*      */         }
/*  807 */         this.canGetSyntheticAttribute = param2PacketStream.readBoolean();
/*  808 */         if (param2VirtualMachineImpl.traceReceives) {
/*  809 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetSyntheticAttribute(boolean): " + this.canGetSyntheticAttribute);
/*      */         }
/*  811 */         this.canGetOwnedMonitorInfo = param2PacketStream.readBoolean();
/*  812 */         if (param2VirtualMachineImpl.traceReceives) {
/*  813 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetOwnedMonitorInfo(boolean): " + this.canGetOwnedMonitorInfo);
/*      */         }
/*  815 */         this.canGetCurrentContendedMonitor = param2PacketStream.readBoolean();
/*  816 */         if (param2VirtualMachineImpl.traceReceives) {
/*  817 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetCurrentContendedMonitor(boolean): " + this.canGetCurrentContendedMonitor);
/*      */         }
/*  819 */         this.canGetMonitorInfo = param2PacketStream.readBoolean();
/*  820 */         if (param2VirtualMachineImpl.traceReceives) {
/*  821 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetMonitorInfo(boolean): " + this.canGetMonitorInfo);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class ClassPaths
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */
/*      */       final String baseDir;
/*      */       final String[] classpaths;
/*      */       final String[] bootclasspaths;
/*      */
/*      */       static ClassPaths process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/*  836 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/*  837 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/*  841 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 13);
/*  842 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  843 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.ClassPaths" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  845 */         packetStream.send();
/*  846 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClassPaths waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  851 */         param2PacketStream.waitForReply();
/*  852 */         return new ClassPaths(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private ClassPaths(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/*  873 */         if (param2VirtualMachineImpl.traceReceives) {
/*  874 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.ClassPaths" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*  876 */         this.baseDir = param2PacketStream.readString();
/*  877 */         if (param2VirtualMachineImpl.traceReceives) {
/*  878 */           param2VirtualMachineImpl.printReceiveTrace(4, "baseDir(String): " + this.baseDir);
/*      */         }
/*  880 */         if (param2VirtualMachineImpl.traceReceives) {
/*  881 */           param2VirtualMachineImpl.printReceiveTrace(4, "classpaths(String[]): ");
/*      */         }
/*  883 */         int i = param2PacketStream.readInt();
/*  884 */         this.classpaths = new String[i]; int j;
/*  885 */         for (j = 0; j < i; j++) {
/*  886 */           this.classpaths[j] = param2PacketStream.readString();
/*  887 */           if (param2VirtualMachineImpl.traceReceives) {
/*  888 */             param2VirtualMachineImpl.printReceiveTrace(5, "classpaths[i](String): " + this.classpaths[j]);
/*      */           }
/*      */         }
/*  891 */         if (param2VirtualMachineImpl.traceReceives) {
/*  892 */           param2VirtualMachineImpl.printReceiveTrace(4, "bootclasspaths(String[]): ");
/*      */         }
/*  894 */         j = param2PacketStream.readInt();
/*  895 */         this.bootclasspaths = new String[j];
/*  896 */         for (byte b = 0; b < j; b++) {
/*  897 */           this.bootclasspaths[b] = param2PacketStream.readString();
/*  898 */           if (param2VirtualMachineImpl.traceReceives) {
/*  899 */             param2VirtualMachineImpl.printReceiveTrace(5, "bootclasspaths[i](String): " + this.bootclasspaths[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class DisposeObjects
/*      */     {
/*      */       static final int COMMAND = 14;
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
/*      */       static class Request
/*      */       {
/*      */         final ObjectReferenceImpl object;
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
/*      */         final int refCnt;
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
/*      */         Request(ObjectReferenceImpl param3ObjectReferenceImpl, int param3Int) {
/*  954 */           this.object = param3ObjectReferenceImpl;
/*  955 */           this.refCnt = param3Int;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/*  959 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/*  960 */             param3PacketStream.vm.printTrace("Sending:                     object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */           }
/*  962 */           param3PacketStream.writeObjectRef(this.object.ref());
/*  963 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/*  964 */             param3PacketStream.vm.printTrace("Sending:                     refCnt(int): " + this.refCnt);
/*      */           }
/*  966 */           param3PacketStream.writeInt(this.refCnt);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */       static DisposeObjects process(VirtualMachineImpl param2VirtualMachineImpl, Request[] param2ArrayOfRequest) throws JDWPException {
/*  973 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayOfRequest);
/*  974 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, Request[] param2ArrayOfRequest) {
/*  979 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 14);
/*  980 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/*  981 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.DisposeObjects" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/*  983 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/*  984 */           packetStream.vm.printTrace("Sending:                 requests(Request[]): ");
/*      */         }
/*  986 */         packetStream.writeInt(param2ArrayOfRequest.length);
/*  987 */         for (byte b = 0; b < param2ArrayOfRequest.length; b++) {
/*  988 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/*  989 */             packetStream.vm.printTrace("Sending:                     requests[i](Request): ");
/*      */           }
/*  991 */           param2ArrayOfRequest[b].write(packetStream);
/*      */         }
/*  993 */         packetStream.send();
/*  994 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static DisposeObjects waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/*  999 */         param2PacketStream.waitForReply();
/* 1000 */         return new DisposeObjects(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private DisposeObjects(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1005 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1006 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.DisposeObjects" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class HoldEvents
/*      */     {
/*      */       static final int COMMAND = 15;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static HoldEvents process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/* 1029 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/* 1030 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/* 1034 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 15);
/* 1035 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1036 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.HoldEvents" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1038 */         packetStream.send();
/* 1039 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static HoldEvents waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1044 */         param2PacketStream.waitForReply();
/* 1045 */         return new HoldEvents(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private HoldEvents(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1050 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1051 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.HoldEvents" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class ReleaseEvents
/*      */     {
/*      */       static final int COMMAND = 16;
/*      */
/*      */
/*      */
/*      */
/*      */       static ReleaseEvents process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/* 1067 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/* 1068 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/* 1072 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 16);
/* 1073 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1074 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.ReleaseEvents" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1076 */         packetStream.send();
/* 1077 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ReleaseEvents waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1082 */         param2PacketStream.waitForReply();
/* 1083 */         return new ReleaseEvents(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private ReleaseEvents(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1088 */         if (param2VirtualMachineImpl.traceReceives)
/* 1089 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.ReleaseEvents" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */       } }
/*      */     static class CapabilitiesNew { static final int COMMAND = 17; final boolean canWatchFieldModification; final boolean canWatchFieldAccess;
/*      */       final boolean canGetBytecodes;
/*      */       final boolean canGetSyntheticAttribute;
/*      */       final boolean canGetOwnedMonitorInfo;
/*      */       final boolean canGetCurrentContendedMonitor;
/*      */       final boolean canGetMonitorInfo;
/*      */       final boolean canRedefineClasses;
/*      */       final boolean canAddMethod;
/*      */       final boolean canUnrestrictedlyRedefineClasses;
/*      */       final boolean canPopFrames;
/*      */       final boolean canUseInstanceFilters;
/*      */       final boolean canGetSourceDebugExtension;
/*      */       final boolean canRequestVMDeathEvent;
/*      */       final boolean canSetDefaultStratum;
/*      */
/*      */       static CapabilitiesNew process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/* 1107 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/* 1108 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */       final boolean canGetInstanceInfo; final boolean canRequestMonitorEvents; final boolean canGetMonitorFrameInfo; final boolean canUseSourceNameFilters; final boolean canGetConstantPool; final boolean canForceEarlyReturn; final boolean reserved22; final boolean reserved23; final boolean reserved24; final boolean reserved25; final boolean reserved26; final boolean reserved27; final boolean reserved28; final boolean reserved29; final boolean reserved30; final boolean reserved31; final boolean reserved32;
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/* 1112 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 17);
/* 1113 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1114 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.CapabilitiesNew" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1116 */         packetStream.send();
/* 1117 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static CapabilitiesNew waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1122 */         param2PacketStream.waitForReply();
/* 1123 */         return new CapabilitiesNew(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private CapabilitiesNew(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1296 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1297 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.CapabilitiesNew" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1299 */         this.canWatchFieldModification = param2PacketStream.readBoolean();
/* 1300 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1301 */           param2VirtualMachineImpl.printReceiveTrace(4, "canWatchFieldModification(boolean): " + this.canWatchFieldModification);
/*      */         }
/* 1303 */         this.canWatchFieldAccess = param2PacketStream.readBoolean();
/* 1304 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1305 */           param2VirtualMachineImpl.printReceiveTrace(4, "canWatchFieldAccess(boolean): " + this.canWatchFieldAccess);
/*      */         }
/* 1307 */         this.canGetBytecodes = param2PacketStream.readBoolean();
/* 1308 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1309 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetBytecodes(boolean): " + this.canGetBytecodes);
/*      */         }
/* 1311 */         this.canGetSyntheticAttribute = param2PacketStream.readBoolean();
/* 1312 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1313 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetSyntheticAttribute(boolean): " + this.canGetSyntheticAttribute);
/*      */         }
/* 1315 */         this.canGetOwnedMonitorInfo = param2PacketStream.readBoolean();
/* 1316 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1317 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetOwnedMonitorInfo(boolean): " + this.canGetOwnedMonitorInfo);
/*      */         }
/* 1319 */         this.canGetCurrentContendedMonitor = param2PacketStream.readBoolean();
/* 1320 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1321 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetCurrentContendedMonitor(boolean): " + this.canGetCurrentContendedMonitor);
/*      */         }
/* 1323 */         this.canGetMonitorInfo = param2PacketStream.readBoolean();
/* 1324 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1325 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetMonitorInfo(boolean): " + this.canGetMonitorInfo);
/*      */         }
/* 1327 */         this.canRedefineClasses = param2PacketStream.readBoolean();
/* 1328 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1329 */           param2VirtualMachineImpl.printReceiveTrace(4, "canRedefineClasses(boolean): " + this.canRedefineClasses);
/*      */         }
/* 1331 */         this.canAddMethod = param2PacketStream.readBoolean();
/* 1332 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1333 */           param2VirtualMachineImpl.printReceiveTrace(4, "canAddMethod(boolean): " + this.canAddMethod);
/*      */         }
/* 1335 */         this.canUnrestrictedlyRedefineClasses = param2PacketStream.readBoolean();
/* 1336 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1337 */           param2VirtualMachineImpl.printReceiveTrace(4, "canUnrestrictedlyRedefineClasses(boolean): " + this.canUnrestrictedlyRedefineClasses);
/*      */         }
/* 1339 */         this.canPopFrames = param2PacketStream.readBoolean();
/* 1340 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1341 */           param2VirtualMachineImpl.printReceiveTrace(4, "canPopFrames(boolean): " + this.canPopFrames);
/*      */         }
/* 1343 */         this.canUseInstanceFilters = param2PacketStream.readBoolean();
/* 1344 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1345 */           param2VirtualMachineImpl.printReceiveTrace(4, "canUseInstanceFilters(boolean): " + this.canUseInstanceFilters);
/*      */         }
/* 1347 */         this.canGetSourceDebugExtension = param2PacketStream.readBoolean();
/* 1348 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1349 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetSourceDebugExtension(boolean): " + this.canGetSourceDebugExtension);
/*      */         }
/* 1351 */         this.canRequestVMDeathEvent = param2PacketStream.readBoolean();
/* 1352 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1353 */           param2VirtualMachineImpl.printReceiveTrace(4, "canRequestVMDeathEvent(boolean): " + this.canRequestVMDeathEvent);
/*      */         }
/* 1355 */         this.canSetDefaultStratum = param2PacketStream.readBoolean();
/* 1356 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1357 */           param2VirtualMachineImpl.printReceiveTrace(4, "canSetDefaultStratum(boolean): " + this.canSetDefaultStratum);
/*      */         }
/* 1359 */         this.canGetInstanceInfo = param2PacketStream.readBoolean();
/* 1360 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1361 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetInstanceInfo(boolean): " + this.canGetInstanceInfo);
/*      */         }
/* 1363 */         this.canRequestMonitorEvents = param2PacketStream.readBoolean();
/* 1364 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1365 */           param2VirtualMachineImpl.printReceiveTrace(4, "canRequestMonitorEvents(boolean): " + this.canRequestMonitorEvents);
/*      */         }
/* 1367 */         this.canGetMonitorFrameInfo = param2PacketStream.readBoolean();
/* 1368 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1369 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetMonitorFrameInfo(boolean): " + this.canGetMonitorFrameInfo);
/*      */         }
/* 1371 */         this.canUseSourceNameFilters = param2PacketStream.readBoolean();
/* 1372 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1373 */           param2VirtualMachineImpl.printReceiveTrace(4, "canUseSourceNameFilters(boolean): " + this.canUseSourceNameFilters);
/*      */         }
/* 1375 */         this.canGetConstantPool = param2PacketStream.readBoolean();
/* 1376 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1377 */           param2VirtualMachineImpl.printReceiveTrace(4, "canGetConstantPool(boolean): " + this.canGetConstantPool);
/*      */         }
/* 1379 */         this.canForceEarlyReturn = param2PacketStream.readBoolean();
/* 1380 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1381 */           param2VirtualMachineImpl.printReceiveTrace(4, "canForceEarlyReturn(boolean): " + this.canForceEarlyReturn);
/*      */         }
/* 1383 */         this.reserved22 = param2PacketStream.readBoolean();
/* 1384 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1385 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved22(boolean): " + this.reserved22);
/*      */         }
/* 1387 */         this.reserved23 = param2PacketStream.readBoolean();
/* 1388 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1389 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved23(boolean): " + this.reserved23);
/*      */         }
/* 1391 */         this.reserved24 = param2PacketStream.readBoolean();
/* 1392 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1393 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved24(boolean): " + this.reserved24);
/*      */         }
/* 1395 */         this.reserved25 = param2PacketStream.readBoolean();
/* 1396 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1397 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved25(boolean): " + this.reserved25);
/*      */         }
/* 1399 */         this.reserved26 = param2PacketStream.readBoolean();
/* 1400 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1401 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved26(boolean): " + this.reserved26);
/*      */         }
/* 1403 */         this.reserved27 = param2PacketStream.readBoolean();
/* 1404 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1405 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved27(boolean): " + this.reserved27);
/*      */         }
/* 1407 */         this.reserved28 = param2PacketStream.readBoolean();
/* 1408 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1409 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved28(boolean): " + this.reserved28);
/*      */         }
/* 1411 */         this.reserved29 = param2PacketStream.readBoolean();
/* 1412 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1413 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved29(boolean): " + this.reserved29);
/*      */         }
/* 1415 */         this.reserved30 = param2PacketStream.readBoolean();
/* 1416 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1417 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved30(boolean): " + this.reserved30);
/*      */         }
/* 1419 */         this.reserved31 = param2PacketStream.readBoolean();
/* 1420 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1421 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved31(boolean): " + this.reserved31);
/*      */         }
/* 1423 */         this.reserved32 = param2PacketStream.readBoolean();
/* 1424 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1425 */           param2VirtualMachineImpl.printReceiveTrace(4, "reserved32(boolean): " + this.reserved32);
/*      */         }
/*      */       } }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class RedefineClasses
/*      */     {
/*      */       static final int COMMAND = 18;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class ClassDef
/*      */       {
/*      */         final ReferenceTypeImpl refType;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final byte[] classfile;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         ClassDef(ReferenceTypeImpl param3ReferenceTypeImpl, byte[] param3ArrayOfbyte) {
/* 1466 */           this.refType = param3ReferenceTypeImpl;
/* 1467 */           this.classfile = param3ArrayOfbyte;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 1471 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 1472 */             param3PacketStream.vm.printTrace("Sending:                     refType(ReferenceTypeImpl): " + ((this.refType == null) ? "NULL" : ("ref=" + this.refType.ref())));
/*      */           }
/* 1474 */           param3PacketStream.writeClassRef(this.refType.ref());
/* 1475 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 1476 */             param3PacketStream.vm.printTrace("Sending:                     classfile(byte[]): ");
/*      */           }
/* 1478 */           param3PacketStream.writeInt(this.classfile.length);
/* 1479 */           for (byte b = 0; b < this.classfile.length; b++) {
/* 1480 */             if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 1481 */               param3PacketStream.vm.printTrace("Sending:                         classfile[i](byte): " + this.classfile[b]);
/*      */             }
/* 1483 */             param3PacketStream.writeByte(this.classfile[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */       static RedefineClasses process(VirtualMachineImpl param2VirtualMachineImpl, ClassDef[] param2ArrayOfClassDef) throws JDWPException {
/* 1491 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayOfClassDef);
/* 1492 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassDef[] param2ArrayOfClassDef) {
/* 1497 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 18);
/* 1498 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1499 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.RedefineClasses" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1501 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1502 */           packetStream.vm.printTrace("Sending:                 classes(ClassDef[]): ");
/*      */         }
/* 1504 */         packetStream.writeInt(param2ArrayOfClassDef.length);
/* 1505 */         for (byte b = 0; b < param2ArrayOfClassDef.length; b++) {
/* 1506 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1507 */             packetStream.vm.printTrace("Sending:                     classes[i](ClassDef): ");
/*      */           }
/* 1509 */           param2ArrayOfClassDef[b].write(packetStream);
/*      */         }
/* 1511 */         packetStream.send();
/* 1512 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static RedefineClasses waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1517 */         param2PacketStream.waitForReply();
/* 1518 */         return new RedefineClasses(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private RedefineClasses(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1523 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1524 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.RedefineClasses" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class SetDefaultStratum
/*      */     {
/*      */       static final int COMMAND = 19;
/*      */
/*      */
/*      */
/*      */       static SetDefaultStratum process(VirtualMachineImpl param2VirtualMachineImpl, String param2String) throws JDWPException {
/* 1539 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2String);
/* 1540 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, String param2String) {
/* 1545 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 19);
/* 1546 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1547 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.SetDefaultStratum" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1549 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1550 */           packetStream.vm.printTrace("Sending:                 stratumID(String): " + param2String);
/*      */         }
/* 1552 */         packetStream.writeString(param2String);
/* 1553 */         packetStream.send();
/* 1554 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SetDefaultStratum waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1559 */         param2PacketStream.waitForReply();
/* 1560 */         return new SetDefaultStratum(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private SetDefaultStratum(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1565 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1566 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.SetDefaultStratum" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class AllClassesWithGeneric
/*      */     {
/*      */       static final int COMMAND = 20;
/*      */
/*      */
/*      */
/*      */       final ClassInfo[] classes;
/*      */
/*      */
/*      */
/*      */
/*      */       static AllClassesWithGeneric process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/* 1586 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/* 1587 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/* 1591 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 20);
/* 1592 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1593 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.AllClassesWithGeneric" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1595 */         packetStream.send();
/* 1596 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static AllClassesWithGeneric waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1601 */         param2PacketStream.waitForReply();
/* 1602 */         return new AllClassesWithGeneric(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */
/*      */
/*      */
/*      */
/*      */         final long typeID;
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final String genericSignature;
/*      */
/*      */
/*      */
/*      */         final int status;
/*      */
/*      */
/*      */
/*      */
/*      */         private ClassInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 1636 */           this.refTypeTag = param3PacketStream.readByte();
/* 1637 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1638 */             param3VirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 1640 */           this.typeID = param3PacketStream.readClassRef();
/* 1641 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1642 */             param3VirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/* 1644 */           this.signature = param3PacketStream.readString();
/* 1645 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1646 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 1648 */           this.genericSignature = param3PacketStream.readString();
/* 1649 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1650 */             param3VirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 1652 */           this.status = param3PacketStream.readInt();
/* 1653 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1654 */             param3VirtualMachineImpl.printReceiveTrace(5, "status(int): " + this.status);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private AllClassesWithGeneric(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1666 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1667 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.AllClassesWithGeneric" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1669 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1670 */           param2VirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/* 1672 */         int i = param2PacketStream.readInt();
/* 1673 */         this.classes = new ClassInfo[i];
/* 1674 */         for (byte b = 0; b < i; b++) {
/* 1675 */           if (param2VirtualMachineImpl.traceReceives) {
/* 1676 */             param2VirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/* 1678 */           this.classes[b] = new ClassInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class InstanceCounts
/*      */     {
/*      */       static final int COMMAND = 21;
/*      */
/*      */
/*      */
/*      */       final long[] counts;
/*      */
/*      */
/*      */
/*      */       static InstanceCounts process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl[] param2ArrayOfReferenceTypeImpl) throws JDWPException {
/* 1697 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayOfReferenceTypeImpl);
/* 1698 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl[] param2ArrayOfReferenceTypeImpl) {
/* 1703 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 1, 21);
/* 1704 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1705 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.VirtualMachine.InstanceCounts" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1707 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1708 */           packetStream.vm.printTrace("Sending:                 refTypesCount(ReferenceTypeImpl[]): ");
/*      */         }
/* 1710 */         packetStream.writeInt(param2ArrayOfReferenceTypeImpl.length);
/* 1711 */         for (byte b = 0; b < param2ArrayOfReferenceTypeImpl.length; b++) {
/* 1712 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1713 */             packetStream.vm.printTrace("Sending:                     refTypesCount[i](ReferenceTypeImpl): " + ((param2ArrayOfReferenceTypeImpl[b] == null) ? "NULL" : ("ref=" + param2ArrayOfReferenceTypeImpl[b].ref())));
/*      */           }
/* 1715 */           packetStream.writeClassRef(param2ArrayOfReferenceTypeImpl[b].ref());
/*      */         }
/* 1717 */         packetStream.send();
/* 1718 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static InstanceCounts waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1723 */         param2PacketStream.waitForReply();
/* 1724 */         return new InstanceCounts(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private InstanceCounts(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1734 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1735 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.VirtualMachine.InstanceCounts" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1737 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1738 */           param2VirtualMachineImpl.printReceiveTrace(4, "counts(long[]): ");
/*      */         }
/* 1740 */         int i = param2PacketStream.readInt();
/* 1741 */         this.counts = new long[i];
/* 1742 */         for (byte b = 0; b < i; b++) {
/* 1743 */           this.counts[b] = param2PacketStream.readLong();
/* 1744 */           if (param2VirtualMachineImpl.traceReceives) {
/* 1745 */             param2VirtualMachineImpl.printReceiveTrace(5, "counts[i](long): " + this.counts[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   static class ReferenceType
/*      */   {
/*      */     static final int COMMAND_SET = 2;
/*      */
/*      */
/*      */
/*      */
/*      */     static class Signature
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */
/*      */       final String signature;
/*      */
/*      */
/*      */
/*      */       static Signature process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 1772 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 1773 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 1778 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 1);
/* 1779 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1780 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Signature" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1782 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1783 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 1785 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 1786 */         packetStream.send();
/* 1787 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Signature waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1792 */         param2PacketStream.waitForReply();
/* 1793 */         return new Signature(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Signature(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1803 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1804 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Signature" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1806 */         this.signature = param2PacketStream.readString();
/* 1807 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1808 */           param2VirtualMachineImpl.printReceiveTrace(4, "signature(String): " + this.signature);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class ClassLoader
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */       final ClassLoaderReferenceImpl classLoader;
/*      */
/*      */
/*      */       static ClassLoader process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 1824 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 1825 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 1830 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 2);
/* 1831 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1832 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.ClassLoader" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1834 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1835 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 1837 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 1838 */         packetStream.send();
/* 1839 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClassLoader waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1844 */         param2PacketStream.waitForReply();
/* 1845 */         return new ClassLoader(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ClassLoader(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1855 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1856 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.ClassLoader" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1858 */         this.classLoader = param2PacketStream.readClassLoaderReference();
/* 1859 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1860 */           param2VirtualMachineImpl.printReceiveTrace(4, "classLoader(ClassLoaderReferenceImpl): " + ((this.classLoader == null) ? "NULL" : ("ref=" + this.classLoader.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Modifiers
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */       final int modBits;
/*      */
/*      */
/*      */
/*      */       static Modifiers process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 1878 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 1879 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 1884 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 3);
/* 1885 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1886 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Modifiers" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1888 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1889 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 1891 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 1892 */         packetStream.send();
/* 1893 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Modifiers waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1898 */         param2PacketStream.waitForReply();
/* 1899 */         return new Modifiers(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Modifiers(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 1910 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1911 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Modifiers" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 1913 */         this.modBits = param2PacketStream.readInt();
/* 1914 */         if (param2VirtualMachineImpl.traceReceives) {
/* 1915 */           param2VirtualMachineImpl.printReceiveTrace(4, "modBits(int): " + this.modBits);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Fields
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */
/*      */
/*      */       final FieldInfo[] declared;
/*      */
/*      */
/*      */
/*      */       static Fields process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 1933 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 1934 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 1939 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 4);
/* 1940 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 1941 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Fields" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 1943 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 1944 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 1946 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 1947 */         packetStream.send();
/* 1948 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Fields waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 1953 */         param2PacketStream.waitForReply();
/* 1954 */         return new Fields(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class FieldInfo
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int modBits;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private FieldInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 1987 */           this.fieldID = param3PacketStream.readFieldRef();
/* 1988 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1989 */             param3VirtualMachineImpl.printReceiveTrace(5, "fieldID(long): " + this.fieldID);
/*      */           }
/* 1991 */           this.name = param3PacketStream.readString();
/* 1992 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1993 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 1995 */           this.signature = param3PacketStream.readString();
/* 1996 */           if (param3VirtualMachineImpl.traceReceives) {
/* 1997 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 1999 */           this.modBits = param3PacketStream.readInt();
/* 2000 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2001 */             param3VirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Fields(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2013 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2014 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Fields" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2016 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2017 */           param2VirtualMachineImpl.printReceiveTrace(4, "declared(FieldInfo[]): ");
/*      */         }
/* 2019 */         int i = param2PacketStream.readInt();
/* 2020 */         this.declared = new FieldInfo[i];
/* 2021 */         for (byte b = 0; b < i; b++) {
/* 2022 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2023 */             param2VirtualMachineImpl.printReceiveTrace(5, "declared[i](FieldInfo): ");
/*      */           }
/* 2025 */           this.declared[b] = new FieldInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Methods
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */
/*      */
/*      */
/*      */       final MethodInfo[] declared;
/*      */
/*      */
/*      */
/*      */       static Methods process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2044 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2045 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2050 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 5);
/* 2051 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2052 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Methods" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2054 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2055 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2057 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2058 */         packetStream.send();
/* 2059 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Methods waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2064 */         param2PacketStream.waitForReply();
/* 2065 */         return new Methods(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class MethodInfo
/*      */       {
/*      */         final long methodID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int modBits;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private MethodInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 2098 */           this.methodID = param3PacketStream.readMethodRef();
/* 2099 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2100 */             param3VirtualMachineImpl.printReceiveTrace(5, "methodID(long): " + this.methodID);
/*      */           }
/* 2102 */           this.name = param3PacketStream.readString();
/* 2103 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2104 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2106 */           this.signature = param3PacketStream.readString();
/* 2107 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2108 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2110 */           this.modBits = param3PacketStream.readInt();
/* 2111 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2112 */             param3VirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Methods(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2124 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2125 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Methods" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2127 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2128 */           param2VirtualMachineImpl.printReceiveTrace(4, "declared(MethodInfo[]): ");
/*      */         }
/* 2130 */         int i = param2PacketStream.readInt();
/* 2131 */         this.declared = new MethodInfo[i];
/* 2132 */         for (byte b = 0; b < i; b++) {
/* 2133 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2134 */             param2VirtualMachineImpl.printReceiveTrace(5, "declared[i](MethodInfo): ");
/*      */           }
/* 2136 */           this.declared[b] = new MethodInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */
/*      */
/*      */       final ValueImpl[] values;
/*      */
/*      */
/*      */
/*      */       static class Field
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */         Field(long param3Long) {
/* 2159 */           this.fieldID = param3Long;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 2163 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 2164 */             param3PacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 2166 */           param3PacketStream.writeFieldRef(this.fieldID);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static GetValues process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, Field[] param2ArrayOfField) throws JDWPException {
/* 2174 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2ArrayOfField);
/* 2175 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, Field[] param2ArrayOfField) {
/* 2181 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 6);
/* 2182 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2183 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.GetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2185 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2186 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2188 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2189 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2190 */           packetStream.vm.printTrace("Sending:                 fields(Field[]): ");
/*      */         }
/* 2192 */         packetStream.writeInt(param2ArrayOfField.length);
/* 2193 */         for (byte b = 0; b < param2ArrayOfField.length; b++) {
/* 2194 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2195 */             packetStream.vm.printTrace("Sending:                     fields[i](Field): ");
/*      */           }
/* 2197 */           param2ArrayOfField[b].write(packetStream);
/*      */         }
/* 2199 */         packetStream.send();
/* 2200 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static GetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2205 */         param2PacketStream.waitForReply();
/* 2206 */         return new GetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private GetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2217 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2218 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.GetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2220 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2221 */           param2VirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 2223 */         int i = param2PacketStream.readInt();
/* 2224 */         this.values = new ValueImpl[i];
/* 2225 */         for (byte b = 0; b < i; b++) {
/* 2226 */           this.values[b] = param2PacketStream.readValue();
/* 2227 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2228 */             param2VirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class SourceFile
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */
/*      */       final String sourceFile;
/*      */
/*      */
/*      */       static SourceFile process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2244 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2245 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2250 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 7);
/* 2251 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2252 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.SourceFile" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2254 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2255 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2257 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2258 */         packetStream.send();
/* 2259 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SourceFile waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2264 */         param2PacketStream.waitForReply();
/* 2265 */         return new SourceFile(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private SourceFile(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2276 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2277 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.SourceFile" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2279 */         this.sourceFile = param2PacketStream.readString();
/* 2280 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2281 */           param2VirtualMachineImpl.printReceiveTrace(4, "sourceFile(String): " + this.sourceFile);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class NestedTypes
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */
/*      */       final TypeInfo[] classes;
/*      */
/*      */
/*      */       static NestedTypes process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2296 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2297 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2302 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 8);
/* 2303 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2304 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.NestedTypes" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2306 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2307 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2309 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2310 */         packetStream.send();
/* 2311 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static NestedTypes waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2316 */         param2PacketStream.waitForReply();
/* 2317 */         return new NestedTypes(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static class TypeInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */
/*      */
/*      */
/*      */         final long typeID;
/*      */
/*      */
/*      */
/*      */         private TypeInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 2334 */           this.refTypeTag = param3PacketStream.readByte();
/* 2335 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2336 */             param3VirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 2338 */           this.typeID = param3PacketStream.readClassRef();
/* 2339 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2340 */             param3VirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private NestedTypes(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2352 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2353 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.NestedTypes" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2355 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2356 */           param2VirtualMachineImpl.printReceiveTrace(4, "classes(TypeInfo[]): ");
/*      */         }
/* 2358 */         int i = param2PacketStream.readInt();
/* 2359 */         this.classes = new TypeInfo[i];
/* 2360 */         for (byte b = 0; b < i; b++) {
/* 2361 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2362 */             param2VirtualMachineImpl.printReceiveTrace(5, "classes[i](TypeInfo): ");
/*      */           }
/* 2364 */           this.classes[b] = new TypeInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Status
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */
/*      */
/*      */
/*      */
/*      */       final int status;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Status process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2387 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2388 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2393 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 9);
/* 2394 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2395 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Status" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2397 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2398 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2400 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2401 */         packetStream.send();
/* 2402 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Status waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2407 */         param2PacketStream.waitForReply();
/* 2408 */         return new Status(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Status(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2419 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2420 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Status" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2422 */         this.status = param2PacketStream.readInt();
/* 2423 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2424 */           param2VirtualMachineImpl.printReceiveTrace(4, "status(int): " + this.status);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class Interfaces
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */
/*      */
/*      */       final InterfaceTypeImpl[] interfaces;
/*      */
/*      */
/*      */       static Interfaces process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2440 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2441 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2446 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 10);
/* 2447 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2448 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Interfaces" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2450 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2451 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2453 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2454 */         packetStream.send();
/* 2455 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Interfaces waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2460 */         param2PacketStream.waitForReply();
/* 2461 */         return new Interfaces(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Interfaces(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2471 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2472 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Interfaces" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2474 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2475 */           param2VirtualMachineImpl.printReceiveTrace(4, "interfaces(InterfaceTypeImpl[]): ");
/*      */         }
/* 2477 */         int i = param2PacketStream.readInt();
/* 2478 */         this.interfaces = new InterfaceTypeImpl[i];
/* 2479 */         for (byte b = 0; b < i; b++) {
/* 2480 */           this.interfaces[b] = param2VirtualMachineImpl.interfaceType(param2PacketStream.readClassRef());
/* 2481 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2482 */             param2VirtualMachineImpl.printReceiveTrace(5, "interfaces[i](InterfaceTypeImpl): " + ((this.interfaces[b] == null) ? "NULL" : ("ref=" + this.interfaces[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class ClassObject
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */
/*      */       final ClassObjectReferenceImpl classObject;
/*      */
/*      */
/*      */       static ClassObject process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2497 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2498 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2503 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 11);
/* 2504 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2505 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.ClassObject" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2507 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2508 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2510 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2511 */         packetStream.send();
/* 2512 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClassObject waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2517 */         param2PacketStream.waitForReply();
/* 2518 */         return new ClassObject(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ClassObject(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2528 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2529 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.ClassObject" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2531 */         this.classObject = param2PacketStream.readClassObjectReference();
/* 2532 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2533 */           param2VirtualMachineImpl.printReceiveTrace(4, "classObject(ClassObjectReferenceImpl): " + ((this.classObject == null) ? "NULL" : ("ref=" + this.classObject.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class SourceDebugExtension
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */
/*      */
/*      */       final String extension;
/*      */
/*      */
/*      */       static SourceDebugExtension process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2549 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2550 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2555 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 12);
/* 2556 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2557 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.SourceDebugExtension" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2559 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2560 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2562 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2563 */         packetStream.send();
/* 2564 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SourceDebugExtension waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2569 */         param2PacketStream.waitForReply();
/* 2570 */         return new SourceDebugExtension(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private SourceDebugExtension(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2580 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2581 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.SourceDebugExtension" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2583 */         this.extension = param2PacketStream.readString();
/* 2584 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2585 */           param2VirtualMachineImpl.printReceiveTrace(4, "extension(String): " + this.extension);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class SignatureWithGeneric
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */
/*      */
/*      */       final String signature;
/*      */
/*      */
/*      */       final String genericSignature;
/*      */
/*      */
/*      */
/*      */       static SignatureWithGeneric process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2605 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2606 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2611 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 13);
/* 2612 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2613 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.SignatureWithGeneric" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2615 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2616 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2618 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2619 */         packetStream.send();
/* 2620 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SignatureWithGeneric waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2625 */         param2PacketStream.waitForReply();
/* 2626 */         return new SignatureWithGeneric(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private SignatureWithGeneric(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2642 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2643 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.SignatureWithGeneric" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2645 */         this.signature = param2PacketStream.readString();
/* 2646 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2647 */           param2VirtualMachineImpl.printReceiveTrace(4, "signature(String): " + this.signature);
/*      */         }
/* 2649 */         this.genericSignature = param2PacketStream.readString();
/* 2650 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2651 */           param2VirtualMachineImpl.printReceiveTrace(4, "genericSignature(String): " + this.genericSignature);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class FieldsWithGeneric
/*      */     {
/*      */       static final int COMMAND = 14;
/*      */
/*      */
/*      */
/*      */
/*      */       final FieldInfo[] declared;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static FieldsWithGeneric process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2674 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2675 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2680 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 14);
/* 2681 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2682 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.FieldsWithGeneric" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2684 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2685 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2687 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2688 */         packetStream.send();
/* 2689 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static FieldsWithGeneric waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2694 */         param2PacketStream.waitForReply();
/* 2695 */         return new FieldsWithGeneric(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class FieldInfo
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final String genericSignature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int modBits;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private FieldInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 2734 */           this.fieldID = param3PacketStream.readFieldRef();
/* 2735 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2736 */             param3VirtualMachineImpl.printReceiveTrace(5, "fieldID(long): " + this.fieldID);
/*      */           }
/* 2738 */           this.name = param3PacketStream.readString();
/* 2739 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2740 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2742 */           this.signature = param3PacketStream.readString();
/* 2743 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2744 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2746 */           this.genericSignature = param3PacketStream.readString();
/* 2747 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2748 */             param3VirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 2750 */           this.modBits = param3PacketStream.readInt();
/* 2751 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2752 */             param3VirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private FieldsWithGeneric(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2764 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2765 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.FieldsWithGeneric" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2767 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2768 */           param2VirtualMachineImpl.printReceiveTrace(4, "declared(FieldInfo[]): ");
/*      */         }
/* 2770 */         int i = param2PacketStream.readInt();
/* 2771 */         this.declared = new FieldInfo[i];
/* 2772 */         for (byte b = 0; b < i; b++) {
/* 2773 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2774 */             param2VirtualMachineImpl.printReceiveTrace(5, "declared[i](FieldInfo): ");
/*      */           }
/* 2776 */           this.declared[b] = new FieldInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class MethodsWithGeneric
/*      */     {
/*      */       static final int COMMAND = 15;
/*      */
/*      */
/*      */
/*      */
/*      */       final MethodInfo[] declared;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static MethodsWithGeneric process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2800 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2801 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2806 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 15);
/* 2807 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2808 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.MethodsWithGeneric" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2810 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2811 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2813 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2814 */         packetStream.send();
/* 2815 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static MethodsWithGeneric waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2820 */         param2PacketStream.waitForReply();
/* 2821 */         return new MethodsWithGeneric(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class MethodInfo
/*      */       {
/*      */         final long methodID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final String genericSignature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int modBits;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private MethodInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 2860 */           this.methodID = param3PacketStream.readMethodRef();
/* 2861 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2862 */             param3VirtualMachineImpl.printReceiveTrace(5, "methodID(long): " + this.methodID);
/*      */           }
/* 2864 */           this.name = param3PacketStream.readString();
/* 2865 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2866 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 2868 */           this.signature = param3PacketStream.readString();
/* 2869 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2870 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 2872 */           this.genericSignature = param3PacketStream.readString();
/* 2873 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2874 */             param3VirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 2876 */           this.modBits = param3PacketStream.readInt();
/* 2877 */           if (param3VirtualMachineImpl.traceReceives) {
/* 2878 */             param3VirtualMachineImpl.printReceiveTrace(5, "modBits(int): " + this.modBits);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private MethodsWithGeneric(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2890 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2891 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.MethodsWithGeneric" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2893 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2894 */           param2VirtualMachineImpl.printReceiveTrace(4, "declared(MethodInfo[]): ");
/*      */         }
/* 2896 */         int i = param2PacketStream.readInt();
/* 2897 */         this.declared = new MethodInfo[i];
/* 2898 */         for (byte b = 0; b < i; b++) {
/* 2899 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2900 */             param2VirtualMachineImpl.printReceiveTrace(5, "declared[i](MethodInfo): ");
/*      */           }
/* 2902 */           this.declared[b] = new MethodInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Instances
/*      */     {
/*      */       static final int COMMAND = 16;
/*      */
/*      */
/*      */
/*      */       final ObjectReferenceImpl[] instances;
/*      */
/*      */
/*      */
/*      */       static Instances process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, int param2Int) throws JDWPException {
/* 2921 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Int);
/* 2922 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, int param2Int) {
/* 2928 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 16);
/* 2929 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2930 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.Instances" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2932 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2933 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2935 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2936 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2937 */           packetStream.vm.printTrace("Sending:                 maxInstances(int): " + param2Int);
/*      */         }
/* 2939 */         packetStream.writeInt(param2Int);
/* 2940 */         packetStream.send();
/* 2941 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Instances waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 2946 */         param2PacketStream.waitForReply();
/* 2947 */         return new Instances(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Instances(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 2957 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2958 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.Instances" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 2960 */         if (param2VirtualMachineImpl.traceReceives) {
/* 2961 */           param2VirtualMachineImpl.printReceiveTrace(4, "instances(ObjectReferenceImpl[]): ");
/*      */         }
/* 2963 */         int i = param2PacketStream.readInt();
/* 2964 */         this.instances = new ObjectReferenceImpl[i];
/* 2965 */         for (byte b = 0; b < i; b++) {
/* 2966 */           this.instances[b] = param2PacketStream.readTaggedObjectReference();
/* 2967 */           if (param2VirtualMachineImpl.traceReceives) {
/* 2968 */             param2VirtualMachineImpl.printReceiveTrace(5, "instances[i](ObjectReferenceImpl): " + ((this.instances[b] == null) ? "NULL" : ("ref=" + this.instances[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class ClassFileVersion
/*      */     {
/*      */       static final int COMMAND = 17;
/*      */
/*      */       final int majorVersion;
/*      */
/*      */       final int minorVersion;
/*      */
/*      */
/*      */       static ClassFileVersion process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 2985 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 2986 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 2991 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 17);
/* 2992 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 2993 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.ClassFileVersion" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 2995 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 2996 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 2998 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 2999 */         packetStream.send();
/* 3000 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClassFileVersion waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3005 */         param2PacketStream.waitForReply();
/* 3006 */         return new ClassFileVersion(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private ClassFileVersion(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3021 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3022 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.ClassFileVersion" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3024 */         this.majorVersion = param2PacketStream.readInt();
/* 3025 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3026 */           param2VirtualMachineImpl.printReceiveTrace(4, "majorVersion(int): " + this.majorVersion);
/*      */         }
/* 3028 */         this.minorVersion = param2PacketStream.readInt();
/* 3029 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3030 */           param2VirtualMachineImpl.printReceiveTrace(4, "minorVersion(int): " + this.minorVersion);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class ConstantPool
/*      */     {
/*      */       static final int COMMAND = 18;
/*      */
/*      */
/*      */       final int count;
/*      */
/*      */
/*      */       final byte[] bytes;
/*      */
/*      */
/*      */       static ConstantPool process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) throws JDWPException {
/* 3049 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl);
/* 3050 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl) {
/* 3055 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 2, 18);
/* 3056 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3057 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ReferenceType.ConstantPool" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3059 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3060 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 3062 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 3063 */         packetStream.send();
/* 3064 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ConstantPool waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3069 */         param2PacketStream.waitForReply();
/* 3070 */         return new ConstantPool(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private ConstantPool(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3085 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3086 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ReferenceType.ConstantPool" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3088 */         this.count = param2PacketStream.readInt();
/* 3089 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3090 */           param2VirtualMachineImpl.printReceiveTrace(4, "count(int): " + this.count);
/*      */         }
/* 3092 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3093 */           param2VirtualMachineImpl.printReceiveTrace(4, "bytes(byte[]): ");
/*      */         }
/* 3095 */         int i = param2PacketStream.readInt();
/* 3096 */         this.bytes = new byte[i];
/* 3097 */         for (byte b = 0; b < i; b++) {
/* 3098 */           this.bytes[b] = param2PacketStream.readByte();
/* 3099 */           if (param2VirtualMachineImpl.traceReceives) {
/* 3100 */             param2VirtualMachineImpl.printReceiveTrace(5, "bytes[i](byte): " + this.bytes[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ClassType
/*      */   {
/*      */     static final int COMMAND_SET = 3;
/*      */
/*      */
/*      */     static class Superclass
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final ClassTypeImpl superclass;
/*      */
/*      */       static Superclass process(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl) throws JDWPException {
/* 3120 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassTypeImpl);
/* 3121 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl) {
/* 3126 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 3, 1);
/* 3127 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3128 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassType.Superclass" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3130 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3131 */           packetStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + ((param2ClassTypeImpl == null) ? "NULL" : ("ref=" + param2ClassTypeImpl.ref())));
/*      */         }
/* 3133 */         packetStream.writeClassRef(param2ClassTypeImpl.ref());
/* 3134 */         packetStream.send();
/* 3135 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Superclass waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3140 */         param2PacketStream.waitForReply();
/* 3141 */         return new Superclass(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Superclass(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3151 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3152 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassType.Superclass" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3154 */         this.superclass = param2VirtualMachineImpl.classType(param2PacketStream.readClassRef());
/* 3155 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3156 */           param2VirtualMachineImpl.printReceiveTrace(4, "superclass(ClassTypeImpl): " + ((this.superclass == null) ? "NULL" : ("ref=" + this.superclass.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class FieldValue
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final ValueImpl value;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         FieldValue(long param3Long, ValueImpl param3ValueImpl) {
/* 3191 */           this.fieldID = param3Long;
/* 3192 */           this.value = param3ValueImpl;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 3196 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 3197 */             param3PacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 3199 */           param3PacketStream.writeFieldRef(this.fieldID);
/* 3200 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 3201 */             param3PacketStream.vm.printTrace("Sending:                     value(ValueImpl): " + this.value);
/*      */           }
/* 3203 */           param3PacketStream.writeUntaggedValue(this.value);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static SetValues process(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, FieldValue[] param2ArrayOfFieldValue) throws JDWPException {
/* 3211 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassTypeImpl, param2ArrayOfFieldValue);
/* 3212 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, FieldValue[] param2ArrayOfFieldValue) {
/* 3218 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 3, 2);
/* 3219 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3220 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassType.SetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3222 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3223 */           packetStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + ((param2ClassTypeImpl == null) ? "NULL" : ("ref=" + param2ClassTypeImpl.ref())));
/*      */         }
/* 3225 */         packetStream.writeClassRef(param2ClassTypeImpl.ref());
/* 3226 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3227 */           packetStream.vm.printTrace("Sending:                 values(FieldValue[]): ");
/*      */         }
/* 3229 */         packetStream.writeInt(param2ArrayOfFieldValue.length);
/* 3230 */         for (byte b = 0; b < param2ArrayOfFieldValue.length; b++) {
/* 3231 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3232 */             packetStream.vm.printTrace("Sending:                     values[i](FieldValue): ");
/*      */           }
/* 3234 */           param2ArrayOfFieldValue[b].write(packetStream);
/*      */         }
/* 3236 */         packetStream.send();
/* 3237 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3242 */         param2PacketStream.waitForReply();
/* 3243 */         return new SetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private SetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3248 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3249 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassType.SetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 3;
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
/*      */       final ValueImpl returnValue;
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
/*      */       final ObjectReferenceImpl exception;
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
/*      */       static InvokeMethod process(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) throws JDWPException {
/* 3320 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassTypeImpl, param2ThreadReferenceImpl, param2Long, param2ArrayOfValueImpl, param2Int);
/* 3321 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) {
/* 3330 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 3, 3);
/* 3331 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3332 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassType.InvokeMethod" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3334 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3335 */           packetStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + ((param2ClassTypeImpl == null) ? "NULL" : ("ref=" + param2ClassTypeImpl.ref())));
/*      */         }
/* 3337 */         packetStream.writeClassRef(param2ClassTypeImpl.ref());
/* 3338 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3339 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 3341 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 3342 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3343 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 3345 */         packetStream.writeMethodRef(param2Long);
/* 3346 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3347 */           packetStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3349 */         packetStream.writeInt(param2ArrayOfValueImpl.length);
/* 3350 */         for (byte b = 0; b < param2ArrayOfValueImpl.length; b++) {
/* 3351 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3352 */             packetStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + param2ArrayOfValueImpl[b]);
/*      */           }
/* 3354 */           packetStream.writeValue(param2ArrayOfValueImpl[b]);
/*      */         }
/* 3356 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3357 */           packetStream.vm.printTrace("Sending:                 options(int): " + param2Int);
/*      */         }
/* 3359 */         packetStream.writeInt(param2Int);
/* 3360 */         packetStream.send();
/* 3361 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3366 */         param2PacketStream.waitForReply();
/* 3367 */         return new InvokeMethod(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private InvokeMethod(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3382 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3383 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassType.InvokeMethod" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3385 */         this.returnValue = param2PacketStream.readValue();
/* 3386 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3387 */           param2VirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 3389 */         this.exception = param2PacketStream.readTaggedObjectReference();
/* 3390 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3391 */           param2VirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + ((this.exception == null) ? "NULL" : ("ref=" + this.exception.ref())));
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class NewInstance
/*      */     {
/*      */       static final int COMMAND = 4;
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
/*      */       final ObjectReferenceImpl newObject;
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
/*      */       final ObjectReferenceImpl exception;
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
/*      */       static NewInstance process(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) throws JDWPException {
/* 3460 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassTypeImpl, param2ThreadReferenceImpl, param2Long, param2ArrayOfValueImpl, param2Int);
/* 3461 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassTypeImpl param2ClassTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) {
/* 3470 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 3, 4);
/* 3471 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3472 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassType.NewInstance" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3474 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3475 */           packetStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + ((param2ClassTypeImpl == null) ? "NULL" : ("ref=" + param2ClassTypeImpl.ref())));
/*      */         }
/* 3477 */         packetStream.writeClassRef(param2ClassTypeImpl.ref());
/* 3478 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3479 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 3481 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 3482 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3483 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 3485 */         packetStream.writeMethodRef(param2Long);
/* 3486 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3487 */           packetStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3489 */         packetStream.writeInt(param2ArrayOfValueImpl.length);
/* 3490 */         for (byte b = 0; b < param2ArrayOfValueImpl.length; b++) {
/* 3491 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3492 */             packetStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + param2ArrayOfValueImpl[b]);
/*      */           }
/* 3494 */           packetStream.writeValue(param2ArrayOfValueImpl[b]);
/*      */         }
/* 3496 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3497 */           packetStream.vm.printTrace("Sending:                 options(int): " + param2Int);
/*      */         }
/* 3499 */         packetStream.writeInt(param2Int);
/* 3500 */         packetStream.send();
/* 3501 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static NewInstance waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3506 */         param2PacketStream.waitForReply();
/* 3507 */         return new NewInstance(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private NewInstance(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3523 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3524 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassType.NewInstance" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3526 */         this.newObject = param2PacketStream.readTaggedObjectReference();
/* 3527 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3528 */           param2VirtualMachineImpl.printReceiveTrace(4, "newObject(ObjectReferenceImpl): " + ((this.newObject == null) ? "NULL" : ("ref=" + this.newObject.ref())));
/*      */         }
/* 3530 */         this.exception = param2PacketStream.readTaggedObjectReference();
/* 3531 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3532 */           param2VirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + ((this.exception == null) ? "NULL" : ("ref=" + this.exception.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ArrayType
/*      */   {
/*      */     static final int COMMAND_SET = 4;
/*      */
/*      */
/*      */     static class NewInstance
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final ObjectReferenceImpl newArray;
/*      */
/*      */
/*      */       static NewInstance process(VirtualMachineImpl param2VirtualMachineImpl, ArrayTypeImpl param2ArrayTypeImpl, int param2Int) throws JDWPException {
/* 3552 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayTypeImpl, param2Int);
/* 3553 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ArrayTypeImpl param2ArrayTypeImpl, int param2Int) {
/* 3559 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 4, 1);
/* 3560 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3561 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ArrayType.NewInstance" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3563 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3564 */           packetStream.vm.printTrace("Sending:                 arrType(ArrayTypeImpl): " + ((param2ArrayTypeImpl == null) ? "NULL" : ("ref=" + param2ArrayTypeImpl.ref())));
/*      */         }
/* 3566 */         packetStream.writeClassRef(param2ArrayTypeImpl.ref());
/* 3567 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3568 */           packetStream.vm.printTrace("Sending:                 length(int): " + param2Int);
/*      */         }
/* 3570 */         packetStream.writeInt(param2Int);
/* 3571 */         packetStream.send();
/* 3572 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static NewInstance waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3577 */         param2PacketStream.waitForReply();
/* 3578 */         return new NewInstance(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private NewInstance(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3588 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3589 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ArrayType.NewInstance" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3591 */         this.newArray = param2PacketStream.readTaggedObjectReference();
/* 3592 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3593 */           param2VirtualMachineImpl.printReceiveTrace(4, "newArray(ObjectReferenceImpl): " + ((this.newArray == null) ? "NULL" : ("ref=" + this.newArray.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
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
/*      */   static class InterfaceType
/*      */   {
/*      */     static final int COMMAND_SET = 5;
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
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 1;
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
/*      */       final ValueImpl returnValue;
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
/*      */       final ObjectReferenceImpl exception;
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
/*      */       static InvokeMethod process(VirtualMachineImpl param2VirtualMachineImpl, InterfaceTypeImpl param2InterfaceTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) throws JDWPException {
/* 3668 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2InterfaceTypeImpl, param2ThreadReferenceImpl, param2Long, param2ArrayOfValueImpl, param2Int);
/* 3669 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, InterfaceTypeImpl param2InterfaceTypeImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) {
/* 3678 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 5, 1);
/* 3679 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3680 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.InterfaceType.InvokeMethod" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3682 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3683 */           packetStream.vm.printTrace("Sending:                 clazz(InterfaceTypeImpl): " + ((param2InterfaceTypeImpl == null) ? "NULL" : ("ref=" + param2InterfaceTypeImpl.ref())));
/*      */         }
/* 3685 */         packetStream.writeClassRef(param2InterfaceTypeImpl.ref());
/* 3686 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3687 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 3689 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 3690 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3691 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 3693 */         packetStream.writeMethodRef(param2Long);
/* 3694 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3695 */           packetStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 3697 */         packetStream.writeInt(param2ArrayOfValueImpl.length);
/* 3698 */         for (byte b = 0; b < param2ArrayOfValueImpl.length; b++) {
/* 3699 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3700 */             packetStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + param2ArrayOfValueImpl[b]);
/*      */           }
/* 3702 */           packetStream.writeValue(param2ArrayOfValueImpl[b]);
/*      */         }
/* 3704 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3705 */           packetStream.vm.printTrace("Sending:                 options(int): " + param2Int);
/*      */         }
/* 3707 */         packetStream.writeInt(param2Int);
/* 3708 */         packetStream.send();
/* 3709 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3714 */         param2PacketStream.waitForReply();
/* 3715 */         return new InvokeMethod(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private InvokeMethod(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3730 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3731 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.InterfaceType.InvokeMethod" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3733 */         this.returnValue = param2PacketStream.readValue();
/* 3734 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3735 */           param2VirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 3737 */         this.exception = param2PacketStream.readTaggedObjectReference();
/* 3738 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3739 */           param2VirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + ((this.exception == null) ? "NULL" : ("ref=" + this.exception.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */   static class Method
/*      */   {
/*      */     static final int COMMAND_SET = 6;
/*      */
/*      */
/*      */     static class LineTable
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final long start;
/*      */
/*      */       final long end;
/*      */
/*      */       final LineInfo[] lines;
/*      */
/*      */
/*      */       static LineTable process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) throws JDWPException {
/* 3764 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Long);
/* 3765 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) {
/* 3771 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 6, 1);
/* 3772 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3773 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.Method.LineTable" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3775 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3776 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 3778 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 3779 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3780 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 3782 */         packetStream.writeMethodRef(param2Long);
/* 3783 */         packetStream.send();
/* 3784 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static LineTable waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3789 */         param2PacketStream.waitForReply();
/* 3790 */         return new LineTable(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static class LineInfo
/*      */       {
/*      */         final long lineCodeIndex;
/*      */
/*      */
/*      */
/*      */         final int lineNumber;
/*      */
/*      */
/*      */
/*      */         private LineInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 3807 */           this.lineCodeIndex = param3PacketStream.readLong();
/* 3808 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3809 */             param3VirtualMachineImpl.printReceiveTrace(5, "lineCodeIndex(long): " + this.lineCodeIndex);
/*      */           }
/* 3811 */           this.lineNumber = param3PacketStream.readInt();
/* 3812 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3813 */             param3VirtualMachineImpl.printReceiveTrace(5, "lineNumber(int): " + this.lineNumber);
/*      */           }
/*      */         }
/*      */       }
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
/*      */       private LineTable(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3835 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3836 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Method.LineTable" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3838 */         this.start = param2PacketStream.readLong();
/* 3839 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3840 */           param2VirtualMachineImpl.printReceiveTrace(4, "start(long): " + this.start);
/*      */         }
/* 3842 */         this.end = param2PacketStream.readLong();
/* 3843 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3844 */           param2VirtualMachineImpl.printReceiveTrace(4, "end(long): " + this.end);
/*      */         }
/* 3846 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3847 */           param2VirtualMachineImpl.printReceiveTrace(4, "lines(LineInfo[]): ");
/*      */         }
/* 3849 */         int i = param2PacketStream.readInt();
/* 3850 */         this.lines = new LineInfo[i];
/* 3851 */         for (byte b = 0; b < i; b++) {
/* 3852 */           if (param2VirtualMachineImpl.traceReceives) {
/* 3853 */             param2VirtualMachineImpl.printReceiveTrace(5, "lines[i](LineInfo): ");
/*      */           }
/* 3855 */           this.lines[b] = new LineInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class VariableTable
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */       final int argCnt;
/*      */
/*      */       final SlotInfo[] slots;
/*      */
/*      */
/*      */       static VariableTable process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) throws JDWPException {
/* 3873 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Long);
/* 3874 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) {
/* 3880 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 6, 2);
/* 3881 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 3882 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.Method.VariableTable" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 3884 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3885 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 3887 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 3888 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 3889 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 3891 */         packetStream.writeMethodRef(param2Long);
/* 3892 */         packetStream.send();
/* 3893 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static VariableTable waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 3898 */         param2PacketStream.waitForReply();
/* 3899 */         return new VariableTable(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class SlotInfo
/*      */       {
/*      */         final long codeIndex;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int length;
/*      */
/*      */
/*      */
/*      */
/*      */         final int slot;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private SlotInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 3938 */           this.codeIndex = param3PacketStream.readLong();
/* 3939 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3940 */             param3VirtualMachineImpl.printReceiveTrace(5, "codeIndex(long): " + this.codeIndex);
/*      */           }
/* 3942 */           this.name = param3PacketStream.readString();
/* 3943 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3944 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 3946 */           this.signature = param3PacketStream.readString();
/* 3947 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3948 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 3950 */           this.length = param3PacketStream.readInt();
/* 3951 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3952 */             param3VirtualMachineImpl.printReceiveTrace(5, "length(int): " + this.length);
/*      */           }
/* 3954 */           this.slot = param3PacketStream.readInt();
/* 3955 */           if (param3VirtualMachineImpl.traceReceives) {
/* 3956 */             param3VirtualMachineImpl.printReceiveTrace(5, "slot(int): " + this.slot);
/*      */           }
/*      */         }
/*      */       }
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
/*      */       private VariableTable(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 3974 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3975 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Method.VariableTable" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 3977 */         this.argCnt = param2PacketStream.readInt();
/* 3978 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3979 */           param2VirtualMachineImpl.printReceiveTrace(4, "argCnt(int): " + this.argCnt);
/*      */         }
/* 3981 */         if (param2VirtualMachineImpl.traceReceives) {
/* 3982 */           param2VirtualMachineImpl.printReceiveTrace(4, "slots(SlotInfo[]): ");
/*      */         }
/* 3984 */         int i = param2PacketStream.readInt();
/* 3985 */         this.slots = new SlotInfo[i];
/* 3986 */         for (byte b = 0; b < i; b++) {
/* 3987 */           if (param2VirtualMachineImpl.traceReceives) {
/* 3988 */             param2VirtualMachineImpl.printReceiveTrace(5, "slots[i](SlotInfo): ");
/*      */           }
/* 3990 */           this.slots[b] = new SlotInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Bytecodes
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */       final byte[] bytes;
/*      */
/*      */
/*      */
/*      */       static Bytecodes process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) throws JDWPException {
/* 4008 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Long);
/* 4009 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) {
/* 4015 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 6, 3);
/* 4016 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4017 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.Method.Bytecodes" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4019 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4020 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 4022 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 4023 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4024 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 4026 */         packetStream.writeMethodRef(param2Long);
/* 4027 */         packetStream.send();
/* 4028 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Bytecodes waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4033 */         param2PacketStream.waitForReply();
/* 4034 */         return new Bytecodes(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       private Bytecodes(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4041 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4042 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Method.Bytecodes" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4044 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4045 */           param2VirtualMachineImpl.printReceiveTrace(4, "bytes(byte[]): ");
/*      */         }
/* 4047 */         int i = param2PacketStream.readInt();
/* 4048 */         this.bytes = new byte[i];
/* 4049 */         for (byte b = 0; b < i; b++) {
/* 4050 */           this.bytes[b] = param2PacketStream.readByte();
/* 4051 */           if (param2VirtualMachineImpl.traceReceives) {
/* 4052 */             param2VirtualMachineImpl.printReceiveTrace(5, "bytes[i](byte): " + this.bytes[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class IsObsolete
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */
/*      */
/*      */
/*      */       final boolean isObsolete;
/*      */
/*      */
/*      */
/*      */
/*      */       static IsObsolete process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) throws JDWPException {
/* 4073 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Long);
/* 4074 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) {
/* 4080 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 6, 4);
/* 4081 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4082 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.Method.IsObsolete" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4084 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4085 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 4087 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 4088 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4089 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 4091 */         packetStream.writeMethodRef(param2Long);
/* 4092 */         packetStream.send();
/* 4093 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static IsObsolete waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4098 */         param2PacketStream.waitForReply();
/* 4099 */         return new IsObsolete(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private IsObsolete(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4111 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4112 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Method.IsObsolete" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4114 */         this.isObsolete = param2PacketStream.readBoolean();
/* 4115 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4116 */           param2VirtualMachineImpl.printReceiveTrace(4, "isObsolete(boolean): " + this.isObsolete);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class VariableTableWithGeneric
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */
/*      */
/*      */
/*      */       final int argCnt;
/*      */
/*      */
/*      */
/*      */       final SlotInfo[] slots;
/*      */
/*      */
/*      */
/*      */       static VariableTableWithGeneric process(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) throws JDWPException {
/* 4139 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ReferenceTypeImpl, param2Long);
/* 4140 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ReferenceTypeImpl param2ReferenceTypeImpl, long param2Long) {
/* 4146 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 6, 5);
/* 4147 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4148 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.Method.VariableTableWithGeneric" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4150 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4151 */           packetStream.vm.printTrace("Sending:                 refType(ReferenceTypeImpl): " + ((param2ReferenceTypeImpl == null) ? "NULL" : ("ref=" + param2ReferenceTypeImpl.ref())));
/*      */         }
/* 4153 */         packetStream.writeClassRef(param2ReferenceTypeImpl.ref());
/* 4154 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4155 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 4157 */         packetStream.writeMethodRef(param2Long);
/* 4158 */         packetStream.send();
/* 4159 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static VariableTableWithGeneric waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4164 */         param2PacketStream.waitForReply();
/* 4165 */         return new VariableTableWithGeneric(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class SlotInfo
/*      */       {
/*      */         final long codeIndex;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String name;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String signature;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final String genericSignature;
/*      */
/*      */
/*      */
/*      */
/*      */         final int length;
/*      */
/*      */
/*      */
/*      */
/*      */         final int slot;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         private SlotInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 4210 */           this.codeIndex = param3PacketStream.readLong();
/* 4211 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4212 */             param3VirtualMachineImpl.printReceiveTrace(5, "codeIndex(long): " + this.codeIndex);
/*      */           }
/* 4214 */           this.name = param3PacketStream.readString();
/* 4215 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4216 */             param3VirtualMachineImpl.printReceiveTrace(5, "name(String): " + this.name);
/*      */           }
/* 4218 */           this.signature = param3PacketStream.readString();
/* 4219 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4220 */             param3VirtualMachineImpl.printReceiveTrace(5, "signature(String): " + this.signature);
/*      */           }
/* 4222 */           this.genericSignature = param3PacketStream.readString();
/* 4223 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4224 */             param3VirtualMachineImpl.printReceiveTrace(5, "genericSignature(String): " + this.genericSignature);
/*      */           }
/* 4226 */           this.length = param3PacketStream.readInt();
/* 4227 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4228 */             param3VirtualMachineImpl.printReceiveTrace(5, "length(int): " + this.length);
/*      */           }
/* 4230 */           this.slot = param3PacketStream.readInt();
/* 4231 */           if (param3VirtualMachineImpl.traceReceives) {
/* 4232 */             param3VirtualMachineImpl.printReceiveTrace(5, "slot(int): " + this.slot);
/*      */           }
/*      */         }
/*      */       }
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
/*      */       private VariableTableWithGeneric(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4250 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4251 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Method.VariableTableWithGeneric" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4253 */         this.argCnt = param2PacketStream.readInt();
/* 4254 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4255 */           param2VirtualMachineImpl.printReceiveTrace(4, "argCnt(int): " + this.argCnt);
/*      */         }
/* 4257 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4258 */           param2VirtualMachineImpl.printReceiveTrace(4, "slots(SlotInfo[]): ");
/*      */         }
/* 4260 */         int i = param2PacketStream.readInt();
/* 4261 */         this.slots = new SlotInfo[i];
/* 4262 */         for (byte b = 0; b < i; b++) {
/* 4263 */           if (param2VirtualMachineImpl.traceReceives) {
/* 4264 */             param2VirtualMachineImpl.printReceiveTrace(5, "slots[i](SlotInfo): ");
/*      */           }
/* 4266 */           this.slots[b] = new SlotInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class Field
/*      */   {
/*      */     static final int COMMAND_SET = 8;
/*      */   }
/*      */
/*      */
/*      */   static class ObjectReference
/*      */   {
/*      */     static final int COMMAND_SET = 9;
/*      */
/*      */
/*      */     static class ReferenceType
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final byte refTypeTag;
/*      */       final long typeID;
/*      */
/*      */       static ReferenceType process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4291 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4292 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 4297 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 1);
/* 4298 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4299 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.ReferenceType" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4301 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4302 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4304 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4305 */         packetStream.send();
/* 4306 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ReferenceType waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4311 */         param2PacketStream.waitForReply();
/* 4312 */         return new ReferenceType(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private ReferenceType(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4328 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4329 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.ReferenceType" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4331 */         this.refTypeTag = param2PacketStream.readByte();
/* 4332 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4333 */           param2VirtualMachineImpl.printReceiveTrace(4, "refTypeTag(byte): " + this.refTypeTag);
/*      */         }
/* 4335 */         this.typeID = param2PacketStream.readClassRef();
/* 4336 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4337 */           param2VirtualMachineImpl.printReceiveTrace(4, "typeID(long): ref=" + this.typeID);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */       final ValueImpl[] values;
/*      */
/*      */
/*      */
/*      */       static class Field
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */         Field(long param3Long) {
/* 4360 */           this.fieldID = param3Long;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 4364 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 4365 */             param3PacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 4367 */           param3PacketStream.writeFieldRef(this.fieldID);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static GetValues process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, Field[] param2ArrayOfField) throws JDWPException {
/* 4375 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl, param2ArrayOfField);
/* 4376 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, Field[] param2ArrayOfField) {
/* 4382 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 2);
/* 4383 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4384 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.GetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4386 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4387 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4389 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4390 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4391 */           packetStream.vm.printTrace("Sending:                 fields(Field[]): ");
/*      */         }
/* 4393 */         packetStream.writeInt(param2ArrayOfField.length);
/* 4394 */         for (byte b = 0; b < param2ArrayOfField.length; b++) {
/* 4395 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4396 */             packetStream.vm.printTrace("Sending:                     fields[i](Field): ");
/*      */           }
/* 4398 */           param2ArrayOfField[b].write(packetStream);
/*      */         }
/* 4400 */         packetStream.send();
/* 4401 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static GetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4406 */         param2PacketStream.waitForReply();
/* 4407 */         return new GetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private GetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4420 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4421 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.GetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4423 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4424 */           param2VirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 4426 */         int i = param2PacketStream.readInt();
/* 4427 */         this.values = new ValueImpl[i];
/* 4428 */         for (byte b = 0; b < i; b++) {
/* 4429 */           this.values[b] = param2PacketStream.readValue();
/* 4430 */           if (param2VirtualMachineImpl.traceReceives) {
/* 4431 */             param2VirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class FieldValue
/*      */       {
/*      */         final long fieldID;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final ValueImpl value;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         FieldValue(long param3Long, ValueImpl param3ValueImpl) {
/* 4467 */           this.fieldID = param3Long;
/* 4468 */           this.value = param3ValueImpl;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 4472 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 4473 */             param3PacketStream.vm.printTrace("Sending:                     fieldID(long): " + this.fieldID);
/*      */           }
/* 4475 */           param3PacketStream.writeFieldRef(this.fieldID);
/* 4476 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 4477 */             param3PacketStream.vm.printTrace("Sending:                     value(ValueImpl): " + this.value);
/*      */           }
/* 4479 */           param3PacketStream.writeUntaggedValue(this.value);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static SetValues process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, FieldValue[] param2ArrayOfFieldValue) throws JDWPException {
/* 4487 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl, param2ArrayOfFieldValue);
/* 4488 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, FieldValue[] param2ArrayOfFieldValue) {
/* 4494 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 3);
/* 4495 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4496 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.SetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4498 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4499 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4501 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4502 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4503 */           packetStream.vm.printTrace("Sending:                 values(FieldValue[]): ");
/*      */         }
/* 4505 */         packetStream.writeInt(param2ArrayOfFieldValue.length);
/* 4506 */         for (byte b = 0; b < param2ArrayOfFieldValue.length; b++) {
/* 4507 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4508 */             packetStream.vm.printTrace("Sending:                     values[i](FieldValue): ");
/*      */           }
/* 4510 */           param2ArrayOfFieldValue[b].write(packetStream);
/*      */         }
/* 4512 */         packetStream.send();
/* 4513 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4518 */         param2PacketStream.waitForReply();
/* 4519 */         return new SetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private SetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4524 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4525 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.SetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class MonitorInfo
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */
/*      */       final ThreadReferenceImpl owner;
/*      */
/*      */       final int entryCount;
/*      */
/*      */       final ThreadReferenceImpl[] waiters;
/*      */
/*      */       static MonitorInfo process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4542 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4543 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 4548 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 5);
/* 4549 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4550 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.MonitorInfo" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4552 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4553 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4555 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4556 */         packetStream.send();
/* 4557 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static MonitorInfo waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4562 */         param2PacketStream.waitForReply();
/* 4563 */         return new MonitorInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private MonitorInfo(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4584 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4585 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.MonitorInfo" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4587 */         this.owner = param2PacketStream.readThreadReference();
/* 4588 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4589 */           param2VirtualMachineImpl.printReceiveTrace(4, "owner(ThreadReferenceImpl): " + ((this.owner == null) ? "NULL" : ("ref=" + this.owner.ref())));
/*      */         }
/* 4591 */         this.entryCount = param2PacketStream.readInt();
/* 4592 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4593 */           param2VirtualMachineImpl.printReceiveTrace(4, "entryCount(int): " + this.entryCount);
/*      */         }
/* 4595 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4596 */           param2VirtualMachineImpl.printReceiveTrace(4, "waiters(ThreadReferenceImpl[]): ");
/*      */         }
/* 4598 */         int i = param2PacketStream.readInt();
/* 4599 */         this.waiters = new ThreadReferenceImpl[i];
/* 4600 */         for (byte b = 0; b < i; b++) {
/* 4601 */           this.waiters[b] = param2PacketStream.readThreadReference();
/* 4602 */           if (param2VirtualMachineImpl.traceReceives) {
/* 4603 */             param2VirtualMachineImpl.printReceiveTrace(5, "waiters[i](ThreadReferenceImpl): " + ((this.waiters[b] == null) ? "NULL" : ("ref=" + this.waiters[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class InvokeMethod
/*      */     {
/*      */       static final int COMMAND = 6;
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
/*      */       final ValueImpl returnValue;
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
/*      */       final ObjectReferenceImpl exception;
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
/*      */       static InvokeMethod process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ClassTypeImpl param2ClassTypeImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) throws JDWPException {
/* 4676 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl, param2ThreadReferenceImpl, param2ClassTypeImpl, param2Long, param2ArrayOfValueImpl, param2Int);
/* 4677 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ClassTypeImpl param2ClassTypeImpl, long param2Long, ValueImpl[] param2ArrayOfValueImpl, int param2Int) {
/* 4687 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 6);
/* 4688 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4689 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.InvokeMethod" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4691 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4692 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4694 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4695 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4696 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 4698 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 4699 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4700 */           packetStream.vm.printTrace("Sending:                 clazz(ClassTypeImpl): " + ((param2ClassTypeImpl == null) ? "NULL" : ("ref=" + param2ClassTypeImpl.ref())));
/*      */         }
/* 4702 */         packetStream.writeClassRef(param2ClassTypeImpl.ref());
/* 4703 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4704 */           packetStream.vm.printTrace("Sending:                 methodID(long): " + param2Long);
/*      */         }
/* 4706 */         packetStream.writeMethodRef(param2Long);
/* 4707 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4708 */           packetStream.vm.printTrace("Sending:                 arguments(ValueImpl[]): ");
/*      */         }
/* 4710 */         packetStream.writeInt(param2ArrayOfValueImpl.length);
/* 4711 */         for (byte b = 0; b < param2ArrayOfValueImpl.length; b++) {
/* 4712 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4713 */             packetStream.vm.printTrace("Sending:                     arguments[i](ValueImpl): " + param2ArrayOfValueImpl[b]);
/*      */           }
/* 4715 */           packetStream.writeValue(param2ArrayOfValueImpl[b]);
/*      */         }
/* 4717 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4718 */           packetStream.vm.printTrace("Sending:                 options(int): " + param2Int);
/*      */         }
/* 4720 */         packetStream.writeInt(param2Int);
/* 4721 */         packetStream.send();
/* 4722 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static InvokeMethod waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4727 */         param2PacketStream.waitForReply();
/* 4728 */         return new InvokeMethod(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private InvokeMethod(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4743 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4744 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.InvokeMethod" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4746 */         this.returnValue = param2PacketStream.readValue();
/* 4747 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4748 */           param2VirtualMachineImpl.printReceiveTrace(4, "returnValue(ValueImpl): " + this.returnValue);
/*      */         }
/* 4750 */         this.exception = param2PacketStream.readTaggedObjectReference();
/* 4751 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4752 */           param2VirtualMachineImpl.printReceiveTrace(4, "exception(ObjectReferenceImpl): " + ((this.exception == null) ? "NULL" : ("ref=" + this.exception.ref())));
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class DisableCollection
/*      */     {
/*      */       static final int COMMAND = 7;
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
/*      */       static DisableCollection process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4784 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4785 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 4790 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 7);
/* 4791 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4792 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.DisableCollection" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4794 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4795 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4797 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4798 */         packetStream.send();
/* 4799 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static DisableCollection waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4804 */         param2PacketStream.waitForReply();
/* 4805 */         return new DisableCollection(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private DisableCollection(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4810 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4811 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.DisableCollection" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class EnableCollection
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static EnableCollection process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4830 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4831 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 4836 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 8);
/* 4837 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4838 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.EnableCollection" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4840 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4841 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4843 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4844 */         packetStream.send();
/* 4845 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static EnableCollection waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4850 */         param2PacketStream.waitForReply();
/* 4851 */         return new EnableCollection(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private EnableCollection(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4856 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4857 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.EnableCollection" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class IsCollected
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */
/*      */       final boolean isCollected;
/*      */
/*      */
/*      */       static IsCollected process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4872 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4873 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 4878 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 9);
/* 4879 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4880 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.IsCollected" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4882 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4883 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4885 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4886 */         packetStream.send();
/* 4887 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static IsCollected waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4892 */         param2PacketStream.waitForReply();
/* 4893 */         return new IsCollected(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private IsCollected(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4903 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4904 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.IsCollected" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4906 */         this.isCollected = param2PacketStream.readBoolean();
/* 4907 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4908 */           param2VirtualMachineImpl.printReceiveTrace(4, "isCollected(boolean): " + this.isCollected);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class ReferringObjects
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */
/*      */
/*      */
/*      */
/*      */       final ObjectReferenceImpl[] referringObjects;
/*      */
/*      */
/*      */
/*      */
/*      */       static ReferringObjects process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, int param2Int) throws JDWPException {
/* 4930 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl, param2Int);
/* 4931 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl, int param2Int) {
/* 4937 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 9, 10);
/* 4938 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 4939 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ObjectReference.ReferringObjects" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 4941 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4942 */           packetStream.vm.printTrace("Sending:                 object(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 4944 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 4945 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 4946 */           packetStream.vm.printTrace("Sending:                 maxReferrers(int): " + param2Int);
/*      */         }
/* 4948 */         packetStream.writeInt(param2Int);
/* 4949 */         packetStream.send();
/* 4950 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ReferringObjects waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 4955 */         param2PacketStream.waitForReply();
/* 4956 */         return new ReferringObjects(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ReferringObjects(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 4966 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4967 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ObjectReference.ReferringObjects" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 4969 */         if (param2VirtualMachineImpl.traceReceives) {
/* 4970 */           param2VirtualMachineImpl.printReceiveTrace(4, "referringObjects(ObjectReferenceImpl[]): ");
/*      */         }
/* 4972 */         int i = param2PacketStream.readInt();
/* 4973 */         this.referringObjects = new ObjectReferenceImpl[i];
/* 4974 */         for (byte b = 0; b < i; b++) {
/* 4975 */           this.referringObjects[b] = param2PacketStream.readTaggedObjectReference();
/* 4976 */           if (param2VirtualMachineImpl.traceReceives) {
/* 4977 */             param2VirtualMachineImpl.printReceiveTrace(5, "referringObjects[i](ObjectReferenceImpl): " + ((this.referringObjects[b] == null) ? "NULL" : ("ref=" + this.referringObjects[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class StringReference
/*      */   {
/*      */     static final int COMMAND_SET = 10;
/*      */
/*      */
/*      */     static class Value
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final String stringValue;
/*      */
/*      */       static Value process(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 4997 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ObjectReferenceImpl);
/* 4998 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 5003 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 10, 1);
/* 5004 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5005 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.StringReference.Value" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5007 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5008 */           packetStream.vm.printTrace("Sending:                 stringObject(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 5010 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 5011 */         packetStream.send();
/* 5012 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Value waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5017 */         param2PacketStream.waitForReply();
/* 5018 */         return new Value(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Value(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5028 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5029 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.StringReference.Value" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5031 */         this.stringValue = param2PacketStream.readString();
/* 5032 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5033 */           param2VirtualMachineImpl.printReceiveTrace(4, "stringValue(String): " + this.stringValue);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ThreadReference
/*      */   {
/*      */     static final int COMMAND_SET = 11;
/*      */
/*      */
/*      */     static class Name
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final String threadName;
/*      */
/*      */       static Name process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5052 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5053 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5058 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 1);
/* 5059 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5060 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Name" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5062 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5063 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5065 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5066 */         packetStream.send();
/* 5067 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Name waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5072 */         param2PacketStream.waitForReply();
/* 5073 */         return new Name(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Name(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5083 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5084 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Name" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5086 */         this.threadName = param2PacketStream.readString();
/* 5087 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5088 */           param2VirtualMachineImpl.printReceiveTrace(4, "threadName(String): " + this.threadName);
/*      */         }
/*      */       }
/*      */     }
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
/*      */     static class Suspend
/*      */     {
/*      */       static final int COMMAND = 2;
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
/*      */       static Suspend process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5123 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5124 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5129 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 2);
/* 5130 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5131 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Suspend" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5133 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5134 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5136 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5137 */         packetStream.send();
/* 5138 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Suspend waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5143 */         param2PacketStream.waitForReply();
/* 5144 */         return new Suspend(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Suspend(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5149 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5150 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Suspend" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Resume
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Resume process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5169 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5170 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5175 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 3);
/* 5176 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5177 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Resume" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5179 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5180 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5182 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5183 */         packetStream.send();
/* 5184 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Resume waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5189 */         param2PacketStream.waitForReply();
/* 5190 */         return new Resume(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Resume(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5195 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5196 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Resume" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class Status
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */
/*      */       final int threadStatus;
/*      */
/*      */       final int suspendStatus;
/*      */
/*      */
/*      */       static Status process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5213 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5214 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5219 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 4);
/* 5220 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5221 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Status" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5223 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5224 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5226 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5227 */         packetStream.send();
/* 5228 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Status waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5233 */         param2PacketStream.waitForReply();
/* 5234 */         return new Status(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private Status(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5251 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5252 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Status" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5254 */         this.threadStatus = param2PacketStream.readInt();
/* 5255 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5256 */           param2VirtualMachineImpl.printReceiveTrace(4, "threadStatus(int): " + this.threadStatus);
/*      */         }
/* 5258 */         this.suspendStatus = param2PacketStream.readInt();
/* 5259 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5260 */           param2VirtualMachineImpl.printReceiveTrace(4, "suspendStatus(int): " + this.suspendStatus);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class ThreadGroup
/*      */     {
/*      */       static final int COMMAND = 5;
/*      */
/*      */       final ThreadGroupReferenceImpl group;
/*      */
/*      */
/*      */       static ThreadGroup process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5274 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5275 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5280 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 5);
/* 5281 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5282 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.ThreadGroup" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5284 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5285 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5287 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5288 */         packetStream.send();
/* 5289 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ThreadGroup waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5294 */         param2PacketStream.waitForReply();
/* 5295 */         return new ThreadGroup(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ThreadGroup(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5305 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5306 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.ThreadGroup" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5308 */         this.group = param2PacketStream.readThreadGroupReference();
/* 5309 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5310 */           param2VirtualMachineImpl.printReceiveTrace(4, "group(ThreadGroupReferenceImpl): " + ((this.group == null) ? "NULL" : ("ref=" + this.group.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Frames
/*      */     {
/*      */       static final int COMMAND = 6;
/*      */
/*      */
/*      */
/*      */       final Frame[] frames;
/*      */
/*      */
/*      */
/*      */
/*      */       static Frames process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, int param2Int1, int param2Int2) throws JDWPException {
/* 5330 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2Int1, param2Int2);
/* 5331 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, int param2Int1, int param2Int2) {
/* 5338 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 6);
/* 5339 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5340 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Frames" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5342 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5343 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5345 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5346 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5347 */           packetStream.vm.printTrace("Sending:                 startFrame(int): " + param2Int1);
/*      */         }
/* 5349 */         packetStream.writeInt(param2Int1);
/* 5350 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5351 */           packetStream.vm.printTrace("Sending:                 length(int): " + param2Int2);
/*      */         }
/* 5353 */         packetStream.writeInt(param2Int2);
/* 5354 */         packetStream.send();
/* 5355 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Frames waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5360 */         param2PacketStream.waitForReply();
/* 5361 */         return new Frames(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static class Frame
/*      */       {
/*      */         final long frameID;
/*      */
/*      */
/*      */         final Location location;
/*      */
/*      */
/*      */
/*      */         private Frame(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 5377 */           this.frameID = param3PacketStream.readFrameRef();
/* 5378 */           if (param3VirtualMachineImpl.traceReceives) {
/* 5379 */             param3VirtualMachineImpl.printReceiveTrace(5, "frameID(long): " + this.frameID);
/*      */           }
/* 5381 */           this.location = param3PacketStream.readLocation();
/* 5382 */           if (param3VirtualMachineImpl.traceReceives) {
/* 5383 */             param3VirtualMachineImpl.printReceiveTrace(5, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Frames(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5395 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5396 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Frames" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5398 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5399 */           param2VirtualMachineImpl.printReceiveTrace(4, "frames(Frame[]): ");
/*      */         }
/* 5401 */         int i = param2PacketStream.readInt();
/* 5402 */         this.frames = new Frame[i];
/* 5403 */         for (byte b = 0; b < i; b++) {
/* 5404 */           if (param2VirtualMachineImpl.traceReceives) {
/* 5405 */             param2VirtualMachineImpl.printReceiveTrace(5, "frames[i](Frame): ");
/*      */           }
/* 5407 */           this.frames[b] = new Frame(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class FrameCount
/*      */     {
/*      */       static final int COMMAND = 7;
/*      */
/*      */
/*      */       final int frameCount;
/*      */
/*      */
/*      */
/*      */       static FrameCount process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5424 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5425 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5430 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 7);
/* 5431 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5432 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.FrameCount" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5434 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5435 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5437 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5438 */         packetStream.send();
/* 5439 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static FrameCount waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5444 */         param2PacketStream.waitForReply();
/* 5445 */         return new FrameCount(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private FrameCount(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5455 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5456 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.FrameCount" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5458 */         this.frameCount = param2PacketStream.readInt();
/* 5459 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5460 */           param2VirtualMachineImpl.printReceiveTrace(4, "frameCount(int): " + this.frameCount);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class OwnedMonitors
/*      */     {
/*      */       static final int COMMAND = 8;
/*      */
/*      */
/*      */       final ObjectReferenceImpl[] owned;
/*      */
/*      */
/*      */
/*      */       static OwnedMonitors process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5478 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5479 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5484 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 8);
/* 5485 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5486 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitors" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5488 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5489 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5491 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5492 */         packetStream.send();
/* 5493 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static OwnedMonitors waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5498 */         param2PacketStream.waitForReply();
/* 5499 */         return new OwnedMonitors(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private OwnedMonitors(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5509 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5510 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitors" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5512 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5513 */           param2VirtualMachineImpl.printReceiveTrace(4, "owned(ObjectReferenceImpl[]): ");
/*      */         }
/* 5515 */         int i = param2PacketStream.readInt();
/* 5516 */         this.owned = new ObjectReferenceImpl[i];
/* 5517 */         for (byte b = 0; b < i; b++) {
/* 5518 */           this.owned[b] = param2PacketStream.readTaggedObjectReference();
/* 5519 */           if (param2VirtualMachineImpl.traceReceives) {
/* 5520 */             param2VirtualMachineImpl.printReceiveTrace(5, "owned[i](ObjectReferenceImpl): " + ((this.owned[b] == null) ? "NULL" : ("ref=" + this.owned[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class CurrentContendedMonitor
/*      */     {
/*      */       static final int COMMAND = 9;
/*      */
/*      */
/*      */
/*      */       final ObjectReferenceImpl monitor;
/*      */
/*      */
/*      */
/*      */
/*      */       static CurrentContendedMonitor process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5542 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5543 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5548 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 9);
/* 5549 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5550 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.CurrentContendedMonitor" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5552 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5553 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5555 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5556 */         packetStream.send();
/* 5557 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static CurrentContendedMonitor waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5562 */         param2PacketStream.waitForReply();
/* 5563 */         return new CurrentContendedMonitor(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private CurrentContendedMonitor(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5574 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5575 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.CurrentContendedMonitor" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5577 */         this.monitor = param2PacketStream.readTaggedObjectReference();
/* 5578 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5579 */           param2VirtualMachineImpl.printReceiveTrace(4, "monitor(ObjectReferenceImpl): " + ((this.monitor == null) ? "NULL" : ("ref=" + this.monitor.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */     static class Stop
/*      */     {
/*      */       static final int COMMAND = 10;
/*      */
/*      */
/*      */
/*      */
/*      */       static Stop process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ObjectReferenceImpl param2ObjectReferenceImpl) throws JDWPException {
/* 5595 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2ObjectReferenceImpl);
/* 5596 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ObjectReferenceImpl param2ObjectReferenceImpl) {
/* 5602 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 10);
/* 5603 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5604 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Stop" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5606 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5607 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5609 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5610 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5611 */           packetStream.vm.printTrace("Sending:                 throwable(ObjectReferenceImpl): " + ((param2ObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ObjectReferenceImpl.ref())));
/*      */         }
/* 5613 */         packetStream.writeObjectRef(param2ObjectReferenceImpl.ref());
/* 5614 */         packetStream.send();
/* 5615 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Stop waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5620 */         param2PacketStream.waitForReply();
/* 5621 */         return new Stop(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Stop(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5626 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5627 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Stop" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class Interrupt
/*      */     {
/*      */       static final int COMMAND = 11;
/*      */
/*      */
/*      */
/*      */       static Interrupt process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5641 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5642 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5647 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 11);
/* 5648 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5649 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.Interrupt" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5651 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5652 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5654 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5655 */         packetStream.send();
/* 5656 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Interrupt waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5661 */         param2PacketStream.waitForReply();
/* 5662 */         return new Interrupt(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Interrupt(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5667 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5668 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.Interrupt" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class SuspendCount
/*      */     {
/*      */       static final int COMMAND = 12;
/*      */
/*      */
/*      */       final int suspendCount;
/*      */
/*      */
/*      */       static SuspendCount process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5684 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5685 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5690 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 12);
/* 5691 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5692 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.SuspendCount" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5694 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5695 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5697 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5698 */         packetStream.send();
/* 5699 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SuspendCount waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5704 */         param2PacketStream.waitForReply();
/* 5705 */         return new SuspendCount(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private SuspendCount(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5715 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5716 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.SuspendCount" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5718 */         this.suspendCount = param2PacketStream.readInt();
/* 5719 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5720 */           param2VirtualMachineImpl.printReceiveTrace(4, "suspendCount(int): " + this.suspendCount);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class OwnedMonitorsStackDepthInfo
/*      */     {
/*      */       static final int COMMAND = 13;
/*      */
/*      */
/*      */
/*      */
/*      */       final monitor[] owned;
/*      */
/*      */
/*      */
/*      */
/*      */       static OwnedMonitorsStackDepthInfo process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) throws JDWPException {
/* 5742 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl);
/* 5743 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl) {
/* 5748 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 13);
/* 5749 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5750 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitorsStackDepthInfo" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5752 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5753 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5755 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5756 */         packetStream.send();
/* 5757 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static OwnedMonitorsStackDepthInfo waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5762 */         param2PacketStream.waitForReply();
/* 5763 */         return new OwnedMonitorsStackDepthInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static class monitor
/*      */       {
/*      */         final ObjectReferenceImpl monitor;
/*      */
/*      */
/*      */         final int stack_depth;
/*      */
/*      */
/*      */
/*      */         private monitor(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 5779 */           this.monitor = param3PacketStream.readTaggedObjectReference();
/* 5780 */           if (param3VirtualMachineImpl.traceReceives) {
/* 5781 */             param3VirtualMachineImpl.printReceiveTrace(5, "monitor(ObjectReferenceImpl): " + ((this.monitor == null) ? "NULL" : ("ref=" + this.monitor.ref())));
/*      */           }
/* 5783 */           this.stack_depth = param3PacketStream.readInt();
/* 5784 */           if (param3VirtualMachineImpl.traceReceives) {
/* 5785 */             param3VirtualMachineImpl.printReceiveTrace(5, "stack_depth(int): " + this.stack_depth);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private OwnedMonitorsStackDepthInfo(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5797 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5798 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.OwnedMonitorsStackDepthInfo" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5800 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5801 */           param2VirtualMachineImpl.printReceiveTrace(4, "owned(monitor[]): ");
/*      */         }
/* 5803 */         int i = param2PacketStream.readInt();
/* 5804 */         this.owned = new monitor[i];
/* 5805 */         for (byte b = 0; b < i; b++) {
/* 5806 */           if (param2VirtualMachineImpl.traceReceives) {
/* 5807 */             param2VirtualMachineImpl.printReceiveTrace(5, "owned[i](monitor): ");
/*      */           }
/* 5809 */           this.owned[b] = new monitor(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
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
/*      */
/*      */
/*      */     static class ForceEarlyReturn
/*      */     {
/*      */       static final int COMMAND = 14;
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
/*      */
/*      */       static ForceEarlyReturn process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ValueImpl param2ValueImpl) throws JDWPException {
/* 5864 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2ValueImpl);
/* 5865 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, ValueImpl param2ValueImpl) {
/* 5871 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 11, 14);
/* 5872 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5873 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadReference.ForceEarlyReturn" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5875 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5876 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 5878 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 5879 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5880 */           packetStream.vm.printTrace("Sending:                 value(ValueImpl): " + param2ValueImpl);
/*      */         }
/* 5882 */         packetStream.writeValue(param2ValueImpl);
/* 5883 */         packetStream.send();
/* 5884 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ForceEarlyReturn waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5889 */         param2PacketStream.waitForReply();
/* 5890 */         return new ForceEarlyReturn(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private ForceEarlyReturn(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5895 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5896 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadReference.ForceEarlyReturn" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ThreadGroupReference
/*      */   {
/*      */     static final int COMMAND_SET = 12;
/*      */
/*      */
/*      */     static class Name
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final String groupName;
/*      */
/*      */       static Name process(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) throws JDWPException {
/* 5915 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadGroupReferenceImpl);
/* 5916 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) {
/* 5921 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 12, 1);
/* 5922 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5923 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadGroupReference.Name" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5925 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5926 */           packetStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + ((param2ThreadGroupReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadGroupReferenceImpl.ref())));
/*      */         }
/* 5928 */         packetStream.writeObjectRef(param2ThreadGroupReferenceImpl.ref());
/* 5929 */         packetStream.send();
/* 5930 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Name waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5935 */         param2PacketStream.waitForReply();
/* 5936 */         return new Name(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Name(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5946 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5947 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadGroupReference.Name" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 5949 */         this.groupName = param2PacketStream.readString();
/* 5950 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5951 */           param2VirtualMachineImpl.printReceiveTrace(4, "groupName(String): " + this.groupName);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     static class Parent
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */       final ThreadGroupReferenceImpl parentGroup;
/*      */
/*      */
/*      */       static Parent process(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) throws JDWPException {
/* 5965 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadGroupReferenceImpl);
/* 5966 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) {
/* 5971 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 12, 2);
/* 5972 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 5973 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadGroupReference.Parent" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 5975 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 5976 */           packetStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + ((param2ThreadGroupReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadGroupReferenceImpl.ref())));
/*      */         }
/* 5978 */         packetStream.writeObjectRef(param2ThreadGroupReferenceImpl.ref());
/* 5979 */         packetStream.send();
/* 5980 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Parent waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 5985 */         param2PacketStream.waitForReply();
/* 5986 */         return new Parent(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Parent(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 5998 */         if (param2VirtualMachineImpl.traceReceives) {
/* 5999 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadGroupReference.Parent" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6001 */         this.parentGroup = param2PacketStream.readThreadGroupReference();
/* 6002 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6003 */           param2VirtualMachineImpl.printReceiveTrace(4, "parentGroup(ThreadGroupReferenceImpl): " + ((this.parentGroup == null) ? "NULL" : ("ref=" + this.parentGroup.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class Children
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */       final ThreadReferenceImpl[] childThreads;
/*      */
/*      */
/*      */       final ThreadGroupReferenceImpl[] childGroups;
/*      */
/*      */
/*      */       static Children process(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) throws JDWPException {
/* 6022 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadGroupReferenceImpl);
/* 6023 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadGroupReferenceImpl param2ThreadGroupReferenceImpl) {
/* 6028 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 12, 3);
/* 6029 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6030 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ThreadGroupReference.Children" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6032 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6033 */           packetStream.vm.printTrace("Sending:                 group(ThreadGroupReferenceImpl): " + ((param2ThreadGroupReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadGroupReferenceImpl.ref())));
/*      */         }
/* 6035 */         packetStream.writeObjectRef(param2ThreadGroupReferenceImpl.ref());
/* 6036 */         packetStream.send();
/* 6037 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Children waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6042 */         param2PacketStream.waitForReply();
/* 6043 */         return new Children(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private Children(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6058 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6059 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ThreadGroupReference.Children" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6061 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6062 */           param2VirtualMachineImpl.printReceiveTrace(4, "childThreads(ThreadReferenceImpl[]): ");
/*      */         }
/* 6064 */         int i = param2PacketStream.readInt();
/* 6065 */         this.childThreads = new ThreadReferenceImpl[i]; int j;
/* 6066 */         for (j = 0; j < i; j++) {
/* 6067 */           this.childThreads[j] = param2PacketStream.readThreadReference();
/* 6068 */           if (param2VirtualMachineImpl.traceReceives) {
/* 6069 */             param2VirtualMachineImpl.printReceiveTrace(5, "childThreads[i](ThreadReferenceImpl): " + ((this.childThreads[j] == null) ? "NULL" : ("ref=" + this.childThreads[j].ref())));
/*      */           }
/*      */         }
/* 6072 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6073 */           param2VirtualMachineImpl.printReceiveTrace(4, "childGroups(ThreadGroupReferenceImpl[]): ");
/*      */         }
/* 6075 */         j = param2PacketStream.readInt();
/* 6076 */         this.childGroups = new ThreadGroupReferenceImpl[j];
/* 6077 */         for (byte b = 0; b < j; b++) {
/* 6078 */           this.childGroups[b] = param2PacketStream.readThreadGroupReference();
/* 6079 */           if (param2VirtualMachineImpl.traceReceives) {
/* 6080 */             param2VirtualMachineImpl.printReceiveTrace(5, "childGroups[i](ThreadGroupReferenceImpl): " + ((this.childGroups[b] == null) ? "NULL" : ("ref=" + this.childGroups[b].ref())));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ArrayReference
/*      */   {
/*      */     static final int COMMAND_SET = 13;
/*      */
/*      */
/*      */     static class Length
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final int arrayLength;
/*      */
/*      */       static Length process(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl) throws JDWPException {
/* 6100 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayReferenceImpl);
/* 6101 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl) {
/* 6106 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 13, 1);
/* 6107 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6108 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ArrayReference.Length" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6110 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6111 */           packetStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + ((param2ArrayReferenceImpl == null) ? "NULL" : ("ref=" + param2ArrayReferenceImpl.ref())));
/*      */         }
/* 6113 */         packetStream.writeObjectRef(param2ArrayReferenceImpl.ref());
/* 6114 */         packetStream.send();
/* 6115 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Length waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6120 */         param2PacketStream.waitForReply();
/* 6121 */         return new Length(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Length(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6131 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6132 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ArrayReference.Length" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6134 */         this.arrayLength = param2PacketStream.readInt();
/* 6135 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6136 */           param2VirtualMachineImpl.printReceiveTrace(4, "arrayLength(int): " + this.arrayLength);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */       final List<?> values;
/*      */
/*      */
/*      */
/*      */       static GetValues process(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl, int param2Int1, int param2Int2) throws JDWPException {
/* 6153 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayReferenceImpl, param2Int1, param2Int2);
/* 6154 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl, int param2Int1, int param2Int2) {
/* 6161 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 13, 2);
/* 6162 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6163 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ArrayReference.GetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6165 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6166 */           packetStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + ((param2ArrayReferenceImpl == null) ? "NULL" : ("ref=" + param2ArrayReferenceImpl.ref())));
/*      */         }
/* 6168 */         packetStream.writeObjectRef(param2ArrayReferenceImpl.ref());
/* 6169 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6170 */           packetStream.vm.printTrace("Sending:                 firstIndex(int): " + param2Int1);
/*      */         }
/* 6172 */         packetStream.writeInt(param2Int1);
/* 6173 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6174 */           packetStream.vm.printTrace("Sending:                 length(int): " + param2Int2);
/*      */         }
/* 6176 */         packetStream.writeInt(param2Int2);
/* 6177 */         packetStream.send();
/* 6178 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static GetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6183 */         param2PacketStream.waitForReply();
/* 6184 */         return new GetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private GetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6196 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6197 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ArrayReference.GetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6199 */         this.values = param2PacketStream.readArrayRegion();
/* 6200 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6201 */           param2VirtualMachineImpl.printReceiveTrace(4, "values(List<?>): " + this.values);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static SetValues process(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl, int param2Int, ValueImpl[] param2ArrayOfValueImpl) throws JDWPException {
/* 6222 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ArrayReferenceImpl, param2Int, param2ArrayOfValueImpl);
/* 6223 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ArrayReferenceImpl param2ArrayReferenceImpl, int param2Int, ValueImpl[] param2ArrayOfValueImpl) {
/* 6230 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 13, 3);
/* 6231 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6232 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ArrayReference.SetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6234 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6235 */           packetStream.vm.printTrace("Sending:                 arrayObject(ArrayReferenceImpl): " + ((param2ArrayReferenceImpl == null) ? "NULL" : ("ref=" + param2ArrayReferenceImpl.ref())));
/*      */         }
/* 6237 */         packetStream.writeObjectRef(param2ArrayReferenceImpl.ref());
/* 6238 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6239 */           packetStream.vm.printTrace("Sending:                 firstIndex(int): " + param2Int);
/*      */         }
/* 6241 */         packetStream.writeInt(param2Int);
/* 6242 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6243 */           packetStream.vm.printTrace("Sending:                 values(ValueImpl[]): ");
/*      */         }
/* 6245 */         packetStream.writeInt(param2ArrayOfValueImpl.length);
/* 6246 */         for (byte b = 0; b < param2ArrayOfValueImpl.length; b++) {
/* 6247 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6248 */             packetStream.vm.printTrace("Sending:                     values[i](ValueImpl): " + param2ArrayOfValueImpl[b]);
/*      */           }
/* 6250 */           packetStream.writeUntaggedValue(param2ArrayOfValueImpl[b]);
/*      */         }
/* 6252 */         packetStream.send();
/* 6253 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6258 */         param2PacketStream.waitForReply();
/* 6259 */         return new SetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private SetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6264 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6265 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ArrayReference.SetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   static class ClassLoaderReference
/*      */   {
/*      */     static final int COMMAND_SET = 14;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class VisibleClasses
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */
/*      */
/*      */
/*      */       final ClassInfo[] classes;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static VisibleClasses process(VirtualMachineImpl param2VirtualMachineImpl, ClassLoaderReferenceImpl param2ClassLoaderReferenceImpl) throws JDWPException {
/* 6297 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassLoaderReferenceImpl);
/* 6298 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassLoaderReferenceImpl param2ClassLoaderReferenceImpl) {
/* 6303 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 14, 1);
/* 6304 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6305 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassLoaderReference.VisibleClasses" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6307 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6308 */           packetStream.vm.printTrace("Sending:                 classLoaderObject(ClassLoaderReferenceImpl): " + ((param2ClassLoaderReferenceImpl == null) ? "NULL" : ("ref=" + param2ClassLoaderReferenceImpl.ref())));
/*      */         }
/* 6310 */         packetStream.writeObjectRef(param2ClassLoaderReferenceImpl.ref());
/* 6311 */         packetStream.send();
/* 6312 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static VisibleClasses waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6317 */         param2PacketStream.waitForReply();
/* 6318 */         return new VisibleClasses(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static class ClassInfo
/*      */       {
/*      */         final byte refTypeTag;
/*      */
/*      */
/*      */
/*      */         final long typeID;
/*      */
/*      */
/*      */
/*      */         private ClassInfo(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 6335 */           this.refTypeTag = param3PacketStream.readByte();
/* 6336 */           if (param3VirtualMachineImpl.traceReceives) {
/* 6337 */             param3VirtualMachineImpl.printReceiveTrace(5, "refTypeTag(byte): " + this.refTypeTag);
/*      */           }
/* 6339 */           this.typeID = param3PacketStream.readClassRef();
/* 6340 */           if (param3VirtualMachineImpl.traceReceives) {
/* 6341 */             param3VirtualMachineImpl.printReceiveTrace(5, "typeID(long): ref=" + this.typeID);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private VisibleClasses(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6353 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6354 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassLoaderReference.VisibleClasses" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6356 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6357 */           param2VirtualMachineImpl.printReceiveTrace(4, "classes(ClassInfo[]): ");
/*      */         }
/* 6359 */         int i = param2PacketStream.readInt();
/* 6360 */         this.classes = new ClassInfo[i];
/* 6361 */         for (byte b = 0; b < i; b++) {
/* 6362 */           if (param2VirtualMachineImpl.traceReceives) {
/* 6363 */             param2VirtualMachineImpl.printReceiveTrace(5, "classes[i](ClassInfo): ");
/*      */           }
/* 6365 */           this.classes[b] = new ClassInfo(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class EventRequest
/*      */   {
/*      */     static final int COMMAND_SET = 15;
/*      */
/*      */
/*      */     static class Set
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */       final int requestID;
/*      */
/*      */
/*      */       static class Modifier
/*      */       {
/*      */         final byte modKind;
/*      */
/*      */         ModifierCommon aModifierCommon;
/*      */
/*      */
/*      */         static abstract class ModifierCommon
/*      */         {
/*      */           abstract void write(PacketStream param4PacketStream);
/*      */         }
/*      */
/*      */
/*      */         Modifier(byte param3Byte, ModifierCommon param3ModifierCommon) {
/* 6398 */           this.modKind = param3Byte;
/* 6399 */           this.aModifierCommon = param3ModifierCommon;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 6403 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6404 */             param3PacketStream.vm.printTrace("Sending:                     modKind(byte): " + this.modKind);
/*      */           }
/* 6406 */           param3PacketStream.writeByte(this.modKind);
/* 6407 */           this.aModifierCommon.write(param3PacketStream);
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */         static class Count
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 1;
/*      */
/*      */
/*      */
/*      */           final int count;
/*      */
/*      */
/*      */
/*      */
/*      */           static Modifier create(int param4Int) {
/* 6426 */             return new Modifier((byte)1, new Count(param4Int));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           Count(int param4Int) {
/* 6435 */             this.count = param4Int;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6439 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6440 */               param4PacketStream.vm.printTrace("Sending:                         count(int): " + this.count);
/*      */             }
/* 6442 */             param4PacketStream.writeInt(this.count);
/*      */           }
/*      */         }
/*      */
/*      */         static class Conditional
/*      */           extends ModifierCommon {
/*      */           static final byte ALT_ID = 2;
/*      */           final int exprID;
/*      */
/*      */           static Modifier create(int param4Int) {
/* 6452 */             return new Modifier((byte)2, new Conditional(param4Int));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           Conditional(int param4Int) {
/* 6461 */             this.exprID = param4Int;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6465 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6466 */               param4PacketStream.vm.printTrace("Sending:                         exprID(int): " + this.exprID);
/*      */             }
/* 6468 */             param4PacketStream.writeInt(this.exprID);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class ThreadOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 3;
/*      */
/*      */           final ThreadReferenceImpl thread;
/*      */
/*      */           static Modifier create(ThreadReferenceImpl param4ThreadReferenceImpl) {
/* 6481 */             return new Modifier((byte)3, new ThreadOnly(param4ThreadReferenceImpl));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           ThreadOnly(ThreadReferenceImpl param4ThreadReferenceImpl) {
/* 6490 */             this.thread = param4ThreadReferenceImpl;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6494 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6495 */               param4PacketStream.vm.printTrace("Sending:                         thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 6497 */             param4PacketStream.writeObjectRef(this.thread.ref());
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         static class ClassOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 4;
/*      */
/*      */
/*      */
/*      */
/*      */           final ReferenceTypeImpl clazz;
/*      */
/*      */
/*      */
/*      */
/*      */           static Modifier create(ReferenceTypeImpl param4ReferenceTypeImpl) {
/* 6519 */             return new Modifier((byte)4, new ClassOnly(param4ReferenceTypeImpl));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           ClassOnly(ReferenceTypeImpl param4ReferenceTypeImpl) {
/* 6528 */             this.clazz = param4ReferenceTypeImpl;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6532 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6533 */               param4PacketStream.vm.printTrace("Sending:                         clazz(ReferenceTypeImpl): " + ((this.clazz == null) ? "NULL" : ("ref=" + this.clazz.ref())));
/*      */             }
/* 6535 */             param4PacketStream.writeClassRef(this.clazz.ref());
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */         static class ClassMatch
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 5;
/*      */
/*      */
/*      */
/*      */           final String classPattern;
/*      */
/*      */
/*      */
/*      */           static Modifier create(String param4String) {
/* 6554 */             return new Modifier((byte)5, new ClassMatch(param4String));
/*      */           }
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
/*      */           ClassMatch(String param4String) {
/* 6567 */             this.classPattern = param4String;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6571 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6572 */               param4PacketStream.vm.printTrace("Sending:                         classPattern(String): " + this.classPattern);
/*      */             }
/* 6574 */             param4PacketStream.writeString(this.classPattern);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */         static class ClassExclude
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 6;
/*      */
/*      */
/*      */
/*      */           final String classPattern;
/*      */
/*      */
/*      */
/*      */           static Modifier create(String param4String) {
/* 6593 */             return new Modifier((byte)6, new ClassExclude(param4String));
/*      */           }
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
/*      */           ClassExclude(String param4String) {
/* 6606 */             this.classPattern = param4String;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6610 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6611 */               param4PacketStream.vm.printTrace("Sending:                         classPattern(String): " + this.classPattern);
/*      */             }
/* 6613 */             param4PacketStream.writeString(this.classPattern);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class LocationOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 7;
/*      */
/*      */           final Location loc;
/*      */
/*      */
/*      */           static Modifier create(Location param4Location) {
/* 6627 */             return new Modifier((byte)7, new LocationOnly(param4Location));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           LocationOnly(Location param4Location) {
/* 6636 */             this.loc = param4Location;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6640 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6641 */               param4PacketStream.vm.printTrace("Sending:                         loc(Location): " + this.loc);
/*      */             }
/* 6643 */             param4PacketStream.writeLocation(this.loc);
/*      */           }
/*      */         }
/*      */
/*      */         static class ExceptionOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 8;
/*      */           final ReferenceTypeImpl exceptionOrNull;
/*      */           final boolean caught;
/*      */           final boolean uncaught;
/*      */
/*      */           static Modifier create(ReferenceTypeImpl param4ReferenceTypeImpl, boolean param4Boolean1, boolean param4Boolean2) {
/* 6656 */             return new Modifier((byte)8, new ExceptionOnly(param4ReferenceTypeImpl, param4Boolean1, param4Boolean2));
/*      */           }
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
/*      */
/*      */
/*      */
/*      */
/*      */           ExceptionOnly(ReferenceTypeImpl param4ReferenceTypeImpl, boolean param4Boolean1, boolean param4Boolean2) {
/* 6685 */             this.exceptionOrNull = param4ReferenceTypeImpl;
/* 6686 */             this.caught = param4Boolean1;
/* 6687 */             this.uncaught = param4Boolean2;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6691 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6692 */               param4PacketStream.vm.printTrace("Sending:                         exceptionOrNull(ReferenceTypeImpl): " + ((this.exceptionOrNull == null) ? "NULL" : ("ref=" + this.exceptionOrNull.ref())));
/*      */             }
/* 6694 */             param4PacketStream.writeClassRef(this.exceptionOrNull.ref());
/* 6695 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6696 */               param4PacketStream.vm.printTrace("Sending:                         caught(boolean): " + this.caught);
/*      */             }
/* 6698 */             param4PacketStream.writeBoolean(this.caught);
/* 6699 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6700 */               param4PacketStream.vm.printTrace("Sending:                         uncaught(boolean): " + this.uncaught);
/*      */             }
/* 6702 */             param4PacketStream.writeBoolean(this.uncaught);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class FieldOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 9;
/*      */           final ReferenceTypeImpl declaring;
/*      */           final long fieldID;
/*      */
/*      */           static Modifier create(ReferenceTypeImpl param4ReferenceTypeImpl, long param4Long) {
/* 6715 */             return new Modifier((byte)9, new FieldOnly(param4ReferenceTypeImpl, param4Long));
/*      */           }
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
/*      */           FieldOnly(ReferenceTypeImpl param4ReferenceTypeImpl, long param4Long) {
/* 6729 */             this.declaring = param4ReferenceTypeImpl;
/* 6730 */             this.fieldID = param4Long;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6734 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6735 */               param4PacketStream.vm.printTrace("Sending:                         declaring(ReferenceTypeImpl): " + ((this.declaring == null) ? "NULL" : ("ref=" + this.declaring.ref())));
/*      */             }
/* 6737 */             param4PacketStream.writeClassRef(this.declaring.ref());
/* 6738 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6739 */               param4PacketStream.vm.printTrace("Sending:                         fieldID(long): " + this.fieldID);
/*      */             }
/* 6741 */             param4PacketStream.writeFieldRef(this.fieldID);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class Step
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 10;
/*      */           final ThreadReferenceImpl thread;
/*      */           final int size;
/*      */           final int depth;
/*      */
/*      */           static Modifier create(ThreadReferenceImpl param4ThreadReferenceImpl, int param4Int1, int param4Int2) {
/* 6755 */             return new Modifier((byte)10, new Step(param4ThreadReferenceImpl, param4Int1, param4Int2));
/*      */           }
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
/*      */           Step(ThreadReferenceImpl param4ThreadReferenceImpl, int param4Int1, int param4Int2) {
/* 6776 */             this.thread = param4ThreadReferenceImpl;
/* 6777 */             this.size = param4Int1;
/* 6778 */             this.depth = param4Int2;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6782 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6783 */               param4PacketStream.vm.printTrace("Sending:                         thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 6785 */             param4PacketStream.writeObjectRef(this.thread.ref());
/* 6786 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6787 */               param4PacketStream.vm.printTrace("Sending:                         size(int): " + this.size);
/*      */             }
/* 6789 */             param4PacketStream.writeInt(this.size);
/* 6790 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6791 */               param4PacketStream.vm.printTrace("Sending:                         depth(int): " + this.depth);
/*      */             }
/* 6793 */             param4PacketStream.writeInt(this.depth);
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */         static class InstanceOnly
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 11;
/*      */
/*      */           final ObjectReferenceImpl instance;
/*      */
/*      */
/*      */           static Modifier create(ObjectReferenceImpl param4ObjectReferenceImpl) {
/* 6808 */             return new Modifier((byte)11, new InstanceOnly(param4ObjectReferenceImpl));
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           InstanceOnly(ObjectReferenceImpl param4ObjectReferenceImpl) {
/* 6817 */             this.instance = param4ObjectReferenceImpl;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6821 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6822 */               param4PacketStream.vm.printTrace("Sending:                         instance(ObjectReferenceImpl): " + ((this.instance == null) ? "NULL" : ("ref=" + this.instance.ref())));
/*      */             }
/* 6824 */             param4PacketStream.writeObjectRef(this.instance.ref());
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */         static class SourceNameMatch
/*      */           extends ModifierCommon
/*      */         {
/*      */           static final byte ALT_ID = 12;
/*      */
/*      */
/*      */
/*      */           final String sourceNamePattern;
/*      */
/*      */
/*      */
/*      */
/*      */           static Modifier create(String param4String) {
/* 6844 */             return new Modifier((byte)12, new SourceNameMatch(param4String));
/*      */           }
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
/*      */           SourceNameMatch(String param4String) {
/* 6857 */             this.sourceNamePattern = param4String;
/*      */           }
/*      */
/*      */           void write(PacketStream param4PacketStream) {
/* 6861 */             if ((param4PacketStream.vm.traceFlags & 0x1) != 0) {
/* 6862 */               param4PacketStream.vm.printTrace("Sending:                         sourceNamePattern(String): " + this.sourceNamePattern);
/*      */             }
/* 6864 */             param4PacketStream.writeString(this.sourceNamePattern);
/*      */           }
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Set process(VirtualMachineImpl param2VirtualMachineImpl, byte param2Byte1, byte param2Byte2, Modifier[] param2ArrayOfModifier) throws JDWPException {
/* 6874 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2Byte1, param2Byte2, param2ArrayOfModifier);
/* 6875 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, byte param2Byte1, byte param2Byte2, Modifier[] param2ArrayOfModifier) {
/* 6882 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 15, 1);
/* 6883 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6884 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.EventRequest.Set" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6886 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6887 */           packetStream.vm.printTrace("Sending:                 eventKind(byte): " + param2Byte1);
/*      */         }
/* 6889 */         packetStream.writeByte(param2Byte1);
/* 6890 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6891 */           packetStream.vm.printTrace("Sending:                 suspendPolicy(byte): " + param2Byte2);
/*      */         }
/* 6893 */         packetStream.writeByte(param2Byte2);
/* 6894 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6895 */           packetStream.vm.printTrace("Sending:                 modifiers(Modifier[]): ");
/*      */         }
/* 6897 */         packetStream.writeInt(param2ArrayOfModifier.length);
/* 6898 */         for (byte b = 0; b < param2ArrayOfModifier.length; b++) {
/* 6899 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6900 */             packetStream.vm.printTrace("Sending:                     modifiers[i](Modifier): ");
/*      */           }
/* 6902 */           param2ArrayOfModifier[b].write(packetStream);
/*      */         }
/* 6904 */         packetStream.send();
/* 6905 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Set waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6910 */         param2PacketStream.waitForReply();
/* 6911 */         return new Set(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private Set(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6921 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6922 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.EventRequest.Set" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 6924 */         this.requestID = param2PacketStream.readInt();
/* 6925 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6926 */           param2VirtualMachineImpl.printReceiveTrace(4, "requestID(int): " + this.requestID);
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class Clear
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static Clear process(VirtualMachineImpl param2VirtualMachineImpl, byte param2Byte, int param2Int) throws JDWPException {
/* 6946 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2Byte, param2Int);
/* 6947 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, byte param2Byte, int param2Int) {
/* 6953 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 15, 2);
/* 6954 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6955 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.EventRequest.Clear" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 6957 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6958 */           packetStream.vm.printTrace("Sending:                 eventKind(byte): " + param2Byte);
/*      */         }
/* 6960 */         packetStream.writeByte(param2Byte);
/* 6961 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 6962 */           packetStream.vm.printTrace("Sending:                 requestID(int): " + param2Int);
/*      */         }
/* 6964 */         packetStream.writeInt(param2Int);
/* 6965 */         packetStream.send();
/* 6966 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static Clear waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 6971 */         param2PacketStream.waitForReply();
/* 6972 */         return new Clear(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private Clear(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 6977 */         if (param2VirtualMachineImpl.traceReceives) {
/* 6978 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.EventRequest.Clear" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class ClearAllBreakpoints
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */       static ClearAllBreakpoints process(VirtualMachineImpl param2VirtualMachineImpl) throws JDWPException {
/* 6991 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl);
/* 6992 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl) {
/* 6996 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 15, 3);
/* 6997 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 6998 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.EventRequest.ClearAllBreakpoints" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7000 */         packetStream.send();
/* 7001 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ClearAllBreakpoints waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7006 */         param2PacketStream.waitForReply();
/* 7007 */         return new ClearAllBreakpoints(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private ClearAllBreakpoints(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7012 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7013 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.EventRequest.ClearAllBreakpoints" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */   static class StackFrame
/*      */   {
/*      */     static final int COMMAND_SET = 16;
/*      */
/*      */
/*      */
/*      */
/*      */     static class GetValues
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */
/*      */
/*      */
/*      */       final ValueImpl[] values;
/*      */
/*      */
/*      */
/*      */       static class SlotInfo
/*      */       {
/*      */         final int slot;
/*      */
/*      */
/*      */         final byte sigbyte;
/*      */
/*      */
/*      */
/*      */         SlotInfo(int param3Int, byte param3Byte) {
/* 7049 */           this.slot = param3Int;
/* 7050 */           this.sigbyte = param3Byte;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 7054 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 7055 */             param3PacketStream.vm.printTrace("Sending:                     slot(int): " + this.slot);
/*      */           }
/* 7057 */           param3PacketStream.writeInt(this.slot);
/* 7058 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 7059 */             param3PacketStream.vm.printTrace("Sending:                     sigbyte(byte): " + this.sigbyte);
/*      */           }
/* 7061 */           param3PacketStream.writeByte(this.sigbyte);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static GetValues process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, SlotInfo[] param2ArrayOfSlotInfo) throws JDWPException {
/* 7070 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2Long, param2ArrayOfSlotInfo);
/* 7071 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, SlotInfo[] param2ArrayOfSlotInfo) {
/* 7078 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 16, 1);
/* 7079 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7080 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.StackFrame.GetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7082 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7083 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 7085 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 7086 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7087 */           packetStream.vm.printTrace("Sending:                 frame(long): " + param2Long);
/*      */         }
/* 7089 */         packetStream.writeFrameRef(param2Long);
/* 7090 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7091 */           packetStream.vm.printTrace("Sending:                 slots(SlotInfo[]): ");
/*      */         }
/* 7093 */         packetStream.writeInt(param2ArrayOfSlotInfo.length);
/* 7094 */         for (byte b = 0; b < param2ArrayOfSlotInfo.length; b++) {
/* 7095 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7096 */             packetStream.vm.printTrace("Sending:                     slots[i](SlotInfo): ");
/*      */           }
/* 7098 */           param2ArrayOfSlotInfo[b].write(packetStream);
/*      */         }
/* 7100 */         packetStream.send();
/* 7101 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static GetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7106 */         param2PacketStream.waitForReply();
/* 7107 */         return new GetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private GetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7118 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7119 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.StackFrame.GetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 7121 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7122 */           param2VirtualMachineImpl.printReceiveTrace(4, "values(ValueImpl[]): ");
/*      */         }
/* 7124 */         int i = param2PacketStream.readInt();
/* 7125 */         this.values = new ValueImpl[i];
/* 7126 */         for (byte b = 0; b < i; b++) {
/* 7127 */           this.values[b] = param2PacketStream.readValue();
/* 7128 */           if (param2VirtualMachineImpl.traceReceives) {
/* 7129 */             param2VirtualMachineImpl.printReceiveTrace(5, "values[i](ValueImpl): " + this.values[b]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class SetValues
/*      */     {
/*      */       static final int COMMAND = 2;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static class SlotInfo
/*      */       {
/*      */         final int slot;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         final ValueImpl slotValue;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         SlotInfo(int param3Int, ValueImpl param3ValueImpl) {
/* 7165 */           this.slot = param3Int;
/* 7166 */           this.slotValue = param3ValueImpl;
/*      */         }
/*      */
/*      */         private void write(PacketStream param3PacketStream) {
/* 7170 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 7171 */             param3PacketStream.vm.printTrace("Sending:                     slot(int): " + this.slot);
/*      */           }
/* 7173 */           param3PacketStream.writeInt(this.slot);
/* 7174 */           if ((param3PacketStream.vm.traceFlags & 0x1) != 0) {
/* 7175 */             param3PacketStream.vm.printTrace("Sending:                     slotValue(ValueImpl): " + this.slotValue);
/*      */           }
/* 7177 */           param3PacketStream.writeValue(this.slotValue);
/*      */         }
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static SetValues process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, SlotInfo[] param2ArrayOfSlotInfo) throws JDWPException {
/* 7186 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2Long, param2ArrayOfSlotInfo);
/* 7187 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long, SlotInfo[] param2ArrayOfSlotInfo) {
/* 7194 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 16, 2);
/* 7195 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7196 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.StackFrame.SetValues" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7198 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7199 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 7201 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 7202 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7203 */           packetStream.vm.printTrace("Sending:                 frame(long): " + param2Long);
/*      */         }
/* 7205 */         packetStream.writeFrameRef(param2Long);
/* 7206 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7207 */           packetStream.vm.printTrace("Sending:                 slotValues(SlotInfo[]): ");
/*      */         }
/* 7209 */         packetStream.writeInt(param2ArrayOfSlotInfo.length);
/* 7210 */         for (byte b = 0; b < param2ArrayOfSlotInfo.length; b++) {
/* 7211 */           if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7212 */             packetStream.vm.printTrace("Sending:                     slotValues[i](SlotInfo): ");
/*      */           }
/* 7214 */           param2ArrayOfSlotInfo[b].write(packetStream);
/*      */         }
/* 7216 */         packetStream.send();
/* 7217 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static SetValues waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7222 */         param2PacketStream.waitForReply();
/* 7223 */         return new SetValues(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private SetValues(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7228 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7229 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.StackFrame.SetValues" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */     static class ThisObject
/*      */     {
/*      */       static final int COMMAND = 3;
/*      */
/*      */
/*      */       final ObjectReferenceImpl objectThis;
/*      */
/*      */
/*      */
/*      */       static ThisObject process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long) throws JDWPException {
/* 7246 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2Long);
/* 7247 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long) {
/* 7253 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 16, 3);
/* 7254 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7255 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.StackFrame.ThisObject" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7257 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7258 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 7260 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 7261 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7262 */           packetStream.vm.printTrace("Sending:                 frame(long): " + param2Long);
/*      */         }
/* 7264 */         packetStream.writeFrameRef(param2Long);
/* 7265 */         packetStream.send();
/* 7266 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ThisObject waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7271 */         param2PacketStream.waitForReply();
/* 7272 */         return new ThisObject(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       private ThisObject(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7282 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7283 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.StackFrame.ThisObject" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 7285 */         this.objectThis = param2PacketStream.readTaggedObjectReference();
/* 7286 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7287 */           param2VirtualMachineImpl.printReceiveTrace(4, "objectThis(ObjectReferenceImpl): " + ((this.objectThis == null) ? "NULL" : ("ref=" + this.objectThis.ref())));
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     static class PopFrames
/*      */     {
/*      */       static final int COMMAND = 4;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */       static PopFrames process(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long) throws JDWPException {
/* 7311 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ThreadReferenceImpl, param2Long);
/* 7312 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ThreadReferenceImpl param2ThreadReferenceImpl, long param2Long) {
/* 7318 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 16, 4);
/* 7319 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7320 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.StackFrame.PopFrames" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7322 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7323 */           packetStream.vm.printTrace("Sending:                 thread(ThreadReferenceImpl): " + ((param2ThreadReferenceImpl == null) ? "NULL" : ("ref=" + param2ThreadReferenceImpl.ref())));
/*      */         }
/* 7325 */         packetStream.writeObjectRef(param2ThreadReferenceImpl.ref());
/* 7326 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7327 */           packetStream.vm.printTrace("Sending:                 frame(long): " + param2Long);
/*      */         }
/* 7329 */         packetStream.writeFrameRef(param2Long);
/* 7330 */         packetStream.send();
/* 7331 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static PopFrames waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7336 */         param2PacketStream.waitForReply();
/* 7337 */         return new PopFrames(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
/*      */
/*      */
/*      */       private PopFrames(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7342 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7343 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.StackFrame.PopFrames" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */
/*      */   static class ClassObjectReference
/*      */   {
/*      */     static final int COMMAND_SET = 17;
/*      */
/*      */
/*      */     static class ReflectedType
/*      */     {
/*      */       static final int COMMAND = 1;
/*      */       final byte refTypeTag;
/*      */       final long typeID;
/*      */
/*      */       static ReflectedType process(VirtualMachineImpl param2VirtualMachineImpl, ClassObjectReferenceImpl param2ClassObjectReferenceImpl) throws JDWPException {
/* 7362 */         PacketStream packetStream = enqueueCommand(param2VirtualMachineImpl, param2ClassObjectReferenceImpl);
/* 7363 */         return waitForReply(param2VirtualMachineImpl, packetStream);
/*      */       }
/*      */
/*      */
/*      */       static PacketStream enqueueCommand(VirtualMachineImpl param2VirtualMachineImpl, ClassObjectReferenceImpl param2ClassObjectReferenceImpl) {
/* 7368 */         PacketStream packetStream = new PacketStream(param2VirtualMachineImpl, 17, 1);
/* 7369 */         if ((param2VirtualMachineImpl.traceFlags & 0x1) != 0) {
/* 7370 */           param2VirtualMachineImpl.printTrace("Sending Command(id=" + packetStream.pkt.id + ") JDWP.ClassObjectReference.ReflectedType" + ((packetStream.pkt.flags != 0) ? (", FLAGS=" + packetStream.pkt.flags) : ""));
/*      */         }
/* 7372 */         if ((packetStream.vm.traceFlags & 0x1) != 0) {
/* 7373 */           packetStream.vm.printTrace("Sending:                 classObject(ClassObjectReferenceImpl): " + ((param2ClassObjectReferenceImpl == null) ? "NULL" : ("ref=" + param2ClassObjectReferenceImpl.ref())));
/*      */         }
/* 7375 */         packetStream.writeObjectRef(param2ClassObjectReferenceImpl.ref());
/* 7376 */         packetStream.send();
/* 7377 */         return packetStream;
/*      */       }
/*      */
/*      */
/*      */       static ReflectedType waitForReply(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) throws JDWPException {
/* 7382 */         param2PacketStream.waitForReply();
/* 7383 */         return new ReflectedType(param2VirtualMachineImpl, param2PacketStream);
/*      */       }
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
/*      */       private ReflectedType(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 7399 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7400 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.ClassObjectReference.ReflectedType" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 7402 */         this.refTypeTag = param2PacketStream.readByte();
/* 7403 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7404 */           param2VirtualMachineImpl.printReceiveTrace(4, "refTypeTag(byte): " + this.refTypeTag);
/*      */         }
/* 7406 */         this.typeID = param2PacketStream.readClassRef();
/* 7407 */         if (param2VirtualMachineImpl.traceReceives) {
/* 7408 */           param2VirtualMachineImpl.printReceiveTrace(4, "typeID(long): ref=" + this.typeID);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
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
/*      */   static class Event
/*      */   {
/*      */     static final int COMMAND_SET = 64;
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
/*      */     static class Composite
/*      */     {
/*      */       static final int COMMAND = 100;
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
/*      */       final byte suspendPolicy;
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
/*      */       final Events[] events;
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
/*      */       static class Events
/*      */       {
/*      */         final byte eventKind;
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
/*      */         EventsCommon aEventsCommon;
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
/*      */         static abstract class EventsCommon
/*      */         {
/*      */           abstract byte eventKind();
/*      */         }
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
/*      */         Events(VirtualMachineImpl param3VirtualMachineImpl, PacketStream param3PacketStream) {
/* 7520 */           this.eventKind = param3PacketStream.readByte();
/* 7521 */           if (param3VirtualMachineImpl.traceReceives) {
/* 7522 */             param3VirtualMachineImpl.printReceiveTrace(5, "eventKind(byte): " + this.eventKind);
/*      */           }
/* 7524 */           switch (this.eventKind) {
/*      */             case 90:
/* 7526 */               this.aEventsCommon = new VMStart(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 1:
/* 7529 */               this.aEventsCommon = new SingleStep(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 2:
/* 7532 */               this.aEventsCommon = new Breakpoint(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 40:
/* 7535 */               this.aEventsCommon = new MethodEntry(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 41:
/* 7538 */               this.aEventsCommon = new MethodExit(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 42:
/* 7541 */               this.aEventsCommon = new MethodExitWithReturnValue(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 43:
/* 7544 */               this.aEventsCommon = new MonitorContendedEnter(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 44:
/* 7547 */               this.aEventsCommon = new MonitorContendedEntered(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 45:
/* 7550 */               this.aEventsCommon = new MonitorWait(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 46:
/* 7553 */               this.aEventsCommon = new MonitorWaited(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 4:
/* 7556 */               this.aEventsCommon = new Exception(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 6:
/* 7559 */               this.aEventsCommon = new ThreadStart(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 7:
/* 7562 */               this.aEventsCommon = new ThreadDeath(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 8:
/* 7565 */               this.aEventsCommon = new ClassPrepare(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 9:
/* 7568 */               this.aEventsCommon = new ClassUnload(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 20:
/* 7571 */               this.aEventsCommon = new FieldAccess(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 21:
/* 7574 */               this.aEventsCommon = new FieldModification(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */             case 99:
/* 7577 */               this.aEventsCommon = new VMDeath(param3VirtualMachineImpl, param3PacketStream);
/*      */               break;
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class VMStart
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 90;
/*      */
/*      */           final int requestID;
/*      */
/*      */           final ThreadReferenceImpl thread;
/*      */
/*      */
/*      */           byte eventKind() {
/* 7594 */             return 90;
/*      */           }
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
/*      */           VMStart(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7609 */             this.requestID = param4PacketStream.readInt();
/* 7610 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7611 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7613 */             this.thread = param4PacketStream.readThreadReference();
/* 7614 */             if (param4VirtualMachineImpl.traceReceives)
/* 7615 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */           }
/*      */         }
/*      */
/*      */         static class SingleStep
/*      */           extends EventsCommon {
/*      */           static final byte ALT_ID = 1;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7627 */             return 1;
/*      */           }
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
/*      */           SingleStep(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7646 */             this.requestID = param4PacketStream.readInt();
/* 7647 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7648 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7650 */             this.thread = param4PacketStream.readThreadReference();
/* 7651 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7652 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7654 */             this.location = param4PacketStream.readLocation();
/* 7655 */             if (param4VirtualMachineImpl.traceReceives)
/* 7656 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */
/*      */         static class Breakpoint
/*      */           extends EventsCommon {
/*      */           static final byte ALT_ID = 2;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7668 */             return 2;
/*      */           }
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
/*      */           Breakpoint(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7687 */             this.requestID = param4PacketStream.readInt();
/* 7688 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7689 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7691 */             this.thread = param4PacketStream.readThreadReference();
/* 7692 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7693 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7695 */             this.location = param4PacketStream.readLocation();
/* 7696 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7697 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class MethodEntry
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 40;
/*      */
/*      */           final int requestID;
/*      */
/*      */           final ThreadReferenceImpl thread;
/*      */
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7715 */             return 40;
/*      */           }
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
/*      */           MethodEntry(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7734 */             this.requestID = param4PacketStream.readInt();
/* 7735 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7736 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7738 */             this.thread = param4PacketStream.readThreadReference();
/* 7739 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7740 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7742 */             this.location = param4PacketStream.readLocation();
/* 7743 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7744 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class MethodExit
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 41;
/*      */
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7760 */             return 41;
/*      */           }
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
/*      */           MethodExit(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7779 */             this.requestID = param4PacketStream.readInt();
/* 7780 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7781 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7783 */             this.thread = param4PacketStream.readThreadReference();
/* 7784 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7785 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7787 */             this.location = param4PacketStream.readLocation();
/* 7788 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7789 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class MethodExitWithReturnValue
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 42;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final ValueImpl value;
/*      */
/*      */           byte eventKind() {
/* 7805 */             return 42;
/*      */           }
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
/*      */           MethodExitWithReturnValue(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7829 */             this.requestID = param4PacketStream.readInt();
/* 7830 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7831 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7833 */             this.thread = param4PacketStream.readThreadReference();
/* 7834 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7835 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7837 */             this.location = param4PacketStream.readLocation();
/* 7838 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7839 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 7841 */             this.value = param4PacketStream.readValue();
/* 7842 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7843 */               param4VirtualMachineImpl.printReceiveTrace(6, "value(ValueImpl): " + this.value);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */         static class MonitorContendedEnter
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 43;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7858 */             return 43;
/*      */           }
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
/*      */           MonitorContendedEnter(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7882 */             this.requestID = param4PacketStream.readInt();
/* 7883 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7884 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7886 */             this.thread = param4PacketStream.readThreadReference();
/* 7887 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7888 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7890 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 7891 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7892 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */             }
/* 7894 */             this.location = param4PacketStream.readLocation();
/* 7895 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7896 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */         static class MonitorContendedEntered
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 44;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */
/*      */           byte eventKind() {
/* 7911 */             return 44;
/*      */           }
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
/*      */           MonitorContendedEntered(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7935 */             this.requestID = param4PacketStream.readInt();
/* 7936 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7937 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7939 */             this.thread = param4PacketStream.readThreadReference();
/* 7940 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7941 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 7943 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 7944 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7945 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */             }
/* 7947 */             this.location = param4PacketStream.readLocation();
/* 7948 */             if (param4VirtualMachineImpl.traceReceives)
/* 7949 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */           }
/*      */         }
/*      */
/*      */         static class MonitorWait
/*      */           extends EventsCommon {
/*      */           static final byte ALT_ID = 45;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */           final long timeout;
/*      */
/*      */           byte eventKind() {
/* 7963 */             return 45;
/*      */           }
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
/*      */
/*      */
/*      */
/*      */
/*      */           MonitorWait(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 7992 */             this.requestID = param4PacketStream.readInt();
/* 7993 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7994 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 7996 */             this.thread = param4PacketStream.readThreadReference();
/* 7997 */             if (param4VirtualMachineImpl.traceReceives) {
/* 7998 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8000 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 8001 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8002 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */             }
/* 8004 */             this.location = param4PacketStream.readLocation();
/* 8005 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8006 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8008 */             this.timeout = param4PacketStream.readLong();
/* 8009 */             if (param4VirtualMachineImpl.traceReceives)
/* 8010 */               param4VirtualMachineImpl.printReceiveTrace(6, "timeout(long): " + this.timeout);
/*      */           }
/*      */         }
/*      */
/*      */         static class MonitorWaited
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 46;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final ObjectReferenceImpl object;
/*      */           final Location location;
/*      */           final boolean timed_out;
/*      */
/*      */           byte eventKind() {
/* 8025 */             return 46;
/*      */           }
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
/*      */
/*      */
/*      */
/*      */
/*      */           MonitorWaited(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8054 */             this.requestID = param4PacketStream.readInt();
/* 8055 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8056 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8058 */             this.thread = param4PacketStream.readThreadReference();
/* 8059 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8060 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8062 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 8063 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8064 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */             }
/* 8066 */             this.location = param4PacketStream.readLocation();
/* 8067 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8068 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8070 */             this.timed_out = param4PacketStream.readBoolean();
/* 8071 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8072 */               param4VirtualMachineImpl.printReceiveTrace(6, "timed_out(boolean): " + this.timed_out);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class Exception
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 4;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final ObjectReferenceImpl exception;
/*      */           final Location catchLocation;
/*      */
/*      */           byte eventKind() {
/* 8089 */             return 4;
/*      */           }
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
/*      */           Exception(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8147 */             this.requestID = param4PacketStream.readInt();
/* 8148 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8149 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8151 */             this.thread = param4PacketStream.readThreadReference();
/* 8152 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8153 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8155 */             this.location = param4PacketStream.readLocation();
/* 8156 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8157 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8159 */             this.exception = param4PacketStream.readTaggedObjectReference();
/* 8160 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8161 */               param4VirtualMachineImpl.printReceiveTrace(6, "exception(ObjectReferenceImpl): " + ((this.exception == null) ? "NULL" : ("ref=" + this.exception.ref())));
/*      */             }
/* 8163 */             this.catchLocation = param4PacketStream.readLocation();
/* 8164 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8165 */               param4VirtualMachineImpl.printReceiveTrace(6, "catchLocation(Location): " + this.catchLocation);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */         static class ThreadStart
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 6;
/*      */
/*      */
/*      */
/*      */
/*      */           final int requestID;
/*      */
/*      */
/*      */
/*      */
/*      */           final ThreadReferenceImpl thread;
/*      */
/*      */
/*      */
/*      */
/*      */           byte eventKind() {
/* 8193 */             return 6;
/*      */           }
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
/*      */           ThreadStart(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8207 */             this.requestID = param4PacketStream.readInt();
/* 8208 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8209 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8211 */             this.thread = param4PacketStream.readThreadReference();
/* 8212 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8213 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */
/*      */         static class ThreadDeath
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 7;
/*      */
/*      */
/*      */           final int requestID;
/*      */
/*      */           final ThreadReferenceImpl thread;
/*      */
/*      */
/*      */           byte eventKind() {
/* 8232 */             return 7;
/*      */           }
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
/*      */           ThreadDeath(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8246 */             this.requestID = param4PacketStream.readInt();
/* 8247 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8248 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8250 */             this.thread = param4PacketStream.readThreadReference();
/* 8251 */             if (param4VirtualMachineImpl.traceReceives)
/* 8252 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */           }
/*      */         }
/*      */
/*      */         static class ClassPrepare extends EventsCommon {
/*      */           static final byte ALT_ID = 8;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final String signature;
/*      */           final int status;
/*      */
/*      */           byte eventKind() {
/* 8266 */             return 8;
/*      */           }
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
/*      */
/*      */
/*      */
/*      */           ClassPrepare(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8316 */             this.requestID = param4PacketStream.readInt();
/* 8317 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8318 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8320 */             this.thread = param4PacketStream.readThreadReference();
/* 8321 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8322 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8324 */             this.refTypeTag = param4PacketStream.readByte();
/* 8325 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8326 */               param4VirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8328 */             this.typeID = param4PacketStream.readClassRef();
/* 8329 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8330 */               param4VirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8332 */             this.signature = param4PacketStream.readString();
/* 8333 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8334 */               param4VirtualMachineImpl.printReceiveTrace(6, "signature(String): " + this.signature);
/*      */             }
/* 8336 */             this.status = param4PacketStream.readInt();
/* 8337 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8338 */               param4VirtualMachineImpl.printReceiveTrace(6, "status(int): " + this.status);
/*      */             }
/*      */           }
/*      */         }
/*      */
/*      */
/*      */         static class ClassUnload
/*      */           extends EventsCommon
/*      */         {
/*      */           static final byte ALT_ID = 9;
/*      */           final int requestID;
/*      */           final String signature;
/*      */
/*      */           byte eventKind() {
/* 8352 */             return 9;
/*      */           }
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
/*      */           ClassUnload(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8366 */             this.requestID = param4PacketStream.readInt();
/* 8367 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8368 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8370 */             this.signature = param4PacketStream.readString();
/* 8371 */             if (param4VirtualMachineImpl.traceReceives)
/* 8372 */               param4VirtualMachineImpl.printReceiveTrace(6, "signature(String): " + this.signature);
/*      */           }
/*      */         }
/*      */
/*      */         static class FieldAccess extends EventsCommon {
/*      */           static final byte ALT_ID = 20;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final long fieldID;
/*      */           final ObjectReferenceImpl object;
/*      */
/*      */           byte eventKind() {
/* 8387 */             return 20;
/*      */           }
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
/*      */           FieldAccess(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8427 */             this.requestID = param4PacketStream.readInt();
/* 8428 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8429 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8431 */             this.thread = param4PacketStream.readThreadReference();
/* 8432 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8433 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8435 */             this.location = param4PacketStream.readLocation();
/* 8436 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8437 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8439 */             this.refTypeTag = param4PacketStream.readByte();
/* 8440 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8441 */               param4VirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8443 */             this.typeID = param4PacketStream.readClassRef();
/* 8444 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8445 */               param4VirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8447 */             this.fieldID = param4PacketStream.readFieldRef();
/* 8448 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8449 */               param4VirtualMachineImpl.printReceiveTrace(6, "fieldID(long): " + this.fieldID);
/*      */             }
/* 8451 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 8452 */             if (param4VirtualMachineImpl.traceReceives)
/* 8453 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */           } }
/*      */         static class FieldModification extends EventsCommon { static final byte ALT_ID = 21;
/*      */           final int requestID;
/*      */           final ThreadReferenceImpl thread;
/*      */           final Location location;
/*      */           final byte refTypeTag;
/*      */           final long typeID;
/*      */           final long fieldID;
/*      */           final ObjectReferenceImpl object;
/*      */           final ValueImpl valueToBe;
/*      */
/*      */           byte eventKind() {
/* 8466 */             return 21;
/*      */           }
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
/*      */           FieldModification(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8511 */             this.requestID = param4PacketStream.readInt();
/* 8512 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8513 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/* 8515 */             this.thread = param4PacketStream.readThreadReference();
/* 8516 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8517 */               param4VirtualMachineImpl.printReceiveTrace(6, "thread(ThreadReferenceImpl): " + ((this.thread == null) ? "NULL" : ("ref=" + this.thread.ref())));
/*      */             }
/* 8519 */             this.location = param4PacketStream.readLocation();
/* 8520 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8521 */               param4VirtualMachineImpl.printReceiveTrace(6, "location(Location): " + this.location);
/*      */             }
/* 8523 */             this.refTypeTag = param4PacketStream.readByte();
/* 8524 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8525 */               param4VirtualMachineImpl.printReceiveTrace(6, "refTypeTag(byte): " + this.refTypeTag);
/*      */             }
/* 8527 */             this.typeID = param4PacketStream.readClassRef();
/* 8528 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8529 */               param4VirtualMachineImpl.printReceiveTrace(6, "typeID(long): ref=" + this.typeID);
/*      */             }
/* 8531 */             this.fieldID = param4PacketStream.readFieldRef();
/* 8532 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8533 */               param4VirtualMachineImpl.printReceiveTrace(6, "fieldID(long): " + this.fieldID);
/*      */             }
/* 8535 */             this.object = param4PacketStream.readTaggedObjectReference();
/* 8536 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8537 */               param4VirtualMachineImpl.printReceiveTrace(6, "object(ObjectReferenceImpl): " + ((this.object == null) ? "NULL" : ("ref=" + this.object.ref())));
/*      */             }
/* 8539 */             this.valueToBe = param4PacketStream.readValue();
/* 8540 */             if (param4VirtualMachineImpl.traceReceives)
/* 8541 */               param4VirtualMachineImpl.printReceiveTrace(6, "valueToBe(ValueImpl): " + this.valueToBe);
/*      */           } }
/*      */
/*      */         static class VMDeath extends EventsCommon {
/*      */           static final byte ALT_ID = 99;
/*      */           final int requestID;
/*      */
/*      */           byte eventKind() {
/* 8549 */             return 99;
/*      */           }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */           VMDeath(VirtualMachineImpl param4VirtualMachineImpl, PacketStream param4PacketStream) {
/* 8558 */             this.requestID = param4PacketStream.readInt();
/* 8559 */             if (param4VirtualMachineImpl.traceReceives) {
/* 8560 */               param4VirtualMachineImpl.printReceiveTrace(6, "requestID(int): " + this.requestID);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
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
/*      */       Composite(VirtualMachineImpl param2VirtualMachineImpl, PacketStream param2PacketStream) {
/* 8578 */         if (param2VirtualMachineImpl.traceReceives) {
/* 8579 */           param2VirtualMachineImpl.printTrace("Receiving Command(id=" + param2PacketStream.pkt.id + ") JDWP.Event.Composite" + ((param2PacketStream.pkt.flags != 0) ? (", FLAGS=" + param2PacketStream.pkt.flags) : "") + ((param2PacketStream.pkt.errorCode != 0) ? (", ERROR CODE=" + param2PacketStream.pkt.errorCode) : ""));
/*      */         }
/* 8581 */         this.suspendPolicy = param2PacketStream.readByte();
/* 8582 */         if (param2VirtualMachineImpl.traceReceives) {
/* 8583 */           param2VirtualMachineImpl.printReceiveTrace(4, "suspendPolicy(byte): " + this.suspendPolicy);
/*      */         }
/* 8585 */         if (param2VirtualMachineImpl.traceReceives) {
/* 8586 */           param2VirtualMachineImpl.printReceiveTrace(4, "events(Events[]): ");
/*      */         }
/* 8588 */         int i = param2PacketStream.readInt();
/* 8589 */         this.events = new Events[i];
/* 8590 */         for (byte b = 0; b < i; b++) {
/* 8591 */           if (param2VirtualMachineImpl.traceReceives) {
/* 8592 */             param2VirtualMachineImpl.printReceiveTrace(5, "events[i](Events): ");
/*      */           }
/* 8594 */           this.events[b] = new Events(param2VirtualMachineImpl, param2PacketStream);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */
/*      */   static class Error {
/*      */     static final int NONE = 0;
/*      */     static final int INVALID_THREAD = 10;
/*      */     static final int INVALID_THREAD_GROUP = 11;
/*      */     static final int INVALID_PRIORITY = 12;
/*      */     static final int THREAD_NOT_SUSPENDED = 13;
/*      */     static final int THREAD_SUSPENDED = 14;
/*      */     static final int THREAD_NOT_ALIVE = 15;
/*      */     static final int INVALID_OBJECT = 20;
/*      */     static final int INVALID_CLASS = 21;
/*      */     static final int CLASS_NOT_PREPARED = 22;
/*      */     static final int INVALID_METHODID = 23;
/*      */     static final int INVALID_LOCATION = 24;
/*      */     static final int INVALID_FIELDID = 25;
/*      */     static final int INVALID_FRAMEID = 30;
/*      */     static final int NO_MORE_FRAMES = 31;
/*      */     static final int OPAQUE_FRAME = 32;
/*      */     static final int NOT_CURRENT_FRAME = 33;
/*      */     static final int TYPE_MISMATCH = 34;
/*      */     static final int INVALID_SLOT = 35;
/*      */     static final int DUPLICATE = 40;
/*      */     static final int NOT_FOUND = 41;
/*      */     static final int INVALID_MONITOR = 50;
/*      */     static final int NOT_MONITOR_OWNER = 51;
/*      */     static final int INTERRUPT = 52;
/*      */     static final int INVALID_CLASS_FORMAT = 60;
/*      */     static final int CIRCULAR_CLASS_DEFINITION = 61;
/*      */     static final int FAILS_VERIFICATION = 62;
/*      */     static final int ADD_METHOD_NOT_IMPLEMENTED = 63;
/*      */     static final int SCHEMA_CHANGE_NOT_IMPLEMENTED = 64;
/*      */     static final int INVALID_TYPESTATE = 65;
/*      */     static final int HIERARCHY_CHANGE_NOT_IMPLEMENTED = 66;
/*      */     static final int DELETE_METHOD_NOT_IMPLEMENTED = 67;
/*      */     static final int UNSUPPORTED_VERSION = 68;
/*      */     static final int NAMES_DONT_MATCH = 69;
/*      */     static final int CLASS_MODIFIERS_CHANGE_NOT_IMPLEMENTED = 70;
/*      */     static final int METHOD_MODIFIERS_CHANGE_NOT_IMPLEMENTED = 71;
/*      */     static final int NOT_IMPLEMENTED = 99;
/*      */     static final int NULL_POINTER = 100;
/*      */     static final int ABSENT_INFORMATION = 101;
/*      */     static final int INVALID_EVENT_TYPE = 102;
/*      */     static final int ILLEGAL_ARGUMENT = 103;
/*      */     static final int OUT_OF_MEMORY = 110;
/*      */     static final int ACCESS_DENIED = 111;
/*      */     static final int VM_DEAD = 112;
/*      */     static final int INTERNAL = 113;
/*      */     static final int UNATTACHED_THREAD = 115;
/*      */     static final int INVALID_TAG = 500;
/*      */     static final int ALREADY_INVOKING = 502;
/*      */     static final int INVALID_INDEX = 503;
/*      */     static final int INVALID_LENGTH = 504;
/*      */     static final int INVALID_STRING = 506;
/*      */     static final int INVALID_CLASS_LOADER = 507;
/*      */     static final int INVALID_ARRAY = 508;
/*      */     static final int TRANSPORT_LOAD = 509;
/*      */     static final int TRANSPORT_INIT = 510;
/*      */     static final int NATIVE_METHOD = 511;
/*      */     static final int INVALID_COUNT = 512;
/*      */   }
/*      */
/*      */   static class EventKind {
/*      */     static final int SINGLE_STEP = 1;
/*      */     static final int BREAKPOINT = 2;
/*      */     static final int FRAME_POP = 3;
/*      */     static final int EXCEPTION = 4;
/*      */     static final int USER_DEFINED = 5;
/*      */     static final int THREAD_START = 6;
/*      */     static final int THREAD_DEATH = 7;
/*      */     static final int THREAD_END = 7;
/*      */     static final int CLASS_PREPARE = 8;
/*      */     static final int CLASS_UNLOAD = 9;
/*      */     static final int CLASS_LOAD = 10;
/*      */     static final int FIELD_ACCESS = 20;
/*      */     static final int FIELD_MODIFICATION = 21;
/*      */     static final int EXCEPTION_CATCH = 30;
/*      */     static final int METHOD_ENTRY = 40;
/*      */     static final int METHOD_EXIT = 41;
/*      */     static final int METHOD_EXIT_WITH_RETURN_VALUE = 42;
/*      */     static final int MONITOR_CONTENDED_ENTER = 43;
/*      */     static final int MONITOR_CONTENDED_ENTERED = 44;
/*      */     static final int MONITOR_WAIT = 45;
/*      */     static final int MONITOR_WAITED = 46;
/*      */     static final int VM_START = 90;
/*      */     static final int VM_INIT = 90;
/*      */     static final int VM_DEATH = 99;
/*      */     static final int VM_DISCONNECTED = 100;
/*      */   }
/*      */
/*      */   static class ThreadStatus {
/*      */     static final int ZOMBIE = 0;
/*      */     static final int RUNNING = 1;
/*      */     static final int SLEEPING = 2;
/*      */     static final int MONITOR = 3;
/*      */     static final int WAIT = 4;
/*      */   }
/*      */
/*      */   static class SuspendStatus {
/*      */     static final int SUSPEND_STATUS_SUSPENDED = 1;
/*      */   }
/*      */
/*      */   static class ClassStatus {
/*      */     static final int VERIFIED = 1;
/*      */     static final int PREPARED = 2;
/*      */     static final int INITIALIZED = 4;
/*      */     static final int ERROR = 8;
/*      */   }
/*      */
/*      */   static class TypeTag {
/*      */     static final int CLASS = 1;
/*      */     static final int INTERFACE = 2;
/*      */     static final int ARRAY = 3;
/*      */   }
/*      */
/*      */   static class Tag {
/*      */     static final int ARRAY = 91;
/*      */     static final int BYTE = 66;
/*      */     static final int CHAR = 67;
/*      */     static final int OBJECT = 76;
/*      */     static final int FLOAT = 70;
/*      */     static final int DOUBLE = 68;
/*      */     static final int INT = 73;
/*      */     static final int LONG = 74;
/*      */     static final int SHORT = 83;
/*      */     static final int VOID = 86;
/*      */     static final int BOOLEAN = 90;
/*      */     static final int STRING = 115;
/*      */     static final int THREAD = 116;
/*      */     static final int THREAD_GROUP = 103;
/*      */     static final int CLASS_LOADER = 108;
/*      */     static final int CLASS_OBJECT = 99;
/*      */   }
/*      */
/*      */   static class StepDepth {
/*      */     static final int INTO = 0;
/*      */     static final int OVER = 1;
/*      */     static final int OUT = 2;
/*      */   }
/*      */
/*      */   static class StepSize {
/*      */     static final int MIN = 0;
/*      */     static final int LINE = 1;
/*      */   }
/*      */
/*      */   static class SuspendPolicy {
/*      */     static final int NONE = 0;
/*      */     static final int EVENT_THREAD = 1;
/*      */     static final int ALL = 2;
/*      */   }
/*      */
/*      */   static class InvokeOptions {
/*      */     static final int INVOKE_SINGLE_THREADED = 1;
/*      */     static final int INVOKE_NONVIRTUAL = 2;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\JDWP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
