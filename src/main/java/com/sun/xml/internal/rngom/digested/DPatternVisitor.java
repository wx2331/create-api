package com.sun.xml.internal.rngom.digested;

public interface DPatternVisitor<V> {
  V onAttribute(DAttributePattern paramDAttributePattern);
  
  V onChoice(DChoicePattern paramDChoicePattern);
  
  V onData(DDataPattern paramDDataPattern);
  
  V onElement(DElementPattern paramDElementPattern);
  
  V onEmpty(DEmptyPattern paramDEmptyPattern);
  
  V onGrammar(DGrammarPattern paramDGrammarPattern);
  
  V onGroup(DGroupPattern paramDGroupPattern);
  
  V onInterleave(DInterleavePattern paramDInterleavePattern);
  
  V onList(DListPattern paramDListPattern);
  
  V onMixed(DMixedPattern paramDMixedPattern);
  
  V onNotAllowed(DNotAllowedPattern paramDNotAllowedPattern);
  
  V onOneOrMore(DOneOrMorePattern paramDOneOrMorePattern);
  
  V onOptional(DOptionalPattern paramDOptionalPattern);
  
  V onRef(DRefPattern paramDRefPattern);
  
  V onText(DTextPattern paramDTextPattern);
  
  V onValue(DValuePattern paramDValuePattern);
  
  V onZeroOrMore(DZeroOrMorePattern paramDZeroOrMorePattern);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DPatternVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */