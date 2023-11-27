package main.java.Writer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

/**
 * Klasse um sich mit der DB zu verbinden.
 */
public class postgresqlWriter {

  String url =
    "jdbc:postgresql://0fabe2a5-b6e7-40be-9932-501b9ccfc580.659dc287bad647f9b4fe17c4e4c38dcc.databases.appdomain.cloud:30227/ibmclouddb";
  String user = "ibm_cloud_391e3280_a390_41f4_98aa_846226b753f6";
  String password =
    "cb5dbd35ea06f352e87e956be31a648f871cdef7dc6562372c96f5b2a2fcacc0";
  Connection connection;

  /**
   * Methode, um sich mit der DB zu verbinden.
   */
  public Connection connect() {
    connection = null;
    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(url, user, password);

      connection
        .prepareStatement(
          "CREATE TABLE IF NOT EXISTS sensor_data(id VARCHAR(36) PRIMARY KEY,timestamp TIMESTAMP NOT NULL,sensor VARCHAR(255),unit VARCHAR(5),value DOUBLE PRECISION)"
        )
        .executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");
    return connection;
  }

  /**
   * Methode damit Daten vom CSV in die DB geschrieben werden.
   * @param fileName Dateiname des CSVs
   * @param connection DB connection
   */
  public void uploadCsv(String fileName, Connection connection) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      reader.readLine();

      String sql =
        "INSERT INTO sensor_data (id, timestamp, sensor, unit, value) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        String line;
        int batchSize = 10000;

        int count = 0;
        while ((line = reader.readLine()) != null) {
          String[] data = line.split(",");

          statement.setString(1, data[0]);
          statement.setTimestamp(2, Timestamp.valueOf(data[1]));
          statement.setString(3, data[2]);
          statement.setString(4, data[3]);
          statement.setDouble(5, Double.parseDouble(data[4]));

          statement.addBatch();

          count++;

          if (count % batchSize == 0) {
            statement.executeBatch();
            statement.clearBatch();
          }
        }
        statement.executeBatch();
        System.out.println("Sensor data successfully saved in DB");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
