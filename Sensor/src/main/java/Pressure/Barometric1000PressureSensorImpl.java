package main.java.Pressure;

import main.java.RandomNumberGen;

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
    measurementValue = RandomNumberGen.generateRandomNumber(0.5, 1.05);
  }
}
