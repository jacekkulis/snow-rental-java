package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item {
	@Id
	@Column(unique = true, updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	@Column(length=1000)
	private String description;
	private String uniqueCode;
	
	private int size;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "imageId")
	private Image image;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) id;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((description == null) ? 0 : description.hashCode());
		result = (prime * result) + ((uniqueCode == null) ? 0 : uniqueCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final Item item = (Item) obj;
		if (id != item.id)
			return false;
		if (!name.equals(item.name))
			return false;
		if (!description.equals(item.description))
			return false;
		if (uniqueCode != item.uniqueCode)
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Item [id=").append(id).append(", name=").append(name).append(", description=")
				.append(description).append(", uniqueCode=").append(uniqueCode).append(", type=")
				.append(this.getClass()).append("]");
		return builder.toString();
	}
}
