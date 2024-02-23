package com.mycompany.myapp.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.springframework.core.io.ResourceLoader;

import java.io.*;

public class WordToPdfConverter {
    public static void convert() {
        try {
            ClassLoader classLoader = ResourceLoader.class.getClassLoader();

            // Load the Word document
            InputStream wordFileInputStream = classLoader.getResourceAsStream("templates/report/test.docx");
            XWPFDocument document = new XWPFDocument(wordFileInputStream);

            // Create a PDF document
            PDDocument pdfDocument = new PDDocument();
            for (XWPFPictureData picture : document.getAllPictures()) {
                byte[] bytes = picture.getData();
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, bytes, "image");

                PDPage page = new PDPage();
                pdfDocument.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                    contentStream.drawImage(pdImage, 100, 100); // Adjust the position as needed
                }
            }

            // Save the PDF
            FileOutputStream pdfFileOutputStream = new FileOutputStream("output.pdf");
            pdfDocument.save(pdfFileOutputStream);
            pdfDocument.close();

            System.out.println("Word to PDF conversion completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
