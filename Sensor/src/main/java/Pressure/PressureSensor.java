package main.java.Pressure;

import main.java.Sensor;

/**
 * Abstrakte Klasse für Drucksensoren, erweitert die abstrakte Klasse Sensor.
 */
public abstract class PressureSensor extends Sensor {

  private String unit;
  protected double measurementValue;

  /**
   * Konstruktor für PressureSensor.
   *
   * @param name Der Name des Drucksensors.
   * @param unit Die Einheit der Messung.
   */
  public PressureSensor(String name, String unit) {
    super(name);
    this.unit = unit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getValue() {
    return measurementValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnit() {
    return unit;
  }
}
