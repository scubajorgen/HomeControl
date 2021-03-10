/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.io.IOException;
import java.time.LocalTime;
/**
 *
 * @author jorgen
 */
public class LocalTimeSerializer  extends JsonSerializer<LocalTime>
{
    @Override
    public void serialize(LocalTime arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException 
    {
        arg1.writeString(arg0.toString());  
    }
}
