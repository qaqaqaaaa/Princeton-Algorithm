/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
构造链表，node 双箭头pre、next，完成第一个节点以及最后一个节点的插入与删除
 */
public class Deque<Item> implements Iterable<Item> {
    private final Node guard;
    private int size;
    // construct an empty deque
    public Deque() {
        size = 0;
        guard = new Node();

        //设定guard指向自己，随后插入新节点时方便完成指向
        guard.next = guard;
        guard.pre = guard;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();
        for (int i = 0; i < 5; i++) {
            dq.addFirst("A" + i);
        }
        for (int i = 0; i < 5; i++) {
            dq.addLast("B" + i);
        }
        for (String s : dq) {
            System.out.println(s);
        }
        System.out.println("dq has " + dq.size() + " elements in total");
        for (int i = 0; i < 10; i++) {
            System.out.println(dq.removeFirst());
            System.out.println(dq.removeLast());
            System.out.println(dq.size());
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // 边界条件
        if (item == null) {
            throw new IllegalArgumentException("can not add null into the deque");
        }
        Node fisrt = new Node(item, guard.next, guard);
        guard.next.pre = fisrt;
        guard.next = fisrt;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        //边界条件
        if (item == null) {
            throw new IllegalArgumentException("can not add null into the deque");
        }
        Node last = new Node(item, guard, guard.pre);
        guard.pre.next = last;
        guard.pre = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("can not remove value from an empty deque");
        }
        Node old = guard.next;
        guard.next = guard.next.next;
        guard.next.pre = guard;
        size--;
        return old.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("can not remove value from an empty deque");
        }
        Node old = guard.pre;
        guard.pre = guard.pre.pre;
        guard.pre.next = guard;
        size--;
        return old.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // node structure
    private class Node {
        private final Item item;
        private Node next;
        private Node pre;

        private Node() {
            item = null;
            next = null;
            pre = null;
        }

        private Node(Item item, Node next, Node pre) {
            this.item = item;
            this.next = next;
            this.pre = pre;
        }
    }

    // Iterator implements Iterator 接口
    private class DequeIterator implements Iterator<Item> {

        private int remains = size;
        private Node cur = guard;

        public boolean hasNext() {
            return remains != 0;
        }

        public Item next() {
            if (remains == 0) {
                throw new NoSuchElementException();
            }
            Node old = cur.next;
            cur = cur.next;
            remains--;
            return old.item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
