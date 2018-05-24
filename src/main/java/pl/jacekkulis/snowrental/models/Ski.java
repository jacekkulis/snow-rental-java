package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

@Entity
public class Ski extends Item {

	private int radius;
	
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) radius;
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
		
		boolean isEqual = true;
	
		final Ski ski = (Ski) obj;
		if (radius != ski.radius)
			isEqual = false;

		return isEqual;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Ski [radius=").append("]");
		return builder.toString();
	}
}
