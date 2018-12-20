/*
 * Stuart's Raspberry Pi servlet
 * 
 * 
 */
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pi4j.io.gpio.*;
// import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
// import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Servlet implementation class thing
 */
@WebServlet("/thing")
public class thing extends HttpServlet {
	
	static {
		System.setProperty("pi4j.linking", "dynamic");
		}
	
	private static final long serialVersionUID = 1L;
	final GpioController gpio  ;
	final GpioPinDigitalOutput pin0;
	final GpioPinDigitalOutput pin1;
	final GpioPinDigitalOutput pin2;
	
	Timer timer = new Timer();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public thing() {
        super();
        //  constructor 
        // create gpio controller
        gpio = GpioFactory.getInstance();
        // provision gpio pin #01 as an output pin and turn off
        pin0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "redLED", PinState.LOW);
        pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "greenLED", PinState.LOW);
        pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "blueLED", PinState.LOW);
        // set shutdown state for this pin
        pin0.setShutdownOptions(true, PinState.LOW);
        pin1.setShutdownOptions(true, PinState.LOW);
        pin2.setShutdownOptions(true, PinState.LOW);
    
        // set timer going every 30 seconds to update status
        timer.schedule(new StatsTimer(), 30000, 30000);
    }

    /**
     * 
     * 
     */
    public class StatsTimer extends TimerTask {
    	  public void run() {
    	    // do timer stuff here
    		  }
    	}
     
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Initial request so just print out the buttons
		String qs = request.getQueryString();
		java.io.PrintWriter pw=response.getWriter();
		// if there's no query then assume ui and give some buttons
		if(qs==null) {
			
			pw.write("<body>"+ 
	                   "<p> Choose a color! </p>"+
	                   "<form action=\"thing\" method=\"POST\">"+
	                   "<input type=\"submit\" name=\"button\" value=\"red\"/>"+
	                   "</form>"+
	                   "<form action=\"thing\" method=\"POST\">"+
	                   "<input type=\"submit\" name=\"button\" value=\"green\"/>"+
	                   "</form>"+
	                   "<form action=\"thing\" method=\"POST\">"+
	                   "<input type=\"submit\" name=\"button\" value=\"yellow\"/>"+         
	                   "</form>"+
	                 "</body>"); 
			} else {
				// we have a query string
				// build our response
				StringBuilder sb= new StringBuilder();
				
				sb.append("<body><p>" + qs + "received</p><p>");
				
				if(qs.equals("red") ){
					pin0.toggle();
					sb.append("red toggled");
				}
				if(qs.equals("green")) {
					pin1.toggle();
					sb.append("green toggled");
				}
				if(qs.equals("yellow")) {
					pin2.toggle();
					sb.append("yellow toggled");
				}
				if (qs.equals("red:on")) {
					pin0.high();}
				if (qs.equals("red:off")) {
					pin0.low();}
				if(qs.equals("green:on")) {
					pin1.high();}
				if(qs.equals("green:off")) {
					pin1.low();}
				if (qs.equals("yellow:on")) {
					pin2.high();}
				if (qs.equals("yellow:off")) {
					pin2.low();}
				if (qs.equals("off")) { // turn all off
					pin0.low();
					pin1.low();
					pin2.low();
					sb.append("all off");}
				if (qs.equals("on")) {  // turn all on
					pin0.high();
					pin1.high();
					pin2.high();
					sb.append("all on");}
				
				// doQuery(qs);
				sb.append("</p></body>");
				pw.write(sb.toString()); 
			}
		
		}
	
	// execute our query
	void doQuery(String query) {
		if (query=="red:on") {pin0.high();}
		if (query=="red:off") {pin0.low();}
		if(query=="green:on") {pin1.high();}
		if(query=="green:off") {pin1.low();}
		if (query=="yellow:on") {pin2.high();}
		if (query=="yellow:off") {pin2.low();}
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		java.io.PrintWriter pw=response.getWriter();
		String qs = request.getQueryString();
		pw.write("<body>"+ qs +
                "<p> Choose a color! </p>"+
                "<form action=\"thing\" method=\"POST\">"+
                "<input type=\"submit\" name=\"button\" value=\"red\"/>"+
                "</form>"+
                "<form action=\"thing\" method=\"POST\">"+
                "<input type=\"submit\" name=\"button\" value=\"green\"/>"+
                "</form>"+
                "<form action=\"thing\" method=\"POST\">"+
                "<input type=\"submit\" name=\"button\" value=\"yellow\"/>"); 
		
		String act=request.getParameter("button"); 
	    switch(act) {
	      case "red":
	        pin0.toggle();// togglePin();
	        // redLed.high();
	        pw.write("<p>Red Clicked!</p>");
	        break;
	      case "green":
	    	  pin1.toggle();  //	  togglePin();
	   
	        pw.write("<p>Green LED Toggled</p>");
	        break;
	      case "yellow":
	    pin2.toggle();	//    togglePin();
	   	        pw.write("<p>Yellow Clicked!</p>");
	        break;
	    }
	    pw.write("</form></body>"); 
	}
}
