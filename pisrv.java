package com.luca.servlet;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;

public class MyServlet extends javax.servlet.http.HttpServlet {

  private GpioController gpio=GpioFactory.getInstance();
  private GpioPinDigitalOutput redLed=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23,PinState.LOW);
  private GpioPinDigitalOutput greenLed=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22,PinState.LOW);
  private GpioPinDigitalOutput blueLed=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21,PinState.LOW);
  private GpioPinDigitalOutput[] pins=new GpioPinDigitalOutput[]{redLed,greenLed,blueLed};

  @Override
  public void doGet(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
    java.io.PrintWriter print=response.getWriter();
    print.write("<body>"+ 
                   "<p> Choose a color! </p>"+
                   "<form action=\"first\" method=\"POST\">"+
                   "<input type=\"submit\" name=\"button\" value=\"red\"/>"+
                   "</form>"+
                   "<form action=\"first\" method=\"POST\">"+
                   "<input type=\"submit\" name=\"button\" value=\"green\"/>"+
                   "</form>"+
                   "<form action=\"first\" method=\"POST\">"+
                   "<input type=\"submit\" name=\"button\" value=\"blue\"/>"+         
                   "</form>"+
                 "</body>"); 
  }

  public void doPost(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
    java.io.PrintWriter pw=response.getWriter();
    String act=request.getParameter("button"); 
    switch(act) {
      case "red":
        togglePin();
        redLed.high();
        pw.write("<p>the led is red!</p>");
        break;
      case "green":
        togglePin();
        greenLed.high();
        pw.write("<p>the led is green</p>");
        break;
      case "blue":
        togglePin();
        blueLed.high();
        pw.write("<p>the led is blue!</p>");
        break;
    }
  }

  private void togglePin() {
    for (GpioPinDigitalOutput pin : pins) 
      if (pin.isHigh()) pin.toggle();
  }
}
