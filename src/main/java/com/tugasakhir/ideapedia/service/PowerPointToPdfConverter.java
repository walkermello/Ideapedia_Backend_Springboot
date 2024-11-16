package com.tugasakhir.ideapedia.service;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PowerPointToPdfConverter {

    public static File convertPPTToPDF(File pptFile) throws IOException {
        // Load PPT file
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptFile));
        Dimension slideSize = ppt.getPageSize();

        // Create a new PDF document
        PDDocument pdfDocument = new PDDocument();

        // Scale factor for high-quality rendering
        double scale = 2.0; // Adjust scale (e.g., 2.0 for 2x resolution)
        int scaledWidth = (int) (slideSize.getWidth() * scale);
        int scaledHeight = (int) (slideSize.getHeight() * scale);

        // Iterate through slides
        for (XSLFSlide slide : ppt.getSlides()) {
            // Render slide as high-resolution image
            BufferedImage slideImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = slideImage.createGraphics();

            // Set rendering hints for high quality
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            // Fill background with white color
            graphics.setPaint(Color.WHITE);
            graphics.fillRect(0, 0, scaledWidth, scaledHeight);

            // Scale graphics and draw slide content
            graphics.scale(scale, scale);
            slide.draw(graphics);
            graphics.dispose();

            // Add slide image to PDF
            PDPage pdfPage = new PDPage(new PDRectangle((float) slideSize.getWidth(), (float) slideSize.getHeight()));
            pdfDocument.addPage(pdfPage);

            // Write slide image to PDF content stream
            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, pdfPage);
            PDImageXObject pdfImage = org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory.createFromImage(pdfDocument, slideImage);
            contentStream.drawImage(pdfImage, 0, 0, (float) slideSize.getWidth(), (float) slideSize.getHeight());
            contentStream.close();
        }

        // Save PDF to file
        File pdfFile = new File("output.pdf");
        pdfDocument.save(new FileOutputStream(pdfFile));
        pdfDocument.close();
        return pdfFile;
    }

    public static void main(String[] args) {
        try {
            File pptFile = new File("input.pptx");
            File pdfFile = convertPPTToPDF(pptFile);
            System.out.println("PDF file saved at: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
