/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 *
 * @author jorgen
 */
public class TadoLanguage
{
    public static final String ENGHLISH="en";
    public static final String DUTCH   ="nl";
    public static final String SPANISH ="es";
    public static final String ITALIAN ="it";
    public static final String GERMAN  ="de";
    public static final String FRENCH  ="fr";
    public String language;
    
    public TadoLanguage(String language)
    {
        this.language=language;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }
}
