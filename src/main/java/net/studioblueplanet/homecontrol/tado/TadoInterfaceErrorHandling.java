/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.io.IOException;


import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatus;

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

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
        {
            // handle SERVER_ERROR
            throw new TadoException(TadoException.TadoExceptionType.SERVER_ERROR, httpResponse.getStatusText(), httpResponse.getStatusCode().value());

        } 
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
        {
            throw new TadoException(TadoException.TadoExceptionType.CLIENT_ERROR, httpResponse.getStatusText(), httpResponse.getStatusCode().value());
            // handle CLIENT_ERROR
/*
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                throw new TadoNotFoundException();
            }
            else if (httpResponse.getStatusCode() == HttpStatus.UNAUTHORIZED)
            {
                throw new TadoUnauthorizedException();
            }
            else if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST)
            {
                throw new TadoBadRequestException();
            }
*/
        }
    }
}
