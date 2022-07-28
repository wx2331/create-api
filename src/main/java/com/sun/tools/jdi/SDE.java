/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ class SDE
/*     */ {
/*     */   private static final int INIT_SIZE_FILE = 3;
/*     */   private static final int INIT_SIZE_LINE = 100;
/*     */   private static final int INIT_SIZE_STRATUM = 3;
/*     */   static final String BASE_STRATUM_NAME = "Java";
/*  41 */   static final String NullString = null;
/*     */
/*     */
/*     */   private class FileTableRecord
/*     */   {
/*     */     int fileId;
/*     */
/*     */     String sourceName;
/*     */
/*     */     String sourcePath;
/*     */
/*     */     boolean isConverted = false;
/*     */
/*     */     String getSourcePath(ReferenceTypeImpl param1ReferenceTypeImpl) {
/*  55 */       if (!this.isConverted) {
/*  56 */         if (this.sourcePath == null) {
/*  57 */           this.sourcePath = param1ReferenceTypeImpl.baseSourceDir() + this.sourceName;
/*     */         } else {
/*  59 */           StringBuffer stringBuffer = new StringBuffer();
/*  60 */           for (byte b = 0; b < this.sourcePath.length(); b++) {
/*  61 */             char c = this.sourcePath.charAt(b);
/*  62 */             if (c == '/') {
/*  63 */               stringBuffer.append(File.separatorChar);
/*     */             } else {
/*  65 */               stringBuffer.append(c);
/*     */             }
/*     */           }
/*  68 */           this.sourcePath = stringBuffer.toString();
/*     */         }
/*  70 */         this.isConverted = true;
/*     */       }
/*  72 */       return this.sourcePath;
/*     */     }
/*     */
/*     */     private FileTableRecord() {} }
/*     */
/*     */   private class LineTableRecord {
/*     */     int jplsStart;
/*     */     int jplsEnd;
/*     */     int jplsLineInc;
/*     */     int njplsStart;
/*     */     int njplsEnd;
/*     */     int fileId;
/*     */
/*     */     private LineTableRecord() {}
/*     */   }
/*     */
/*     */   private class StratumTableRecord { String id;
/*     */     int fileIndex;
/*     */     int lineIndex;
/*     */
/*     */     private StratumTableRecord() {} }
/*     */
/*     */   class Stratum { private Stratum(int param1Int) {
/*  95 */       this.sti = param1Int;
/*     */     }
/*     */     private final int sti;
/*     */     String id() {
/*  99 */       return (SDE.this.stratumTable[this.sti]).id;
/*     */     }
/*     */
/*     */     boolean isJava() {
/* 103 */       return (this.sti == SDE.this.baseStratumIndex);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     List<String> sourceNames(ReferenceTypeImpl param1ReferenceTypeImpl) {
/* 115 */       int j = (SDE.this.stratumTable[this.sti]).fileIndex;
/*     */
/* 117 */       int k = (SDE.this.stratumTable[this.sti + 1]).fileIndex;
/* 118 */       ArrayList<String> arrayList = new ArrayList(k - j);
/* 119 */       for (int i = j; i < k; i++) {
/* 120 */         arrayList.add((SDE.this.fileTable[i]).sourceName);
/*     */       }
/* 122 */       return arrayList;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     List<String> sourcePaths(ReferenceTypeImpl param1ReferenceTypeImpl) {
/* 134 */       int j = (SDE.this.stratumTable[this.sti]).fileIndex;
/*     */
/* 136 */       int k = (SDE.this.stratumTable[this.sti + 1]).fileIndex;
/* 137 */       ArrayList<String> arrayList = new ArrayList(k - j);
/* 138 */       for (int i = j; i < k; i++) {
/* 139 */         arrayList.add(SDE.this.fileTable[i].getSourcePath(param1ReferenceTypeImpl));
/*     */       }
/* 141 */       return arrayList;
/*     */     }
/*     */
/*     */
/*     */     LineStratum lineStratum(ReferenceTypeImpl param1ReferenceTypeImpl, int param1Int) {
/* 146 */       int i = SDE.this.stiLineTableIndex(this.sti, param1Int);
/* 147 */       if (i < 0) {
/* 148 */         return null;
/*     */       }
/* 150 */       return new LineStratum(this.sti, i, param1ReferenceTypeImpl, param1Int);
/*     */     } }
/*     */
/*     */
/*     */
/*     */   class LineStratum
/*     */   {
/*     */     private final int sti;
/*     */     private final int lti;
/*     */     private final ReferenceTypeImpl refType;
/*     */     private final int jplsLine;
/* 161 */     private String sourceName = null;
/* 162 */     private String sourcePath = null;
/*     */
/*     */
/*     */
/*     */     private LineStratum(int param1Int1, int param1Int2, ReferenceTypeImpl param1ReferenceTypeImpl, int param1Int3) {
/* 167 */       this.sti = param1Int1;
/* 168 */       this.lti = param1Int2;
/* 169 */       this.refType = param1ReferenceTypeImpl;
/* 170 */       this.jplsLine = param1Int3;
/*     */     }
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 174 */       if (param1Object instanceof LineStratum) {
/* 175 */         LineStratum lineStratum = (LineStratum)param1Object;
/* 176 */         return (this.lti == lineStratum.lti && this.sti == lineStratum.sti &&
/*     */
/* 178 */           lineNumber() == lineStratum.lineNumber() && this.refType
/* 179 */           .equals(lineStratum.refType));
/*     */       }
/* 181 */       return false;
/*     */     }
/*     */
/*     */
/*     */
/*     */     public int hashCode() {
/* 187 */       return lineNumber() * 17 ^ this.refType.hashCode();
/*     */     }
/*     */
/*     */     int lineNumber() {
/* 191 */       return SDE.this.stiLineNumber(this.sti, this.lti, this.jplsLine);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     void getSourceInfo() {
/* 200 */       if (this.sourceName != null) {
/*     */         return;
/*     */       }
/*     */
/* 204 */       int i = SDE.this.stiFileTableIndex(this.sti, this.lti);
/* 205 */       if (i == -1) {
/* 206 */         throw new InternalError("Bad SourceDebugExtension, no matching source id " +
/*     */
/* 208 */             (SDE.this.lineTable[this.lti]).fileId + " jplsLine: " + this.jplsLine);
/*     */       }
/* 210 */       FileTableRecord fileTableRecord = SDE.this.fileTable[i];
/* 211 */       this.sourceName = fileTableRecord.sourceName;
/* 212 */       this.sourcePath = fileTableRecord.getSourcePath(this.refType);
/*     */     }
/*     */
/*     */     String sourceName() {
/* 216 */       getSourceInfo();
/* 217 */       return this.sourceName;
/*     */     }
/*     */
/*     */     String sourcePath() {
/* 221 */       getSourceInfo();
/* 222 */       return this.sourcePath;
/*     */     }
/*     */   }
/*     */
/* 226 */   private FileTableRecord[] fileTable = null;
/* 227 */   private LineTableRecord[] lineTable = null;
/* 228 */   private StratumTableRecord[] stratumTable = null;
/*     */
/* 230 */   private int fileIndex = 0;
/* 231 */   private int lineIndex = 0;
/* 232 */   private int stratumIndex = 0;
/* 233 */   private int currentFileId = 0;
/*     */
/* 235 */   private int defaultStratumIndex = -1;
/* 236 */   private int baseStratumIndex = -2;
/* 237 */   private int sdePos = 0;
/*     */
/*     */   final String sourceDebugExtension;
/* 240 */   String jplsFilename = null;
/* 241 */   String defaultStratumId = null;
/*     */   boolean isValid = false;
/*     */
/*     */   SDE(String paramString) {
/* 245 */     this.sourceDebugExtension = paramString;
/* 246 */     decode();
/*     */   }
/*     */
/*     */   SDE() {
/* 250 */     this.sourceDebugExtension = null;
/* 251 */     createProxyForAbsentSDE();
/*     */   }
/*     */
/*     */   char sdePeek() {
/* 255 */     if (this.sdePos >= this.sourceDebugExtension.length()) {
/* 256 */       syntax();
/*     */     }
/* 258 */     return this.sourceDebugExtension.charAt(this.sdePos);
/*     */   }
/*     */
/*     */   char sdeRead() {
/* 262 */     if (this.sdePos >= this.sourceDebugExtension.length()) {
/* 263 */       syntax();
/*     */     }
/* 265 */     return this.sourceDebugExtension.charAt(this.sdePos++);
/*     */   }
/*     */
/*     */   void sdeAdvance() {
/* 269 */     this.sdePos++;
/*     */   }
/*     */
/*     */   void syntax() {
/* 273 */     throw new InternalError("bad SourceDebugExtension syntax - position " + this.sdePos);
/*     */   }
/*     */
/*     */
/*     */   void syntax(String paramString) {
/* 278 */     throw new InternalError("bad SourceDebugExtension syntax: " + paramString);
/*     */   }
/*     */
/*     */   void assureLineTableSize() {
/* 282 */     byte b = (this.lineTable == null) ? 0 : this.lineTable.length;
/* 283 */     if (this.lineIndex >= b) {
/*     */
/* 285 */       byte b2 = (b == 0) ? 100 : (b * 2);
/* 286 */       LineTableRecord[] arrayOfLineTableRecord = new LineTableRecord[b2]; byte b1;
/* 287 */       for (b1 = 0; b1 < b; b1++) {
/* 288 */         arrayOfLineTableRecord[b1] = this.lineTable[b1];
/*     */       }
/* 290 */       for (; b1 < b2; b1++) {
/* 291 */         arrayOfLineTableRecord[b1] = new LineTableRecord();
/*     */       }
/* 293 */       this.lineTable = arrayOfLineTableRecord;
/*     */     }
/*     */   }
/*     */
/*     */   void assureFileTableSize() {
/* 298 */     byte b = (this.fileTable == null) ? 0 : this.fileTable.length;
/* 299 */     if (this.fileIndex >= b) {
/*     */
/* 301 */       byte b2 = (b == 0) ? 3 : (b * 2);
/* 302 */       FileTableRecord[] arrayOfFileTableRecord = new FileTableRecord[b2]; byte b1;
/* 303 */       for (b1 = 0; b1 < b; b1++) {
/* 304 */         arrayOfFileTableRecord[b1] = this.fileTable[b1];
/*     */       }
/* 306 */       for (; b1 < b2; b1++) {
/* 307 */         arrayOfFileTableRecord[b1] = new FileTableRecord();
/*     */       }
/* 309 */       this.fileTable = arrayOfFileTableRecord;
/*     */     }
/*     */   }
/*     */
/*     */   void assureStratumTableSize() {
/* 314 */     byte b = (this.stratumTable == null) ? 0 : this.stratumTable.length;
/* 315 */     if (this.stratumIndex >= b) {
/*     */
/* 317 */       byte b2 = (b == 0) ? 3 : (b * 2);
/* 318 */       StratumTableRecord[] arrayOfStratumTableRecord = new StratumTableRecord[b2]; byte b1;
/* 319 */       for (b1 = 0; b1 < b; b1++) {
/* 320 */         arrayOfStratumTableRecord[b1] = this.stratumTable[b1];
/*     */       }
/* 322 */       for (; b1 < b2; b1++) {
/* 323 */         arrayOfStratumTableRecord[b1] = new StratumTableRecord();
/*     */       }
/* 325 */       this.stratumTable = arrayOfStratumTableRecord;
/*     */     }
/*     */   }
/*     */
/*     */   String readLine() {
/* 330 */     StringBuffer stringBuffer = new StringBuffer();
/*     */
/*     */
/* 333 */     ignoreWhite(); char c;
/* 334 */     while ((c = sdeRead()) != '\n' && c != '\r') {
/* 335 */       stringBuffer.append(c);
/*     */     }
/*     */
/* 338 */     if (c == '\r' && sdePeek() == '\n') {
/* 339 */       sdeRead();
/*     */     }
/* 341 */     ignoreWhite();
/* 342 */     return stringBuffer.toString();
/*     */   }
/*     */
/*     */   private int defaultStratumTableIndex() {
/* 346 */     if (this.defaultStratumIndex == -1 && this.defaultStratumId != null) {
/* 347 */       this
/* 348 */         .defaultStratumIndex = stratumTableIndex(this.defaultStratumId);
/*     */     }
/* 350 */     return this.defaultStratumIndex;
/*     */   }
/*     */
/*     */
/*     */
/*     */   int stratumTableIndex(String paramString) {
/* 356 */     if (paramString == null) {
/* 357 */       return defaultStratumTableIndex();
/*     */     }
/* 359 */     for (byte b = 0; b < this.stratumIndex - 1; b++) {
/* 360 */       if ((this.stratumTable[b]).id.equals(paramString)) {
/* 361 */         return b;
/*     */       }
/*     */     }
/* 364 */     return defaultStratumTableIndex();
/*     */   }
/*     */
/*     */   Stratum stratum(String paramString) {
/* 368 */     int i = stratumTableIndex(paramString);
/* 369 */     return new Stratum(i);
/*     */   }
/*     */
/*     */   List<String> availableStrata() {
/* 373 */     ArrayList<String> arrayList = new ArrayList();
/*     */
/* 375 */     for (byte b = 0; b < this.stratumIndex - 1; b++) {
/* 376 */       StratumTableRecord stratumTableRecord = this.stratumTable[b];
/* 377 */       arrayList.add(stratumTableRecord.id);
/*     */     }
/* 379 */     return arrayList;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void ignoreWhite() {
/*     */     char c;
/* 414 */     while ((c = sdePeek()) == ' ' || c == '\t') {
/* 415 */       sdeAdvance();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   void ignoreLine() {
/*     */     char c;
/* 422 */     while ((c = sdeRead()) != '\n' && c != '\r');
/*     */
/*     */
/* 425 */     if (c == '\r' && sdePeek() == '\n') {
/* 426 */       sdeAdvance();
/*     */     }
/* 428 */     ignoreWhite();
/*     */   }
/*     */
/*     */   int readNumber() {
/* 432 */     int i = 0;
/*     */
/*     */
/* 435 */     ignoreWhite(); char c;
/* 436 */     while ((c = sdePeek()) >= '0' && c <= '9') {
/* 437 */       sdeAdvance();
/* 438 */       i = i * 10 + c - 48;
/*     */     }
/* 440 */     ignoreWhite();
/* 441 */     return i;
/*     */   }
/*     */
/*     */   void storeFile(int paramInt, String paramString1, String paramString2) {
/* 445 */     assureFileTableSize();
/* 446 */     (this.fileTable[this.fileIndex]).fileId = paramInt;
/* 447 */     (this.fileTable[this.fileIndex]).sourceName = paramString1;
/* 448 */     (this.fileTable[this.fileIndex]).sourcePath = paramString2;
/* 449 */     this.fileIndex++;
/*     */   }
/*     */
/*     */   void fileLine() {
/* 453 */     boolean bool = false;
/*     */
/*     */
/* 456 */     String str2 = null;
/*     */
/*     */
/* 459 */     if (sdePeek() == '+') {
/* 460 */       sdeAdvance();
/* 461 */       bool = true;
/*     */     }
/* 463 */     int i = readNumber();
/* 464 */     String str1 = readLine();
/* 465 */     if (bool == true) {
/* 466 */       str2 = readLine();
/*     */     }
/*     */
/* 469 */     storeFile(i, str1, str2);
/*     */   }
/*     */
/*     */
/*     */   void storeLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 474 */     assureLineTableSize();
/* 475 */     (this.lineTable[this.lineIndex]).jplsStart = paramInt1;
/* 476 */     (this.lineTable[this.lineIndex]).jplsEnd = paramInt2;
/* 477 */     (this.lineTable[this.lineIndex]).jplsLineInc = paramInt3;
/* 478 */     (this.lineTable[this.lineIndex]).njplsStart = paramInt4;
/* 479 */     (this.lineTable[this.lineIndex]).njplsEnd = paramInt5;
/* 480 */     (this.lineTable[this.lineIndex]).fileId = paramInt6;
/* 481 */     this.lineIndex++;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void lineLine() {
/* 490 */     int i = 1;
/* 491 */     int j = 1;
/*     */
/*     */
/*     */
/* 495 */     int k = readNumber();
/*     */
/*     */
/* 498 */     if (sdePeek() == '#') {
/* 499 */       sdeAdvance();
/* 500 */       this.currentFileId = readNumber();
/*     */     }
/*     */
/*     */
/* 504 */     if (sdePeek() == ',') {
/* 505 */       sdeAdvance();
/* 506 */       i = readNumber();
/*     */     }
/*     */
/* 509 */     if (sdeRead() != ':') {
/* 510 */       syntax();
/*     */     }
/* 512 */     int m = readNumber();
/* 513 */     if (sdePeek() == ',') {
/* 514 */       sdeAdvance();
/* 515 */       j = readNumber();
/*     */     }
/* 517 */     ignoreLine();
/*     */
/* 519 */     storeLine(m, m + i * j - 1, j, k, k + i - 1, this.currentFileId);
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
/*     */   void storeStratum(String paramString) {
/* 533 */     if (this.stratumIndex > 0 &&
/* 534 */       (this.stratumTable[this.stratumIndex - 1]).fileIndex == this.fileIndex && (this.stratumTable[this.stratumIndex - 1]).lineIndex == this.lineIndex)
/*     */     {
/*     */
/*     */
/*     */
/* 539 */       this.stratumIndex--;
/*     */     }
/*     */
/*     */
/* 543 */     assureStratumTableSize();
/* 544 */     (this.stratumTable[this.stratumIndex]).id = paramString;
/* 545 */     (this.stratumTable[this.stratumIndex]).fileIndex = this.fileIndex;
/* 546 */     (this.stratumTable[this.stratumIndex]).lineIndex = this.lineIndex;
/* 547 */     this.stratumIndex++;
/* 548 */     this.currentFileId = 0;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   void stratumSection() {
/* 555 */     storeStratum(readLine());
/*     */   }
/*     */
/*     */   void fileSection() {
/* 559 */     ignoreLine();
/* 560 */     while (sdePeek() != '*') {
/* 561 */       fileLine();
/*     */     }
/*     */   }
/*     */
/*     */   void lineSection() {
/* 566 */     ignoreLine();
/* 567 */     while (sdePeek() != '*') {
/* 568 */       lineLine();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   void ignoreSection() {
/* 576 */     ignoreLine();
/* 577 */     while (sdePeek() != '*') {
/* 578 */       ignoreLine();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void createJavaStratum() {
/* 588 */     this.baseStratumIndex = this.stratumIndex;
/* 589 */     storeStratum("Java");
/* 590 */     storeFile(1, this.jplsFilename, NullString);
/*     */
/* 592 */     storeLine(1, 65536, 1, 1, 65536, 1);
/* 593 */     storeStratum("Aux");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void decode() {
/* 602 */     if (this.sourceDebugExtension.length() < 4 ||
/* 603 */       sdeRead() != 'S' ||
/* 604 */       sdeRead() != 'M' ||
/* 605 */       sdeRead() != 'A' ||
/* 606 */       sdeRead() != 'P') {
/*     */       return;
/*     */     }
/* 609 */     ignoreLine();
/* 610 */     this.jplsFilename = readLine();
/* 611 */     this.defaultStratumId = readLine();
/* 612 */     createJavaStratum();
/*     */     while (true) {
/* 614 */       if (sdeRead() != '*') {
/* 615 */         syntax();
/*     */       }
/* 617 */       switch (sdeRead()) {
/*     */         case 'S':
/* 619 */           stratumSection();
/*     */           continue;
/*     */         case 'F':
/* 622 */           fileSection();
/*     */           continue;
/*     */         case 'L':
/* 625 */           lineSection();
/*     */           continue;
/*     */
/*     */         case 'E':
/* 629 */           storeStratum("*terminator*");
/* 630 */           this.isValid = true;
/*     */           return;
/*     */       }
/* 633 */       ignoreSection();
/*     */     }
/*     */   }
/*     */
/*     */
/*     */   void createProxyForAbsentSDE() {
/* 639 */     this.jplsFilename = null;
/* 640 */     this.defaultStratumId = "Java";
/* 641 */     this.defaultStratumIndex = this.stratumIndex;
/* 642 */     createJavaStratum();
/* 643 */     storeStratum("*terminator*");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private int stiLineTableIndex(int paramInt1, int paramInt2) {
/* 653 */     int j = (this.stratumTable[paramInt1]).lineIndex;
/*     */
/* 655 */     int k = (this.stratumTable[paramInt1 + 1]).lineIndex;
/* 656 */     for (int i = j; i < k; i++) {
/* 657 */       if (paramInt2 >= (this.lineTable[i]).jplsStart && paramInt2 <= (this.lineTable[i]).jplsEnd)
/*     */       {
/* 659 */         return i;
/*     */       }
/*     */     }
/* 662 */     return -1;
/*     */   }
/*     */
/*     */   private int stiLineNumber(int paramInt1, int paramInt2, int paramInt3) {
/* 666 */     return (this.lineTable[paramInt2]).njplsStart + (paramInt3 - (this.lineTable[paramInt2]).jplsStart) / (this.lineTable[paramInt2]).jplsLineInc;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   private int fileTableIndex(int paramInt1, int paramInt2) {
/* 673 */     int j = (this.stratumTable[paramInt1]).fileIndex;
/*     */
/* 675 */     int k = (this.stratumTable[paramInt1 + 1]).fileIndex;
/* 676 */     for (int i = j; i < k; i++) {
/* 677 */       if ((this.fileTable[i]).fileId == paramInt2) {
/* 678 */         return i;
/*     */       }
/*     */     }
/* 681 */     return -1;
/*     */   }
/*     */
/*     */   private int stiFileTableIndex(int paramInt1, int paramInt2) {
/* 685 */     return fileTableIndex(paramInt1, (this.lineTable[paramInt2]).fileId);
/*     */   }
/*     */
/*     */   boolean isValid() {
/* 689 */     return this.isValid;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\SDE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
