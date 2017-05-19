package processing.results;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Josué on 17/05/2017.
 */
public class ResultsToHtml implements Results {

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

    public void writeElementStart(){
        writeln("<div class='card'>");
    }
    public void writeElementEnd(){
        writeln("</div>");
    }

    @Override
    public void write(String s) {
        writer.print("<p>"+s+"<p>");
    }

    @Override
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
    @Override
    public void close() {
        closeHTML();
        writer.close();
    }
}
