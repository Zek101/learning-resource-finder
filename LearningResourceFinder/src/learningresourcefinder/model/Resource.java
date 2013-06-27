package learningresourcefinder.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="resource")
public class Resource extends BaseEntity
{
	@Column(nullable=false, columnDefinition="VARCHAR(50)")
	private String title;
	
	@Column(columnDefinition="TEXT", nullable=false)
	private String description;
	
	@OneToMany(mappedBy="resource")
	private Set<Problem> problems;
	
	@OneToMany(mappedBy="resources")
	private Set<Program> programs = new HashSet<>();
	
	@ManyToOne
	private User user;
	
	public Resource() {
		this.problems = new HashSet<>();
	}
	
	/**************************** Getters *************************************/
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public User getUser() {
		return this.user;
	}
	
	/**************************** Setters *************************************/

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/**************************** Methods *************************************/
	
	public void addProblem(Problem p) {
		this.problems.add(p);
	}
	
	public void removeProblem(Problem p) {
		this.problems.remove(p);
	}
	
	public void getProgram( Program progr){
		programs.add(progr);
	}
	
	public void removeProgram(Program progr){
		programs.remove(progr);
	}
}
