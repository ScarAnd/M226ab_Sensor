package main.java.Pressure;

import main.java.RandomNumberGen;

/**
 * Kind-Klasse von PressureSensor für einen spezifischen Barometer-Drucksensor.
 */
public class Barometric1000PressureSensorImpl extends PressureSensor {

  private String name;

  public Barometric1000PressureSensorImpl(String name, String unit) {
    super(name, unit);
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
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
