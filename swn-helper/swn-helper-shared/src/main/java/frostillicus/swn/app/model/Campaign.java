package frostillicus.swn.app.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.jnosql.artemis.Column;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

@Entity
public class Campaign {
	@Id
	private String id;
	@Column
	@NotEmpty
	private String name;
	@Column
	private List<String> characterIds;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getCharacterIds() {
		return characterIds;
	}
	public void setCharacterIds(List<String> characterIds) {
		this.characterIds = characterIds;
	}
	
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Campaign [id=" + id + ", name=" + name + ", characterIds=" + characterIds + "]";
	}
}
