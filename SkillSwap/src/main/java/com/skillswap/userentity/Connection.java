package com.skillswap.userentity;

import jakarta.persistence.*;

@Entity
@Table(name="Connection",
uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})

)
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @ManyToOne
    @JoinColumn(name="sender_id")
    private UserEntiity sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private UserEntiity receiver;

    @Column
    private String isConnected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntiity getSender() {
        return sender;
    }

    public void setSender(UserEntiity sender) {
        this.sender = sender;
    }

    public UserEntiity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntiity receiver) {
        this.receiver = receiver;
    }

    public String getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }
}
