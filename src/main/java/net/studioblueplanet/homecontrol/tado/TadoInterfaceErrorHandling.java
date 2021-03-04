/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.studioblueplanet.homecontrol.tado.entities.TadoError;
import net.studioblueplanet.homecontrol.tado.entities.TadoErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatus;
import com.google.gson.Gson;

/**
 *
 * @author jorgen
 */
@Component
public class TadoInterfaceErrorHandling implements ResponseErrorHandler
{
    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException
    {

        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException
    {
        String errorMessage;
        InputStream inputStream=httpResponse.getBody();
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = inputStream.read()) != -1; ) 
        {
            sb.append((char) ch);
        }
        Gson gson = new Gson();
        
        TadoErrorMessage msg=gson.fromJson(sb.toString(), TadoErrorMessage.class);
        
        errorMessage=msg.getMessages();
        
        // second best:
        if (errorMessage==null)
        {
            errorMessage=httpResponse.getStatusText();
        }
        
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
        {
            // handle SERVER_ERROR
            throw new TadoException(TadoException.TadoExceptionType.SERVER_ERROR, 
                                    "Tado reported: "+httpResponse.getRawStatusCode()+" - "+errorMessage, 
                                    httpResponse.getRawStatusCode());

        } 
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
        {
            // handle CLIENT_ERROR
            throw new TadoException(TadoException.TadoExceptionType.CLIENT_ERROR, 
                                    "Tado reported: "+httpResponse.getRawStatusCode()+" - "+errorMessage, 
                                    httpResponse.getRawStatusCode());
        }
    }
}
