/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d4death.dcore.list;

import fr.d4death.dcore.Plug;
import fr.d4death.dcore.Registerer;
import java.util.Iterator;

/**
 *
 * @author D4Death
 */
public class PlugList<E extends Plug> extends Registerer<E> implements Iterable<E> {
    
    Node first;
    Node last;
    
    public PlugList(Class type) {
        super(type);
    }

    @Override
    public void register(E plug) {
        
        if(first == null) {
            first = new Node(plug);
            return;
        }
        
        if(last == null) {
            last = new Node(plug);
            first.after = last;
            return;
        }
        
        Node local = new Node(plug);
        last.after = local;
        last = local;
    }
    
    
    /*
    @Override
    public Iterator<E> iterator() {
        return new Iterator() {

            @Override
            public boolean hasNext() {
                return lastPolled != null;
            }

            @Override
            public E next() {
                try {
                    return lastPolled == null ? null : lastPolled.plug == null ? next() : lastPolled.plug;
                } finally {
                    remove();
                }
            }

            @Override
            public void remove() {
                if(lastPolled != null) {
                    lastPolled = lastPolled.after;
                } else {
                    lastPolled = first;
                }
            }
            
        };
    }*/
    
    
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node lastPolled = first;
            
            @Override
            public boolean hasNext() {
                return lastPolled != null;
            }

            @Override
            public E next() {
                try {
                    return first != null ? first.plug : null;
                } finally {
                    remove();
                }
            }

            @Override
            public void remove() {
                first = first == null ? null : first.after == null ? null : first.after;
            }
            
        };
    }
    public Iterator<E> getCircularIterator() {
        return new Iterator() {
            
            Node lastPolled = first; 
            
            @Override
            public boolean hasNext() {
                
                return true;
            }

            @Override
            public E next() {
                try {
                    return lastPolled == null ? first.plug : lastPolled.plug;
                } finally {
                    remove();
                }
                
            }

            @Override
            public void remove() {
                if(lastPolled != null) {
                    lastPolled = lastPolled.after;
                } else {
                    lastPolled = first.after;
                }
            }
            
        };
    }
    protected class Node {
        public Node after;
        public E plug;
        
        public Node() {}
        
        public Node(Node after, E plug) {
            this.after = after;
            this.plug = plug;
        }
        
        public Node(Node after) {
            this(after, null);
        }
        
        public Node(E plug) {
            this(null, plug);
        }
        
    }
    
}
