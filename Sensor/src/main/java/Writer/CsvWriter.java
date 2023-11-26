package main.java.Writer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import main.java.Pressure.Aqualung2021ProDivePressureSensorImpl;
import main.java.Pressure.Barometric1000PressureSensorImpl;
import main.java.Pressure.PressureSensor;
import main.java.Sensor;
import main.java.Temperatur.AbraconTemp500SensorImpl;
import main.java.Temperatur.LittlefuseTempSensorImpl;
import main.java.Temperatur.TemperaturSensor;

/**
 * Klasse um Sensoren und deren Ergebnisse in eine CSV Datei zu schreiben.
 */
public class CsvWriter {

  private static final String DEFAULT_CSV_FILE_PATH = "output.csv";
  private static final long DEFAULT_INTERVAL_MS = 1000;

  private Timer timer;
  private int sensorType;
  private int sensorFactory;
  private String sensorName;
  private String csvFileName;
  private long intervallInMs;
  private Sensor sensor;

  /**
   * Void in welchem der User Input abgefragt wird.
   */
  public void run() {
    configureUserInput();
    start();
  }

  /**
   * User Abfrage nach Sensor usw.
   */
  private void configureUserInput() {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.print(
        "Geben Sie den Dateinamen für die CSV-Datei ein (oder Enter für '" +
        DEFAULT_CSV_FILE_PATH +
        "'): "
      );
      csvFileName = scanner.nextLine();
      csvFileName = csvFileName.isEmpty() ? DEFAULT_CSV_FILE_PATH : csvFileName;

      System.out.print(
        "Geben Sie das Zeitintervall in Millisekunden ein (oder Enter für " +
        DEFAULT_INTERVAL_MS +
        "): "
      );
      String intervalInput = scanner.nextLine();

      intervallInMs =
        intervalInput.isEmpty()
          ? DEFAULT_INTERVAL_MS
          : Long.parseLong(intervalInput);

      do {
        System.out.print(
          "Welchen Sensor Typ möchten Sie verwenden (Drücken Sie '1' für Pressure oder '2' für Temperatur): "
        );
        sensorType = scanner.nextInt();
        scanner.nextLine();
        if (sensorType != 1 && sensorType != 2) {
          System.err.println(
            "Ungültige Auswahl. Bitte geben Sie '1' oder '2' ein."
          );
        }
      } while (sensorType != 1 && sensorType != 2);

      do {
        switch (sensorType) {
          case 1:
            System.out.print(
              "Welchen Sensor möchten Sie verwenden (Drücken Sie '1' für Barometric1000PressureSensor oder '2' für Aqualung2021ProDivePressure): "
            );
            sensorFactory = scanner.nextInt();
            scanner.nextLine();
            break;
          case 2:
            System.out.print(
              "Welchen Sensor möchten Sie verwenden (Drücken Sie '1' für AbraconTemp500Sensor oder '2' für LittlefuseTempSensor): "
            );
            sensorFactory = scanner.nextInt();
            scanner.nextLine();
          default:
            break;
        }
        if (sensorFactory != 1 && sensorFactory != 2) {
          System.err.println(
            "Ungültige Auswahl. Bitte geben Sie '1' oder '2' ein."
          );
        }
      } while (sensorFactory != 1 && sensorFactory != 2);

      System.out.print("Geben Sie den Sensorname ein, welche Sie verwenden: ");
      sensorName = scanner.nextLine();
      sensor = createSensor();
    }
  }

  /**
   * Methode, um einen Sensor anzusprechen.
   */
  private Sensor createSensor() {
    Sensor result = null;
    switch (sensorType) {
      case 1:
        if (sensorFactory == 1) {
          PressureSensor pressureSensor = new Barometric1000PressureSensorImpl(
            sensorName,
            "hPa"
          );
          result = pressureSensor;
        } else {
          PressureSensor pressureSensor = new Aqualung2021ProDivePressureSensorImpl(
            sensorName,
            "hPa"
          );
          result = pressureSensor;
        }
        break;
      case 2:
        if (sensorFactory == 1) {
          TemperaturSensor temperaturSensor = new AbraconTemp500SensorImpl(
            sensorName,
            "°C"
          );
          result = temperaturSensor;
        } else {
          TemperaturSensor temperaturSensor = new LittlefuseTempSensorImpl(
            sensorName,
            "°C"
          );
          result = temperaturSensor;
        }
        break;
      default:
        break;
    }
    return result;
  }

  /**
   * Void um die erhaltenen Daten des Sensors in ein csv zu schreiben.
   */
  public void writeToCSV() {
    try (FileWriter writer = new FileWriter(csvFileName, true)) {
      sensor.doMeasurement();
      double value = sensor.getValue();
      String unit = sensor.getUnit();
      String name = sensor.getName();
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      String sensor = sensorType == 1
        ? sensorFactory == 1
          ? "Barometric1000PressureSensor"
          : "Aqualung2021ProDivePressure"
        : sensorFactory == 1 ? "AbraconTemp500Sensor" : " LittlefuseTempSensor";

      writer
        .append(timestamp.toString())
        .append(",")
        .append(name + " ( " + sensor + " ) ")
        .append(",")
        .append(unit)
        .append(",")
        .append(String.valueOf(value))
        .append("\n");

      System.out.println("Messwerte geschrieben" + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Void, welcher den Sensor in einem bestimmten Intervall abfragt.
   */
  public void start() {
    timer = new Timer();
    timer.scheduleAtFixedRate(
      new TimerTask() {
        @Override
        public void run() {
          writeToCSV();
        }
      },
      0,
      intervallInMs
    );
  }
}
