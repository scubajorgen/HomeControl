/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.List;

/**
 *
 * @author jorgen
 */
public class TadoCharacteristics
{
    private List<String> capabilities;

    public List<String> getCapabilities()
    {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities)
    {
        this.capabilities = capabilities;
    }
}
