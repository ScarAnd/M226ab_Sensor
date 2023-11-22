package main.java.Pressure;

import java.util.Random;

/**
 * Kind-Klasse von PressureSensor für einen spezifischen Barometer-Drucksensor.
 */
public class Aqualung2021ProDivePressureSensorImpl extends PressureSensor {

  public Aqualung2021ProDivePressureSensorImpl(String name, String unit) {
    super(name, unit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    String name = "Aqualung2021Pro";
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doMeasurement() {
    Random random = new Random();
    measurementValue = random.nextDouble() * 10;
  }
}
