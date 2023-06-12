package nl.han.rwd.srd.api.exception;

public class SRDEXception extends RuntimeException
{
    public SRDEXception(String message, Exception ex) {
        super(message, ex);
    }
}
