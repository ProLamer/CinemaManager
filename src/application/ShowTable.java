package application;

public class ShowTable {
	private String administrator;
	private String numOfBooked;
	private String numOfPaid;
	private String salary;
	public ShowTable(String administrator, String numOfBooked, String numOfPaid, String salary) {
		super();
		this.administrator = administrator;
		this.numOfBooked = numOfBooked;
		this.numOfPaid = numOfPaid;
		this.salary = salary;
	}
	public String getAdministrator() {
		return administrator;
	}
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	public String getNumOfBooked() {
		return numOfBooked;
	}
	public void setNumOfBooked(String numOfBooked) {
		this.numOfBooked = numOfBooked;
	}
	public String getNumOfPaid() {
		return numOfPaid;
	}
	public void setNumOfPaid(String numOfPaid) {
		this.numOfPaid = numOfPaid;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((administrator == null) ? 0 : administrator.hashCode());
		result = prime * result + ((numOfBooked == null) ? 0 : numOfBooked.hashCode());
		result = prime * result + ((numOfPaid == null) ? 0 : numOfPaid.hashCode());
		result = prime * result + ((salary == null) ? 0 : salary.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShowTable other = (ShowTable) obj;
		if (administrator == null) {
			if (other.administrator != null)
				return false;
		} else if (!administrator.equals(other.administrator))
			return false;
		if (numOfBooked == null) {
			if (other.numOfBooked != null)
				return false;
		} else if (!numOfBooked.equals(other.numOfBooked))
			return false;
		if (numOfPaid == null) {
			if (other.numOfPaid != null)
				return false;
		} else if (!numOfPaid.equals(other.numOfPaid))
			return false;
		if (salary == null) {
			if (other.salary != null)
				return false;
		} else if (!salary.equals(other.salary))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ShowTable [administrator=" + administrator + ", numOfBooked=" + numOfBooked + ", numOfPaid=" + numOfPaid
				+ ", salary=" + salary + "]";
	}
	
	
}
