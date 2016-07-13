import rest.StartController;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Vadim on 22.04.2016.
 *
 */

public class TestREST {
    /**
     * TestREST REST main.java.rest
     */

    @Test
    public void testRESTControlGetMySQLDriverManagerDatasource() {
        StartController.MyBean mock = mock(StartController.MyBean.class);
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        when(mock.getMySQLDriverManagerDatasource()).thenReturn(driverManagerDataSource);
        assertEquals(driverManagerDataSource, mock.getMySQLDriverManagerDatasource());
    }
}
