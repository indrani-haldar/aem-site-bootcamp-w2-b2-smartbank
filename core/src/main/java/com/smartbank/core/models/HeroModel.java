package com.smartbank.core.models;


	import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
	import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
	import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
	 
	@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	public class HeroModel {
	 
	    @ValueMapValue
	    private String title;
	 
	    @ValueMapValue
	    private String description;
	 
	    @ValueMapValue
	    private String fileReference;
	 
	    @ValueMapValue
	    private String ctaText;
	 
	    @ValueMapValue
	    private String ctaLink;
	 
	    public String getTitle() {
	        return title;
	    }
	 
	    public String getDescription() {
	        return description;
	    }
	 
	    public String getFileReference() {
	        return fileReference;
	    }
	 
	    public String getCtaText() {
	        return ctaText;
	    }
	 
	    public String getCtaLink() {
	        return ctaLink;
	    }
	}


