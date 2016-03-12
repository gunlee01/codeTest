package scouter.engineer.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 3. 12.
 */
public class RandomAccessFileMultiThread {
    String filePathName = "./testRandomAccessFile.txt";

    public RandomAccessFile openR() throws FileNotFoundException {
        return new RandomAccessFile(filePathName, "r");
    }

    public RandomAccessFile openRW() throws FileNotFoundException {
        return new RandomAccessFile(filePathName, "rw");
    }

    class TestReader extends Thread {
        RandomAccessFile f;
        TestReader(RandomAccessFile f) {
            this.f = f;
        }
        public void run() {
            System.out.println("I'm thread");
            try {
                Thread.sleep(1000);
                f.seek(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        RandomAccessFileMultiThread main = new RandomAccessFileMultiThread();

        //main.doCase0();
        //main.doCase1();
        main.doCase2();
    }

    public void doCase0() {
        System.out.println("[start case0]");
        try {
            RandomAccessFile frw = openRW();

            frw.seek(1);
            frw.writeByte('!');
            frw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[end]");
        }
    }

    public void doCase1() {
        System.out.println("[start case1]");
        try {
            RandomAccessFile frw = openRW();
            new TestReader(frw).start();

            frw.seek(1);
            frw.writeByte('!');
            frw.seek(10);
            Thread.sleep(2000);
            frw.writeByte('$');

            frw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[end]");
        }
    }

    public void doCase2() {
        System.out.println("[start case2]");
        try {
            RandomAccessFile frw = openRW();
            RandomAccessFile fr = openR();
            new TestReader(fr).start();

            frw.seek(1);
            frw.writeByte('!');
            frw.seek(10);
            Thread.sleep(2000);
            frw.writeByte('$');

            frw.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("[end]");
        }
    }
}
