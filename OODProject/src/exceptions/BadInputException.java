package exceptions;

public class BadInputException extends Exception{

	public BadInputException() {
	}



	public class BadUsernameInputException extends Exception{
		
		public BadUsernameInputException(String string){
			super(string);
		}

	}
	
	
	
	public class BadPasswordInputException extends Exception{
		
		public BadPasswordInputException(String string){
			super(string);
		}

	}


}
