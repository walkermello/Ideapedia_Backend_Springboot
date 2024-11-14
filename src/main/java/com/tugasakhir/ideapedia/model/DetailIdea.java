package com.tugasakhir.ideapedia.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstDetailIdea")
public class DetailIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Status")
    private String status;

    @Column(name = "ApprovalDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;

    @Column(name = "RejectedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedDate;

    @Column(name = "Comments")
    private String comments;

    // Relasi dengan Idea
    @ManyToOne
    @JoinColumn(name = "IDIdea") // Tidak perlu insertable, updatable untuk relasi
    private Idea idea;

    // Relasi dengan User
    @ManyToOne
    @JoinColumn(name = "IDApprovedBy")  // Relasi ini dikelola secara langsung
    private User approvedBy;

    // Konstruktor tanpa parameter
    public DetailIdea() {
    }

    // Konstruktor dengan parameter untuk status, approvalDate, dan comments
    public DetailIdea(Long ideaId, String status, Date approvalDate, String comments, Idea idea, User approvedBy) {
        this.status = status;
        this.approvalDate = approvalDate;
        this.comments = comments;
        this.idea = idea;
        this.approvedBy = approvedBy;
    }

    // Getters dan Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Getters untuk relasi
    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }
}
