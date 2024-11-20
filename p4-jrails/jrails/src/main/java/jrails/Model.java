package jrails;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final String dbFile = "database.csv";
    public int id;

    public Model() {
        checkValidTypes();
        this.id = 0;
    }

    private void checkValidTypes() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getType() != String.class && field.getType() != int.class && field.getType() != boolean.class) {
                    throw new RuntimeException("Invalid type for @Column field: " + field.getName());
                }
            }
        }
    }

    public void save() {
        // Create db file if none exists
        checkDBFile();

        // If id not zero, check existence
        if (this.id != 0 && !existsInDatabase()) {
            throw new RuntimeException("Non-existent model w/ non-zero id tried to save");
        }

        // Create new id if needed
        // Denote if entry is new or not
        boolean newEntry = false;
        if (this.id == 0) {
            newEntry = true;
            this.id = createNewId();
        }

        // Convert to CSV string and save
        String CSVString = stringifyModel(); 
        writeToFile(CSVString, newEntry); 
    }

    private void checkDBFile() {
        File file = new File(dbFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create database file");
            }
        }
    }

    private boolean existsInDatabase() {
        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());
                if (currId == this.id) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while checking for entry number " + this.id + " in database");
        }

        return false;
    }

    private int createNewId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());
                if (currId > maxId) {
                    maxId = currId;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while reading from databse for unique id");
        }

        return maxId + 1;
    }

    private String stringifyModel() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();

        // Append id to beginning 
        sb.append(this.id);
        sb.append(",");

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                try {
                    Object value = field.get(this);
                    if (value == null) {
                        sb.append("NULL"); // Handle null values
                    } else if (value instanceof String && ((String) value).contains(",")) {
                        String stringValue = (String) value;
                        sb.append(stringValue.replace(",", "|")); // Handle commas in strings
                    } else {
                        sb.append(value.toString());
                    }
                    sb.append(",");
                } catch (Exception e) {
                    throw new RuntimeException("Error accessing database");
                }
            }
        }

        // Remove trailing comma and return
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public void writeToFile(String text, boolean newEntry) {
        if (newEntry) {
            // Append new entry to the end of file
            try (FileWriter writer = new FileWriter(dbFile, true)) {
                writer.write(text);
                writer.write("\n");
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while writing to the file");
            }
        } else {
            // Construct a new copy of db file with the modified entry
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int currId = Integer.parseInt(values[0].trim());
                    if (currId == this.id) {
                        sb.append(text).append("\n");
                    } else {
                        sb.append(line).append("\n");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while constructing modified file");
            }

            // Overwrite db file w/ new copy
            try (FileWriter writer = new FileWriter(dbFile)) {
                writer.write(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while writing modified file");
            }
        }
    }

    public int id() {
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());

                if (currId == id) {
                    // Found match, now materialize and copy
                    T instance = c.getDeclaredConstructor().newInstance();
                    Field[] fields = c.getFields();

                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        if (field.getName().equals("id")) {
                            field.setInt(instance, currId);
                        }
                        
                        if (field.isAnnotationPresent(Column.class)) {
                            String value = values[i + 1];
                            if (field.getType() == int.class) {
                                field.setInt(instance, Integer.parseInt(value));
                            } else if (field.getType() == String.class) {
                                field.set(instance, value.equals("NULL") ? null : value.replace("|", ","));
                            } else if (field.getType() == boolean.class) {
                                field.setBoolean(instance, Boolean.parseBoolean(value));
                            }
                        }
                    }

                    return instance;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        // No entry with the given id found, return null
        return null;
    }

    public static <T> List<T> all(Class<T> c) {
        List<T> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currId = Integer.parseInt(values[0].trim());
                rows.add(find(c, currId));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while collecting all rows of databse");
        }

        return rows;
    }

    public void destroy() {
        // Check if the model has been saved (id is not 0)
        if (this.id == 0) {
            throw new RuntimeException("Tried to destroy unsaved model");
        }

        // If the model exists in the database, remove it
        if (existsInDatabase()) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    int currId = Integer.parseInt(values[0].trim());
                    if (currId != this.id) {
                        sb.append(line);
                        sb.append("\n");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while parsing db file for delete()");
            }

            try (FileWriter writer = new FileWriter(dbFile)) {
                writer.write(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while removing deleted content");
            }
        } else {
            throw new RuntimeException("Tried to destroy model with id " + this.id + " when none exists");
        }
    }

    public static void reset() {
        try (PrintWriter writer = new PrintWriter(dbFile)) {
            // By not writing anything to the writer, the file content is cleared.
        } catch (Exception e) {
            throw new RuntimeException("Cannot reset nonexistent db file");
        }
    }
}
