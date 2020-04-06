import java.io.Serializable;
public class Payload implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6625037986217386003L;
	private String message;
	private String word;
	private char c;
	public void setMessage(String s) {
		this.message = s;
	}
	public String getMessage() {
		return this.message;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getWord() {
		return this.word;
	}
	public void setChar(char c) {
		this.c = c;
	}
	public char getChar() {
		return this.c;
	}
	
	private PayloadType payloadType;
	public void setPayloadType(PayloadType pt) {
		this.payloadType = pt;
	}
	public PayloadType getPayloadType() {
		return this.payloadType;
	}
	
	private int number;
	public void setNumber(int n) {
		this.number = n;
	}
	public int getNumber() {
		return this.number;
	}
	@Override
	public String toString() {
		return String.format("Type[%s], Number[%s], Message[%s]",
					getPayloadType().toString(), getNumber(), getMessage());
	}
}
