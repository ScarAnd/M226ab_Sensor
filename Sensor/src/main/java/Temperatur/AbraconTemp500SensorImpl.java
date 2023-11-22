package main.java.Temperatur;

import main.java.RandomNumberGen;

/**
 * Kind-Klasse von TemperaturSensor für einen spezifischen Temperatur Messungen.
 */
public class AbraconTemp500SensorImpl extends TemperaturSensor {

  public AbraconTemp500SensorImpl(String name, String unit) {
    super(name, unit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    String name = "AbraconTemp500";
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
