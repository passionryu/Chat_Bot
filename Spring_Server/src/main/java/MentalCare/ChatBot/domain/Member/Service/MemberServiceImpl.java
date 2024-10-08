package MentalCare.ChatBot.domain.Member.Service;

import MentalCare.ChatBot.domain.Member.DTO.Request.MemberRequest;
import MentalCare.ChatBot.domain.Member.DTO.Request.UpdateMemberDTO;
import MentalCare.ChatBot.domain.Member.DTO.Response.EveryMemberResponse;
import MentalCare.ChatBot.domain.Member.DTO.Response.MemberResponse;
import MentalCare.ChatBot.domain.Member.Entity.Member;
import MentalCare.ChatBot.domain.Member.Repository.MemberRepository;
import MentalCare.ChatBot.global.Exception.ErrorCode;
import MentalCare.ChatBot.global.Exception.MemberException;
import MentalCare.ChatBot.global.auth.JWt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtutil;

    //회원 가입
    @Override
    public Long register(MemberRequest request) {

        Member member = request.toEntity();

        //이메일 중복 확인
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_MEMBER);
        }

        //비밀번호 암호화
        member.encodePassword(passwordEncoder);

        //멤버 저장 + 최종 반환값 =id
        return memberRepository.save(member).getMember_no();
    }

    //회원 정보 수정 메서드 구현
    // FIXME : 엔티티에 set함수는 권장되지 않으니 시간 되면 수정할 것
    @Override
    public Long updateMember(String username , UpdateMemberDTO request) {

        //사용자 이름으로 멤버 객체 찾기
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        //Optional에서 Member 객체를 가져옴
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 새로운 정보로 업데이트
        if (member.getUsername() != null) {
            member.setUsername(request.username());
        }
        if (member.getEmail() != null) {
            member.setEmail(request.email());
        }
        if (member.getBirth() != null) {
            member.setBirth(request.birth());
        }
        if (member.getGender() != null) {
            member.setGender(request.gender());
        }

        // 변경된 내용을 저장
        return memberRepository.save(member).getMember_no();

    }

    //회원 정보 삭제 메서드 구현
    // FIXME : 모든 로직이 membercontroller에 전부 구현되어 있으니, 여기에 분담하여 관리할 것
    @Transactional
    @Override
    public void deleteMember(Long member_no) {
        Member member = memberRepository.findById(member_no)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        memberRepository.delete(member);
    }

    //회원 정보 조회 메서드 구현
    // FIXME : 모든 로직이 membercontroller에 전부 구현되어 있으니, 여기에 분담하여 관리할 것
    @Override
    public MemberResponse getmyinfo(String username) {
        //사용자 이름으로 멤버 객체 찾기
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        //Optional에서 Member 객체를 가져옴
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        //MyInfoResponse로 변환하여 반환
        MemberResponse response = MemberResponse.from(member);

        return response;
    }

    //모든 회원 정보 조회 메서드 - 관리자 회원정보 관리 페이지 용
    @Override
    public List<EveryMemberResponse> geteveryinfo(String username) {

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));

        // 모든 회원 정보를 조회
        List<Member> allMembers = memberRepository.findAll();

        // 모든 회원 정보를 EveryMemberResponse로 변환
        List<EveryMemberResponse> responseList = allMembers.stream()
                .map(EveryMemberResponse::from)
                .toList();

        return responseList;
    }
}
