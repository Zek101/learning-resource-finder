package learningresourcefinder.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Task extends BaseEntity
{
	private String name;
	
    @Lob
    /*Forcing type definition to have text type column in postgresql instead of automatic indirect storage of large object (postgresql store lob in a separate table named pg_largeobject and store his id in the "content" column).
     *Without forcing, JDBC driver use write() method of the BlobOutputStream to store Clob into the database;
     * this method take an int as parameter an convert it into a byte causing lose of 3 byte information so character are render as ASCII instead of UTF-8 expected .
     * @see http://stackoverflow.com/questions/9993701/cannot-store-euro-sign-into-lob-string-property-with-hibernate-postgresql
     * @see http://stackoverflow.com/questions/5043992/postgres-utf-8-clobs-with-jdbc
     */
    @Type(type="org.hibernate.type.StringClobType")
    String description;
    
	@ManyToOne
	private User assigner;
	
	@ManyToOne
	private User student;
	
	@ManyToOne
	private Resource resource;
	
	@ManyToOne
	private PlayList playlist;
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssigner() {
		return this.assigner;
	}
	
	public User getUser() {
		return this.student;
	}
	
	public PlayList getPlayList() {
		return this.playlist;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAssigner(User assigner) {
		this.assigner = assigner;
	}

	public void setUser(User student) {
		this.student = student;
	}
	
	public void setPlayList(PlayList playlist) {
		this.playlist = playlist;
	}
}
