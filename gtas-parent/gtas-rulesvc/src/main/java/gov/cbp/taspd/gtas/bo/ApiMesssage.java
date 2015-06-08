package gov.cbp.taspd.gtas.bo;

import java.util.List;

public class ApiMesssage {
    public static final int HELLO   = 0;
    public static final int GOODBYE = 1;

    private String          message;

    private int             status;

    public ApiMesssage() {

    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public static ApiMesssage doSomething(ApiMesssage message) {
        return message;
    }

    public boolean isSomething(String msg,
                               List<Object> list) {
        list.add( this );
        return this.message.equals( msg );
    }

}
