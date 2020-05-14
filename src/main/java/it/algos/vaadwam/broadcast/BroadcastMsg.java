package it.algos.vaadwam.broadcast;

import lombok.Data;

@Data
public class BroadcastMsg {
    private String code;
    private Object payload;

    public BroadcastMsg(String code, Object payload) {
        this.code = code;
        this.payload = payload;
    }
}
