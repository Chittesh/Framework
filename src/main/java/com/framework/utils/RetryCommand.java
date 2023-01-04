package com.framework.utils;

public class RetryCommand {

	private int maxAttempts;
	private int timeout;
	
	public RetryCommand(int maxAttempts, int timeout) {
		this.maxAttempts = maxAttempts;
		this.timeout     = timeout;
	}
	
	public <T, E extends Throwable> T execute(Command<T, E> supplier) throws E {
		
		for(int attempt = 1; true; attempt++) {
						
			try {

				return supplier.execute();
			
			} catch (Exception e) {
				
				System.out.println(
						String.format("--- failed executing supplier %s (%d) - retrying - %s ", 
									  supplier.toString(), 
									  attempt, 
									  e.getMessage())); 
				
				sleep(timeout);
				
				if(attempt >= maxAttempts) 
					throw new RuntimeException(
								String.format("--- failed executing supplier %s - maximum retry count reached", 
											  supplier.toString())); 
					
				
			} 
			
		}
							
	}
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		}
		catch (Exception e) {
			//nothing
		}
	}
	
	public static interface Command<T, E extends Throwable> {
		T execute() throws E;
	}

}
