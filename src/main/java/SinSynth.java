import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Random;

public class SinSynth {

    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        new Process(65.41).start();
        new Process(32.70).start();
        new Process(392.00).start();
        new Process(523.26).start();
    }

}

    class Process extends Thread {
    private final double freq;

        static final int SAMPLE_RATE = 16 * 1024;

        Process(double freq) {
            this.freq = freq;
        }

        @Override
        public void run() {
            while (true) {
                final AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, true);
                SourceDataLine line = null;
                try {
                    line = AudioSystem.getSourceDataLine(af);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                try {
                    line.open(af, SAMPLE_RATE);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                line.start();

                boolean forwardNotBack = true;
                byte[] toneBuffer;
                int count;

                toneBuffer = createSinWaveBuffer(freq, 1000);


                line.write(toneBuffer, 0, toneBuffer.length);

                line.drain();
//                line.stop();
                line.close();

            }
        }
        public static byte[] createSinWaveBuffer(double freq, int ms) {
            int samples = ((ms * SAMPLE_RATE) / ms);
            byte[] output = new byte[samples];
            //
            double period = (double)SAMPLE_RATE / freq;
            for (int i = 0; i < output.length; i++) {
                double angle = 2.0 * Math.PI * i / period;
                var r = (float)new Random().nextInt(127,  129);
                System.out.println(r);
                output[i] = (byte)(Math.sin(angle) * r);  }

            return output;
        }

    }
