// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.Name;

// needs-more-work

public class Link extends ModelElementImpl {

  public Vector _linkRole = new Vector();
  public Association _association = null;
  
  public Link() { }
  public Link(Name name) { super(name); }
  public Link(String nameStr) { super(new Name(nameStr)); }
  public Link(String nameStr, LinkEnd from, LinkEnd to){
    super(new Name(nameStr));
    try {
      addLinkRole(from);
      addLinkRole(to);
    }
    catch (PropertyVetoException pce) { }
  }
  public Link(Instance  srcC, Instance dstC) {
    super();
    try {
      LinkEnd src = new LinkEnd(srcC);
      LinkEnd dst = new LinkEnd(dstC);
      addLinkRole(src);
      addLinkRole(dst);
      //setNamespace(srcC.getNamespace());
    }
    catch (PropertyVetoException pce) { }
  }
 
  public void addLinkRole(LinkEnd x) throws PropertyVetoException {
    if (_linkRole == null) _linkRole = new Vector();
    fireVetoableChange("linkRole", _linkRole, x);
    _linkRole.addElement(x);
    x.setLink(this);
  }
  
  public Vector getLinkRole() { return _linkRole; }
  
  public void removeLinkRole(LinkEnd x) throws PropertyVetoException {
    if (_linkRole == null) return;
    fireVetoableChange("linkRole", _linkRole, x);
    _linkRole.removeElement(x);
  }
  
  public void setLinkRole(Vector x) throws PropertyVetoException {
    if (_linkRole == null) _linkRole = new Vector();
    fireVetoableChange("linkRole", _linkRole, x);
    _linkRole = x;
    java.util.Enumeration enum = _linkRole.elements();
    while (enum.hasMoreElements()) {
      LinkEnd le = (LinkEnd) enum.nextElement();
      le.setLink(this);
    }
  }
  
  
}
