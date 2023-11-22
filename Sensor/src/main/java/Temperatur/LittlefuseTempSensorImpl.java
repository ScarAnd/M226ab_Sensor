package main.java.Temperatur;

import main.java.RandomNumberGen;

/**
 * Kind-Klasse von TemperaturSensor für einen spezifischen Temperatur Messungen.
 */
public class LittlefuseTempSensorImpl extends TemperaturSensor {

  public LittlefuseTempSensorImpl(String name, String unit) {
    super(name, unit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    String name = "LittlefuseTemp";
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doMeasurement() {
    measurementValue = RandomNumberGen.generateRandomNumber(0, 150);
  }
}
