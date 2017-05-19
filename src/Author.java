public class Author {

	private String idAuthor;
	private String name;
	private String company;
	private String email;
	private String role;
		
	public Author(String idAuthor, String name, String company, String email, String role) {
		this.idAuthor = idAuthor;
		this.name = name;
		this.company = company;
		this.email = email;
		this.role = role;
	}
	//pisello
	public void setIdAuthor(String idAuthor) {
		this.idAuthor = idAuthor;
	}

	public String getIdAuthor() {
		return this.idAuthor;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return this.company;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return this.role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((idAuthor == null) ? 0 : idAuthor.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		Author other = (Author) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (idAuthor == null) {
			if (other.idAuthor != null)
				return false;
		} else if (!idAuthor.equals(other.idAuthor))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Author: \t" + idAuthor + "\nName: \t\t" + name + "\nCompany: \t" + company + 
				"\nEmail: \t\t" + email + "\nRole: \t\t" + role ;
	}
	
	public static void main(String[] args) {
		Author a1, a2, a3;
		
		a1 = new Author("1a3","Alessandro Salvato", "ARM", "sandrosalvato94@live.it",
				        "Digital Designer at RTL");
		a2 = new Author("2e7","Emanuele Parisi", "Bionformatics", "ema_par93@yahoo.it",
		        "PhD");
		a3 = new Author("8a0","Giacomo Vitantonio", "ARM", "giacomino@live.it",
		        "PhD");
		
		System.out.println(a1.toString());
		System.out.println(a2.toString());
		System.out.println(a3.toString());
	}

}
