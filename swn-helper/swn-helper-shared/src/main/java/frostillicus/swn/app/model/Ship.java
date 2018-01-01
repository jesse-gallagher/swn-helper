package frostillicus.swn.app.model;

import javax.validation.constraints.NotEmpty;

import org.jnosql.artemis.Column;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

@Entity
public class Ship {
	@Id
	private String id;
	
	@Column
	@NotEmpty
	private String name;

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

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Ship [id=" + id + ", name=" + name + "]";
	}

}
