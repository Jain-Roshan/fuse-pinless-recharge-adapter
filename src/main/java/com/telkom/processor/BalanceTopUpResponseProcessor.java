package com.telkom.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telkom.balanceTopup.BalanceTopUpResponse;
import com.telkom.balanceTopup.BalanceTopUpResponseWrapper;
import com.telkom.balanceTopup.BalanceTopupWrapper;

public class BalanceTopUpResponseProcessor implements Processor{

	private static Logger logger = LoggerFactory.getLogger(BalanceTopUpResponseProcessor.class);
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("==========Inside BalanceTopUpResponseProcessor ==========" );
		BalanceTopUpResponse balanceTopUpResponse =  objectMapper.readValue(exchange.getIn().getBody(String.class),BalanceTopUpResponse.class);
		BalanceTopUpResponseWrapper.BALANCE_TOPUP_RESPONSE = balanceTopUpResponse;
		logger.info("=====code is "+balanceTopUpResponse.getCode());
		exchange.getOut().setHeader("responseCode", balanceTopUpResponse.getCode());
		exchange.getOut().setHeader("merchantid", BalanceTopupWrapper.BALANCE_TOPUP.getChannel().getId());
		logger.info("==========Exit BalanceTopUpResponseProcessor responseCode ==>" + exchange.getOut().getHeader("responseCode") + " merchantid ==> " + exchange.getOut().getHeader("merchantid"));
	}

}
