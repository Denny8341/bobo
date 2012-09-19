/**
 * This software is licensed to you under the Apache License, Version 2.0 (the
 * "Apache License").
 *
 * LinkedIn's contributions are made under the Apache License. If you contribute
 * to the Software, the contributions will be deemed to have been made under the
 * Apache License, unless you expressly indicate otherwise. Please do not make any
 * contributions that would be inconsistent with the Apache License.
 *
 * You may obtain a copy of the Apache License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, this software
 * distributed under the Apache License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Apache
 * License for the specific language governing permissions and limitations for the
 * software governed under the Apache License.
 *
 * © 2012 LinkedIn Corp. All Rights Reserved.  
 */

package com.browseengine.bobo.geosearch.query;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Weight;

import com.browseengine.bobo.geosearch.impl.GeoConverter;
import com.browseengine.bobo.geosearch.impl.GeoUtil;

/**
 * Lucene query implementation that queries a geo index based on a centroid coordinate and a range.
 *
 * NOTE:  Early versions of this class took longitude before latitude in the constructor.  In order to be consistent throughout the library
 * this has been modified.  If you are upgrading from a prior version please take note of this change.
 * 
 * @author Shane Detsch
 * @author Ken McCracken
 *
 */
public class GeoQuery extends Query {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    float rangeInKm;
    GeoConverter geoConvertor;
    
    private static final float MINIMUM_RANGE_IN_KM = 0.0f;

    double centroidLatitude;
    double centroidLongitude;
    
    /**
     * NOTE:  Early versions of this class took longitude before latitude in the constructor.  In order to be consistent throughout the library
     * this has been modified.  If you are upgrading from a prior version please take note of this change.
     * 
     * @param centroidLatitude  Latitude in degrees
     * @param centroidLongitude  Longitude in degrees
     * @param rangeInKm  search radius in Kilometers
     */
    public GeoQuery(double centroidLatitude, double centroidLongitude, Float rangeInKm) {
        this.centroidLatitude = centroidLatitude;
        this.centroidLongitude = centroidLongitude;
        this.rangeInKm = rangeInKm;
        
        if (null == rangeInKm) {
            throw new IllegalArgumentException("please specify rangeInKilometers");
        }
       
        if (this.rangeInKm < MINIMUM_RANGE_IN_KM) {
            throw new IllegalArgumentException("range must be positive");
        }
        
        if(!GeoUtil.isValidLatitude(centroidLatitude)  || !GeoUtil.isValidLongitude(centroidLongitude)) {
            throw new IllegalArgumentException("bad latitude or longitude: " + centroidLatitude + ", " + centroidLongitude );
        }

    }
    
    /**
     * @return the centroidX
     */
    public double getCentroidLongitude() {
        return this.centroidLongitude;
    }
    /**
     * @return the centroidX
     */
    public double getCentroidLatitude() {
        return this.centroidLatitude;
    }
    
    public float getRangeInKm() {
        return this.rangeInKm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Weight createWeight(Searcher searcher) throws IOException {
        return new GeoWeight(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GeoQuery [centroidLatitude=" + this.centroidLatitude + ", centroidLongitude=" + this.centroidLongitude 
                + ", rangeInKm =" + rangeInKm + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(String arg0) {
        return toString();
    }

    

}
