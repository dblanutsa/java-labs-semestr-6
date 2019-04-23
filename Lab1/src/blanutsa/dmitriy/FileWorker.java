package blanutsa.dmitriy;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileWorker {

    public static final int MIN_LEXEME_LENGTH = 3;
    public static final int MAX_LEXEME_LENGTH = 5;

    private Scanner scanner = new Scanner(System.in);

    public void clean() {
        System.out.println("Enter path: ");
        String path = scanner.nextLine();

        if (path.equals("q")) {
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File " + path + " not exists.");
        } else if (file.isDirectory()) {
            openDirectory(new File(path));
        } else if (file.isFile()) {
            openFile(file);
        }
    }

    private void openDirectory(File directory) {
        System.out.println("In directory: " + directory.getName());
        File[] elements = directory.listFiles();

        if (elements != null) {
            ExecutorService executorService = Executors.newFixedThreadPool(elements.length);

            for(File element : elements) {
                executorService.submit(() -> {
                    if(element.isFile()){
                        openFile(element);
                    } else if(element.isDirectory()){
                        openDirectory(element);
                    }
                    return null;
                });
            }

            executorService.shutdown();
        }
    }

    private void openFile(File file) {
        System.out.println("    In file: " + file.getName());
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (line != null){
                String updated = cleanRow(line);
                text.append(updated).append("\r\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error occurred during opening file.");
        }

        if (text.length() > 0) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(text.toString());
            } catch (IOException e) {
                System.err.println("Error occurred during writing file.");
            }
        }
    }

    private String cleanRow(String row) {
        String[] lexemes = row.split("\\s");

        for (String lexeme : lexemes) {
            if (!lexeme.equals("") && lexeme.length() >= MIN_LEXEME_LENGTH && lexeme.length() <= MAX_LEXEME_LENGTH) {
                row = row.replace(lexeme, "");

                System.out.println(String.format("      Removed \"%s\"", lexeme));
            }
        }
        return row;
    }
}
