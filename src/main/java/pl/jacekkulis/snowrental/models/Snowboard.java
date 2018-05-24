package pl.jacekkulis.snowrental.models;

import javax.persistence.*;

@Entity
public class Snowboard extends Item {

	private String flex;
	
	public String getFlex() {
		return flex;
	}
	public void setFlex(String flex) {
		this.flex = flex;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((flex == null) ? 0 : flex.hashCode());
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

		final Snowboard snowboard = (Snowboard) obj;
		if (!flex.equals(snowboard.flex))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Snowboard [flex=").append(flex).append("]");
		return builder.toString();
	}
}
