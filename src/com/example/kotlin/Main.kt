package com.example.kotlin

import java.io.*
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.HashMap

/**
 * Program Naive Bayes
 *
 * @author Faza Zulfika P P
 * @version 1.0
 * @since 6 Oktober 2017
 */
object Main {

    /**
     * trainingAttributes merupakan data training untuk mencari hipotesa data
     * testingAttributes merupakan data testing untuk mencari kesimpulan berdasarkan hipotesa yang ditemukan
     */
    private var trainingAttributes: Array<Array<String>>? = null
    private var testingAttributes: Array<Array<String>>? = null

    /**
     * TRAINING_DATA_FILES merupakan lokasi data training
     */
    private val TRAINING_DATA_FILES = "G:\\Materi Semester 5\\Machine Learning\\Bayesian\\Example-1\\data\\TrainingData.txt"

    /**
     * TESTING_DATA_FILES merupakan lokasi data testing
     */
    private val TESTING_DATA_FILES = "G:\\Materi Semester 5\\Machine Learning\\Bayesian\\Example-1\\data\\TestingData.txt"

    /**
     * hypotesisMap merupakan data hipotesa
     */
    private var hypotesisMap: HashMap<String, HypotesisItem>? = null

    /**
     * yesCount merupakan jumlah data Yes
     * noCount merupakan jumlah data No
     */
    private var yesCount: Double = 0.toDouble()
    private var noCount: Double = 0.toDouble()

    /**
     * DECIMAL_FORMAT digunakan agar data decimal yang ditampilkan hanya memiliki 2 angka dibelakang koma
     */
    private val DECIMAL_FORMAT = DecimalFormat("#.##")

    /**
     * Merupakan fungsi main untuk menjalankan program java
     *
     * @param args merupakan argumen dari user
     */
    @JvmStatic
    fun main(args: Array<String>) {
        trainingAttributes = setData(TRAINING_DATA_FILES) // Mengambil data training
        testingAttributes = setData(TESTING_DATA_FILES) // Mengambil data testing
        hypotesisMap = setHypotesis() // Membuat hipotesa dari data training

        showHMAP() // Menampilkan HMAP seluruh data
        showResult() // Menampilkan hasil dari testing dengan data testing
    }

    /**
     * Merupakan fungsi yang digunakan untuk membaca data dari dalam file dan mengconvertnya menjadi bentuk array of string dua dimensi
     *
     * @param filePath merupakan lokasi file yang ingin dibaca isinya, setiap kata pada file haruslah dipisahkan dengan spasi (" ")
     * @return merupakan isi file dalam bentuk array of string dua dimensi
     */
    private fun setData(filePath: String): Array<Array<String>>? {
        val resultList = ArrayList<Array<String>>() // List untuk menampung sementara isi file
        var attributes: Array<Array<String>>? = null // Array of string yang akan dikembalikan

        try {
            val file = File(filePath) // Membuat object file dari file yang diinginkan
            val br = BufferedReader(FileReader(file)) // Membuat object untuk membaca file
            var str: String? = ""

            /*
             * Looping untuk membaca keseluruhan isi file
             */
            while (str != null) {
                str = br.readLine() // Mengambil setiap baris file

                if (str != null)
                // Jika baris ada
                    resultList.add(str.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) // Memisahkan setiap kata menurut spasi (" ")
            }
        } catch (e: FileNotFoundException) { // Jika file tidak ditemukan
            println("File tidak ditemukan")
        } catch (e: IOException) { // Jika terjadi kesalahan saat membaca file
            println("Kesalahan saat membaca file")
        } finally {
            if (resultList.size > 0) { // Jika file tidak kosong
                val rowSize = resultList.size
                val columnSize = resultList[0].size

                attributes = Array(rowSize) { arrayOfNulls(columnSize) } // Membuat array of string berukuran data
                attributes = resultList.toTypedArray<Array<String>>() // Menyimpan data dari list ke array of string
            }
        }

        return attributes // Mengembalikan data
    }

    /**
     * Merupakan fungsi untuk mengetahui jumlah setiap attribute pada setiap jawaban yang ada (Yes / No)
     *
     * @return Merupakan hashmap dengan key yang merupakan attribute, dan value yang merupakan hypotesa item
     */
    private fun setHypotesis(): HashMap<String, HypotesisItem> {
        val hypotesisMap = HashMap<String, HypotesisItem>()

        yesCount = 0.0
        noCount = 0.0

        /*
         * Mengambil setiap row data training
         */
        for (attributesRow in trainingAttributes!!) {
            val attributesRowLength = attributesRow.size - 1 // Mengambil jumlab attribute setiap row
            val answer = attributesRow[attributesRowLength] // Mengambil jawaban dari setiap row

            /*
             * Mengambil setiap attribute pada setiap row
             */
            for (i in 0..attributesRowLength - 1) {
                val attribute = attributesRow[i] // Mengambil attribute
                var hypotesisItem: HypotesisItem? = hypotesisMap[attribute] // Mengambil hypotesa pada setiap attribute

                /*
                 * Jika attribute baru / belum pernah disimpan
                 */
                if (hypotesisItem == null) {
                    hypotesisItem = HypotesisItem() //  Membuat hypotesa item baru
                    hypotesisMap.put(attribute, hypotesisItem) //  Memasukkan hypotesa item untuk attribute baru
                }

                /*
                 * Mengecek jawaban dari row ini
                 */
                when (answer) {
                    "Yes" // Jika Yes
                    -> hypotesisItem.yes = hypotesisItem.yes + 1 // Jumlah Yes attribute tersebut ditambahkan
                    "No" // Jika NO
                    -> hypotesisItem.no = hypotesisItem.no + 1 // Jumlah No attribute tersebut ditambahkan
                }
            }

            /*
             * Mengecek jawaban dari row ini
             */
            when (answer) {
                "Yes" // Jika Yes
                -> yesCount++ // Jumlah Yes ditambahkan
                "No" // Jika NO
                -> noCount++ // Jumlah No ditambahkan
            }
        }

        return hypotesisMap
    }

    /**
     * Merupakan fungsi untuk menampilkan HMAP dari setiap attribute
     */
    private fun showHMAP() {
        println("<-- Show HMAP Zone -->")

        val totalCount = yesCount + noCount

        for ((key, hypotesisItem) in hypotesisMap!!) {

            println("Attribute : " + key)
            println("HMAP Yes : " + DECIMAL_FORMAT.format(hypotesisItem.yes / yesCount)) // Menampilkan HMAP yes attribute
            println("HMAP No : " + DECIMAL_FORMAT.format(hypotesisItem.no / noCount)) // Menampilkan HMAP no attribute

            println("")
        }

        println("HMAP Yes total : " + DECIMAL_FORMAT.format(yesCount / totalCount)) // Menampilkan HMAP Yes
        println("HMAP No total : " + DECIMAL_FORMAT.format(noCount / totalCount)) // Menampilkan HMAP no

        println("")
    }

    /**
     * Merupakan fungsi untuk mencari jawaban dari data testing yang ada
     */
    private fun showResult() {
        println("<-- Testing Zone -->")

        val totalCount = yesCount + noCount // Mengambil jumlah seluruh row data
        val yesHMAP = yesCount / totalCount // Menghitung HMAP Yes data
        val noHMAP = noCount / totalCount // Menghitung HMAP No data

        /*
         * Mengambil setiap row testing data
         */
        for (attributesRow in testingAttributes!!) {
            var attributeYesHMAPTotal = 1.0 // Temporary HMAP seluruh yes attribute
            var attributeNoHMAPTotal = 1.0 // Temporary HMAP seluruh no attribute

            print("Attribute : ")

            /*
             * Mengambil setiap attribute data
             */
            for (attribute in attributesRow) {
                val hypotesisItem = hypotesisMap!![attribute]

                attributeYesHMAPTotal *= hypotesisItem.getYes() / yesCount // Menghitung HMAP yes attribute
                attributeNoHMAPTotal *= hypotesisItem.getNo() / noCount // Menghitung HMAP no attribute

                print(attribute + " ")
            }

            val yesResult = attributeYesHMAPTotal * yesHMAP // Mencari HMAP kesimpulan yes data
            val noResult = attributeNoHMAPTotal * noHMAP // Mencari HMAP kesimpuna no data

            println(", Result : " + if (yesResult >= noResult) "Yes" else "No") // Menampilkan jawaban
        }
    }
}
