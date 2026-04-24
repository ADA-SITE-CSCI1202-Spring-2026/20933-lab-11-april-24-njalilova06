public class Exercise1 {

    static class MathTask implements Runnable {
        private final int start;
        private final int end;

        public MathTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            long sum = 0;

            for (int i = start; i < end; i++) {
                sum += (long) i * i * i + i;
            }
        }
    }

    public static long runWithThreads(int threadCount, int totalWork) {
        Thread[] threads = new Thread[threadCount];

        int chunkSize = totalWork / threadCount;

        long startTime = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = (i == threadCount - 1) ? totalWork : start + chunkSize;

            threads[i] = new Thread(new MathTask(start, end));
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.nanoTime();

        return (endTime - startTime) / 1_000_000;
    }

    public static void main(String[] args) {

        int totalWork = 10_000_000;

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + coreCount);

        long timeSingle = runWithThreads(1, totalWork);
        System.out.println("Time with 1 thread: " + timeSingle + " ms");

        long timeMulti = runWithThreads(coreCount, totalWork);
        System.out.println("Time with " + coreCount + " threads: " + timeMulti + " ms");
    }
}