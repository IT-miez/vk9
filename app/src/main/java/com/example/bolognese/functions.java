package com.example.bolognese;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class functions {

    private ArrayList<Struct> theatre_list = new ArrayList<Struct>();
    private ArrayList<String> theatre_list_spinner = new ArrayList<String>();
    private ArrayList<Shows> shows_list = new ArrayList<Shows>();



    public ArrayList<String> readXML ()  {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            String vaara1 = "1014";
            String vaara2 = "1002";
            String vaara3 = "1012";
            String vaara4 = "1021";

            for (int i = 0; i < nList.getLength(); i++){
                Node node = nList.item(i);
                System.out.println("Element is this; " + node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (element.getElementsByTagName("ID").item(0).getTextContent().equals(vaara1) || element.getElementsByTagName("ID").item(0).getTextContent().equals(vaara2) || element.getElementsByTagName("ID").item(0).getTextContent().equals(vaara3) || element.getElementsByTagName("ID").item(0).getTextContent().equals(vaara4)) {

                    } else {
                        theatre_list.add(new Struct(element.getElementsByTagName("Name").item(0).getTextContent(), element.getElementsByTagName("ID").item(0).getTextContent()));
                        theatre_list_spinner.add(element.getElementsByTagName("Name").item(0).getTextContent());
                    }

                    System.out.print("Name: ");
                    System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());
                    System.out.print("ID: ");
                    System.out.println(element.getElementsByTagName("ID").item(0).getTextContent());
                }


            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException saxException) {
            saxException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return theatre_list_spinner;
    }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getURL(String chosenTheatre){
            String first = "https://www.finnkino.fi/xml/Schedule/?area=";
            String second = "&dt=";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDateTime now = LocalDateTime.now();
            String timenow = dtf.format(now);
            System.out.println(timenow);
            String givenID = "";

            for (int i = 0; i < theatre_list.size(); i++){
                String compare = theatre_list.get(i).getName();
                if(compare.equals(chosenTheatre)){
                    givenID = theatre_list.get(i).getID();
                }
            }

            String tulos = first + givenID + second + timenow;
            System.out.println(tulos);
            return tulos;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public String listShows(String chosenTheatre, String data) {
            try {
                shows_list.clear();
                String URLL = getURL(chosenTheatre);
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                String urlString = URLL;
                System.out.println(urlString);
                Document doc = builder.parse(urlString);
                doc.getDocumentElement().normalize();
                System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    System.out.println("Element is this; " + node.getNodeName());
                    String delims = "T";

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        String [] tokens = element.getElementsByTagName("dttmShowStart").item(0).getTextContent().split(delims);
                        data = data + "Title: "+element.getElementsByTagName("Title").item(0).getTextContent() + "\n" + "Kesto (min): " +element.getElementsByTagName("LengthInMinutes").item(0).getTextContent() + "\n" + "Start time: "+ tokens[1] +"\n\n";
                    }
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException saxException) {
                saxException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return data;
        }
    }
