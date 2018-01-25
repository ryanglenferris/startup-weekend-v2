import dao.Attendee_DAO;
import dao.Attendee_DAO_SQL2O;
import dao.Event_DAO;
import dao.Event_DAO_SQL2O;
import models.Attendee;
import models.Event;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {

        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/startupweekend.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Event_DAO eventDao = new Event_DAO_SQL2O(sql2o);
        Attendee_DAO attendeeDao= new Attendee_DAO_SQL2O(sql2o);

     /* CREATE */
        //event page
        post("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            String newEventTitle = request.queryParams("event-title");
            String newEventDescription = request.queryParams("event-description");
            Event newEvent = new Event(newEventTitle, newEventDescription);

            eventDao.addEvent(newEvent);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        //new attendees
        post("/events/:event_id/attendee/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idEventAttending = Integer.parseInt(request.params("event_id"));

            String newAttendeeName = request.queryParams("attendee-name");

            Attendee newAttendee = new Attendee(newAttendeeName, idEventAttending);
            attendeeDao.addAttendee(newAttendee);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

      /* READ */
        //home page
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> events = eventDao.getAllEvents();
            model.put("events", events);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //events page
        get("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        //event details
        get("/events/:event_id", (Request request, Response response) -> {
            Map<String, Object> model = new HashMap<>();
            int event_id = Integer.parseInt(request.params("event_id"));

            Event event = eventDao.findByIdEvent(event_id);
            model.put("event", event);

            List<Attendee> attendees = eventDao.getAllAttendeesByEvent(event_id);
            model.put("attendees", attendees);

            return new ModelAndView(model, "event-details.hbs");
        }, new HandlebarsTemplateEngine());

        //attendee details
        get("/events/:event_id/attendees/:attendee_id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int attendee_id = Integer.parseInt(request.params("attendee_id"));

            Attendee selectedAttendee = attendeeDao.findByIdAttendee(attendee_id);
            model.put("selectedAttendee", selectedAttendee);

            return new ModelAndView(model, "");
        }, new HandlebarsTemplateEngine());

    /* UPDATE */
        //event details
        get("/events/:event_id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int event_id = Integer.parseInt(request.params("event_id"));

            Event updateEvent = eventDao.findByIdEvent(event_id);
            model.put("updateEvent", updateEvent);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        //ATTENDEE DETAILS
        //route1
        get("/events/:event_id/attendees/:attendee_id/update", (request, response) -> {
        //route2
//        get("/attendees/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            //If error, check that 'attendee_id' being called correctly.
            int attendee_id = Integer.parseInt(request.params("attendee_id"));

            Attendee editAttendee = attendeeDao.findByIdAttendee(attendee_id);
            model.put("editAttendee", editAttendee);

            Attendee attendee = attendeeDao.findByIdAttendee(attendee_id);
            model.put("attendee", attendee);

            return new ModelAndView(model, "attendee-form.hbs");
        }, new HandlebarsTemplateEngine());

        //process updated event details
        post("/events/:event_id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int updatedEventId = Integer.parseInt(request.params("event_id"));

            String updatedEventTitle = request.queryParams("event-title");
            String updatedEventDescription = request.queryParams("event-description");

            eventDao.updateEvent(updatedEventId, updatedEventTitle, updatedEventDescription);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

    /* DELETE */

    } //End of 'Public Static Void Main'
} //End of App.java
