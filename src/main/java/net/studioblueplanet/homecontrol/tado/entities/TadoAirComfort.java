/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.List;

/**
 * Indication for the air freshness
 * @author jorgen
 */
public class TadoAirComfort
{
    /** Indication for in home freshness */
    private TadoFreshness           freshness;
    
    /** Indication for comfort per room/zone */
    private List<TadoRoomComfort>   comfort;

    public TadoFreshness getFreshness()
    {
        return freshness;
    }

    public void setFreshness(TadoFreshness freshness)
    {
        this.freshness = freshness;
    }

    public List<TadoRoomComfort> getComfort()
    {
        return comfort;
    }

    public void setComfort(List<TadoRoomComfort> comfort)
    {
        this.comfort = comfort;
    }
}
