package com.telkom.route;

import java.net.UnknownHostException;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telkom.processor.BalanceTopUpResponseProcessor;
import com.telkom.processor.RechargeRequestBalanceTopUpProcessor;
import com.telkom.processor.RechargeRequestResponseProcessor;
import com.telkom.processor.SinglePhasePinlessRechargeProcessor;
import com.telkom.processor.ValidateAccountBalanceTopUpProcessor;
import com.telkom.processor.ValidateAccountResponseProcessor;

public class PinlessRechargeRoute extends RouteBuilder{

	public static final Logger looger = LoggerFactory.getLogger(PinlessRechargeRoute.class); 
	@Override
	public void configure() throws Exception {
	
		onException(UnknownHostException.class).handled(true).log("VPN not connected.").transform()
		.simple("VPN not connected.");
        
        from("cxf:bean:cxfSoapServiceEndpoint")
        /*.choice().when().simple("${headers.camelHttpPath} == '/rechargeRequest'")
			           .log("======== inside rechargeRequest route ========")
			           .process(new RechargeRequestBalanceTopUpProcessor())
			           .to("direct:pinlessRechargeProcess")
			   		   .log(" ==== Response from pinless recharge process ==== ${header.CamelHttpResponseCode}")
			   		   .process(new BalanceTopUpResponseProcessor())
			   		   .choice().when().simple("${header.responseCode} == '201' && ${header.merchantid} != 'ABSA' ")
			   		   				.process(new SinglePhasePinlessRechargeProcessor())
			   				        .to("direct:singlePhasePinlessRechargeProcess")
			   				        .process(new RechargeRequestResponseProcessor())
			   		            .otherwise()
			   		      	  		.process(new RechargeRequestResponseProcessor())
			   		   .endChoice()
			    .when().simple("${headers.camelHttpPath} == '/validateAccount'")
			    	   .log("======= inside validateAccount route ========")
			           .process(new ValidateAccountBalanceTopUpProcessor())
			           .to("direct:pinlessRechargeProcess")
			   		   .log(" ==== Check Response from pinless recharge process ==== ")
			   		   .process(new BalanceTopUpResponseProcessor())
			   		   .choice().when().simple("${header.responseCode} == '201' && ${header.merchantid} != 'ABSA' ")
			   		                .process(new SinglePhasePinlessRechargeProcessor())
			   		   				.to(ExchangePattern.InOnly, "direct:singlePhasePinlessRechargeProcess")
			   				        .process(new ValidateAccountResponseProcessor())
			   		            .otherwise()
			   		      	  		.process(new ValidateAccountResponseProcessor())
			   		   .endChoice()
			   		//.process(new ValidateAccountResponseProcessor())
        .end();*/
         
        .log("======= inside validateAccount route ========")
        .process(new ValidateAccountBalanceTopUpProcessor())
        .to("direct:pinlessRechargeProcess")
		   .log(" ==== Check Response from pinless recharge process ==== ")
		   .process(new BalanceTopUpResponseProcessor())
		   .choice().when().simple("${header.responseCode} == '201' && ${header.merchantid} != 'ABSA' ")
		                .process(new SinglePhasePinlessRechargeProcessor())
		   				//.to(ExchangePattern.InOnly, "direct:singlePhasePinlessRechargeProcess")
				        .process(new ValidateAccountResponseProcessor())
		            .otherwise()
		      	  		.process(new ValidateAccountResponseProcessor())
		   .endChoice()
		   .end();
		
        from("direct:pinlessRechargeProcess").log(" ==== POST pinless recharge process for balanceTopUp==== ").removeHeaders("*").removeProperties("*")
		.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.recipientList(simple("{{pinlessrechargeprocess}}"));
        
        
        from("direct:singlePhasePinlessRechargeProcess").log(" ==== POST single phase pinless recharge process for balanceTopUp==== ").removeHeaders("*").removeProperties("*")
		.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.recipientList(simple("{{singlephase}}"));
	}

}