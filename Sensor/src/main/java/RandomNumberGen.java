package main.java;

import java.util.Random;

/**
 * Klasse um eine Random Nummer zu generieren, mit einem min und max value
 */
public class RandomNumberGen {

  /**
   * @param min min value für random Nummer.
   * @param max max value für random Nummer.
   */
  public static double generateRandomNumber(double min, double max) {
    if (min >= max) {
      throw new IllegalArgumentException(
        "Der tiefste Wert muss kleiner als der höchste Wert sein."
      );
    }

    Random random = new Random();
    double randomNumber = min + random.nextDouble() * (max - min);

    return randomNumber;
  }
}
