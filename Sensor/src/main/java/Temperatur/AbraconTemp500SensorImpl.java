package main.java.Temperatur;

import main.java.RandomNumberGen;

/**
 * Kind-Klasse von TemperaturSensor für einen spezifischen Temperatur Messungen.
 */
public class AbraconTemp500SensorImpl extends TemperaturSensor {

  private String name;

  public AbraconTemp500SensorImpl(String name, String unit) {
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
    measurementValue = RandomNumberGen.generateRandomNumber(-150, 260);
  }
}
