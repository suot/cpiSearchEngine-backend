package com.uwindsor.cpisearch.Util;


import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***************************************************************
 *
 *  Compilation:  javac Brtue.java
 *  Execution:    java Brute pattern text
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using brute force.
 *
 *  % java Brute abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *
 *  % java Brute rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:         rab
 *
 *  % java Brute rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad

 *
 *  % java Brute bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                                   bcara
 *
 *  % java Brute abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ***************************************************************/

public class BruteForceMatch {

    /***************************************************************************
     *  String versions
     ***************************************************************************/

    // return offset of first match or N if no match
    public static int search1(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++) {
                if (txt.charAt(i+j) != pat.charAt(j))
                    break;
            }
            if (j == M) return i;            // found at offset i
        }
        return N;                            // not found
    }

    // return offset of first match or N if no match
    public static int search2(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            if (txt.charAt(i) == pat.charAt(j)) j++;
            else { i -= j; j = 0;  }
        }
        if (j == M) return i - M;    // found
        else        return N;        // not found
    }


    /***************************************************************************
     *  char[] array versions
     ***************************************************************************/

    // return offset of first match or N if no match
    public static int search1(char[] pattern, char[] text) {
        int M = pattern.length;
        int N = text.length;

        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++) {
                if (text[i+j] != pattern[j])
                    break;
            }
            if (j == M) return i;            // found at offset i
        }
        return N;                            // not found
    }

    // return offset of first match or N if no match
    public static int search2(char[] pattern, char[] text) {
        int M = pattern.length;
        int N = text.length;
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            if (text[i] == pattern[j]) j++;
            else { i -= j; j = 0;  }
        }
        if (j == M) return i - M;    // found
        else        return N;        // not found
    }


    public static String offset_search(String word, String text){
        int position = search1(word, text);
        int from = position-100;
        int to = position+100;

        if(from <= 0)
            from = 0;

        if (to >= text.length())
            to = text.length()-1;

        return text.substring(from, to);

//        String prefix = "<font color=\"red\">";
//        String postfix = "</font>";
//        String textWithHighlight = text.substring(from, position-1) + prefix + text.substring(position, position+word.length()-1) + postfix + text.substring(position+word.length(), to);
//
//        return textWithHighlight;
    }


    // test client

}