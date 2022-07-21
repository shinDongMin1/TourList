package com.sku.TourList.domain;

import com.sku.TourList.domain.enums.BoardType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table
public class Board extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;

    @Builder
    public Board(Long id, String title, String content, BoardType boardType, Member user){
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
    }

    public void update(Board board){
        this.title = board.getTitle ();
        this.content = board.getContent ();
        this.boardType = board.getBoardType ();
    }
}
