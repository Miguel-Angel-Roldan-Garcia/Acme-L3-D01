package acme.features.students.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.datatypes.Nature;
import acme.entities.individual.students.Activity;
import acme.entities.individual.students.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

    @Autowired
    protected StudentActivityRepository repository;

    @Override
    public void check() {
	boolean status;

	status = super.getRequest().hasData("masterId", int.class);

	super.getResponse().setChecked(status);
    }

    @Override
    public void authorise() {
	boolean status;
	int masterId;
	Enrolment enrolment;

	masterId = super.getRequest().getData("masterId", int.class);
	enrolment = this.repository.findOneEnrolmentById(masterId);
	status = enrolment != null && !enrolment.isDraftMode()
		&& super.getRequest().getPrincipal().hasRole(enrolment.getStudent());

	super.getResponse().setAuthorised(status);
    }

    @Override
    public void load() {
	Activity object;
	int masterId;
	Enrolment enrolment;

	masterId = super.getRequest().getData("masterId", int.class);
	enrolment = this.repository.findOneEnrolmentById(masterId);

	object = new Activity();
	object.setEnrolment(enrolment);
	object.setInitialDate(MomentHelper.getCurrentMoment());
	object.setFinishDate(MomentHelper.getCurrentMoment());

	super.getBuffer().setData(object);
    }

    @Override
    public void bind(final Activity object) {
	assert object != null;

	super.bind(object, "title", "abstract$", "initialDate", "finishDate", "link", "nature");
    }

    @Override
    public void validate(final Activity object) {
	assert object != null;
	if (!super.getBuffer().getErrors().hasErrors("initialDate")
		&& !super.getBuffer().getErrors().hasErrors("finishDate"))
	    super.state(MomentHelper.isBefore(object.getInitialDate(), object.getFinishDate()), "*",
		    "student.activity.form.error.not-valid-dates");
    }

    @Override
    public void perform(final Activity object) {
	assert object != null;

	this.repository.save(object);
    }

    @Override
    public void unbind(final Activity object) {
	assert object != null;

	Tuple tuple;
	SelectChoices choices;

	choices = SelectChoices.from(Nature.class, object.getNature());
	tuple = super.unbind(object, "title", "abstract$", "initialDate", "finishDate", "link", "nature");
	tuple.put("masterId", super.getRequest().getData("masterId", int.class));
	tuple.put("nature", choices.getSelected().getKey());
	tuple.put("natures", choices);
	super.getResponse().setData(tuple);
    }

}