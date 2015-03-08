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
 * @param <E>
 */
public class ThreeSectionsPlugList<E extends Plug> extends Registerer<E> implements Iterable<E> {

    public ThreeSectionsPlugList(Class type) {
        super(type);
        first.insertBefore(second);
        third.insertAfter(second);
    }
    
    public Node first = new Node();
    public Node second = new Node();
    public Node third = new Node();
    
    public void addToTheStart(E plug) {
        if(plug == null) {return;}
        if(first.plug == null) {
            first.plug = plug;
        } else {
            first.insertAfter(new Node(plug));
        }
    }
    
    public void add(E plug) {
        if(plug == null) {return;}
        if(second.plug == null) {
            second.plug = plug;
        } else {
            second.insertAfter(new Node(plug));
        }
    }
    
    public void addToTheEnd(E plug) {
        if(plug == null) {return;}
        if(third.plug == null) {
            third.plug = plug;
        } else {
            third.insertAfter(new Node(plug));
        }
    }
    
    public boolean isEmpty() {
        return !(first.plug != null || second.plug != null || third.plug != null);
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node lastPolled = first;
            
            @Override
            public E next() {
                try {
                    return get();
                } finally {
                    remove();
                }
            }
            
            @Override
            public boolean hasNext() {
                //return lastPolled == null || !isEmpty() ;
                if (lastPolled != null) {
                    if(isEmpty()) {
                        return false;
                    }
                    if(lastPolled == second && second.plug == null &&  third.plug == null) {
                        return false;
                    }
                    if(lastPolled == third && third.plug == null) {
                        return false;
                    }
                    
                    return true;
                } else {
                    return false;
                }
                
            }
            
            @Override
            public void remove() {
                lastPolled = lastPolled == null ? null: lastPolled.after;
            }
            
            private E getNext() {
                remove();
                return get();
            }
            
            private E get() {
                return lastPolled == null ? null : lastPolled.plug == null ? getNext() : lastPolled.plug;
            }
            
        };
    }
    
    public Iterator<E> getCircularIterator() {
        return new Iterator() {
            
            Node lastPolled = first; 
            
            @Override
            public boolean hasNext() {
                return first != null;
            }

            @Override
            public void remove() {
                lastPolled = lastPolled == null ? first.after : lastPolled.after;
            }
            
            private E firstAndGet() {
                lastPolled = first;
                return get();
            }
            
            private E getNext() {
                remove();
                return get();        
            }
            
            private E get() {
                return lastPolled == null ? firstAndGet() : lastPolled.plug == null ? getNext() : lastPolled.plug;
            }
            
            @Override
            public E next() {
                try {
                    return get();
                } finally {
                    remove();
                }
            }
        };
    }

    @Override
    public void register(E e) {
        add(e);
    }
    
    protected class Node {
        Node before;
        E plug;
        Node after;
        public Node(Node before, E plug, Node after) {
            this.before = before;
            this.plug = plug;
            this.after = after;
        }
        public Node(E plug) {
            this.plug = plug;
        }
        public Node() {}
        public void insertAfter(Node node) {
            if(node == null) {
                return;
            }
            
            this.before = node;
            node.after = this;
            
            if(after != null) {
                this.after = node.after;
                node.after.before = this;
            }
        }
        public void insertBefore(Node node) {
            
            if(node == null) {
                return;
            }
            
            this.after = node;
            node.before = this;
            
            if(before != null) {
                this.before = node.before;
                node.before.after = this;
            }
        }
        public void insertBetween(Node first, Node second) {
            if(first.after == second) {
                insertAfter(first);
                return;
            }
            if(second.after == first) {
                insertAfter(second);
            }
        }
    }
    private boolean isInit(Node node) {
        return node != null ? node.plug != null : false;
    }
}
 