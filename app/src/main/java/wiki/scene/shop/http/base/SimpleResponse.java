package wiki.scene.shop.http.base;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

    public int code;
    public String msg;

    public LzyResponse toBaseResponse() {
        LzyResponse lzyResponse = new LzyResponse();
        lzyResponse.code = code;
        lzyResponse.msg = msg;
        return lzyResponse;
    }
}