package com.example.kotlin;

/**
 * Merupakan class yang digunakan untuk menyimpan jumlah data yes dan no untuk setiap attribute
 *
 * @author Faza Zulfika P P
 * @version 1.0
 * @since 6 Oktober 2017
 */
public class HypotesisItem {

    /*
     * Merupakan variabel untuk menyimpan jumlah data yes dan no untuk setiap attribute
     */
    private int yes, no;

    /*
     * Default constructor
     */
    public HypotesisItem() {
        this(0, 0);
    }

    /*
     * Overloading constructor
     */
    public HypotesisItem(int yes, int no) {
        this.yes = yes;
        this.no = no;
    }

    /* Standard setter dan getter */

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    /* -------------------------- */
}
