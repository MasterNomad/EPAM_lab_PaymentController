package edu.epam.izhevsk.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTest {


    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private AccountService accountServiceMock;
    @Mock
    private DepositService depositServiceMock;

    @Before
    public void setUp() throws InsufficientFundsException {
        when(accountServiceMock.isUserAuthenticated(100L)).thenReturn(true);
        when(depositServiceMock.deposit(gt(100L), anyLong())).thenThrow(new InsufficientFundsException());
    }

    @Test
    public void testUserAuthenticated() throws InsufficientFundsException {
        paymentController.deposit(50L, 100L);
        verify(accountServiceMock, times(1)).isUserAuthenticated(100L);
    }

    @Test(expected = SecurityException.class)
    public void testFailedAuthentication() throws InsufficientFundsException {
        paymentController.deposit(50L, 99L);
    }

    @Test(expected = InsufficientFundsException.class)
    public void testFailedDepositOperation() throws InsufficientFundsException {
        paymentController.deposit(101L, 100L);
    }
}
