package MentalCare.ChatBot.domain.Diary.Entity;

import MentalCare.ChatBot.domain.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryNo;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    private String diaryText;
    private String diarySummary;
    private String comicURL;
    private String diaryEmotion;
    private String weather;
    private String weatherEmoji; //이모티콘은 본질적으로 유니코드 문자로 표현되며, String 타입으로 진행
    private LocalDate diaryDate;

    /*member_no를 통해 member 엔티티와 일대다 연결*/
    /*private Long member_no 필드를 위 엔티티에 넣을 필요는 없음*/
}