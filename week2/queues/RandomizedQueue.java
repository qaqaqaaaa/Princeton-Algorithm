/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
/*
随机返回array中一个值
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int capacity = 8;
    private int index;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[capacity];
        index = 0;
    }

    // 重新改变数组大小
    private void resize(int newSize) {
        Item[] copy = (Item[]) new Object[newSize];
        for (int i = 0; i < index; i++) {
            copy[i] = array[i];
        }
        array = copy;
        this.capacity = newSize;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return index == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return index;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("cannot add null into a randomizedQueue");
        }

        //当数组full，扩大2倍
        if (index == capacity) {
            resize(capacity * 2);
        }
        array[index++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("the queue is empty");
        }

        //取数组最后一个元素替换返回的元素，并置最后一个位置为null
        int ind = StdRandom.uniform(index);
        Item val = array[ind];
        array[ind] = array[index - 1];
        array[--index] = null;

        //当数组为1/4 full,缩小2倍
        if (index > 0 && index * 4 == capacity) {
            resize(capacity / 2);
        }
        return val;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("the queue is empty");
        }
        int ind = StdRandom.uniform(index);
        Item val = array[ind];
        return val;
    }

    //Iterator 利用shuffle，每次返回不一样的序列
    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] copyArray;
        private int remain;

        public RandomizedQueueIterator() {
            copyArray = (Item[]) new Object[index];
            for (int i = 0; i < index; i++) {
                copyArray[i] = array[i];
            }
            StdRandom.shuffle(copyArray);
            remain = index;
        }

        public boolean hasNext() {
            return remain != 0;
        }

        public Item next() {
            if (remain == 0) {
                throw new NoSuchElementException();
            }
            return copyArray[--remain];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        for (int i = 0; i < 18; i++) {
            rq.enqueue("A" + i);
        }
        System.out.println("first iterator");
        for (String s : rq) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println("second iterator ");
        for (String s : rq) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (int i = 0; i < 18; i++) {
            System.out.print("deque ");
            System.out.print(rq.dequeue());
            System.out.println(". remain " + rq.size() + " elements. now capacity ");
        }

    }
}
