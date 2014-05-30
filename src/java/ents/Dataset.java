/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ents;

import java.io.Serializable;

/**
 *
 * @author edsgICT-WB52
 */
public class Dataset implements Serializable {
    private static final long serialVersionUID = 1L;
        
    private String id;
    private String title;
    
    public Dataset(String id, String name){
       this.id = id;       
       this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }    
      
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dataset)) {
            return false;
        }
        Dataset other = (Dataset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ents.Dataset[ id=" + id + " ]";
    }
    
}
