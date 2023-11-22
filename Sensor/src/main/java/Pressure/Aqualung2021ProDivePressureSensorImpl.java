package main.java.Pressure;

import main.java.RandomNumberGen;

/**
 * Kind-Klasse von PressureSensor für einen spezifischen Barometer-Drucksensor.
 */
public class Aqualung2021ProDivePressureSensorImpl extends PressureSensor {

  private String name;

  public Aqualung2021ProDivePressureSensorImpl(String name, String unit) {
    super(name, unit);
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doMeasurement() {
    measurementValue = RandomNumberGen.generateRandomNumber(0, 10);
  }
}
