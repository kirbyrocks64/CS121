package jrails;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Model {
    private static final String DBFile = "database.csv";
    private int id;

    // Initialize the Model with default values & check for valid @Column types
    public Model() {
        checkValidColumns();
        this.id = 0;
    }

    // Check if the DBFile has the required columns. If not, add the required columns
    private void checkValidColumns() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getType() != String.class && field.getType() != Integer.class && field.getType() != Boolean.class) {
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

        String CSVModel = convertToCSV(); 

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
                        newDBLine.append("\"").append(value).append("\""); // Handle commas in strings
                    } else {
                        newDBLine.append(value.toString());
                    }
                    newDBLine.append(",");
                } catch (Exception e) {
                    throw new RuntimeException("Error accessing field " + field.getName() + " of model ID " + this.id);
                }
            }
        }

        // Remove the last comma if there's at least one field
        if (newDBLine.length() > 0) {
            newDBLine.setLength(newDBLine.length() - 1);
        }

        return newDBLine.toString();
    }


    public int id() {
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        throw new UnsupportedOperationException();
    }

    public static <T> List<T> all(Class<T> c) {
        // Returns a List<element type>
        throw new UnsupportedOperationException();
    }

    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public static void reset() {
        try (PrintWriter w = new PrintWriter(DBFile)) {
            // By writing nothing, the DBFile's content is replaced with nothing
        } catch (Exception e) {
            throw new RuntimeException("Database file not found");
        }
    }
}
