package com.mycompany.myapp.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;
import java.util.stream.IntStream;

public class GenerateDocxUtils {
    private static XWPFDocument replace(XWPFDocument doc, Map<String, String> fields) {
        doc
            .getParagraphs()
            .stream()
            .flatMap(xwpfParagraph -> xwpfParagraph.getRuns().stream())
            .filter(xwpfRun -> xwpfRun.text().contains("${"))
            .forEach(xwpfRun -> fields
                .entrySet()
                .stream()
                .filter(field -> xwpfRun.text().contains("${" + field.getKey() + "}"))
                .forEach(field -> {
                    String[] values = field.getValue().split("\n");
                    String text = xwpfRun.text();
                    text = text.replace("${" + field.getKey() + "}", values[0]);
                    xwpfRun.setText(text, 0);
                    IntStream.range(1, values.length).forEach(i -> {
                        xwpfRun.addBreak();
                        xwpfRun.setText(values[i]);
                    });
                }));
        doc
            .getTables()
            .stream()
            .flatMap(xwpfTable -> xwpfTable.getRows().stream())
            .flatMap(xwpfTableRow -> xwpfTableRow.getTableCells().stream())
            .flatMap(xwpfTableCell -> xwpfTableCell.getParagraphs().stream())
            .flatMap(xwpfParagraph -> xwpfParagraph.getRuns().stream())
            .filter(xwpfRun -> xwpfRun.text().contains("${"))
            .forEach(xwpfRun -> fields
                .entrySet()
                .stream()
                .filter(field -> xwpfRun.text().contains("${" + field.getKey() + "}"))
                .forEach(field -> {
                    String text = xwpfRun.text();
                    text = text.replace("${" + field.getKey() + "}", field.getValue());
                    xwpfRun.setText(text, 0);
                }));
        return doc;
    }

    public static byte[] generateDocx(InputStream inputStream, Map<String, String> bean) throws IOException {
        //tao file word tu template
        bean
            .entrySet()
            .stream()
            .forEach(map -> {
                if (map.getValue() == null) {
                    bean.put(map.getKey(), "");
                }
            });
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {
            GenerateDocxUtils.replace(doc, bean);
            doc.write(baos);
        } catch (Exception e) {

        }
        return baos.toByteArray();
    }
}
