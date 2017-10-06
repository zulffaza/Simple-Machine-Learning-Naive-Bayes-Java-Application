package com.example.java;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Program Naive Bayes
 *
 * @author Faza Zulfika P P
 * @version 1.0
 * @since 6 Oktober 2017
 */
public class Main {

    /**
     * trainingAttributes merupakan data training untuk mencari hipotesa data
     * testingAttributes merupakan data testing untuk mencari kesimpulan berdasarkan hipotesa yang ditemukan
     */
    private static String[][] trainingAttributes, testingAttributes;

    /**
     * TRAINING_DATA_FILES merupakan lokasi data training
     */
    private static final String TRAINING_DATA_FILES = "G:\\Materi Semester 5\\Machine Learning\\Bayesian\\Example-1\\data\\TrainingData.txt";

    /**
     * TESTING_DATA_FILES merupakan lokasi data testing
     */
    private static final String TESTING_DATA_FILES = "G:\\Materi Semester 5\\Machine Learning\\Bayesian\\Example-1\\data\\TestingData.txt";

    /**
     * hypotesisMap merupakan data hipotesa
     */
    private static HashMap<String, HypotesisItem> hypotesisMap;

    /**
     * yesCount merupakan jumlah data Yes
     * noCount merupakan jumlah data No
     */
    private static double yesCount, noCount;

    /**
     * DECIMAL_FORMAT digunakan agar data decimal yang ditampilkan hanya memiliki 2 angka dibelakang koma
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * Merupakan fungsi main untuk menjalankan program java
     *
     * @param args merupakan argumen dari user
     */
    public static void main(String[] args) {
        trainingAttributes = setData(TRAINING_DATA_FILES); // Mengambil data training
        testingAttributes = setData(TESTING_DATA_FILES); // Mengambil data testing
        hypotesisMap = setHypotesis(); // Membuat hipotesa dari data training

        showHMAP(); // Menampilkan HMAP seluruh data
        showResult(); // Menampilkan hasil dari testing dengan data testing
    }

    /**
     * Merupakan fungsi yang digunakan untuk membaca data dari dalam file dan mengconvertnya menjadi bentuk array of string dua dimensi
     *
     * @param filePath merupakan lokasi file yang ingin dibaca isinya, setiap kata pada file haruslah dipisahkan dengan spasi (" ")
     * @return merupakan isi file dalam bentuk array of string dua dimensi
     */
    private static String[][] setData(String filePath) {
        ArrayList<String[]> resultList = new ArrayList<>(); // List untuk menampung sementara isi file
        String[][] attributes = null; // Array of string yang akan dikembalikan

        try {
            File file = new File(filePath); // Membuat object file dari file yang diinginkan
            BufferedReader br = new BufferedReader(new FileReader(file)); // Membuat object untuk membaca file
            String str = "";

            /*
             * Looping untuk membaca keseluruhan isi file
             */
            while (str != null) {
                str = br.readLine(); // Mengambil setiap baris file

                if (str != null) // Jika baris ada
                    resultList.add(str.split(" ")); // Memisahkan setiap kata menurut spasi (" ")
            }
        } catch (FileNotFoundException e) { // Jika file tidak ditemukan
            System.out.println("File tidak ditemukan");
        } catch (IOException e) { // Jika terjadi kesalahan saat membaca file
            System.out.println("Kesalahan saat membaca file");
        } finally {
            if (resultList.size() > 0) { // Jika file tidak kosong
                int rowSize = resultList.size();
                int columnSize = resultList.get(0).length;

                attributes = new String[rowSize][columnSize]; // Membuat array of string berukuran data
                attributes = resultList.toArray(attributes); // Menyimpan data dari list ke array of string
            }
        }

        return attributes; // Mengembalikan data
    }

    /**
     * Merupakan fungsi untuk mengetahui jumlah setiap attribute pada setiap jawaban yang ada (Yes / No)
     *
     * @return Merupakan hashmap dengan key yang merupakan attribute, dan value yang merupakan hypotesa item
     */
    private static HashMap<String, HypotesisItem> setHypotesis() {
        HashMap<String, HypotesisItem> hypotesisMap = new HashMap<>();

        yesCount = 0;
        noCount = 0;

        /*
         * Mengambil setiap row data training
         */
        for (String[] attributesRow : trainingAttributes) {
            int attributesRowLength = attributesRow.length - 1; // Mengambil jumlab attribute setiap row
            String answer = attributesRow[attributesRowLength]; // Mengambil jawaban dari setiap row

            /*
             * Mengambil setiap attribute pada setiap row
             */
            for (int i = 0; i < attributesRowLength; i++) {
                String attribute = attributesRow[i]; // Mengambil attribute
                HypotesisItem hypotesisItem = hypotesisMap.get(attribute); // Mengambil hypotesa pada setiap attribute

                /*
                 * Jika attribute baru / belum pernah disimpan
                 */
                if (hypotesisItem == null) {
                    hypotesisItem = new HypotesisItem(); //  Membuat hypotesa item baru
                    hypotesisMap.put(attribute, hypotesisItem); //  Memasukkan hypotesa item untuk attribute baru
                }

                /*
                 * Mengecek jawaban dari row ini
                 */
                switch (answer) {
                    case "Yes": // Jika Yes
                        hypotesisItem.setYes(hypotesisItem.getYes() + 1); // Jumlah Yes attribute tersebut ditambahkan
                        break;
                    case "No": // Jika NO
                        hypotesisItem.setNo(hypotesisItem.getNo() + 1); // Jumlah No attribute tersebut ditambahkan
                        break;
                }
            }

            /*
             * Mengecek jawaban dari row ini
             */
            switch (answer) {
                case "Yes": // Jika Yes
                    yesCount++; // Jumlah Yes ditambahkan
                    break;
                case "No": // Jika NO
                    noCount++; // Jumlah No ditambahkan
                    break;
            }
        }

        return hypotesisMap;
    }

    /**
     * Merupakan fungsi untuk menampilkan HMAP dari setiap attribute
     */
    private static void showHMAP() {
        System.out.println("<-- Show HMAP Zone -->");

        double totalCount = yesCount + noCount;

        for (Map.Entry<String, HypotesisItem> hypotesisItemEntry : hypotesisMap.entrySet()) {
            HypotesisItem hypotesisItem = hypotesisItemEntry.getValue();

            System.out.println("Attribute : " + hypotesisItemEntry.getKey());
            System.out.println("HMAP Yes : " + DECIMAL_FORMAT.format(hypotesisItem.getYes() / yesCount)); // Menampilkan HMAP yes attribute
            System.out.println("HMAP No : " + DECIMAL_FORMAT.format(hypotesisItem.getNo() / noCount)); // Menampilkan HMAP no attribute

            System.out.println("");
        }

        System.out.println("HMAP Yes total : " + DECIMAL_FORMAT.format(yesCount / totalCount)); // Menampilkan HMAP Yes
        System.out.println("HMAP No total : " + DECIMAL_FORMAT.format(noCount / totalCount)); // Menampilkan HMAP no

        System.out.println("");
    }

    /**
     * Merupakan fungsi untuk mencari jawaban dari data testing yang ada
     */
    private static void showResult() {
        System.out.println("<-- Testing Zone -->");

        double totalCount = yesCount + noCount; // Mengambil jumlah seluruh row data
        double yesHMAP = yesCount / totalCount; // Menghitung HMAP Yes data
        double noHMAP = noCount / totalCount; // Menghitung HMAP No data

        /*
         * Mengambil setiap row testing data
         */
        for (String[] attributesRow : testingAttributes) {
            double attributeYesHMAPTotal = 1; // Temporary HMAP seluruh yes attribute
            double attributeNoHMAPTotal = 1; // Temporary HMAP seluruh no attribute

            System.out.print("Attribute : ");

            /*
             * Mengambil setiap attribute data
             */
            for (String attribute : attributesRow) {
                HypotesisItem hypotesisItem = hypotesisMap.get(attribute);

                attributeYesHMAPTotal *= hypotesisItem.getYes() / yesCount; // Menghitung HMAP yes attribute
                attributeNoHMAPTotal *= hypotesisItem.getNo() / noCount; // Menghitung HMAP no attribute

                System.out.print(attribute + " ");
            }

            double yesResult = attributeYesHMAPTotal * yesHMAP; // Mencari HMAP kesimpulan yes data
            double noResult = attributeNoHMAPTotal * noHMAP; // Mencari HMAP kesimpuna no data

            System.out.println(", Result : " + (yesResult >= noResult ? "Yes" : "No")); // Menampilkan jawaban
        }
    }
}
