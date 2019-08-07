package com.telkom.processor;

import java.math.BigInteger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telkom.balanceTopup.BalanceTopUpResponseWrapper;
import com.telkom.balanceTopup.BalanceTopup;
import com.telkom.balanceTopup.BalanceTopupWrapper;
import com.telkom.balanceTopup.Characteristic;

import za.co.telkom.ValidateAccountResponse;
import za.co.telkom.eai.billing.topupenquirymanage.ResponseType;
import za.co.telkom.eai.billing.topupenquirymanage.ResponseType.Payload;
import za.co.telkom.eai.billing.topupenquirymanage.TopUpEnquiryManage;
import za.co.telkom.eai.billing_types.AccountDetailsType;
import za.co.telkom.eai.billing_types.ResultType;


public class ValidateAccountResponseProcessor implements Processor{
	private static Logger logger = LoggerFactory.getLogger(ValidateAccountResponseProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("==========Inside ValidateAccountResponseProcessor ==========" + exchange.getIn().getBody(String.class));
		ValidateAccountResponse validateAccountResponse  = new ValidateAccountResponse();
		TopUpEnquiryManage topUpEnquiryManage = new TopUpEnquiryManage();
		ResponseType responseType = new ResponseType();
		Payload payload = new Payload();
		ResultType resultType = new ResultType();
		AccountDetailsType accountDetailsType = new AccountDetailsType();
		//Integer responseCode = (Integer) exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE);
		String responseCode = BalanceTopUpResponseWrapper.BALANCE_TOPUP_RESPONSE.getCode();
		if (responseCode != null && "201".equalsIgnoreCase(responseCode)) {
			resultType.setResultCode(new BigInteger("0"));
			resultType.setResultMsg("Validation Success");
			
			BalanceTopup balanceTopUp = BalanceTopupWrapper.BALANCE_TOPUP;
			payload.setTransactionType("011");
			
			for(Characteristic characteristic : balanceTopUp.getCharacteristic()) {
				if("SessionID".equalsIgnoreCase(characteristic.getName())) {
					payload.setSessionID(characteristic.getValue());
				}
			}
			
			payload.setTopUpReferenceNumber(balanceTopUp.getId());
			payload.setCardNumber(balanceTopUp.getProduct().getId());
			payload.setRechargeAmount(balanceTopUp.getAmount().getAmount().toString());
			
			accountDetailsType.setAccountID("0");
			accountDetailsType.setAccountStatus("NA");
			accountDetailsType.setAccountBalance("0");
			
			payload.setMainAccount(accountDetailsType);
			responseType.setPayload(payload);
		}else {
			resultType.setResultCode(new BigInteger("1"));
			resultType.setResultMsgCode("MPR-001");
			resultType.setResultMsg("Invalid Merchant");
		}
		
		responseType.setResult(resultType);
		topUpEnquiryManage.setResponse(responseType);
		validateAccountResponse.setTopUpEnquiryManage(topUpEnquiryManage);
		exchange.getOut().setBody(validateAccountResponse, ValidateAccountResponse.class);
		logger.info("==========Exit ValidateAccountResponseProcessor =========="+exchange.getOut().getBody());
	}

}