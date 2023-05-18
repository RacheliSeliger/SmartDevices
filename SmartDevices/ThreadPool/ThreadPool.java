package net.codejava.javaee;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPool<V> implements Executor {

    private List<Thread> threadList = new ArrayList<>();
    private WaitablePQueue<Task> taskPQ = new WaitablePQueue<>();
    private boolean isShutdown = false;
    private AtomicBoolean isPaused = new AtomicBoolean(false);
    private int numberOfThreads = 0;
    private Object isPauseLock = new Object();
    private Lock threadLock = new ReentrantLock();


    /*-------------------------------------------------------------------------*/

    public ThreadPool(int numberOfThreads){
        setNumOfThreads(numberOfThreads);
    }

    /*-------------------------------------------------------------------------*/

    @Override
    public void execute(Runnable runnable) {
        submit(runnable, Priority.MEDIUM);
    }
    public Future<Void> submit(Runnable runnable, Priority priority){
        Callable<Void> callable = () -> {
            runnable.run();
            return null;
        };
        return submit(callable, priority);
    };

    public <V>Future<V> submit(Runnable runnable, Priority priority, V value){
        Callable<V> callable = () -> {
            runnable.run();
            return value;
        };
        return submit(callable, priority);
    };

    public <V>Future<V> submit(Callable<V> callable){

        return submit(callable, Priority.MEDIUM);
    };

    public <V>Future<V> submit(Callable<V> callable, Priority priority){

        if (isShutdown) {
            throw new RejectedExecutionException("Thread pool is shutdown");
        }

        Task<V> task = new Task<>(callable, priority.getValue());
        taskPQ.enqueue(task);

        return task.getFuture();

    };

    public void setNumOfThreads(int numThreads){
        int differenceNumThread = numberOfThreads - numThreads;

        if (differenceNumThread > 0) {
            Callable stopThreadTask = makeThreadStop();
            Task stopTask = new Task<>(stopThreadTask, 5);
            for (int i = 0; i < differenceNumThread; ++i) {
                taskPQ.enqueue(stopTask);
            }
        }
        else {
            differenceNumThread = -differenceNumThread;

            for(int i = 0; i < differenceNumThread; ++i)
            {
                WorkerThread newWorker = new WorkerThread();
                threadList.add(newWorker);
                newWorker.start();
            }
        }
        this.numberOfThreads = numThreads;
    };

    public void pause(){
        isPaused.set(true);
    };

    public void resume(){

        if (!isPaused.get()){
            throw new IllegalArgumentException("thread pool is not paused");
        }

        isPaused.set(false);
        synchronized (isPauseLock){
            this.isPauseLock.notifyAll();
        }
    };

    public void shutdown(){

        if (isPaused.get()){
            resume();
        }

        isShutdown = true;
        Callable stopThreadTask = makeThreadStop();
        Task shutdownTask = new Task<>(stopThreadTask, 0);

        for (int i = 0; i < numberOfThreads; ++i){
            taskPQ.enqueue(shutdownTask);
        }

    };

    public void awaitTermination() throws InterruptedException {
        if(!isShutdown) {
            throw new RejectedExecutionException("Thread pool should be shutdown before");
        }

        for (int i = 0; i < threadList.size(); i++) {
            Thread thread = threadList.get(i);
            thread.join();
        }
    };


    /*-------------------------------------------------------------------------*/

    private class Task<V> implements Comparable<Task>{

        private Callable callable = null;
        private int priority = 0;
        private Future future = null;

        private Task(Callable callable, int priority){
            this.callable = callable;
            this.priority = priority;
            this.future = new Taskfuture(this);

        }

        @Override
        public int compareTo(Task task) {

            return  task.getPriority() - this.priority;
        }

        private int getPriority() {
            return priority;
        }

        private Future getFuture(){
            return this.future;
        };

        private Callable getCallable(){
            return callable;
        };

        private class Taskfuture implements Future<V> {

            private volatile boolean isCancelled = false;
            private volatile boolean isDone = false;
            private V result = null;
            private Semaphore resultSemaphore = new Semaphore(0);
            private Task curTask = null;

            Taskfuture(Task curTask) {
                this.curTask = curTask;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {

                if (taskPQ.remove(curTask)) {
                    isCancelled = true;
                    resultSemaphore.release();
                }
                return isCancelled;
            }

            @Override
            public boolean isCancelled() {
                return isCancelled;
            }

            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public V get() throws InterruptedException{
                resultSemaphore.acquire();

                return result;
            }

            @Override
            public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
                if (!resultSemaphore.tryAcquire(timeout, unit)) {
                    throw new TimeoutException();
                }

                return result;
            }

            private void setResultAndDone(V result){
                this.result = result;
                this.isDone = true;
                resultSemaphore.release();
            }

        }
    }

    /*-----------------------------------------------------------------------------*/

    private class WorkerThread extends Thread{
        V result = null;
        boolean isRunning = true;
        Task task = null;
        Callable callable = null;
        Future future = null;


        public void setIsRunningFlag(boolean isRunning){
            this.isRunning = isRunning;
        }
        @Override
        public void run() {
            while (isRunning)
            {
                task = taskPQ.dequeue();

                checkIsPause();

                callable = task.getCallable();
                future = task.getFuture();


                try {
                    result = (V) callable.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                ((Task.Taskfuture)future).setResultAndDone(result);


            }

            threadLock.lock();
            threadList.remove(this);
            threadLock.unlock();

        }
    }


    /*-----------------------------------------------------------------------------*/

    private void checkIsPause(){
        if (isPaused.get()){
            synchronized (isPauseLock){
                try {
                    isPauseLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Callable makeThreadStop(){

        Callable stopThreadTask = () -> {
            Thread curThread = Thread.currentThread();
            ((WorkerThread)curThread).setIsRunningFlag(false);
            return null;
        };

        return stopThreadTask;
    }
}

