package main.java;

import main.java.Pressure.Barometric1000PressureSensorImpl;
import main.java.Pressure.PressureSensor;

public abstract class Sensor {

  private String name;

  public Sensor(String name) {
    this.name = name;
  }

  /**
   * Gibt die Einheit der Messung zurück.
   *
   * @return Einheit der Messung, z.B. Grad Celsius.
   */
  public abstract String getUnit();

  /**
   * Gibt den Messwert in der Einheit zurück.
   *
   * @return Messwert als double.
   */
  public abstract double getValue();

  /**
   * Gibt den Namen des Messwerts zurück.
   *
   * @return Name des Messwerts, z.B. "Aussentemperatur in Baar".
   */
  public abstract String getName();

  /**
   * Führt die Messung durch.
   * Diese Methode sollte den Sensor auslesen, z.B. durch Ansprechen einer seriellen Schnittstelle,
   * einen HTTP-Request oder eine andere geeignete Methode.
   */
  public abstract void doMeasurement();

  public static void main(String[] args) {
    PressureSensor pressureSensor = new Barometric1000PressureSensorImpl(
      "Barometer1",
      "hPa"
    );

    pressureSensor.doMeasurement();

    System.out.println("Sensor Name: " + pressureSensor.getName());
    System.out.println(
      "Messwert: " + pressureSensor.getValue() + " " + pressureSensor.getUnit()
    );
  }
}
