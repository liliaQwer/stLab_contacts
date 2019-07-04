import dao.DAO;
import model.Address;
import model.Attachment;
import model.Contact;
import model.Phone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.ContactService;
import service.ContactServiceImpl;
import utils.ApplicationException;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ContactServiceTest {


    @Mock
    private DAO<Contact> contactDAO;

    @Mock
    private DAO<Attachment> attachmentDAO;

    @Mock
    private DAO<Phone> phoneDAO;

    @Mock
    private DAO<Address> addressDAO;

    @Mock
    private DataSource ds;

    @Mock
    private Connection connection;

    private ContactService contactService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        contactService = new ContactServiceImpl(ds, contactDAO, addressDAO, attachmentDAO, phoneDAO);
    }

    @Test
    public void test_get_contact_by_id() throws SQLException {

        Integer userId = 5;
        when(ds.getConnection()).thenReturn(connection);
        when(contactDAO.get(eq(connection), eq(userId))).thenReturn(new Contact());

        try {
            assertThat(contactService.get(userId), is(notNullValue()));
        } catch (ApplicationException ex) {
            fail(ex.getMessage());
        }
    }
}
