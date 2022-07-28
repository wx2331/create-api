package com.sun.tools.doclets.internal.toolkit.taglets;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder;

public interface InheritableTaglet extends Taglet {
  void inherit(DocFinder.Input paramInput, DocFinder.Output paramOutput);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolkit\taglets\InheritableTaglet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */