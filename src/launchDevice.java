// START SNIPPET: blink-gpio-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  BlinkGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


class launchDevice  {

	Thread runner;
	String idTag;
	final GpioController gpio;
	static GpioPinDigitalOutput led1;
	public launchDevice() {
		 System.out.println("<--Pi4J--> GPIO Blink Example ... started.");
	          gpio = GpioFactory.getInstance();
	          led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
	   
	}


	public static void launch() {
       led1.blink(4000, 4000);
 	// (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
//         gpio.shutdown();   
//         led1.removeAllListeners();
	}

}