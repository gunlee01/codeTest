package scouter.engineer.test.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 3. 12.
 */
public class RandomAccessSpeed {
    public static void main(String[] args) {
        RandomAccessSpeed main = new RandomAccessSpeed();
        //main.makeBigfile();

        if(args.length > 0 && args[0] != null) {
            switch (args[0]) {
                case "1":
                    main.case1();
                    break;
                case "2":
                    main.case2();
                    break;
                case "3":
                    main.case3();
                    break;
                case "4":
                    main.case4();
                    break;
                case "5":
                    main.case5();
                    break;
                case "6":
                    main.case6();
                    break;
            }
        }

    }

    /**
     * write 20byte buffer to the RAF.
     */
    public void case1() {
        int bufferSize = 20;
        byte[] buffer = new byte[bufferSize];
        int loopSize = 1024*1024*100/bufferSize;
        int[] pos = new int[loopSize];

        for (int i = 0; i < bufferSize; i++) {
            buffer[i] = (byte) 255;
        }

        for (int i = 0; i < loopSize; i++) {
            pos[i] = ThreadLocalRandom.current().nextInt(loopSize-bufferSize);
        }

        try {
            RandomAccessFile f = new RandomAccessFile("testRandomBig.txt", "rw");

            long start = System.currentTimeMillis();
            for (int i = 0; i < loopSize; i++) {
                f.seek(pos[i]);
                f.write(buffer);
            }

            System.out.println("[elapsed] " + (System.currentTimeMillis()-start));
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read 288*20byte buffer to the RAF.
     */
    public void case2() {
        int bufferSize = 20*288;
        byte[] buffer = new byte[bufferSize];
        int loopSize = 1024*1024*100/bufferSize;
        int[] pos = new int[loopSize];

        for (int i = 0; i < bufferSize; i++) {
            buffer[i] = (byte) 255;
        }

        for (int i = 0; i < loopSize; i++) {
            pos[i] = ThreadLocalRandom.current().nextInt(loopSize-bufferSize);
        }

        try {
            RandomAccessFile f = new RandomAccessFile("testRandomBig.txt", "r");

            long start = System.currentTimeMillis();
            for (int i = 0; i < loopSize; i++) {
                f.seek(pos[i]);
                f.read(buffer);
            }

            System.out.println("[elapsed] " + (System.currentTimeMillis()-start));
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read and write on random access file - same pointer
     * write 100m buffer 20byte
     * read 100m buffer 20byte
     */
    public void case3() {
        String fileName = "testRandomBig.txttestRandomBig.txt";

        int wBufferSize = 20;
        int rBufferSize = 20;
        long wAccessByte = 1024*1024*100L;
        long rAccessByte = 1024*1024*120L;
        int wLoopSize = (int)(wAccessByte / wBufferSize);
        int rLoopSize = (int)(rAccessByte / rBufferSize);

        System.out.println("[wloop]" + wLoopSize);
        System.out.println("[rloop]" + rLoopSize);

        FileBoy f = new FileBoy(fileName);

        new WThread(f, wBufferSize, wLoopSize).start();
        new RThread(f, rBufferSize, rLoopSize).start();
    }

    /**
     * read and write on random access file - different pointer
     * write 100m buffer 20byte
     * read 120m buffer 20byte
     * It's better than the same pointer file : 20%~30% better
     */
    public void case4() {
        String fileName = "testRandomBig.txt";

        int wBufferSize = 20;
        int rBufferSize = 20;
        long wAccessByte = 1024*1024*100L;
        long rAccessByte = 1024*1024*120L;
        int wLoopSize = (int)(wAccessByte / wBufferSize);
        int rLoopSize = (int)(rAccessByte / rBufferSize);

        System.out.println("[wloop]" + wLoopSize);
        System.out.println("[rloop]" + rLoopSize);

        FileBoy f = new FileBoy(fileName);

        new WThread(f, wBufferSize, wLoopSize).start();
        new RRThread(f, rBufferSize, rLoopSize).start();
    }

    /**
     * read and write on random access file - same pointer
     * write 100m buffer 20byte
     * read 100m buffer 20byte
     * for AWS EC2 - t2 middle - 100G EBS storage
     */
    public void case5() {
        String fileName = "testRandomBig.txttestRandomBig.txt";

        int wBufferSize = 20;
        int rBufferSize = 20;
        long wAccessByte = 1024*1024*100L;
        long rAccessByte = 1024*1024*100L;
        int wLoopSize = (int)(wAccessByte / wBufferSize);
        int rLoopSize = (int)(rAccessByte / rBufferSize);

        System.out.println("[wloop]" + wLoopSize);
        System.out.println("[rloop]" + rLoopSize);

        FileBoy f = new FileBoy(fileName);

        new WThread(f, wBufferSize, wLoopSize).start();
        new RThread(f, rBufferSize, rLoopSize).start();
    }

    /**
     * read and write on random access file - different pointer
     * write 100m buffer 20by
     * read 240m buffer 20byte
     * It's better than the same pointer file : 20%~30% better
     * for AWS EC2 - t2 middle - 100G EBS storage
     */
    public void case6() {
        String fileName = "testRandomBig.txt";

        int wBufferSize = 20;
        int rBufferSize = 20;
        long wAccessByte = 1024*1024*100L;
        long rAccessByte = 1024*1024*400L;
        int wLoopSize = (int)(wAccessByte / wBufferSize);
        int rLoopSize = (int)(rAccessByte / rBufferSize);

        System.out.println("[wloop]" + wLoopSize);
        System.out.println("[rloop]" + rLoopSize);

        FileBoy f = new FileBoy(fileName);

        new WThread(f, wBufferSize, wLoopSize).start();
        new RRThread(f, rBufferSize, rLoopSize).start();
    }

    public class WThread extends Thread{
        FileBoy boy;
        int bufferSize;

        int loopSize;

        public WThread(FileBoy boy, int bufferSize, int loopSize) {
            this.boy = boy;
            this.bufferSize = bufferSize;
            this.loopSize = loopSize;
        }
        @Override
        public void run() {
            byte[] buffer = new byte[bufferSize];
            int[] pos = new int[loopSize];

            for (int i = 0; i < bufferSize; i++) {
                buffer[i] = (byte) 255;
            }
            for (int i = 0; i < loopSize; i++) {
                pos[i] = ThreadLocalRandom.current().nextInt(loopSize-bufferSize);
            }

            try {
                boy.openW();
                System.out.println("[WSTART]");
                long start = System.currentTimeMillis();
                for (int i = 0; i < loopSize; i++) {
                    boy.writeW(buffer);
                }
                System.out.println("[WEND]" + (System.currentTimeMillis()-start));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public class RThread extends Thread{
        FileBoy boy;
        int bufferSize;
        int loopSize;

        public RThread(FileBoy boy, int bufferSize, int loopSize) {
            this.boy = boy;
            this.bufferSize = bufferSize;
            this.loopSize = loopSize;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[bufferSize];
            int[] pos = new int[loopSize];

            for (int i = 0; i < bufferSize; i++) {
                buffer[i] = (byte) 255;
            }
            for (int i = 0; i < loopSize; i++) {
                pos[i] = ThreadLocalRandom.current().nextInt(loopSize-bufferSize);
            }

            try {
                boy.openW();
                System.out.println("[W-RSTART]");
                long start = System.currentTimeMillis();
                for (int i = 0; i < loopSize; i++) {
                    boy.readW(buffer);
                }
                System.out.println("[W-REND]" + (System.currentTimeMillis()-start));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class RRThread extends Thread{
        FileBoy boy;
        int bufferSize;
        int loopSize;

        public RRThread(FileBoy boy, int bufferSize, int loopSize) {
            this.boy = boy;
            this.bufferSize = bufferSize;
            this.loopSize = loopSize;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[bufferSize];
            int[] pos = new int[loopSize];

            for (int i = 0; i < bufferSize; i++) {
                buffer[i] = (byte) 255;
            }
            for (int i = 0; i < loopSize; i++) {
                pos[i] = ThreadLocalRandom.current().nextInt(loopSize-bufferSize);
            }

            try {
                boy.openR();
                System.out.println("[R-RSTART]");
                long start = System.currentTimeMillis();
                for (int i = 0; i < loopSize; i++) {
                    boy.readR(buffer);
                }
                System.out.println("[R-REND]" + (System.currentTimeMillis()-start));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class FileBoy {
        //write 20 byte
        //read 288*20 byte

        String fname;
        RandomAccessFile rfile;
        RandomAccessFile wfile;
        FileBoy(String fname) {
            this.fname = fname;
        }

        public void openR() throws FileNotFoundException {
            synchronized (this) {
                if (rfile == null) {
                    rfile = new RandomAccessFile(fname, "r");
                }
            }
        }

        public void openW() throws FileNotFoundException {
            synchronized (this) {
                if (wfile == null) {
                    wfile = new RandomAccessFile(fname, "rw");
                }
            }
        }

        public void closeR() throws FileNotFoundException {
            synchronized (rfile) {
                try {
                    rfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void closeW() throws FileNotFoundException {
            synchronized (wfile) {
                try {
                    wfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void readR(byte[] buffer) throws IOException {
            synchronized (rfile) {
                rfile.read(buffer);
            }
        }

        public void readW(byte[] buffer) throws IOException {
            synchronized (wfile) {
                wfile.read(buffer);
            }
        }

        public void writeW(byte[] buffer) throws IOException {
            synchronized (wfile) {
                wfile.write(buffer);
            }
        }

        public void seekR(int pos) throws IOException {
            synchronized (rfile) {
                rfile.seek(pos);
            }
        }

        public synchronized void seekW(int pos) throws IOException {
            synchronized (wfile) {
                wfile.seek(pos);
            }
        }
    }


    public void makeBigfile() {
        try {
            RandomAccessFile f = new RandomAccessFile("testRandomBig.txt", "rw");
            int bufferSize = 4096;
            byte[] b = new byte[bufferSize];
            for (int i = 0; i < 20; i++) {
                b[i] = (byte) 255;
            }

            long start = System.currentTimeMillis();
            for (int i = 0; i < 1024*1024*100/bufferSize; i++) {
                f.write(b);
            }
            System.out.println("[elapsed] " + (System.currentTimeMillis()-start));
            System.out.println("[size-mb] " + f.length()/1024/1024);

            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
