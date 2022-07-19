package com.community.aemlab.core.models.concept;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdaptiveImageModel {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String title;
    
    @ValueMapValue
    @Default(values = "span")
    private String titleStyle;

    @ValueMapValue
    private String file2Reference;
    

    public String getTitle() {
        return title;
    }

    public String getFileReference2() {
        return file2Reference;
    }
    
    public String getTitleStyle() {
        return titleStyle;
    }

    public String getSerset() {
        return "";
    }

}
