package com.xceptance.xlt.common.util;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * {@link NodeList} implementation. <br>
 * I don't remember why exactly i implemented it this way, but it is simple and works.
 * 
 * @author matthias mitterreiter
 */
public class ConcreteNodeList implements NodeList
{
    private final ArrayList<Node> list;

    public ConcreteNodeList()
    {
        list = new ArrayList<Node>();
    }

    @Override
    public Node item(final int index)
    {
        return list.get(index);
    }

    @Override
    public int getLength()
    {
        return list.size();
    }

    public void add(final Node n)
    {
        list.add(n);
    }

    public void remove(final Node n)
    {
        list.remove(n);
    }

    public void remove(final int index)
    {
        list.remove(index);
    }
}
