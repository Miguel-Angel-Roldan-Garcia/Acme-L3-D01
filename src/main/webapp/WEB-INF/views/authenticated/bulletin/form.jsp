<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="authenticated.bulletin.label.instantiationMoment" path="instantiationMoment"/>
	<acme:input-textbox code="authenticated.bulletin.label.title" path="title"/>
	<acme:input-textarea code="authenticated.bulletin.label.message" path="message"/>
	<acme:input-textbox code="authenticated.bulletin.label.critical" path="critical"/>
	<acme:input-textbox code="authenticated.bulletin.label.link" path="link"/>
	
</acme:form>