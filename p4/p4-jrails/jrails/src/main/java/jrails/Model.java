package jrails;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private static final String DBFile = "database.csv";
    private int id;

    // Initialize the model with default values & check for valid @Column types
    public Model() {
        checkValidColumns();
        this.id = 0;
    }

    // Check if the db file has the required columns. If not, add the required columns
    private void checkValidColumns() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getType() != String.class && field.getType() != int.class && field.getType() != Boolean.class) {
                    throw new RuntimeException("Field " + field.getName() + " has invalid type; must be str, int, or bool");
                }
            }
        }
    }


    public void save() {
        // Check if db file exists and create one if not
        File newFile = new File(DBFile);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create database file");
            }
        }
        /* checkDBFileExists(); */

        if ((this.id != 0) && !existsinDB()) {
            throw new RuntimeException("Tried to save non-zero model that does not exist");
        }

        boolean newEntry = false;
        if (this.id == 0) {
            newEntry = true;
            this.id = makeNewID();
        }

        // Turn model to string and write to db file
        String CSVModel = convertToCSV(); 
        writeToFile(CSVModel, newEntry);
    }

    /* private void checkDBFileExists() {
        File newFile = new File(DBFile);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create database file");
            }
        }
    } */

    private boolean existsinDB() {
        try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                int modelId = Integer.parseInt(vals[0].trim());
                if (modelId == this.id) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking databse for existence");
        }

        return false;
    }

    private int makeNewID() {
        int newId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                int modelId = Integer.parseInt(vals[0].trim());
                if (modelId > newId) {
                    newId = modelId;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing databse for new id");
        }

        return newId++;
    }

    private String convertToCSV() {
        StringBuilder newDBLine = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();

        // CSV string starts w/ ID for easy identification
        newDBLine.append(this.id);
        newDBLine.append(",");

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    Object value = field.get(this);

                    if (value == null) {
                        newDBLine.append("NULL"); // Handle null case
                    } else if (value instanceof String && ((String) value).contains(",")) {
                        newDBLine.append("|").append(value).append("|"); // Mark strings w/ commas
                    } else {
                        newDBLine.append(value.toString());
                    }
                    newDBLine.append(",");
                } catch (Exception e) {
                    throw new RuntimeException("Error accessing field " + field.getName() + " of model ID " + this.id);
                }
            }
        }

        // Remove trailing comma
        newDBLine.setLength(newDBLine.length() - 1);

        /* // Remove the last comma if there's at least one field
        if (newDBLine.length() > 0) {
            newDBLine.setLength(newDBLine.length() - 1);
        } */

        return newDBLine.toString();
    }

    public void writeToFile(String text, boolean newEntry) {
        // If new entry, append to end
        if (newEntry) {
            try (FileWriter writer = new FileWriter(DBFile, true)) {
                writer.write(text);
                writer.write("\n");
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while writing to file");
            }
        } else {
        // If old entry, replace existing instance
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
                FileWriter writer = new FileWriter(DBFile, true);
                String line;

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int currId = Integer.parseInt(values[0].trim());
                    if (currId == this.id) {
                        line = text;
                    }
                    sb.append(line);
                    sb.append("\n");
                }
                writer.write(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while writing to file");
            }
        }
    }


    public int id() {
        return this.id;
    }


    public static <T> T find(Class<T> c, int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());

                if (currId == id) {
                    T instance = c.getDeclaredConstructor().newInstance();
                    Field[] fields = c.getDeclaredFields();

                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];

                        if (field.isAnnotationPresent(Column.class)) {
                            String value = values[i].trim();

                            if (field.getType().equals(Integer.class)) {
                                field.setInt(instance, Integer.parseInt(value));
                            } else if (field.getType().equals(String.class)) {
                                field.set(instance, value.equals("NULL") ? null : value.replace("|", ""));
                            } else if (field.getType().equals(boolean.class)) {
                                field.setBoolean(instance, Boolean.parseBoolean(value));
                            }
                        }
                    }

                    return instance;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while find() for a model instance");
        }

        return null;
    }

    public static <T> List<T> all(Class<T> c) {
        List<T> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());
                rows.add(find(c, currId));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while all()");
        }

        return rows;
    }

    public void destroy() {
        if (this.id == 0) {
            throw new RuntimeException("Tried to destroy() model not saved in db");
        }

        if (existsinDB()) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(DBFile))) {
                FileWriter writer = new FileWriter(DBFile, true);
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int currId = Integer.parseInt(values[0].trim());
                    if (currId != this.id) {
                        sb.append(line);
                        sb.append("\n");
                    }
                }
                writer.write(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while writing to the file");
            }
        } else {
            throw new RuntimeException("Model with id " + this.id + " does not exist in the database.");
        }
    }

    public static void reset() {
        try (PrintWriter w = new PrintWriter(DBFile)) {
            // By writing nothing, the DBFile's content is replaced with nothing
        } catch (Exception e) {
            throw new RuntimeException("Database file not found");
        }
    }
}
