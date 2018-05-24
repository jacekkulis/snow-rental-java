package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

@Entity
public class Image {
	@Id
	@Column(unique = true, updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private byte[] bytes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}