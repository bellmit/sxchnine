/*
package com.project.controller;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import com.project.business.PaymentService;
import com.project.model.Order;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(RestPactRunner.class)
@Provider("payment-service")
@PactFolder("pact")
public class PaymentControllerTestContract {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController = new PaymentController(paymentService);

    @TestTarget
    public MockMvcTarget target = new MockMvcTarget();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        target.setControllers(paymentController);
    }

    @State("Pay Order")
    public void testPayOrder(){
        target.setRunTimes(1);
        when(paymentService.checkout(any(Order.class))).thenReturn(1);

    }

}
*/
