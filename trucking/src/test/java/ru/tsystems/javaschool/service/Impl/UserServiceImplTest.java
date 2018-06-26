package ru.tsystems.javaschool.service.Impl;

import org.junit.Test;
import org.mockito.InjectMocks;
import ru.tsystems.javaschool.exceptions.TruckingServiceException;
import ru.tsystems.javaschool.service.Impl.UserServiceImpl;
import ru.tsystems.javaschool.service.UserService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserServiceImplTest {

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    public void isEmailValidTest_01() throws TruckingServiceException {
        assertTrue(userService.isEmailValid("dsflj@dm.ru"));
    }

    @Test
    public void isEmailValidTest_02() throws TruckingServiceException {
        assertTrue(userService.isEmailValid("ds123_5.-sflj@dm.44.ru"));
    }

    @Test
    public void isEmailValidTest_03() throws TruckingServiceException {
        assertFalse(userService.isEmailValid("dsfljm.ru"));
    }

    @Test
    public void isEmailValidTest_04() throws TruckingServiceException {
        assertFalse(userService.isEmailValid("dsflj@d"));
    }
}
