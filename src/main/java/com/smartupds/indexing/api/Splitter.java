/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.api;

/**
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public interface Splitter {
    
    /**
     * Method to override and use for splitting a file.
     */
    public void split();
    
}
