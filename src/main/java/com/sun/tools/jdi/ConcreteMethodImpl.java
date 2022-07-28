/*     */ package com.sun.tools.jdi;
/*     */ 
/*     */ import com.sun.jdi.AbsentInformationException;
/*     */ import com.sun.jdi.LocalVariable;
/*     */ import com.sun.jdi.Location;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcreteMethodImpl
/*     */   extends MethodImpl
/*     */ {
/*     */   private static class SoftLocationXRefs
/*     */   {
/*     */     final String stratumID;
/*     */     final Map<Integer, List<Location>> lineMapper;
/*     */     final List<Location> lineLocations;
/*     */     final int lowestLine;
/*     */     final int highestLine;
/*     */     
/*     */     SoftLocationXRefs(String param1String, Map<Integer, List<Location>> param1Map, List<Location> param1List, int param1Int1, int param1Int2) {
/*  65 */       this.stratumID = param1String;
/*  66 */       this.lineMapper = Collections.unmodifiableMap(param1Map);
/*  67 */       this
/*  68 */         .lineLocations = Collections.unmodifiableList(param1List);
/*  69 */       this.lowestLine = param1Int1;
/*  70 */       this.highestLine = param1Int2;
/*     */     }
/*     */   }
/*     */   
/*  74 */   private Location location = null;
/*     */   private SoftReference<SoftLocationXRefs> softBaseLocationXRefsRef;
/*     */   private SoftReference<SoftLocationXRefs> softOtherLocationXRefsRef;
/*  77 */   private SoftReference<List<LocalVariable>> variablesRef = null;
/*     */   private boolean absentVariableInformation = false;
/*  79 */   private long firstIndex = -1L;
/*  80 */   private long lastIndex = -1L;
/*  81 */   private SoftReference<byte[]> bytecodesRef = null;
/*  82 */   private int argSlotCount = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ConcreteMethodImpl(VirtualMachine paramVirtualMachine, ReferenceTypeImpl paramReferenceTypeImpl, long paramLong, String paramString1, String paramString2, String paramString3, int paramInt) {
/*  90 */     super(paramVirtualMachine, paramReferenceTypeImpl, paramLong, paramString1, paramString2, paramString3, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Location location() {
/*  95 */     if (this.location == null) {
/*  96 */       getBaseLocations();
/*     */     }
/*  98 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Location> sourceNameFilter(List<Location> paramList, SDE.Stratum paramStratum, String paramString) throws AbsentInformationException {
/* 105 */     if (paramString == null) {
/* 106 */       return paramList;
/*     */     }
/*     */     
/* 109 */     ArrayList<Location> arrayList = new ArrayList();
/* 110 */     for (Location location : paramList) {
/* 111 */       if (((LocationImpl)location).sourceName(paramStratum).equals(paramString)) {
/* 112 */         arrayList.add(location);
/*     */       }
/*     */     } 
/* 115 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Location> allLineLocations(SDE.Stratum paramStratum, String paramString) throws AbsentInformationException {
/* 122 */     List<Location> list = (getLocations(paramStratum)).lineLocations;
/*     */     
/* 124 */     if (list.size() == 0) {
/* 125 */       throw new AbsentInformationException();
/*     */     }
/*     */     
/* 128 */     return Collections.unmodifiableList(
/* 129 */         sourceNameFilter(list, paramStratum, paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Location> locationsOfLine(SDE.Stratum paramStratum, String paramString, int paramInt) throws AbsentInformationException {
/* 136 */     SoftLocationXRefs softLocationXRefs = getLocations(paramStratum);
/*     */     
/* 138 */     if (softLocationXRefs.lineLocations.size() == 0) {
/* 139 */       throw new AbsentInformationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     List<Location> list = softLocationXRefs.lineMapper.get(new Integer(paramInt));
/*     */     
/* 148 */     if (list == null) {
/* 149 */       list = new ArrayList(0);
/*     */     }
/* 151 */     return Collections.unmodifiableList(
/* 152 */         sourceNameFilter(list, paramStratum, paramString));
/*     */   }
/*     */ 
/*     */   
/*     */   public Location locationOfCodeIndex(long paramLong) {
/* 157 */     if (this.firstIndex == -1L) {
/* 158 */       getBaseLocations();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     if (paramLong < this.firstIndex || paramLong > this.lastIndex) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     return new LocationImpl(virtualMachine(), this, paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LineInfo codeIndexToLineInfo(SDE.Stratum paramStratum, long paramLong) {
/* 174 */     if (this.firstIndex == -1L) {
/* 175 */       getBaseLocations();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (paramLong < this.firstIndex || paramLong > this.lastIndex) {
/* 182 */       throw new InternalError("Location with invalid code index");
/*     */     }
/*     */ 
/*     */     
/* 186 */     List<Location> list = (getLocations(paramStratum)).lineLocations;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (list.size() == 0) {
/* 192 */       return super.codeIndexToLineInfo(paramStratum, paramLong);
/*     */     }
/*     */     
/* 195 */     Iterator<Location> iterator = list.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     LocationImpl locationImpl = (LocationImpl)iterator.next();
/* 205 */     while (iterator.hasNext()) {
/* 206 */       LocationImpl locationImpl1 = (LocationImpl)iterator.next();
/* 207 */       if (locationImpl1.codeIndex() > paramLong) {
/*     */         break;
/*     */       }
/* 210 */       locationImpl = locationImpl1;
/*     */     } 
/* 212 */     return locationImpl.getLineInfo(paramStratum);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LocalVariable> variables() throws AbsentInformationException {
/* 217 */     return getVariables();
/*     */   }
/*     */   
/*     */   public List<LocalVariable> variablesByName(String paramString) throws AbsentInformationException {
/* 221 */     List<LocalVariable> list = getVariables();
/*     */     
/* 223 */     ArrayList<LocalVariable> arrayList = new ArrayList(2);
/* 224 */     Iterator<LocalVariable> iterator = list.iterator();
/* 225 */     while (iterator.hasNext()) {
/* 226 */       LocalVariable localVariable = iterator.next();
/* 227 */       if (localVariable.name().equals(paramString)) {
/* 228 */         arrayList.add(localVariable);
/*     */       }
/*     */     } 
/* 231 */     return arrayList;
/*     */   }
/*     */   
/*     */   public List<LocalVariable> arguments() throws AbsentInformationException {
/* 235 */     List<LocalVariable> list = getVariables();
/*     */     
/* 237 */     ArrayList<LocalVariable> arrayList = new ArrayList(list.size());
/* 238 */     Iterator<LocalVariable> iterator = list.iterator();
/* 239 */     while (iterator.hasNext()) {
/* 240 */       LocalVariable localVariable = iterator.next();
/* 241 */       if (localVariable.isArgument()) {
/* 242 */         arrayList.add(localVariable);
/*     */       }
/*     */     } 
/* 245 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] bytecodes() {
/* 250 */     byte[] arrayOfByte = (this.bytecodesRef == null) ? null : this.bytecodesRef.get();
/* 251 */     if (arrayOfByte == null) {
/*     */       
/*     */       try {
/* 254 */         arrayOfByte = (JDWP.Method.Bytecodes.process(this.vm, this.declaringType, this.ref)).bytes;
/* 255 */       } catch (JDWPException jDWPException) {
/* 256 */         throw jDWPException.toJDIException();
/*     */       } 
/* 258 */       this.bytecodesRef = (SoftReference)new SoftReference<>(arrayOfByte);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     return (byte[])arrayOfByte.clone();
/*     */   }
/*     */   
/*     */   int argSlotCount() throws AbsentInformationException {
/* 269 */     if (this.argSlotCount == -1) {
/* 270 */       getVariables();
/*     */     }
/* 272 */     return this.argSlotCount;
/*     */   }
/*     */   
/*     */   private SoftLocationXRefs getLocations(SDE.Stratum paramStratum) {
/* 276 */     if (paramStratum.isJava()) {
/* 277 */       return getBaseLocations();
/*     */     }
/* 279 */     String str = paramStratum.id();
/*     */ 
/*     */     
/* 282 */     SoftLocationXRefs softLocationXRefs = (this.softOtherLocationXRefsRef == null) ? null : this.softOtherLocationXRefsRef.get();
/* 283 */     if (softLocationXRefs != null && softLocationXRefs.stratumID.equals(str)) {
/* 284 */       return softLocationXRefs;
/*     */     }
/*     */     
/* 287 */     ArrayList<LocationImpl> arrayList = new ArrayList();
/* 288 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 289 */     int i = -1;
/* 290 */     int j = -1;
/* 291 */     SDE.LineStratum lineStratum = null;
/*     */     
/* 293 */     SDE.Stratum stratum = this.declaringType.stratum("Java");
/* 294 */     Iterator<Location> iterator = (getBaseLocations()).lineLocations.iterator();
/* 295 */     while (iterator.hasNext()) {
/* 296 */       LocationImpl locationImpl = (LocationImpl)iterator.next();
/* 297 */       int k = locationImpl.lineNumber(stratum);
/*     */       
/* 299 */       SDE.LineStratum lineStratum1 = paramStratum.lineStratum(this.declaringType, k);
/*     */ 
/*     */       
/* 302 */       if (lineStratum1 == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 307 */       int m = lineStratum1.lineNumber();
/*     */ 
/*     */       
/* 310 */       if (m != -1 && 
/* 311 */         !lineStratum1.equals(lineStratum)) {
/* 312 */         lineStratum = lineStratum1;
/*     */ 
/*     */         
/* 315 */         if (m > j) {
/* 316 */           j = m;
/*     */         }
/* 318 */         if (m < i || i == -1) {
/* 319 */           i = m;
/*     */         }
/*     */         
/* 322 */         locationImpl.addStratumLineInfo(new StratumLineInfo(str, m, lineStratum1
/*     */ 
/*     */               
/* 325 */               .sourceName(), lineStratum1
/* 326 */               .sourcePath()));
/*     */ 
/*     */         
/* 329 */         arrayList.add(locationImpl);
/*     */ 
/*     */         
/* 332 */         Integer integer = new Integer(m);
/* 333 */         List<LocationImpl> list = (List)hashMap.get(integer);
/* 334 */         if (list == null) {
/* 335 */           list = new ArrayList(1);
/* 336 */           hashMap.put(integer, list);
/*     */         } 
/* 338 */         list.add(locationImpl);
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     softLocationXRefs = new SoftLocationXRefs(str, (Map)hashMap, (List)arrayList, i, j);
/*     */ 
/*     */     
/* 345 */     this.softOtherLocationXRefsRef = new SoftReference<>(softLocationXRefs);
/* 346 */     return softLocationXRefs;
/*     */   }
/*     */ 
/*     */   
/*     */   private SoftLocationXRefs getBaseLocations() {
/* 351 */     SoftLocationXRefs softLocationXRefs = (this.softBaseLocationXRefsRef == null) ? null : this.softBaseLocationXRefsRef.get();
/* 352 */     if (softLocationXRefs != null) {
/* 353 */       return softLocationXRefs;
/*     */     }
/*     */     
/* 356 */     JDWP.Method.LineTable lineTable = null;
/*     */     try {
/* 358 */       lineTable = JDWP.Method.LineTable.process(this.vm, this.declaringType, this.ref);
/* 359 */     } catch (JDWPException jDWPException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 364 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */     
/* 367 */     int i = lineTable.lines.length;
/*     */     
/* 369 */     ArrayList<LocationImpl> arrayList = new ArrayList(i);
/* 370 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 371 */     int j = -1;
/* 372 */     int k = -1;
/* 373 */     for (byte b = 0; b < i; b++) {
/* 374 */       long l = (lineTable.lines[b]).lineCodeIndex;
/* 375 */       int m = (lineTable.lines[b]).lineNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 385 */       if (b + 1 == i || l != (lineTable.lines[b + 1]).lineCodeIndex) {
/*     */         
/* 387 */         if (m > k) {
/* 388 */           k = m;
/*     */         }
/* 390 */         if (m < j || j == -1) {
/* 391 */           j = m;
/*     */         }
/*     */         
/* 394 */         LocationImpl locationImpl = new LocationImpl(virtualMachine(), this, l);
/* 395 */         locationImpl.addBaseLineInfo(new BaseLineInfo(m, this.declaringType));
/*     */ 
/*     */ 
/*     */         
/* 399 */         arrayList.add(locationImpl);
/*     */ 
/*     */         
/* 402 */         Integer integer = new Integer(m);
/* 403 */         List<LocationImpl> list = (List)hashMap.get(integer);
/* 404 */         if (list == null) {
/* 405 */           list = new ArrayList(1);
/* 406 */           hashMap.put(integer, list);
/*     */         } 
/* 408 */         list.add(locationImpl);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     if (this.location == null) {
/* 417 */       this.firstIndex = lineTable.start;
/* 418 */       this.lastIndex = lineTable.end;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 425 */       if (i > 0) {
/* 426 */         this.location = arrayList.get(0);
/*     */       } else {
/* 428 */         this.location = new LocationImpl(virtualMachine(), this, this.firstIndex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 433 */     softLocationXRefs = new SoftLocationXRefs("Java", (Map)hashMap, (List)arrayList, j, k);
/*     */ 
/*     */     
/* 436 */     this.softBaseLocationXRefsRef = new SoftReference<>(softLocationXRefs);
/* 437 */     return softLocationXRefs;
/*     */   }
/*     */   
/*     */   private List<LocalVariable> getVariables1_4() throws AbsentInformationException {
/* 441 */     JDWP.Method.VariableTable variableTable = null;
/*     */     
/*     */     try {
/* 444 */       variableTable = JDWP.Method.VariableTable.process(this.vm, this.declaringType, this.ref);
/* 445 */     } catch (JDWPException jDWPException) {
/* 446 */       if (jDWPException.errorCode() == 101) {
/* 447 */         this.absentVariableInformation = true;
/* 448 */         throw new AbsentInformationException();
/*     */       } 
/* 450 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 455 */     this.argSlotCount = variableTable.argCnt;
/* 456 */     int i = variableTable.slots.length;
/* 457 */     ArrayList<LocalVariableImpl> arrayList = new ArrayList(i);
/* 458 */     for (byte b = 0; b < i; b++) {
/* 459 */       JDWP.Method.VariableTable.SlotInfo slotInfo = variableTable.slots[b];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 465 */       if (!slotInfo.name.startsWith("this$") && !slotInfo.name.equals("this")) {
/* 466 */         LocationImpl locationImpl1 = new LocationImpl(virtualMachine(), this, slotInfo.codeIndex);
/*     */ 
/*     */         
/* 469 */         LocationImpl locationImpl2 = new LocationImpl(virtualMachine(), this, slotInfo.codeIndex + slotInfo.length - 1L);
/*     */ 
/*     */         
/* 472 */         LocalVariableImpl localVariableImpl = new LocalVariableImpl(virtualMachine(), this, slotInfo.slot, locationImpl1, locationImpl2, slotInfo.name, slotInfo.signature, null);
/*     */ 
/*     */ 
/*     */         
/* 476 */         arrayList.add(localVariableImpl);
/*     */       } 
/*     */     } 
/* 479 */     return (List)arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<LocalVariable> getVariables1() throws AbsentInformationException {
/* 484 */     if (!this.vm.canGet1_5LanguageFeatures()) {
/* 485 */       return getVariables1_4();
/*     */     }
/*     */     
/* 488 */     JDWP.Method.VariableTableWithGeneric variableTableWithGeneric = null;
/*     */     
/*     */     try {
/* 491 */       variableTableWithGeneric = JDWP.Method.VariableTableWithGeneric.process(this.vm, this.declaringType, this.ref);
/* 492 */     } catch (JDWPException jDWPException) {
/* 493 */       if (jDWPException.errorCode() == 101) {
/* 494 */         this.absentVariableInformation = true;
/* 495 */         throw new AbsentInformationException();
/*     */       } 
/* 497 */       throw jDWPException.toJDIException();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 502 */     this.argSlotCount = variableTableWithGeneric.argCnt;
/* 503 */     int i = variableTableWithGeneric.slots.length;
/* 504 */     ArrayList<LocalVariableImpl> arrayList = new ArrayList(i);
/* 505 */     for (byte b = 0; b < i; b++) {
/* 506 */       JDWP.Method.VariableTableWithGeneric.SlotInfo slotInfo = variableTableWithGeneric.slots[b];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 512 */       if (!slotInfo.name.startsWith("this$") && !slotInfo.name.equals("this")) {
/* 513 */         LocationImpl locationImpl1 = new LocationImpl(virtualMachine(), this, slotInfo.codeIndex);
/*     */ 
/*     */         
/* 516 */         LocationImpl locationImpl2 = new LocationImpl(virtualMachine(), this, slotInfo.codeIndex + slotInfo.length - 1L);
/*     */ 
/*     */         
/* 519 */         LocalVariableImpl localVariableImpl = new LocalVariableImpl(virtualMachine(), this, slotInfo.slot, locationImpl1, locationImpl2, slotInfo.name, slotInfo.signature, slotInfo.genericSignature);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 524 */         arrayList.add(localVariableImpl);
/*     */       } 
/*     */     } 
/* 527 */     return (List)arrayList;
/*     */   }
/*     */   
/*     */   private List<LocalVariable> getVariables() throws AbsentInformationException {
/* 531 */     if (this.absentVariableInformation) {
/* 532 */       throw new AbsentInformationException();
/*     */     }
/*     */ 
/*     */     
/* 536 */     List<LocalVariable> list = (this.variablesRef == null) ? null : this.variablesRef.get();
/* 537 */     if (list != null) {
/* 538 */       return list;
/*     */     }
/* 540 */     list = getVariables1();
/* 541 */     list = Collections.unmodifiableList(list);
/* 542 */     this.variablesRef = new SoftReference<>(list);
/* 543 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ConcreteMethodImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */