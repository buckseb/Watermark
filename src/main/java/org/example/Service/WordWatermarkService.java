package org.example.Service;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.vml.CTShape;
import org.docx4j.vml.ObjectFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class WordWatermarkService {

    public InputStream addWatermark(InputStream inputStream, String watermarkText) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

        // Example: Adding tilted watermarks at specific positions
        addTiltedWatermarks(documentPart, watermarkText);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wordMLPackage.save(outputStream); // Save changes to output stream
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void addTiltedWatermarks(MainDocumentPart documentPart, String watermarkText) throws JAXBException {
        ObjectFactory vmlObjectFactory = new ObjectFactory();

        // Define positions for tilted watermarks
        int[] xPositions = {1000, 1500, 2000}; // X positions
        int[] yPositions = {1000, 1500, 2000}; // Y positions

        for (int i = 0; i < xPositions.length; i++) {
            // Create VML shape for watermark
            CTShape shape = createWatermarkShape(vmlObjectFactory, watermarkText, xPositions[i], yPositions[i]);

            // Add shape to document part
            documentPart.addObject(shape);
        }
    }

    private CTShape createWatermarkShape(ObjectFactory vmlObjectFactory, String watermarkText, int xPosition, int yPosition) {
        CTShape shape = vmlObjectFactory.createCTShape();

        // Set shape properties for watermark
        shape.setType("#_x0000_t136");
        shape.setStyle("position:absolute;margin-left:" + xPosition + "pt;margin-top:" + yPosition + "pt;width:200pt;height:30pt;rotation:45;");
        shape.setFillcolor("#ffffff");

        // Create text path for the watermark text
        JAXBElement<org.docx4j.vml.CTTextPath> textPathElement = createTextpath(vmlObjectFactory, watermarkText);
        shape.getEGShapeElements().add(textPathElement);

        return shape;
    }

    private JAXBElement<org.docx4j.vml.CTTextPath> createTextpath(ObjectFactory vmlObjectFactory, String text) {
        org.docx4j.vml.CTTextPath textpath = vmlObjectFactory.createCTTextPath();
        textpath.setStyle("font-family:'Arial';font-size:1pt");
        textpath.setString(text);

        JAXBElement<org.docx4j.vml.CTTextPath> textPathElement = new JAXBElement<>(
                new QName("urn:schemas-microsoft-com:vml", "textpath"),
                org.docx4j.vml.CTTextPath.class,
                textpath
        );
        return textPathElement;
    }
}
