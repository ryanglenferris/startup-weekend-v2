package dao;

import models.Attendee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

//import models.Event;

public class Attendee_DAO_SQL2O implements Attendee_DAO {

    private final Sql2o sql2o;
    public Attendee_DAO_SQL2O(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void addAttendee(Attendee attendee) {
        String sql = "INSERT INTO attendees (attendeeName, eventId) VALUES (:attendeeName, :eventId)";
        try (Connection con = sql2o.open()) {
            int idAttendee = (int) con.createQuery(sql)
                    .bind(attendee)
                    .executeUpdate()
                    .getKey();
            attendee.setIdAttendee(idAttendee);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Attendee findByIdAttendee(int idAttendee) {
        String sql = "SELECT * FROM attendees WHERE idAttendee = :idAttendee";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("idAttendee", idAttendee)
                    .executeAndFetchFirst(Attendee.class);
        }
    }

    @Override
    public List<Attendee> getAllAttendees() {
        String sql = "SELECT * FROM attendees";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Attendee.class);
        }
    }

    @Override
    public void updateAttendee(int idAttendee, String attendeeName) {
        String sql = "UPDATE attendees SET attendeeName = :attendeeName WHERE idAttendee = :idAttendee";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("attendeeName", attendeeName)
                    .addParameter("idAttendee", idAttendee)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteByIdAttendee(int idAttendee) {
        String sql =  "DELETE FROM attendees WHERE idAttendee = :idAttendee";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("idAttendee", idAttendee)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllAttendees() {
        String sql = "DELETE FROM attendees";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}