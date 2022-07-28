/*      */ package com.sun.tools.corba.se.idl;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Scanner
/*      */ {
/*      */   static final int Star = 0;
/*      */   static final int Plus = 1;
/*      */   static final int Dot = 2;
/*      */   static final int None = 3;
/*      */   private int BOL;
/*      */   private ScannerData data;
/*      */   private Stack dataStack;
/*      */   private Vector keywords;
/*      */   private Vector openEndedKeywords;
/*      */   private Vector wildcardKeywords;
/*      */   private boolean verbose;
/*      */   boolean escapedOK;
/*      */   private boolean emitAll;
/*      */   private float corbaLevel;
/*      */   private boolean debug;
/*      */   
/*      */   Scanner(IncludeEntry paramIncludeEntry, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, boolean paramBoolean3) throws IOException {
/* 1543 */     this.data = new ScannerData();
/* 1544 */     this.dataStack = new Stack();
/* 1545 */     this.keywords = new Vector();
/* 1546 */     this.openEndedKeywords = new Vector();
/* 1547 */     this.wildcardKeywords = new Vector();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1556 */     this.escapedOK = true;
/*      */     readFile(paramIncludeEntry);
/*      */     this.verbose = paramBoolean1;
/*      */     this.emitAll = paramBoolean2;
/*      */     sortKeywords(paramArrayOfString);
/*      */     this.corbaLevel = paramFloat;
/*      */     this.debug = paramBoolean3;
/*      */   }
/*      */   
/*      */   void sortKeywords(String[] paramArrayOfString) {
/*      */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*      */       if (wildcardAtEitherEnd(paramArrayOfString[b])) {
/*      */         this.openEndedKeywords.addElement(paramArrayOfString[b]);
/*      */       } else if (wildcardsInside(paramArrayOfString[b])) {
/*      */         this.wildcardKeywords.addElement(paramArrayOfString[b]);
/*      */       } else {
/*      */         this.keywords.addElement(paramArrayOfString[b]);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean wildcardAtEitherEnd(String paramString) {
/*      */     return (paramString.startsWith("*") || paramString.startsWith("+") || paramString.startsWith(".") || paramString.endsWith("*") || paramString.endsWith("+") || paramString.endsWith("."));
/*      */   }
/*      */   
/*      */   private boolean wildcardsInside(String paramString) {
/*      */     return (paramString.indexOf("*") > 0 || paramString.indexOf("+") > 0 || paramString.indexOf(".") > 0);
/*      */   }
/*      */   
/*      */   void readFile(IncludeEntry paramIncludeEntry) throws IOException {
/*      */     String str = paramIncludeEntry.name();
/*      */     str = str.substring(1, str.length() - 1);
/*      */     readFile(paramIncludeEntry, str);
/*      */   }
/*      */   
/*      */   void readFile(IncludeEntry paramIncludeEntry, String paramString) throws IOException {
/*      */     this.data.fileEntry = paramIncludeEntry;
/*      */     this.data.filename = paramString;
/*      */     File file = new File(this.data.filename);
/*      */     int i = (int)file.length();
/*      */     FileReader fileReader = new FileReader(file);
/*      */     String str = System.getProperty("line.separator");
/*      */     this.data.fileBytes = new char[i + str.length()];
/*      */     fileReader.read(this.data.fileBytes, 0, i);
/*      */     fileReader.close();
/*      */     for (byte b = 0; b < str.length(); b++)
/*      */       this.data.fileBytes[i + b] = str.charAt(b); 
/*      */     readChar();
/*      */   }
/*      */   
/*      */   Token getToken() throws IOException {
/*      */     Token token = null;
/*      */     String str = new String("");
/*      */     while (token == null) {
/*      */       try {
/*      */         this.data.oldIndex = this.data.fileIndex;
/*      */         this.data.oldLine = this.data.line;
/*      */         if (this.data.ch <= ' ') {
/*      */           skipWhiteSpace();
/*      */           continue;
/*      */         } 
/*      */         if (this.data.ch == 'L') {
/*      */           readChar();
/*      */           if (this.data.ch == '\'') {
/*      */             token = getCharacterToken(true);
/*      */             readChar();
/*      */             continue;
/*      */           } 
/*      */           if (this.data.ch == '"') {
/*      */             readChar();
/*      */             token = new Token(204, getUntil('"'), true);
/*      */             readChar();
/*      */             continue;
/*      */           } 
/*      */           unread(this.data.ch);
/*      */           unread('L');
/*      */           readChar();
/*      */         } 
/*      */         if ((this.data.ch >= 'a' && this.data.ch <= 'z') || (this.data.ch >= 'A' && this.data.ch <= 'Z') || this.data.ch == '_' || Character.isLetter(this.data.ch)) {
/*      */           token = getString();
/*      */           continue;
/*      */         } 
/*      */         if ((this.data.ch >= '0' && this.data.ch <= '9') || this.data.ch == '.') {
/*      */           token = getNumber();
/*      */           continue;
/*      */         } 
/*      */         switch (this.data.ch) {
/*      */           case ';':
/*      */             token = new Token(100);
/*      */             break;
/*      */           case '{':
/*      */             token = new Token(101);
/*      */             break;
/*      */           case '}':
/*      */             token = new Token(102);
/*      */             break;
/*      */           case ':':
/*      */             readChar();
/*      */             if (this.data.ch == ':') {
/*      */               token = new Token(124);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(103);
/*      */             break;
/*      */           case ',':
/*      */             token = new Token(104);
/*      */             break;
/*      */           case '=':
/*      */             readChar();
/*      */             if (this.data.ch == '=') {
/*      */               token = new Token(130);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(105);
/*      */             break;
/*      */           case '+':
/*      */             token = new Token(106);
/*      */             break;
/*      */           case '-':
/*      */             token = new Token(107);
/*      */             break;
/*      */           case '(':
/*      */             token = new Token(108);
/*      */             break;
/*      */           case ')':
/*      */             token = new Token(109);
/*      */             break;
/*      */           case '<':
/*      */             readChar();
/*      */             if (this.data.ch == '<') {
/*      */               token = new Token(125);
/*      */               break;
/*      */             } 
/*      */             if (this.data.ch == '=') {
/*      */               token = new Token(133);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(110);
/*      */             break;
/*      */           case '>':
/*      */             readChar();
/*      */             if (this.data.ch == '>') {
/*      */               token = new Token(126);
/*      */               break;
/*      */             } 
/*      */             if (this.data.ch == '=') {
/*      */               token = new Token(132);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(111);
/*      */             break;
/*      */           case '[':
/*      */             token = new Token(112);
/*      */             break;
/*      */           case ']':
/*      */             token = new Token(113);
/*      */             break;
/*      */           case '\'':
/*      */             token = getCharacterToken(false);
/*      */             break;
/*      */           case '"':
/*      */             readChar();
/*      */             token = new Token(204, getUntil('"', false, false, false));
/*      */             break;
/*      */           case '\\':
/*      */             readChar();
/*      */             if (this.data.ch == '\n' || this.data.ch == '\r') {
/*      */               token = null;
/*      */               break;
/*      */             } 
/*      */             token = new Token(116);
/*      */             break;
/*      */           case '|':
/*      */             readChar();
/*      */             if (this.data.ch == '|') {
/*      */               token = new Token(134);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(117);
/*      */             break;
/*      */           case '^':
/*      */             token = new Token(118);
/*      */             break;
/*      */           case '&':
/*      */             readChar();
/*      */             if (this.data.ch == '&') {
/*      */               token = new Token(135);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(119);
/*      */             break;
/*      */           case '*':
/*      */             token = new Token(120);
/*      */             break;
/*      */           case '/':
/*      */             readChar();
/*      */             if (this.data.ch == '/') {
/*      */               str = getLineComment();
/*      */               break;
/*      */             } 
/*      */             if (this.data.ch == '*') {
/*      */               str = getBlockComment();
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(121);
/*      */             break;
/*      */           case '%':
/*      */             token = new Token(122);
/*      */             break;
/*      */           case '~':
/*      */             token = new Token(123);
/*      */             break;
/*      */           case '#':
/*      */             token = getDirective();
/*      */             break;
/*      */           case '!':
/*      */             readChar();
/*      */             if (this.data.ch == '=') {
/*      */               token = new Token(131);
/*      */               break;
/*      */             } 
/*      */             unread(this.data.ch);
/*      */             token = new Token(129);
/*      */             break;
/*      */           case '?':
/*      */             try {
/*      */               token = replaceTrigraph();
/*      */               break;
/*      */             } catch (InvalidCharacter invalidCharacter) {}
/*      */           default:
/*      */             throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */         } 
/*      */         readChar();
/*      */       } catch (EOFException eOFException) {
/*      */         token = new Token(999);
/*      */       } 
/*      */     } 
/*      */     token.comment = new Comment(str);
/*      */     if (this.debug)
/*      */       System.out.println("Token: " + token); 
/*      */     return token;
/*      */   }
/*      */   
/*      */   void scanString(String paramString) {
/*      */     this.dataStack.push(this.data);
/*      */     this.data = new ScannerData(this.data);
/*      */     this.data.fileIndex = 0;
/*      */     this.data.oldIndex = 0;
/*      */     int i = paramString.length();
/*      */     this.data.fileBytes = new char[i];
/*      */     paramString.getChars(0, i, this.data.fileBytes, 0);
/*      */     this.data.macrodata = true;
/*      */     try {
/*      */       readChar();
/*      */     } catch (IOException iOException) {}
/*      */   }
/*      */   
/*      */   void scanIncludedFile(IncludeEntry paramIncludeEntry, String paramString, boolean paramBoolean) throws IOException {
/*      */     this.dataStack.push(this.data);
/*      */     this.data = new ScannerData();
/*      */     ((ScannerData)this.dataStack.peek()).indent += ' ';
/*      */     this.data.includeIsImport = paramBoolean;
/*      */     try {
/*      */       readFile(paramIncludeEntry, paramString);
/*      */       if (!this.emitAll && paramBoolean)
/*      */         SymtabEntry.enteringInclude(); 
/*      */       Parser.enteringInclude();
/*      */       if (this.verbose)
/*      */         System.out.println(this.data.indent + Util.getMessage("Compile.parsing", paramString)); 
/*      */     } catch (IOException iOException) {
/*      */       this.data = this.dataStack.pop();
/*      */       throw iOException;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void unread(char paramChar) {
/*      */     if (paramChar == '\n' && !this.data.macrodata)
/*      */       this.data.line--; 
/*      */     this.data.fileIndex--;
/*      */   }
/*      */   
/*      */   void readChar() throws IOException {
/*      */     if (this.data.fileIndex >= this.data.fileBytes.length) {
/*      */       if (this.dataStack.empty())
/*      */         throw new EOFException(); 
/*      */       if (!this.data.macrodata) {
/*      */         if (!this.emitAll && this.data.includeIsImport)
/*      */           SymtabEntry.exitingInclude(); 
/*      */         Parser.exitingInclude();
/*      */       } 
/*      */       if (this.verbose && !this.data.macrodata)
/*      */         System.out.println(this.data.indent + Util.getMessage("Compile.parseDone", this.data.filename)); 
/*      */       this.data = this.dataStack.pop();
/*      */     } else {
/*      */       this.data.ch = (char)(this.data.fileBytes[this.data.fileIndex++] & 0xFF);
/*      */       if (this.data.ch == '\n' && !this.data.macrodata)
/*      */         this.data.line++; 
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getWString() throws IOException {
/*      */     readChar();
/*      */     StringBuffer stringBuffer = new StringBuffer();
/*      */     while (this.data.ch != '"') {
/*      */       if (this.data.ch == '\\') {
/*      */         readChar();
/*      */         if (this.data.ch == 'u') {
/*      */           int i = getNDigitHexNumber(4);
/*      */           System.out.println("Got num: " + i);
/*      */           System.out.println("Which is: " + (char)i);
/*      */           stringBuffer.append((char)i);
/*      */           continue;
/*      */         } 
/*      */         if (this.data.ch >= '0' && this.data.ch <= '7') {
/*      */           stringBuffer.append((char)get3DigitOctalNumber());
/*      */           continue;
/*      */         } 
/*      */         stringBuffer.append('\\');
/*      */         stringBuffer.append(this.data.ch);
/*      */       } else {
/*      */         stringBuffer.append(this.data.ch);
/*      */       } 
/*      */       readChar();
/*      */     } 
/*      */     return stringBuffer.toString();
/*      */   }
/*      */   
/*      */   private Token getCharacterToken(boolean paramBoolean) throws IOException {
/*      */     Token token = null;
/*      */     readChar();
/*      */     if (this.data.ch == '\\') {
/*      */       readChar();
/*      */       if (this.data.ch == 'x' || this.data.ch == 'u') {
/*      */         char c = this.data.ch;
/*      */         int i = getNDigitHexNumber((c == 'x') ? 2 : 4);
/*      */         return new Token(201, (char)i + "\\" + c + Integer.toString(i, 16), paramBoolean);
/*      */       } 
/*      */       if (this.data.ch >= '0' && this.data.ch <= '7') {
/*      */         int i = get3DigitOctalNumber();
/*      */         return new Token(201, (char)i + "\\" + Integer.toString(i, 8), paramBoolean);
/*      */       } 
/*      */       return singleCharEscapeSequence(paramBoolean);
/*      */     } 
/*      */     token = new Token(201, "" + this.data.ch + this.data.ch, paramBoolean);
/*      */     readChar();
/*      */     return token;
/*      */   }
/*      */   
/*      */   private Token singleCharEscapeSequence(boolean paramBoolean) throws IOException {
/*      */     Token token;
/*      */     if (this.data.ch == 'n') {
/*      */       token = new Token(201, "\n\\n", paramBoolean);
/*      */     } else if (this.data.ch == 't') {
/*      */       token = new Token(201, "\t\\t", paramBoolean);
/*      */     } else if (this.data.ch == 'v') {
/*      */       token = new Token(201, "\013\\v", paramBoolean);
/*      */     } else if (this.data.ch == 'b') {
/*      */       token = new Token(201, "\b\\b", paramBoolean);
/*      */     } else if (this.data.ch == 'r') {
/*      */       token = new Token(201, "\r\\r", paramBoolean);
/*      */     } else if (this.data.ch == 'f') {
/*      */       token = new Token(201, "\f\\f", paramBoolean);
/*      */     } else if (this.data.ch == 'a') {
/*      */       token = new Token(201, "\007\\a", paramBoolean);
/*      */     } else if (this.data.ch == '\\') {
/*      */       token = new Token(201, "\\\\\\", paramBoolean);
/*      */     } else if (this.data.ch == '?') {
/*      */       token = new Token(201, "?\\?", paramBoolean);
/*      */     } else if (this.data.ch == '\'') {
/*      */       token = new Token(201, "'\\'", paramBoolean);
/*      */     } else if (this.data.ch == '"') {
/*      */       token = new Token(201, "\"\\\"", paramBoolean);
/*      */     } else {
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */     } 
/*      */     readChar();
/*      */     return token;
/*      */   }
/*      */   
/*      */   private Token getString() throws IOException {
/*      */     StringBuffer stringBuffer = new StringBuffer();
/*      */     boolean bool = false;
/*      */     boolean[] arrayOfBoolean = { false };
/*      */     stringBuffer.append(this.data.ch);
/*      */     readChar();
/*      */     if (this.data.ch == '_' && (bool = this.escapedOK) && this.data.ch == '_')
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch); 
/*      */     while (Character.isLetterOrDigit(this.data.ch) || this.data.ch == '_') {
/*      */       stringBuffer.append(this.data.ch);
/*      */       readChar();
/*      */     } 
/*      */     String str = stringBuffer.toString();
/*      */     if (!bool) {
/*      */       Token token = Token.makeKeywordToken(str, this.corbaLevel, this.escapedOK, arrayOfBoolean);
/*      */       if (token != null)
/*      */         return token; 
/*      */     } 
/*      */     str = getIdentifier(str);
/*      */     if (this.data.ch == '(') {
/*      */       readChar();
/*      */       return new Token(81, str, bool, arrayOfBoolean[0], false);
/*      */     } 
/*      */     return new Token(80, str, bool, arrayOfBoolean[0], false);
/*      */   }
/*      */   
/*      */   private boolean matchesClosedWildKeyword(String paramString) {
/*      */     boolean bool = true;
/*      */     String str = paramString;
/*      */     Enumeration<String> enumeration = this.wildcardKeywords.elements();
/*      */     while (enumeration.hasMoreElements()) {
/*      */       byte b = 3;
/*      */       StringTokenizer stringTokenizer = new StringTokenizer(enumeration.nextElement(), "*+.", true);
/*      */       if (stringTokenizer.hasMoreTokens()) {
/*      */         String str1 = stringTokenizer.nextToken();
/*      */         if (str.startsWith(str1)) {
/*      */           str = str.substring(str1.length());
/*      */           while (stringTokenizer.hasMoreTokens() && bool) {
/*      */             str1 = stringTokenizer.nextToken();
/*      */             if (str1.equals("*")) {
/*      */               b = 0;
/*      */               continue;
/*      */             } 
/*      */             if (str1.equals("+")) {
/*      */               b = 1;
/*      */               continue;
/*      */             } 
/*      */             if (str1.equals(".")) {
/*      */               b = 2;
/*      */               continue;
/*      */             } 
/*      */             if (b == 0) {
/*      */               int i = str.indexOf(str1);
/*      */               if (i >= 0) {
/*      */                 str = str.substring(i + str1.length());
/*      */                 continue;
/*      */               } 
/*      */               bool = false;
/*      */               continue;
/*      */             } 
/*      */             if (b == 1) {
/*      */               int i = str.indexOf(str1);
/*      */               if (i > 0) {
/*      */                 str = str.substring(i + str1.length());
/*      */                 continue;
/*      */               } 
/*      */               bool = false;
/*      */               continue;
/*      */             } 
/*      */             if (b == 2) {
/*      */               int i = str.indexOf(str1);
/*      */               if (i == 1) {
/*      */                 str = str.substring(1 + str1.length());
/*      */                 continue;
/*      */               } 
/*      */               bool = false;
/*      */             } 
/*      */           } 
/*      */           if (bool && str.equals(""))
/*      */             break; 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     return (bool && str.equals(""));
/*      */   }
/*      */   
/*      */   private String matchesOpenWildcard(String paramString) {
/*      */     Enumeration<String> enumeration = this.openEndedKeywords.elements();
/*      */     String str = "";
/*      */     while (enumeration.hasMoreElements()) {
/*      */       byte b = 3;
/*      */       boolean bool = true;
/*      */       String str1 = paramString;
/*      */       StringTokenizer stringTokenizer = new StringTokenizer(enumeration.nextElement(), "*+.", true);
/*      */       while (stringTokenizer.hasMoreTokens() && bool) {
/*      */         String str2 = stringTokenizer.nextToken();
/*      */         if (str2.equals("*")) {
/*      */           b = 0;
/*      */           continue;
/*      */         } 
/*      */         if (str2.equals("+")) {
/*      */           b = 1;
/*      */           continue;
/*      */         } 
/*      */         if (str2.equals(".")) {
/*      */           b = 2;
/*      */           continue;
/*      */         } 
/*      */         if (b == 0) {
/*      */           b = 3;
/*      */           int i = str1.lastIndexOf(str2);
/*      */           if (i >= 0) {
/*      */             str1 = blankOutMatch(str1, i, str2.length());
/*      */             continue;
/*      */           } 
/*      */           bool = false;
/*      */           continue;
/*      */         } 
/*      */         if (b == 1) {
/*      */           b = 3;
/*      */           int i = str1.lastIndexOf(str2);
/*      */           if (i > 0) {
/*      */             str1 = blankOutMatch(str1, i, str2.length());
/*      */             continue;
/*      */           } 
/*      */           bool = false;
/*      */           continue;
/*      */         } 
/*      */         if (b == 2) {
/*      */           b = 3;
/*      */           int i = str1.lastIndexOf(str2);
/*      */           if (i == 1) {
/*      */             str1 = blankOutMatch(str1, 1, str2.length());
/*      */             continue;
/*      */           } 
/*      */           bool = false;
/*      */           continue;
/*      */         } 
/*      */         if (b == 3) {
/*      */           if (str1.startsWith(str2)) {
/*      */             str1 = blankOutMatch(str1, 0, str2.length());
/*      */             continue;
/*      */           } 
/*      */           bool = false;
/*      */         } 
/*      */       } 
/*      */       if (bool)
/*      */         if (b != 0)
/*      */           if (b != 1 || str1.lastIndexOf(' ') == str1.length() - 1)
/*      */             if (b != 2 || str1.lastIndexOf(' ') != str1.length() - 2)
/*      */               if (b != 3 || str1.lastIndexOf(' ') != str1.length() - 1)
/*      */                 bool = false;     
/*      */       if (bool) {
/*      */         str = str + "_" + matchesOpenWildcard(str1.trim());
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     return str;
/*      */   }
/*      */   
/*      */   private String blankOutMatch(String paramString, int paramInt1, int paramInt2) {
/*      */     char[] arrayOfChar = new char[paramInt2];
/*      */     for (byte b = 0; b < paramInt2; b++)
/*      */       arrayOfChar[b] = ' '; 
/*      */     return paramString.substring(0, paramInt1) + new String(arrayOfChar) + paramString.substring(paramInt1 + paramInt2);
/*      */   }
/*      */   
/*      */   private String getIdentifier(String paramString) {
/*      */     if (this.keywords.contains(paramString)) {
/*      */       paramString = '_' + paramString;
/*      */     } else {
/*      */       String str = "";
/*      */       if (matchesClosedWildKeyword(paramString)) {
/*      */         str = "_";
/*      */       } else {
/*      */         str = matchesOpenWildcard(paramString);
/*      */       } 
/*      */       paramString = str + paramString;
/*      */     } 
/*      */     return paramString;
/*      */   }
/*      */   
/*      */   private Token getDirective() throws IOException {
/*      */     readChar();
/*      */     String str = new String();
/*      */     while ((this.data.ch >= 'a' && this.data.ch <= 'z') || (this.data.ch >= 'A' && this.data.ch <= 'Z')) {
/*      */       str = str + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     unread(this.data.ch);
/*      */     for (byte b = 0; b < Token.Directives.length; b++) {
/*      */       if (str.equals(Token.Directives[b]))
/*      */         return new Token(300 + b); 
/*      */     } 
/*      */     return new Token(313, str);
/*      */   }
/*      */   
/*      */   private Token getNumber() throws IOException {
/*      */     if (this.data.ch == '.')
/*      */       return getFractionNoInteger(); 
/*      */     if (this.data.ch == '0')
/*      */       return isItHex(); 
/*      */     return getInteger();
/*      */   }
/*      */   
/*      */   private Token getFractionNoInteger() throws IOException {
/*      */     readChar();
/*      */     if (this.data.ch >= '0' && this.data.ch <= '9')
/*      */       return getFraction("."); 
/*      */     return new Token(127);
/*      */   }
/*      */   
/*      */   private Token getFraction(String paramString) throws IOException {
/*      */     while (this.data.ch >= '0' && this.data.ch <= '9') {
/*      */       paramString = paramString + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     if (this.data.ch == 'e' || this.data.ch == 'E')
/*      */       return getExponent(paramString + 'E'); 
/*      */     return new Token(203, paramString);
/*      */   }
/*      */   
/*      */   private Token getExponent(String paramString) throws IOException {
/*      */     readChar();
/*      */     if (this.data.ch == '+' || this.data.ch == '-') {
/*      */       paramString = paramString + this.data.ch;
/*      */       readChar();
/*      */     } else if (this.data.ch < '0' || this.data.ch > '9') {
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */     } 
/*      */     while (this.data.ch >= '0' && this.data.ch <= '9') {
/*      */       paramString = paramString + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     return new Token(203, paramString);
/*      */   }
/*      */   
/*      */   private Token isItHex() throws IOException {
/*      */     readChar();
/*      */     if (this.data.ch == '.') {
/*      */       readChar();
/*      */       return getFraction("0.");
/*      */     } 
/*      */     if (this.data.ch == 'x' || this.data.ch == 'X')
/*      */       return getHexNumber("0x"); 
/*      */     if (this.data.ch == '8' || this.data.ch == '9')
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch); 
/*      */     if (this.data.ch >= '0' && this.data.ch <= '7')
/*      */       return getOctalNumber(); 
/*      */     if (this.data.ch == 'e' || this.data.ch == 'E')
/*      */       return getExponent("0E"); 
/*      */     return new Token(202, "0");
/*      */   }
/*      */   
/*      */   private Token getOctalNumber() throws IOException {
/*      */     String str = "0" + this.data.ch;
/*      */     readChar();
/*      */     while (this.data.ch >= '0' && this.data.ch <= '9') {
/*      */       if (this.data.ch == '8' || this.data.ch == '9')
/*      */         throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch); 
/*      */       str = str + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     return new Token(202, str);
/*      */   }
/*      */   
/*      */   private Token getHexNumber(String paramString) throws IOException {
/*      */     readChar();
/*      */     if ((this.data.ch < '0' || this.data.ch > '9') && (this.data.ch < 'a' || this.data.ch > 'f') && (this.data.ch < 'A' || this.data.ch > 'F'))
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch); 
/*      */     while ((this.data.ch >= '0' && this.data.ch <= '9') || (this.data.ch >= 'a' && this.data.ch <= 'f') || (this.data.ch >= 'A' && this.data.ch <= 'F')) {
/*      */       paramString = paramString + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     return new Token(202, paramString);
/*      */   }
/*      */   
/*      */   private int getNDigitHexNumber(int paramInt) throws IOException {
/*      */     readChar();
/*      */     if (!isHexChar(this.data.ch))
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch); 
/*      */     String str = "" + this.data.ch;
/*      */     readChar();
/*      */     for (byte b = 2; b <= paramInt; b++) {
/*      */       if (!isHexChar(this.data.ch))
/*      */         break; 
/*      */       str = str + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     try {
/*      */       return Integer.parseInt(str, 16);
/*      */     } catch (NumberFormatException numberFormatException) {
/*      */       return 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isHexChar(char paramChar) {
/*      */     return ((this.data.ch >= '0' && this.data.ch <= '9') || (this.data.ch >= 'a' && this.data.ch <= 'f') || (this.data.ch >= 'A' && this.data.ch <= 'F'));
/*      */   }
/*      */   
/*      */   private int get3DigitOctalNumber() throws IOException {
/*      */     char c = this.data.ch;
/*      */     String str = "" + this.data.ch;
/*      */     readChar();
/*      */     if (this.data.ch >= '0' && this.data.ch <= '7') {
/*      */       str = str + this.data.ch;
/*      */       readChar();
/*      */       if (this.data.ch >= '0' && this.data.ch <= '7') {
/*      */         str = str + this.data.ch;
/*      */         if (c > '3')
/*      */           throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), c); 
/*      */         readChar();
/*      */       } 
/*      */     } 
/*      */     int i = 0;
/*      */     try {
/*      */       i = Integer.parseInt(str, 8);
/*      */     } catch (NumberFormatException numberFormatException) {
/*      */       throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), str.charAt(0));
/*      */     } 
/*      */     return i;
/*      */   }
/*      */   
/*      */   private Token getInteger() throws IOException {
/*      */     String str = "" + this.data.ch;
/*      */     readChar();
/*      */     if (this.data.ch == '.') {
/*      */       readChar();
/*      */       return getFraction(str + '.');
/*      */     } 
/*      */     if (this.data.ch == 'e' || this.data.ch == 'E')
/*      */       return getExponent(str + 'E'); 
/*      */     if (this.data.ch >= '0' && this.data.ch <= '9')
/*      */       while (this.data.ch >= '0' && this.data.ch <= '9') {
/*      */         str = str + this.data.ch;
/*      */         readChar();
/*      */         if (this.data.ch == '.') {
/*      */           readChar();
/*      */           return getFraction(str + '.');
/*      */         } 
/*      */       }  
/*      */     return new Token(202, str);
/*      */   }
/*      */   
/*      */   private Token replaceTrigraph() throws IOException {
/*      */     readChar();
/*      */     if (this.data.ch == '?') {
/*      */       readChar();
/*      */       if (this.data.ch == '=') {
/*      */         this.data.ch = '#';
/*      */       } else if (this.data.ch == '/') {
/*      */         this.data.ch = '\\';
/*      */       } else if (this.data.ch == '\'') {
/*      */         this.data.ch = '^';
/*      */       } else if (this.data.ch == '(') {
/*      */         this.data.ch = '[';
/*      */       } else if (this.data.ch == ')') {
/*      */         this.data.ch = ']';
/*      */       } else if (this.data.ch == '!') {
/*      */         this.data.ch = '|';
/*      */       } else if (this.data.ch == '<') {
/*      */         this.data.ch = '{';
/*      */       } else if (this.data.ch == '>') {
/*      */         this.data.ch = '}';
/*      */       } else if (this.data.ch == '-') {
/*      */         this.data.ch = '~';
/*      */       } else {
/*      */         unread(this.data.ch);
/*      */         unread('?');
/*      */         throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */       } 
/*      */       return getToken();
/*      */     } 
/*      */     unread('?');
/*      */     throw new InvalidCharacter(this.data.filename, currentLine(), currentLineNumber(), currentLinePosition(), this.data.ch);
/*      */   }
/*      */   
/*      */   void skipWhiteSpace() throws IOException {
/*      */     while (this.data.ch <= ' ')
/*      */       readChar(); 
/*      */   }
/*      */   
/*      */   private void skipBlockComment() throws IOException {
/*      */     try {
/*      */       boolean bool = false;
/*      */       readChar();
/*      */       while (!bool) {
/*      */         while (this.data.ch != '*')
/*      */           readChar(); 
/*      */         readChar();
/*      */         if (this.data.ch == '/')
/*      */           bool = true; 
/*      */       } 
/*      */     } catch (EOFException eOFException) {
/*      */       ParseException.unclosedComment(this.data.filename);
/*      */       throw eOFException;
/*      */     } 
/*      */   }
/*      */   
/*      */   void skipLineComment() throws IOException {
/*      */     while (this.data.ch != '\n')
/*      */       readChar(); 
/*      */   }
/*      */   
/*      */   private String getLineComment() throws IOException {
/*      */     StringBuffer stringBuffer = new StringBuffer("/");
/*      */     while (this.data.ch != '\n') {
/*      */       if (this.data.ch != '\r')
/*      */         stringBuffer.append(this.data.ch); 
/*      */       readChar();
/*      */     } 
/*      */     return stringBuffer.toString();
/*      */   }
/*      */   
/*      */   private String getBlockComment() throws IOException {
/*      */     StringBuffer stringBuffer = new StringBuffer("/*");
/*      */     try {
/*      */       boolean bool = false;
/*      */       readChar();
/*      */       stringBuffer.append(this.data.ch);
/*      */       while (!bool) {
/*      */         while (this.data.ch != '*') {
/*      */           readChar();
/*      */           stringBuffer.append(this.data.ch);
/*      */         } 
/*      */         readChar();
/*      */         stringBuffer.append(this.data.ch);
/*      */         if (this.data.ch == '/')
/*      */           bool = true; 
/*      */       } 
/*      */     } catch (EOFException eOFException) {
/*      */       ParseException.unclosedComment(this.data.filename);
/*      */       throw eOFException;
/*      */     } 
/*      */     return stringBuffer.toString();
/*      */   }
/*      */   
/*      */   Token skipUntil(char paramChar) throws IOException {
/*      */     while (this.data.ch != paramChar) {
/*      */       if (this.data.ch == '/') {
/*      */         readChar();
/*      */         if (this.data.ch == '/') {
/*      */           skipLineComment();
/*      */           if (paramChar == '\n')
/*      */             break; 
/*      */           continue;
/*      */         } 
/*      */         if (this.data.ch == '*')
/*      */           skipBlockComment(); 
/*      */         continue;
/*      */       } 
/*      */       readChar();
/*      */     } 
/*      */     return getToken();
/*      */   }
/*      */   
/*      */   String getUntil(char paramChar) throws IOException {
/*      */     return getUntil(paramChar, true, true, true);
/*      */   }
/*      */   
/*      */   String getUntil(char paramChar, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*      */     String str = "";
/*      */     while (this.data.ch != paramChar)
/*      */       str = appendToString(str, paramBoolean1, paramBoolean2, paramBoolean3); 
/*      */     return str;
/*      */   }
/*      */   
/*      */   String getUntil(char paramChar1, char paramChar2) throws IOException {
/*      */     String str = "";
/*      */     while (this.data.ch != paramChar1 && this.data.ch != paramChar2)
/*      */       str = appendToString(str, false, false, false); 
/*      */     return str;
/*      */   }
/*      */   
/*      */   private String appendToString(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws IOException {
/*      */     if (paramBoolean3 && this.data.ch == '/') {
/*      */       readChar();
/*      */       if (this.data.ch == '/') {
/*      */         skipLineComment();
/*      */       } else if (this.data.ch == '*') {
/*      */         skipBlockComment();
/*      */       } else {
/*      */         paramString = paramString + '/';
/*      */       } 
/*      */     } else if (this.data.ch == '\\') {
/*      */       readChar();
/*      */       if (this.data.ch == '\n') {
/*      */         readChar();
/*      */       } else if (this.data.ch == '\r') {
/*      */         readChar();
/*      */         if (this.data.ch == '\n')
/*      */           readChar(); 
/*      */       } else {
/*      */         paramString = paramString + '\\' + this.data.ch;
/*      */         readChar();
/*      */       } 
/*      */     } else {
/*      */       if (paramBoolean2 && this.data.ch == '"') {
/*      */         readChar();
/*      */         paramString = paramString + '"';
/*      */         while (this.data.ch != '"')
/*      */           paramString = appendToString(paramString, true, false, paramBoolean3); 
/*      */       } else if (paramBoolean1 && paramBoolean2 && this.data.ch == '(') {
/*      */         readChar();
/*      */         paramString = paramString + '(';
/*      */         while (this.data.ch != ')')
/*      */           paramString = appendToString(paramString, false, false, paramBoolean3); 
/*      */       } else if (paramBoolean1 && this.data.ch == '\'') {
/*      */         readChar();
/*      */         paramString = paramString + "'";
/*      */         while (this.data.ch != '\'')
/*      */           paramString = appendToString(paramString, false, true, paramBoolean3); 
/*      */       } 
/*      */       paramString = paramString + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     return paramString;
/*      */   }
/*      */   
/*      */   String getStringToEOL() throws IOException {
/*      */     String str = new String();
/*      */     while (this.data.ch != '\n') {
/*      */       if (this.data.ch == '\\') {
/*      */         readChar();
/*      */         if (this.data.ch == '\n') {
/*      */           readChar();
/*      */           continue;
/*      */         } 
/*      */         if (this.data.ch == '\r') {
/*      */           readChar();
/*      */           if (this.data.ch == '\n')
/*      */             readChar(); 
/*      */           continue;
/*      */         } 
/*      */         str = str + this.data.ch;
/*      */         readChar();
/*      */         continue;
/*      */       } 
/*      */       str = str + this.data.ch;
/*      */       readChar();
/*      */     } 
/*      */     return str;
/*      */   }
/*      */   
/*      */   String filename() {
/*      */     return this.data.filename;
/*      */   }
/*      */   
/*      */   IncludeEntry fileEntry() {
/*      */     return this.data.fileEntry;
/*      */   }
/*      */   
/*      */   int currentLineNumber() {
/*      */     return this.data.line;
/*      */   }
/*      */   
/*      */   int lastTokenLineNumber() {
/*      */     return this.data.oldLine;
/*      */   }
/*      */   
/*      */   String currentLine() {
/*      */     this.BOL = this.data.fileIndex - 1;
/*      */     try {
/*      */       if (this.data.fileBytes[this.BOL - 1] == '\r' && this.data.fileBytes[this.BOL] == '\n') {
/*      */         this.BOL -= 2;
/*      */       } else if (this.data.fileBytes[this.BOL] == '\n') {
/*      */         this.BOL--;
/*      */       } 
/*      */       while (this.data.fileBytes[this.BOL] != '\n')
/*      */         this.BOL--; 
/*      */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*      */       this.BOL = -1;
/*      */     } 
/*      */     this.BOL++;
/*      */     int i = this.data.fileIndex - 1;
/*      */     try {
/*      */       while (this.data.fileBytes[i] != '\n' && this.data.fileBytes[i] != '\r')
/*      */         i++; 
/*      */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*      */       i = this.data.fileBytes.length;
/*      */     } 
/*      */     if (this.BOL < i)
/*      */       return new String(this.data.fileBytes, this.BOL, i - this.BOL); 
/*      */     return "";
/*      */   }
/*      */   
/*      */   String lastTokenLine() {
/*      */     int i = this.data.fileIndex;
/*      */     this.data.fileIndex = this.data.oldIndex;
/*      */     String str = currentLine();
/*      */     this.data.fileIndex = i;
/*      */     return str;
/*      */   }
/*      */   
/*      */   int currentLinePosition() {
/*      */     return this.data.fileIndex - this.BOL;
/*      */   }
/*      */   
/*      */   int lastTokenLinePosition() {
/*      */     return this.data.oldIndex - this.BOL;
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\Scanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */