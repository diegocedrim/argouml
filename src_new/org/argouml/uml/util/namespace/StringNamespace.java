// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.util.namespace;

import java.util.Iterator;

/**
 * A StringNamespace is a string based namespace (StringNamespaceElement)
 * object. It faciliates creation of these objects via a number of helper
 * methods.
 * 
 * @author mkl
 *  
 */
public class StringNamespace implements Namespace, Cloneable
{

    private java.util.Stack ns = new java.util.Stack();

    private String token = JAVA_NS_TOKEN;

    /**
     * empty namespace with java token by default.
     *  
     */
    public StringNamespace()
    {
    }

    /**
     * empty namespace with given default token
     * 
     * @param token
     *            the scope seperator to use
     */
    public StringNamespace(String token)
    {
        this();
        this.token = token;
    }

    /**
     * construct a namespace from an array of strings with default scope token.
     * 
     * @param elements
     *            an array of strings which represent the namespace.
     */
    public StringNamespace(String[] elements)
    {
        this(elements, JAVA_NS_TOKEN);
    }

    /**
     * construct a namespace from strings with given scope token.
     * 
     * @param elements
     *            an array of strings which represent the namespace.
     * @param token
     *            scope token to use
     */
    public StringNamespace(String[] elements, String token)
    {
        this(token);
        for (int i = 0; i < elements.length; i++)
        {
            pushNamespaceElement(new StringNamespaceElement(elements[i]));
        }
    }

    /**
     * construct a namespace from NamespaceElements with given scope token
     * 
     * @param elements
     *            array of NamespaceElements
     * @param token
     *            scope token
     */
    public StringNamespace(NamespaceElement[] elements, String token)
    {
        this(token);
        for (int i = 0; i < elements.length; i++)
        {
            pushNamespaceElement(
                new StringNamespaceElement(elements[i].toString()));
        }
    }

    /**
     * construct a namespace from NamespaceElements with default scope token
     * 
     * @param elements
     *            array of NamespaceElements
     */
    public StringNamespace(NamespaceElement[] elements)
    {
        this(elements, JAVA_NS_TOKEN);
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#addNamespaceElement(org.argouml.model.util.namespace.NamespaceElement)
     */
    public void pushNamespaceElement(NamespaceElement element)
    {
        ns.push(element);
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#peekNamespaceElement()
     */
    public NamespaceElement peekNamespaceElement()
    {
        return (NamespaceElement) ns.peek();
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#popNamespaceElement()
     */
    public void pushNamespaceElement(String element)
    {
        ns.push(new StringNamespaceElement(element));
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#popNamespaceElement()
     */
    public NamespaceElement popNamespaceElement()
    {
        return (NamespaceElement) ns.pop();
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#getBaseNamespace()
     */
    public Namespace getBaseNamespace()
    {
        StringNamespace result = null;
        try
        {
            result = (StringNamespace) this.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
            return null;
        }
        result.popNamespaceElement();
        return result;
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#getCommonNamespace(org.argouml.model.util.namespace.Namespace)
     */
    public Namespace getCommonNamespace(Namespace namespace)
    {
        Iterator i = iterator();
        Iterator j = namespace.iterator();
        StringNamespace result = new StringNamespace(token);

        for (; i.hasNext() && j.hasNext();)
        {
            NamespaceElement elem1 = (NamespaceElement) i.next();
            NamespaceElement elem2 = (NamespaceElement) j.next();
            if (elem1.toString().equals(elem2.toString()))
                result.pushNamespaceElement(elem1);
        };
        return result;
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#iterator()
     */
    public Iterator iterator()
    {
        return ns.iterator();
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#isEmpty()
     */
    public boolean isEmpty()
    {
        return ns.isEmpty();
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#setDefaultScopeToken(java.lang.String)
     */
    public void setDefaultScopeToken(String token)
    {
        this.token = token;
    }

    /**
     * parse a fully qualified namespace to create a Namespace object.
     * 
     * @param fqn
     *            string representation of namespace
     * @param token
     *            the token of the namespace
     * @return a namespace object
     */
    public static Namespace parse(String fqn, String token)
    {

        String myFqn = fqn;
        StringNamespace sns = new StringNamespace(token);
        int i = myFqn.indexOf(token);
        while (i > -1)
        {
            sns.pushNamespaceElement(myFqn.substring(0, i));
            myFqn = myFqn.substring(i + token.length());
            i = myFqn.indexOf(token);
        }
        if (myFqn.length() > 0)
            sns.pushNamespaceElement(myFqn);
        return (Namespace) sns;
    }

    /**
     * parse the name of a (java) class.
     * 
     * @param c
     *            the class
     * @return the namespace object
     */
    public static Namespace parse(Class c)
    {
        return parse(c.getName(), JAVA_NS_TOKEN);
    }

    /**
     * two namespaces are equal when they are namespaces and have the same
     * string representation
     * 
     * @param ns
     *            the namespace to compare with
     */
    public boolean equals(Object ns)
    {
        if (ns instanceof Namespace)
        {
            String ns1 = this.toString(JAVA_NS_TOKEN);
            String ns2 = ((Namespace) ns).toString(JAVA_NS_TOKEN);
            return ns1.equals(ns2);
        }
        return false;
    }

    /**
     * @see org.argouml.uml.util.namespace.Namespace#toString(java.lang.String)
     */
    public String toString(String token)
    {
        StringBuffer result = new StringBuffer();
        Iterator i = ns.iterator();
        while (i.hasNext())
        {
            result.append((NamespaceElement) i.next());
            if (i.hasNext())
                result.append(token);
        }
        return result.toString();
    }

    /**
     * create a string representation using the default scope token.
     *  
     */
    public String toString()
    {
        return toString(token);
    }

}