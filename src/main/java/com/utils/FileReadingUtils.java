package com.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public abstract class FileReadingUtils {

    public static String getTextFromWord(String filePath) {
        try {
            XWPFDocument docx = new XWPFDocument(new FileInputStream(filePath));
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            return we.getText();
        } catch (IOException e) {
            return "";
        }
    }

    public static String getTextFromDoc(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String text = "";

            while (scanner.hasNextLine()) {
               text += scanner.nextLine();
            }

            return text;
        } catch (IOException e) {
            return "";
        }
    }

    public static String getTextFromPdf(String filePath) {
        try {
            File file = new File(filePath);
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();
            return text;
        } catch (IOException e) {
            return "";
        }
    }
}
