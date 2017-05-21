package processing;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Josué on 17/05/2017.
 */
public class ResultsToHtml{

    private PrintWriter writer;

    public ResultsToHtml() {
        try{
            writer = new PrintWriter("process.html", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        initiateHTML();
    }

    public void writeTitle(String s){
        writeln("<h2>"+s+"</h2>");
    }

    public void writeElementStart(String id){
        writer.println("<div class='card' id='"+id+"'>");
    }

    public void writeCssElement(String id){
        writer.println("<div id='"+id+"'>");
    }
    public void writeEnd(){
        writer.println("</div>");
    }

    public void writeUl(){
        writer.println("<ul>");
    }

    public void writeInLi(String st, String key){
        writer.println("<li id='"+key+"'>"+st+"</li>");
    }

    public void writeUlEnd(){
        writer.println("</ul>");
    }


    public void write(String s) {
        writer.print("<p>"+s+"<p>");
    }

    public void writeln(String s) {
        writer.println("<p>"+s+"<p>");
    }

    public void initiateHTML(){
        writer.println("<!doctype html>");
        writer.println("<html lang=\"fr\"");
        writer.println("<head><meta charset='utf-8'><title>Island Processing</title>");
        writer.println("<link rel='stylesheet' href='style.css'");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<div class='container'>");
    }


    public void closeHTML(){
        writer.println("</div></body></html>");
    }

    public void close() {
        closeHTML();
        writer.close();
    }
}