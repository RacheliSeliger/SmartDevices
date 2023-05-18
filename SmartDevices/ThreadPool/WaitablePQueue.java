package net.codejava.javaee;


import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class WaitablePQueue<T>  {

    private PriorityQueue<T> queue;
    private final Lock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(0);

    public WaitablePQueue(Comparator<? super T> comparator) {
        this.queue = new PriorityQueue<>(comparator);
    }

    public WaitablePQueue() {
        this.queue = new PriorityQueue<>();
    }

    public void enqueue(T element) {

        lock.lock();
        queue.add(element);
        semaphore.release();
        lock.unlock();
    }

    public T dequeue() {
        T returnValue;

        try {
            semaphore.acquire();

        }   catch (InterruptedException e) {
        throw new RuntimeException(e);
        }
        lock.lock();
        returnValue = queue.poll();
        lock.unlock();


        return returnValue;
    }


    public T dequeue(int timeOut) throws  TimeoutException {
        T return_value;
        boolean isAcquire;
        try {
            isAcquire = semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS);

            if (!isAcquire)
            {
                throw new TimeoutException();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        lock.lock();

        return_value = queue.poll();

        lock.unlock();
        return return_value;

    }


    public boolean remove(T element) {
        boolean isFound = false;

        if (semaphore.tryAcquire()) {
            lock.lock();
             isFound = queue.remove(element);
            if(!isFound){
                semaphore.release();
            }

            lock.unlock();
        }
        return isFound;
    }

    public boolean isEmpty() {

        return queue.isEmpty();
    }
}