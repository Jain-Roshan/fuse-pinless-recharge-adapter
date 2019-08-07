package com.telkom.processor;

import java.math.BigInteger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telkom.balanceTopup.BalanceTopup;
import com.telkom.balanceTopup.BalanceTopupWrapper;
import com.telkom.balanceTopup.Characteristic;

import za.co.telkom.RechargeResponse;
import za.co.telkom.eai.billing.topuprechargemanage.ResponseType;
import za.co.telkom.eai.billing.topuprechargemanage.ResponseType.Payload;
import za.co.telkom.eai.billing.topuprechargemanage.TopUpRechargeManage;
import za.co.telkom.eai.billing_types.ResultType;

public class RechargeRequestResponseProcessor implements Processor{
	
	private static Logger logger = LoggerFactory.getLogger(RechargeRequestResponseProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		logger.info("==========Inside RechargeRequestResponseProcessor ==========" + exchange.getIn().getBody(String.class));
		RechargeResponse rechargeResponse = new RechargeResponse();
		TopUpRechargeManage topUpRechargeManage = new TopUpRechargeManage();
		ResponseType responseType = new ResponseType();
		Payload payload = new Payload(); 
		ResultType resultType = new ResultType();
		
		Integer responseCode = (Integer) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
		logger.info("==========Inside RechargeRequestResponseProcessor responseCode =========="+responseCode);
		if (responseCode == 201) {
			resultType.setResultCode(new BigInteger("0"));
			resultType.setResultMsg("Validation Success");
			
			BalanceTopup balanceTopUp = BalanceTopupWrapper.BALANCE_TOPUP;
			for(Characteristic characteristic : balanceTopUp.getCharacteristic()) {
				if(characteristic.getName().equalsIgnoreCase("TransactionType")) {
					payload.setTransactionType(characteristic.getValue());
				}
				if(characteristic.getName().equalsIgnoreCase("SessionID")) {
					payload.setSessionID(characteristic.getValue());
				}
			}
			payload.setTopUpReferenceNumber(balanceTopUp.getId());
			payload.setCardNumber(balanceTopUp.getProduct().getId());
			payload.setRechargeAmount(balanceTopUp.getAmount().getAmount().toString());
			
			//payload.setAccountID("");
			payload.setAccountStatus("NA");
			payload.setAccountBalance("0");
			
			responseType.setPayload(payload);
		}else {
			resultType.setResultCode(new BigInteger("1"));
			resultType.setResultMsgCode("MPR-001");
			resultType.setResultMsg("Invalid Merchant");
		}
		
		responseType.setResult(resultType);
		topUpRechargeManage.setResponse(responseType);
		rechargeResponse.setTopUpRechargeManage(topUpRechargeManage);
		logger.info("==========Exit from RechargeRequestResponseProcessor ==========");
		exchange.getOut().setBody(rechargeResponse);
	}

}
