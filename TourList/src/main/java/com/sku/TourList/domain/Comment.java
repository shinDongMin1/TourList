package com.sku.TourList.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table
public class Comment extends BaseTimeEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Comment(Long id, String reply, Member user, Board board){
        this.id = id;
        this.reply = reply;
        this.user = user;
        this.board = board;
    }

    public void update(Comment comment){
        this.reply = comment.getReply ();
    }
}
