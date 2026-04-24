public class Exercise2 {

    static class SharedResource {
        private int value;
        private boolean bChanged = false;

        public synchronized int get() {
            while (!bChanged) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            bChanged = false;
            System.out.println("Consumed: " + value);
            return value;
        }

        public synchronized void set(int value) {
            this.value = value;
            bChanged = true;

            System.out.println("Produced: " + value);

            notify();
        }
    }

    static class Producer implements Runnable {
        private final SharedResource resource;

        public Producer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.set(i);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final SharedResource resource;

        public Consumer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.get();
            }
        }
    }

    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        Thread producer = new Thread(new Producer(resource));
        Thread consumer = new Thread(new Consumer(resource));

        producer.start();
        consumer.start();
    }
}
