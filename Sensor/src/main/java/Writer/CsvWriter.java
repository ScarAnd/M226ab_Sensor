package main.java.Writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
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

  private static final String DEFAULT_CSV_FILE_PATH = "sensor_data.csv";
  private static final long DEFAULT_INTERVAL_MS = 1000;

  private Timer timer;
  private int sensorType;
  private int sensorFactory;
  private String sensorName;
  private String csvFileName;
  private long intervallInMs;
  private Sensor sensor;
  private boolean csvImport;
  private Connection connection;

  /**
   * Void in welchem der User Input abgefragt wird.
   * @param connection DB connection
   */
  public void run(Connection connection) {
    this.connection = connection;
    try (Scanner scanner = new Scanner(System.in)) {
      askForCSVImport(scanner);
      if (!csvImport) {
        configureUserInput(scanner);
        start();
      }
    }
  }

  /**
   * User Abfrage ob upload zur DB.
   * @param Scanner Konsolen Scanner
   */
  private void askForCSVImport(Scanner scanner) {
    String fileName = null;
    postgresqlWriter postgresqlWriter = new postgresqlWriter();
    System.out.print(
      "Möchten Sie ein CSV mit Sensor Daten in die Datenbank importieren? (Geben Sie 'ja' oder 'nein' ein): "
    );
    String userInput = scanner.nextLine().trim().toLowerCase();
    boolean result = userInput.equals("ja");
    csvImport = result;
    if (csvImport) {
      do {
        System.out.print(
          "Wie heißt Ihre CSV Datei? (oder Enter für '" +
          DEFAULT_CSV_FILE_PATH +
          "'): "
        );
        fileName = scanner.nextLine();
        fileName = fileName.isEmpty() ? DEFAULT_CSV_FILE_PATH : fileName;

        File file = new File(fileName);
        if (!file.exists()) {
          System.out.println(
            "Die Datei '" +
            fileName +
            "' wurde nicht gefunden. Bitte versuchen Sie es erneut."
          );
        }
      } while (!new File(fileName).exists());

      //Upload to DB
      postgresqlWriter.uploadCsv(fileName, connection);
    } else {
      return;
    }
  }

  /**
   * User Abfrage nach Sensor usw.
   * @param Scanner Konsolen Scanner
   */
  private void configureUserInput(Scanner scanner) {
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
    try {
      File file = new File(csvFileName);

      if (file.length() == 0) {
        try (FileWriter writer = new FileWriter(csvFileName, true)) {
          writer.append("ID,Timestamp,SensorInfo,Unit,Value\n");
        }
      }

      try (FileWriter writer = new FileWriter(csvFileName, true)) {
        sensor.doMeasurement();
        double value = sensor.getValue();
        String unit = sensor.getUnit();
        String name = sensor.getName();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String sensorInfo = (sensorType == 1)
          ? (
            sensorFactory == 1
              ? "Barometric1000PressureSensor"
              : "Aqualung2021ProDivePressure"
          )
          : (
            sensorFactory == 1
              ? "AbraconTemp500Sensor"
              : " LittlefuseTempSensor"
          );

        String id = UUID.randomUUID().toString();

        writer
          .append(id)
          .append(",")
          .append(timestamp.toString())
          .append(",")
          .append(name + " ( " + sensorInfo + " ) ")
          .append(",")
          .append(unit)
          .append(",")
          .append(String.valueOf(value))
          .append("\n");

        System.out.println("Messwerte geschrieben" + "\n");
      }
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
