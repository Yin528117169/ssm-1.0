import dao.AppointmentDao;
import entity.Appointment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppointDaoTest extends BaseTest {
    @Autowired
    private AppointmentDao appointmentDao;

    @Test
    public void testInsertAppointment() throws Exception{
        long bookId = 1001;
        long studentId = 3114002618L;
        int insert = appointmentDao.insertAppointment(bookId,studentId);
        System.out.println("insert="+insert);
    }

    @Test
    public void testQueryByKeyWithBook() throws Exception{
        long bookId = 1000;
        long studentId =  12345678;
        Appointment appointment = appointmentDao.queryByKeyWithBook(bookId,studentId);
        System.out.println(appointment);
        System.out.println(appointment.getBook());
    }
}
