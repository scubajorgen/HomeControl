/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author jorgen
 */
public class DoubleSerializer extends StdSerializer<Double>
{
    DecimalFormat formatter=new DecimalFormat("##.#");

    public DoubleSerializer() {
        this(null);
    }
  
    public DoubleSerializer(Class<Double> t)
    {
        super(t);
    }
        
    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) 
            throws IOException, JsonProcessingException 
    {
        String stringValue;
        
        jgen.writeRawValue(formatter.format(value));
    }
} 