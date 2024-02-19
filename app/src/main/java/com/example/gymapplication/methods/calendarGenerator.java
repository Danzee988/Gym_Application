package com.example.gymapplication.methods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class calendarGenerator {

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Create root element: <calendar>
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("calendar");
            doc.appendChild(rootElement);

            // Generate months dynamically for a year
            for (int monthNum = 0; monthNum < 12; monthNum++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 2024);
                calendar.set(Calendar.MONTH, monthNum);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
                String monthName = monthFormat.format(calendar.getTime());

                Element monthElement = doc.createElement("month");
                monthElement.setAttribute("name", monthName);

                // Generate days dynamically for each month
                while (calendar.get(Calendar.MONTH) == monthNum) {
                    Element dayElement = doc.createElement("day");
                    dayElement.setAttribute("date", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
                    Text dayText = doc.createTextNode(
                            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH) +
                                    ", " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) +
                                    " " + calendar.get(Calendar.DAY_OF_MONTH));
                    dayElement.appendChild(dayText);
                    monthElement.appendChild(dayElement);

                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                rootElement.appendChild(monthElement);
            }

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File("calendar.xml"));
            StreamResult result = new StreamResult(new File("/app/src/main/res/layout/calendar.xml"));
            transformer.transform(source, result);

            System.out.println("XML file generated successfully!");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
