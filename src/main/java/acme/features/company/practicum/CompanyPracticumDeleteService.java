/*
 * CompanyPracticumDeleteService.java
 *
 * Copyright (C) 2022-2023 Javier Fernández Castillo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.individual.companies.Practicum;
import acme.entities.individual.companies.PracticumSession;
import acme.entities.individual.lectures.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumDeleteService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;
		Company company;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		company = practicum == null ? null : practicum.getCompany();

		status = practicum != null && practicum.isDraftMode() && super.getRequest().getPrincipal().hasRole(company);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		super.bind(object, "code", "title", "abstract$", "goals");
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		super.state(object.isDraftMode(), "*", "company.practicum.form.error.not-draft-mode");
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		Collection<PracticumSession> practicumSessions;

		practicumSessions = this.repository.findManyPracticumSessionsByPracticumId(object.getId());
		this.repository.deleteAll(practicumSessions);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Tuple tuple;
		Collection<PracticumSession> practicumSessions;
		Double estimatedTotalTime;
		SelectChoices choices;
		Collection<Course> courses;

		courses = this.repository.findManyPublishedCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		practicumSessions = this.repository.findManyPracticumSessionsByPracticumId(object.getId());
		estimatedTotalTime = 0.;

		for (final PracticumSession ps : practicumSessions)
			estimatedTotalTime += ps.getDurationInHours();

		final double tenPercentEstimatedTotalTime = Math.round(estimatedTotalTime * 0.1 * 100.0) / 100.0;

		final String estimatedTotalTimePercent = Math.round(estimatedTotalTime * 100.0) / 100.0 + " (+/- " + tenPercentEstimatedTotalTime + ")";

		tuple = super.unbind(object, "code", "title", "abstract$", "goals", "draftMode");
		tuple.put("courseCode", this.repository.findCourseCodeByPracticumId(object.getId()));
		tuple.put("estimatedTotalTime", estimatedTotalTimePercent);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
