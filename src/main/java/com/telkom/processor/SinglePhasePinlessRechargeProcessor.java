package com.telkom.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telkom.balanceTopup.BalanceTopupWrapper;

public class SinglePhasePinlessRechargeProcessor  implements Processor{

	private static Logger logger = LoggerFactory.getLogger(SinglePhasePinlessRechargeProcessor.class);
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("==========Inside SinglePhasePinlessRechargeProcessor ==========" );
		exchange.getOut().setHeader("responseCode", exchange.getIn().getHeader("responseCode"));
		exchange.getOut().setBody(objectMapper.writeValueAsString(BalanceTopupWrapper.BALANCE_TOPUP));
		logger.info("==========Exit SinglePhasePinlessRechargeProcessor ==========" );
	}

}
