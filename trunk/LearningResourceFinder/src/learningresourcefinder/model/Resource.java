package learningresourcefinder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import learningresourcefinder.search.Searchable;
import learningresourcefinder.web.Slugify;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

@Entity
@Table(name="resource")
@SequenceGenerator(name="ResourceSequence", sequenceName="RESOURCE_SEQUENCE")
public class Resource extends BaseEntityWithShortId implements Searchable {
    
	@Id   @GeneratedValue(generator="ResourceSequence") // We wand Resources to have ids as short as possible (=> they get their own numbering and not the global HIBERNATE_SEQUENCE)
    Long id;
        
    @Column(length = 50, nullable=false)
    @Size(max=50, message="le num d'une ressource ne peut contenir que 50 caractères maximum")
	private String name;
	
    @Column(length=50, nullable=false)
    @Size(max=50)
    private String slug;
    
	@Column()
	private int numberImage;
	
	@Column()
	private String language;
	
	@Column()
	private String format;
	
	@Column()
	private String platform;
	
	@Column()
	private Boolean advertising;
	
	@Column()
	private Integer duration;
    
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;
	
	@OneToMany(mappedBy="resource")
	private Set<Problem> problems = new HashSet<>();

	@ManyToMany(mappedBy="resources")
	List<Competence> competences = new ArrayList<>();
	
	public Resource() {} // No arg constructor for Hibernate
	
	public Resource(String name, String description, User author) {
		this.name = name;
		this.description = description;
		this.slug = Slugify.slugify(name);
		this.createdBy = author;  // We are probably executing this constructor with params in a test batch code (=> no logged in user to be the author).
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@OneToMany(mappedBy="resource")
	List<UrlResource> urlResources = new ArrayList<>();
	
	public int addImageOnDB(){
		return numberImage;
	}
	
	
	/**************************** Getters & Setters ************************************/
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Problem> getProblems() {
		return problems;
	}

	public List<Competence> getProgramPoints() {
		return competences;
	}
	
	public List<UrlResource> getUrlResources() {
		return this.urlResources;
	}

    @Override
    public Map<String, String> getCriterias() {
        Map<String,String> criterias = new HashMap<String,String>();
        criterias.put("name",StringUtils.defaultIfEmpty(name,""));
        criterias.put("description",StringUtils.defaultIfEmpty(description,""));
        return criterias;
    }
    
    @Override
    public String getBoostedCriteriaName() {
        return "name";
    }

    @Override
    public Long getId() {
        return id;
    }
    
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

	public int getNumberImage() {
		return numberImage;
	}

	public void setNumberImage(int resourceid) {
		this.numberImage = resourceid;
	}

}