package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final double CONCERT_A = 440.0;
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    static GuitarString string = new GuitarString(CONCERT_A);

    public static void main(String[] args) {
        double[] guitar = new double[37];
        GuitarString stringA = new GuitarString(CONCERT_A);

        for (int i = 0; i < guitar.length; i++) {
            guitar[i] = CONCERT_A * Math.pow(2, (i - 24) / 12);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int pos = KEYBOARD.indexOf(key);
                if (pos >= 0) {
                    string = new GuitarString(guitar[pos]);
                    string.pluck();
                    continue;
                }
            }

            double sample = stringA.sample() + string.sample();

            StdAudio.play(sample);

            stringA.tic();
            string.tic();
        }
    }
}
