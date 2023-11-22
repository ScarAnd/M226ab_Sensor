package main.java.Pressure;

import java.util.Random;

/**
 * Kind-Klasse von PressureSensor für einen spezifischen Barometer-Drucksensor.
 */
public class Barometric1000PressureSensorImpl extends PressureSensor {

  public Barometric1000PressureSensorImpl(String name, String unit) {
    super(name, unit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    String name = "Barometric1000";
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doMeasurement() {
    Random random = new Random();
    measurementValue = 0.5 + random.nextDouble() * 0.55;
  }
}
