/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.api;

/** This class is responsible for indexing resources
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public interface IndexGenerator {
        
    /** This method is responsible for adding indexes to resources for a particular 
     * knowledge base (e.g. a triplestore).
     * 
     * @param core_name
     */
    public void indexResources(String core_name);
    public void downloadQueries();
}
