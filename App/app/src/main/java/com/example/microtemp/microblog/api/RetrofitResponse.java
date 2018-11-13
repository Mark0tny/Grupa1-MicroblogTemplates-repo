package com.example.microtemp.microblog.api;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access=AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
public class RetrofitResponse {

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC)
    @SerializedName("error")
    private boolean err;

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC)
    @SerializedName("message")
    private String msg;

}
