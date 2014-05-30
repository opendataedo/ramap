/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ents;

/**
 *
 * @author edsgICT-WB52
 */
public class DatasetController {
    Dataset[] datasets;
    public DatasetController(){        
        datasets = loadDataset();
    }
    public String getDatasetId(String title){
        String id= "";
        for(Dataset dataset:datasets){
            if(dataset.getTitle().equals(title)){
                id = dataset.getId();
            }
        }
        return id;
    }
    public Dataset[] getDatasets(){
        return datasets;
    }
    private Dataset[] loadDataset(){              
        return new Dataset[]{
            new Dataset("disaggregated-budget-of-ministry-of-works-for-road-construction-per-lga","Disaggregated Budget of Ministry of Works for Road Construction Per LGA"),
            new Dataset("disaggregated-budgetary-allocation-for-water-resources-per-lga","Disaggregated Budgetary Allocation for Water Resources per LGA"),
            new Dataset("disaggregated-budgetary-allocation-for-rural-water-and-sanitation-per-lga","Disaggregated Budgetary Allocation for Rural Water and Sanitation per LGA"),
            new Dataset("disaggregated-budgetary-allocation-for-the-power-sector-per-lga","Disaggregated Budgetary Allocation for the Power Sector per LGA")                
        };               
    }
}
