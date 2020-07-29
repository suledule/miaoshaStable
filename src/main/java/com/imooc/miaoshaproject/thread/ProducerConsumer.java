package com.imooc.miaoshaproject.thread;

/**
 * 生产者消费者synchronized版
 */
public class ProducerConsumer {
    public static void main(String[] args) {
        Data data = new Data();
        //生产者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.increment();
            }
        },"A").start();

        //消费者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.decrement();
            }
        },"B").start();

        //生产者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.increment();
            }
        },"C").start();

        //消费者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.decrement();
            }
        },"D").start();
    }
}

/**
 * 判断等待，执行业务，通知其他线程
 *
 * 虚假唤醒：：：while与if语句的最大的相同点是都有至少一步的判断。
 * if 语句运行完毕后，接着运行下面的语句
 * while 语句先进行判断，再运行执行语句。执行语句运行完毕，自动返回继续判断while中的条件是否符合(不止一次判断)，符合的话，继续运行执行语句，不符合，则退出循环。
 */
class Data{
    private int num = 0;

    //生产一个
    public synchronized void increment(){
        //使用if会出现虚假唤醒
        while(num != 0){
            try {
                this.wait(); //此线程等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        num++;
        System.out.println(Thread.currentThread().getName()+"-->"+num);
        this.notifyAll(); //唤醒其他的等待线程
    }

    //消费一个
    public synchronized void decrement(){
        while(num == 0){
            try {
                this.wait(); //此线程等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        num--;
        System.out.println(Thread.currentThread().getName()+"-->"+num);
        this.notifyAll(); //唤醒其他的等待线程
    }
}
