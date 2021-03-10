package com.example.orders_parser.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private long id;
    private double amount;
    private String currency;
    private String comment;
    private String filename;
    private long line;
    private String result;

    @JsonCreator
    public Order(
            @JsonProperty("orderId") long id,
            @JsonProperty("amount") double amount,
            @JsonProperty("currency") String currency,
            @JsonProperty("comment") String comment) {

        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"amount\":" + amount +
                ", \"currency\":\"" + currency + '\"' +
                ", \"comment\":\"" + comment + '\"' +
                ", \"filename\":\"" + filename + '\"' +
                ", \"line\":" + line +
                ", \"result\":\"" + result + '\"' +
                '}';
    }
}
