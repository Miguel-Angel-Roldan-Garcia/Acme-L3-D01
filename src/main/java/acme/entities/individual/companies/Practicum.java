
package acme.entities.individual.companies;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.individual.lectures.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Company;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Practicum extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	protected String code;

	@NotBlank
	@Length(max = 75)
	protected String title;

	@NotBlank
	@Length(max = 100)
	protected String abstract$;

	@NotBlank
	@Length(max = 100)
	protected String goals;

	protected boolean draftMode;

	// Derived attributes -----------------------------------------------------

	/* TODO Derived attribute estimatedTotalTime D02-S4-5 */

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Company company;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course course;

}
