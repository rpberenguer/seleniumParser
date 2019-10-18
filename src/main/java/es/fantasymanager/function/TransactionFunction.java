package es.fantasymanager.function;

import java.util.function.Function;

import es.fantasymanager.data.business.TransactionData;
import es.fantasymanager.data.entity.Transaction;

public class TransactionFunction implements Function<TransactionData, Transaction> {

	@Override
	public Transaction apply(TransactionData input) {

		Transaction output = new Transaction();

		return output;
	}
}
